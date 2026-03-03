# 背景与总结
1.bitmap字段，在支持精准去重时，为什么查询性能查。
a.去重过程涉及shuffle，由于bitmap较大，map端的bitmap结果,需要网络传输到reduce端,对网络io压力较大。
b.reduce阶段需要对bitmap做进一步聚合，同时如果reduce数量多，需要聚合多个bitmap对象，对内存、cpu要求较高，如果内存过小容易出现OOM。

2.流程
Hive中根据去重字段range生成bucket_id；
Hive2Doris时按bucket_id进行重分布；
Doris计算时Map聚合后直接输出计算结果。


# 解决思路
1.如何拆解问题 -- 分治思想
a.如果map端的bitmap结果是独立的，比如统计维度组合下的DAU数，如果设备ID也作为一个维度进行分发，则map端是正交独立的。即维度组合下的DAU数 = 所有map端的DAU数之和。那么shuffle做的就很简单了，传输的就只有结果集，reduce做sum即可。
b.bitmap是bigint为基础，每一个数值占用一个bit。
如果是string如何转换成bigint？hash的方式是一种选择，但生成的ID太分散，bitmap不够密集，导致bitmap占用存储大。
同理如果bigint字段本身也很分散，也会造成bitmap不够密集。

所以解决方案是为每一个周期(日周月),都独立创建一个字典表，包含每个设备ID对应的自增长ID，高16位是连续并且相同的，所以很容易减少bitmap的密集度，压缩比更高，用contain就可以满足存储诉求。


# 缺点
1.只支持除了维度外，一个分桶列(比如设备ID),如果多个列.则需要多个分桶，可能有一些问题。
如果一定要支持多个分桶，则可能要分别支持多个表去拆分指标。比如用户数、设备数，在两个表里支持业务需求。
2.因为增加了一个维度，该维度的属性值最多有65536个可能，所以原始数据会发生膨胀。


# HIVE实现
1.为正常的指标维度模型，增加一个分桶字段，该字段就是自增长(设备ID)/65536。如果该值是null,则随机分散到不同的桶里。
select 维度,指标uuid,指标对应的分桶 cast(floor(coalesce(uuid,floor(rand()*10000))/65536) as int) as bucket_id
2.数据重分布，相同bucket_id在同一节点上。
通过 DISTRIBUTE BY bucket_id。确保相同的uuid肯定在同一个物理节点上。

select *,bucket_id
from 
(
	select *
	from biao
	where
) t 
DISTRIBUTE BY bucket_id

3.基于2的结果进行group by，则相当于在map端就可以计算好bitmap的count数量，reduce做sum即可。

select xxx,自定义的reduce进行sum的聚合函数bitmap_union_count(xxxx)
from 2
group by xxx

# DORIS实现
1.基于hive表作为输入。
2.doris聚合表，由原来的维度key+分桶字段bucket_id。
对bucket_id进行分桶。分桶数<65536，比如30个。
DDL ： DISTRIBUTED BY HASH(`bucket_id`) BUCKETS 30

# hive sql的demo
  SET spark.sql.execution.useObjectHashAggregateExec=false;
  
  select xxx,
  自定义聚合函数(uv) as uv
    from
    (
      select * from
      (
        select key1,key2,
        to_bitmap(uuid) as uv,
        bucket_id
        from 
        (
          select *,bucket_id
          from biao 
          where dt = ?
        ) t
        group by key1,key2,bucket_id
      ) a
      DISTRIBUTE BY bucket_id
    ) b
    group by xxxx
    
# 注意
1.hive必须要按照bucket_id重分布，否则无法再map端进行计算成正确的数据。
2.SET spark.sql.execution.useObjectHashAggregateExec=false 必须设置,否则会不准确。
问题发生在自定义聚合函数上，在map端直接输出bitmap的计算结果时，内部维护了一个map<key,Bitmap>结构，key是group by 的维度，当维度达到阈值上限的时候会讲结果先刷到磁盘上。所以相同的key,刷了多次bitmap在磁盘上时，重复计算。
所以不能hash方式聚合。
3.数据容易倾斜 分桶大小不合理，数据容易倾斜
4.doris数据膨胀 分桶会造成数据膨胀