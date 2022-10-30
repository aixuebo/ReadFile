github:hiveJdbc 项目/src/java/org/apache/hadoop/hive/ql/udf/

hive支持的数据类型
boolean、tinyint、smallint、int、bigint、float、double、decimal、string、varchar、ts、date、binary

如果hive的字段类型本来是double,当下游使用该字段时,设置为bigint,即相当于 cast xx as bigint 操作,而该操作会有歧义,不同引擎处理结果是不同的,比如hive会四舍五入,doris会舍。
因此这种场景要取消歧义,使用cast(round(xx) as bigint)方式代替,强制四舍五入,或者ceil函数处理。


使用hash(id) % 10 代替 rand()*10。
提出了distribute by来再分区。distribute by的原理很简单，就是把后面跟着的字段作为key，key相同则分发到相同的partition进行处理。
distribute by 分区列,case when 大分区 then cast(rand()*10 as int) when 小分区 then 1 end 
这样同一个分区字段，根据分区业务上的数据内容多少，可以固定设置每一个分区多少个分区。
但由于每次rand会变化，当发生在部分任务重导时，数据会被错误分发(结果数据总行数正确，但是一部分数据重复出现了2次，相应地一部分数据缺失。)，
因此每一条数据分发到哪个分区，会因为重导后变化。推荐使用如下方式cast(hash(id) % 10 as int) 固定散列。


常用函数
COALESCE(null,'aaa')
正数除法  100/200 = 0 ,但是小数double除法就可以得到小数,因此改写一下cast(字段 as Double)/cast(字段 as Double)

除法,防止分母是0----分子 * 1.0 / (分母 + 1e-6)  这个逻辑有问题，比如 7/0.00001 = 70000,很大的数.即分母不能是小数，因此这种方式不可取

查看数据分布，最大、最小、均值、标准差(方差开根号)  select min(score),max(score),avg(score),stddev(score),var_pop(score)

count(distinct dt,id) 合法 
count(distinct (dt,id)) 不合法

创造语法用于测试
set compatible.grammar=sparksql;
select explode(array(1,1,1,1,1,0.5,0.5,0.5,0.5,0.5,0,0,0,0,0)) as id
LATERAL VIEW explode(split(id_list,',')) ids AS id
输出:每一个元素都是一行数据,用id表示该列name

先分步排序，然后在写集合，确保集合有顺序,能保证spark和hive都能运行成功

排序 sort_array(collect_list(id))  org.apache.hadoop.hive.ql.udf.generic.GenericUDFSortArray

select leaf_value,CONCAT_WS('|','',collect_list(cast(id as string)),'') as id
from
(
   select leaf_value,id
   from aa
   distribute by leaf_value
   sort by leaf_value,id
)t
group by leaf_value

只能保证hadoop成功，spark不成功,语义有问题，应该改成上面的形式，分步进行排序
select leaf_value,CONCAT_WS('|','',collect_list(cast(id as string)),'') as id
from
(
   select leaf_value,id
   from aa
   order by id
)t
group by leaf_value

dt转日、周、月号
datediff('$now.date','1970-01-01') 转日号
floor((datediff('$now.date','1970-01-01')-32)/7) 转周号
year('$now.date')*12+month('$now.date')  转月号

        
IF(org_name_5 LIKE '%联络点',org_name_5,org_name_6)

一、普通UDF
1.concat_ws
例如ELECT concat_ws('.', 'www', array('facebook', 'com')) FROM src LIMIT 1
 返回www.facebook.com
 
连接字符串concat(string A, string B…)

CONCAT_WS(',', COLLECT_SET(cast(com_poi_id as string))) 让set集合里面的元素是字符串类型的,需要强转
concat_ws(';','a','b') 等同于 concat_ws('\073','a','b')

select id,
CONCAT_WS(',', COLLECT_SET(concat(dt,'-',cast(status as string)))) change_log1, ###
CONCAT_WS(',', COLLECT_SET(concat_ws('-',dt,cast(status as string)))) change_log2 
### 推荐方式2,因为可以解决null的问题,如果遇见dt或者status是null,会忽略null值,其他值会保留
### 方式1在concat出现null的时候,该记录就会被过滤掉
from biao	
group by id

注意:
a.concat是拼接字符串,参数一定都是字符串，但是如果出现null,则返回值就一定是null,
比如
select concat('abc', null,'def')  返回值是null,原因是任何与null操作的结果都是null,可以这么理解,如果想过滤null,则使用concat_ws方式
b.concat_ws 会将里面的null自动过滤掉
比如
原始数据100010	100020	100032	NULL	100051	NULL	110022
最后返回值 concat_ws('--'), 100010--100020--100032--100051--110022

2.collect_set,collect_list 该函数要配合group by语法
例如 collect_set(age),返回array,类似set,包含所有不重复的age信息
collect_list是按照出现的顺序展现结果
size(collect_set(age)),即array的值是可以放到size函数里面的
collect_set(age)[0] array的元素使用




COLLECT_SET与 if 或者 case配合:
COLLECT_SET(case when type = 1 then cast(id as string) end ) a 在set里面使用case when ,不需要null的时候可以不用else
CONCAT_WS(',', COLLECT_SET(if(type = 2,cast(id as string),NULL))) b ---同时可以接CONCAT_WS函数,但是CONCAT_WS函数要求数组一定是string类型的,因此在if中进行了强转语法

等价关系
A:
CONCAT_WS('\\;',collect_set(concat(case when event_type = 1 then '成功设置'
when event_type = 2 then '已删除'
when event_type = 3 then '修改'
end,utime)))

B:
CONCAT_WS('\\;',collect_set(concat(if(event_type = 1,'成功设置',if(event_type = 2,'已删除','修改')),utime))) log_value

eg:
a.SELECT id, CONCAT_WS(',', COLLECT_SET(pic)) FROM tbl GROUP BY id
b.select appid, 
          collect_set(app_name)[0],
          collect_set(app_url)[0]
from your_table
group by appid;
c.
另一种做法:可以考虑使用min, max

select appid,
        max(app_name),
        max(app_url)
from  your_table
group by  appid;


sort_array(array(1, 2, 2,5, 3, 3)); 对数据排序,大多数情况下使用在group by中

3.hive 的in 操作
a.select login.uid from login left outer join regusers on login.uid=regusers.uid where regusers.uid is not null

b.select login.uid from login day_login left outer join 
   (select uid from regusers where dt='20130101') day_regusers
on day_login.uid=day_regusers.uid where day_login.dt='20130101' and day_regusers.uid is not null

4.avg(age) 求平均值,参数可以是java的基本类型

count(*) 243
count(if(value >0,1,NULL)) 20
sum(value) 5313288
avg(value) 265664.4
avg(if(value>0,value,NULL)) 265664.4
sum(value)/count(if(value >0,1,NULL)) 265664.4

通过以上数据，可以说明avg得到的结果是总数/非null元素的数量,即avg已经考虑了刨除null对结果的影响，即null的数据不算均值里来
注意:0的值算不算，我估计是算的，因此这个需要用的时候去测试考察一下

5.exp(double) 表示e的几次方,e表示2.718281828459045
6.E(),返回e的值2.718281828459045
7.abs(double) 获取正数,例如abs(-5.3),返回5.3
8.ascii(String) 对string进行getBytes()后,获取第一个字节对应的ascii码
如果参数为null,或者"",则返回0

9. ceil(5.3) = 6 or ceiling(5.3) = 6

10.base64(Bytes),将字节数组转换成Text对象,使用Base64.encodeBase64(bytes)方法反编译
       将字节数组按照base64反编译成字符串
11.floor(5.3) = 5 向下取整

round(3.1415926) = 3 四舍五入取整,相当于round(3.1415926,0)
round(5.32,1) 四舍五入保留一位小数点

12.find_in_set('ab','abc,b,ab,c,def') 返回值是3,即ab在第三个位置,下标从1开始计数
13.ltrim(string),移除左边的空格
   rtrim(string),移除右边的空格  GenericUDFRTrim
   trim(string),移除空格 GenericUDFTrim
   注意,这些trim都是不能过滤回车的,因此我自己定义了strTrim函数,调用的是String的trim方法,会过滤回车
14. length(str | binary)
 1.如果是str参数,返回字符串的个数,字母是1个,汉字也是1个,因为汉字是用UTF-8编码的,会依据UTF-8的分隔符进行查找多少个汉字
 2.如果是binary参数,则返回字节数组的个数
15.regexp_replace('100-200', '(\\d+)', 'num') 返回值num-num,将所有的整数替换成num字符串
 将全部符合正则表达式的地方都替换成指定值
 select regexp_replace('iOS|AppStore|1.0|F222A316-8545-46F4-9FFC-E33799D310B3|iPhone Simulator|no|no|wifi', '\\s+','|') 将空格都替换成|
 regexp_replace(get_json_object(content,'$.idlist.id'),'\\[|\\]','') 注意转义\\
注意,在脚本中 hive <<EOF 执行的时候,要对\进行转义,即\\s+要改成\\\\s+

注意:
presto 的正则,不需要\\两个转义字符,使用一个即可,这个是和hive的区别,比如这个形式就可以正确运行:regexp_extract(diff,'是否生效\s*\:\s*.*?\=>(.*?)\;',1)

regexp_replace(column_name,'\t|\n|\r|\u0001|\u0002|\u0003|\u0004|\u0005','')，### 在不改变字段内容可读性情况下，把所以可能相关的特殊字符都替换成空,避免串行
      
16.rlike(string,regexp) 校验string是否匹配正则表达式
   regexp(string,regexp) 校验string是否匹配正则表达式   src/java/org/apache/hadoop/hive/ql/udf/UDFRegExp.java
   常用语where条件中,因为regexp返回值是boolean类型,因此where条件中true满足条件的将会被返回
   case when status like '%余额不足%'
   
   demo:
   regexp:匹配[1,12,3]字符串中[1,开头   ,1]结尾  ,1,中间的情况  
   String regex = "\\[1,|,1\\|,1,]";
   String str = "[12,200,1,300,12]";
   regexp(str,regex)
   regexp(optype,"\\[1,|,1\\|,1,]")

语法:
if(str regexp '11$',1,0) 是否以11结尾。

17.regexp_extract(string,regexp,index) 返回匹配正则表达式的第index个group,默认index可以省略,默认为1
src/java/org/apache/hadoop/hive/ql/udf/UDFRegExpExtract.java 
例如:
regexp_extract('100-200', '(\\d+)-(\\d+)', 1) will return '100'  例子从100-200中获取第一个分组100
hive regexp_extract(text, '现价([\\d\\.]+)元', 1) 提取99.8 例子原价125.0元现价99.8元
提取正数或者小数部分内容
message = "[字段变更] xxxx: => 5.5;[字段变更] ";
String str = evaluate(message,"xxxx\\: \\=> (\\d+(\\.\\d+)?)",1);
System.out.println(str);
正则表达式 不知道为什么要对; : =进行转义，如果纯java是不需要的，但是hive传进来就报错，需要转义。
		
18.+ - * / 运算
UDFOPPlus 表示a+b
UDFOPMinus 表示a-b取值
UDFOPMultiply 表示乘法,获取a*b的返回值
UDFOPDivide 表示a/b 例如 3/2 = 1.5,而不是整数1.这点与java不同
UDFOPLongDivide 表示a divb,与java相同,返回值仅仅是整除后的值
UDFOPMod 表示取魔操作,即整除后还剩余多少 例如 9 % 2 = 1


UDFOPNegative negative(1) 表示 对参数取负数,即返回-1,如果是negative(-1)则返回结果是1
UDFOPPositive 表示positive(1) 表示 返回该参数,即返回1.如果是positive(-1)则返回结果是-1

UDFOPBitOr 表示a | b 按位操作 例如 3|5 = 7
UDFOPBitAnd 表示 a & b 按位与操作,例如3 & 5 = 1
UDFOPBitNot 表示按位的非操作,即~0 = -1
UDFOPBitXor 表示3^5 = 2

GenericUDAFComputeStats和StringNumDistinctValueEstimator 用于distinct语法实现

19.substr(String,int pos,int lenth) 截取字符串
  例如:substr("abcd",1,2) 返回ab
    substr("abcd",2) 返回bcd,表示从2开始一直到结尾
 注意,pos下标从1开始计算,length表示最终获取多少个数字

19_2.split(url, '\\?')[0]; 让url按照?号拆分成数组,获取第0个元素
size(split(ids,'_')) = 3

20.parse_url(String url,String key) 从url中解析执行部分,然后返回截取的指定地方
   第二个参数是:HOST, PATH, QUERY, REF, PROTOCOL, AUTHORITY, FILE
  例如
 1.parse_url('http://facebook.com/path/p1.php?query=1','HOST') 返回值是facebook.com
 2.parse_url('http://facebook.com/path/p1.php?query=1','QUERY') 返回值是query=1
 3.parse_url('http://facebook.com/path/p1.php?query=1','QUERY','query') 返回值是1

21.三元if判断
if( Test Condition, True Value, False Value ) 
Example: if(1=1, 'working', 'not working') returns 'working'
count(distinct if(f.picture is not null and trim(f.picture) != '',f.id,null)) as xxx
22.返回第一个不是null的值
COALESCE( value1,value2,... ) 返回第一个不是null的值,如果都是null,则返回null
例如COALESCE(cur_plus_money,0)
23.case when then else 语法
a。
CASE Fruit
  WHEN 'APPLE' THEN 'The owner is APPLE'
  WHEN 'ORANGE' THEN 'The owner is ORANGE'
  ELSE 'It is another Fruit'
END
这个前提必须是所有when的类型都是Fruit字段。
如果when比较的类型不同,则改用下面模式
b.
CASE 
  WHEN Fruit = 'APPLE' THEN 'The owner is APPLE'
  WHEN Fruit = 'ORANGE' THEN 'The owner is ORANGE'
  ELSE 'It is another Fruit'
END

24.percentile_approx函数 org.apache.hadoop.hive.ql.udf.generic.GenericUDAFPercentileApprox
会对id列的值进行分桶,分到第三个桶中,获取10分位数中百分比是3%,30%,50%最接近的数值,得到的结果与自己知道的概率比较,看看差距是否很大,差距越大,越说明异常值较多
select percentile_approx(id, array(0.03,0.3,0.5), 10) from dim_temporary.test;
该方法占用内存,因此大数据的时候越来越慢,因为内存不断的在释放,回收
其实percentile_approx还有一个参数B：percentile_approx(col, p，B)，参数B控制内存消耗的近似精度，B越大，结果的准确度越高。默认为10,000。当col字段中的distinct值的个数小于B时，结果为准确的百分位数

上面的描述不太准确,因为没有被我证明,但是下面的内容是可以的,已经被证明的。
percentile_approx 分位数使用
a.例如percentile_approx(grade, 0.95) 取得排位在倒数第5%的成绩。
b.要求多个分位数时，可以把p换为array()，即
### 销售额,获取2、20、40、60、80分位数
percentile_approx(price, array(0.02,0.2,0.4,0.6,0.8)) result
c.结果为
result [7.615431372703787E-4,7.615431372703787E-4,7.615431372703787E-2,11.897465674478463,108.55868087880633]
d.select如何使用结果
select result[3]  = 11.897465674478463
e.explode(percentile_approx(cast(col as double),array(0.05,0.5,0.95),9999))as percentile 结果转换为列

set compatible.grammar=sparksql;例子
select explode(array(1,1,1,1,1,0.5,0.5,0.5,0.5,0.5,0,0,0,0,0)) as id
输出:每一个元素都是一行数据,用id表示该列name

f.占用内存，慎用,可以用窗口函数CUME_DIST代替。

select *
from 
(
  select price,CUME_DIST() OVER(PARTITION BY 'a' ORDER BY price) AS rn
  from 表
) a
  where rn >= 0.8 ## 获取80%的分位数点
  order by rn 
  limit 100

或者
select 
min(if(rn >= 0.1,col,NULL)),
min(if(rn >= 0.2,col,NULL)),
min(if(rn >= 0.3,col,NULL)),
min(if(rn >= 0.4,col,NULL)),
min(if(rn >= 0.5,col,NULL)),
min(if(rn >= 0.6,col,NULL)),
min(if(rn >= 0.7,col,NULL)),
min(if(rn >= 0.8,col,NULL)),
min(if(rn >= 0.9,col,NULL)),
min(if(rn >= 0.99,col,NULL)),
count(*) ### 总数
from
(
  select *,
  CUME_DIST() OVER(PARTITION BY 'a' ORDER BY col) AS rn ###占比
  from biao
  where col > 0
) a



### 计算100个分位数点
select 
percentile_approx(if(col>0,clo,NULL),array(0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1 ,
      0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.2 , 0.21,
      0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29, 0.3 , 0.31, 0.32,
      0.33, 0.34, 0.35, 0.36, 0.37, 0.38, 0.39, 0.4 , 0.41, 0.42, 0.43,
      0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5 , 0.51, 0.52, 0.53, 0.54,
      0.55, 0.56, 0.57, 0.58, 0.59, 0.6 , 0.61, 0.62, 0.63, 0.64, 0.65,
      0.66, 0.67, 0.68, 0.69, 0.7 , 0.71, 0.72, 0.73, 0.74, 0.75, 0.76,
      0.77, 0.78, 0.79, 0.8 , 0.81, 0.82, 0.83, 0.84, 0.85, 0.86, 0.87,
      0.88, 0.89, 0.9 , 0.91, 0.92, 0.93, 0.94, 0.95, 0.96, 0.97, 0.98,
      0.99))
from biao

一行形式:
percentile_approx(if(col>0,clo,NULL),array(0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1 ,0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.2 , 0.21,0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29, 0.3 , 0.31, 0.32,0.33, 0.34, 0.35, 0.36, 0.37, 0.38, 0.39, 0.4 , 0.41, 0.42, 0.43,0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5 , 0.51, 0.52, 0.53, 0.54,0.55, 0.56, 0.57, 0.58, 0.59, 0.6 , 0.61, 0.62, 0.63, 0.64, 0.65,0.66, 0.67, 0.68, 0.69, 0.7 , 0.71, 0.72, 0.73, 0.74, 0.75, 0.76,0.77, 0.78, 0.79, 0.8 , 0.81, 0.82, 0.83, 0.84, 0.85, 0.86, 0.87, 0.88, 0.89, 0.9 , 0.91, 0.92, 0.93, 0.94, 0.95, 0.96, 0.97, 0.98, 0.99)),


### 根据计算好的分位数点,计算两个分位数点.
select lie[0]
from
(
  ### 计算分位数
  percentile_approx(if(列 > 0,列,NULL),array(0.2 , 0.95)) lie,
)

25.时间函数

字符串转换成时间戳
presto:
to_unixtime(cast('2016-08-25' as timestamp)) , ### 10位
to_unixtime(cast('2016-08-25 13:02:03' as timestamp)) ### 10位
to_unixtime(cast('2016-08-25 13:02:03.771' as timestamp)) ### 13位

hive:
select unix_timestamp(cast('2016-08-25 13:02:03' as timestamp))


核心:各种格式转换成date.然后date再转换成各种格式
presto:
a.比如yyyyMMmm 转换成yyyy-MM-mm
   d = date_parse('20191125','%Y%m%d') 按照输入的给定格式先转换成date
   format_datetime(d,'yyyy-MM-dd HH:mm:ss') 将date转换成输出的格式
   
b.日期追加n天  输入是yyyyMMmm  输出是yyyyMMmm
d = date_parse('20191125','%Y%m%d') 按照输入的给定格式先转换成date
d_result = date_add('day',-1, d) 对d进行加减转换--输出date类型
format_datetime(d_result,'yyyyMMdd') 将date类型结果转换成最终输出格式

比如:format_datetime(date_add('day',-1, date_parse('20191125','%Y%m%d')),'yyyyMMdd')  输出20191124

hive  presto有date_parse函数可以把任意字符串转换成date,这个是优点,但hive没有,因此比较麻烦
      因此presto的策略是将任何字符串都转换成date,然后再操作,而hive的策略是将任意字符串转换成10位的时间戳,然后再操作。  
c.将10位的int转换成任意字符串的功能
注意:
如果时间戳是13位的,要手动/1000处理。
参数可以有三种类型,日期字符串类型+日期格式、date类型、时间戳类型

比如:select from_unixtime(1546831968,'yyyyMMdd HH:mm:ss') from dual; 比如1546831968 转换成2019-01-07 11:32:48


d.unix_timestamp 将任意字符串转换成时间戳 --- 当前时间戳
注意:输出的时间戳是10位的
select unix_timestamp() from dual; 没有参数 表示获取此时的10位的时间戳  1603788901
from_unixtime(unix_timestamp(),'yyyyMMdd HH:mm:ss') 将当前时间戳转换成任意日期格式
select unix_timestamp('20160825 13:02:03','yyyyMMdd HH:mm:ss') from dual; 将参数的字符串转换成10位的时间戳
select unix_timestamp(cast(1546831968*1000 as timestamp)),###输出 1546831968000,即时间戳可以作为参数转换成timestamp对象---主要说明参数可以是timestamp类型,但是如果是long类型的时候,要保证参数一定是13位的
concat('$now.delta(1).date',from_unixtime(unix_timestamp(),' HH:mm:ss')) ### 公司内部函数,任意日期+规定时间组合 yyyyMMdd HH:mm:ss

e.from_unixtime -- 将时间戳转换成任意字符串
比如yyyyMMmm 转换成yyyy-MM-mm --- 将任意字符串 转换成另外字符串
即先将字符串转换成时间戳,然后再转换成字符串
long l = unix_timestamp('20160825','yyyyMMdd') 字符串转换成时间戳
from_unixtime(l,'yyyy-MM-dd') 时间戳转换成字符串
比如 select from_unixtime(unix_timestamp('20160825','yyyyMMdd'),'yyyy-MM-dd') 将20160825转换成2016-08-25

f.日期追加n天  输入是yyyyMMmm  输出是yyyyMMmm
long l = unix_timestamp(dt,'yyyyMMdd') +n*3600*24 字符串转换成时间戳 并且 加N天
from_unixtime(l,'yyyyMMdd') 时间戳转换成字符串
比如  from_unixtime(unix_timestamp(dt,'yyyyMMdd')+7*3600*24,'yyyyMMdd') ---20171205 + 7天后，返回20171212格式 类似date_add功能

或者 date_add(cast(unix_timestamp('20160817','yyyyMMdd')*1000 as timestamp) ,1),### 2016-08-18 当前参数日期转化时间戳后+1天
val d = date_add(from_unixtime(1546831968,'yyyy-MM-dd'),1),###10位时间int值加减法

from_unixtime(unix_timestamp(d,'yyyy-MM-dd'),'yyyyMMdd')

g.date_add('2016-08-17',1) 
输出一定是yyyy-MM-dd形式的字符串, 但输入可以是三种类型:yyyy-MM-dd字符串,date、timestamp。

比如
date_add('2016-08-17',1),### 2016-08-18   ---- 字符串进行加减
date_add(cast(unix_timestamp()*1000 as timestamp),1) ,### 2019-12-24 当前时间戳+1天  ---- 时间戳进行加减
date_add(cast(unix_timestamp()*1000 as timestamp),-1); 获取当前的时间戳,相当于mysql的DATE_ADD(DATE_FORMAT(NOW(),'%Y-%m-%d'),INTERVAL -1 DAY)
date_add(from_unixtime(1546831968,'yyyy-MM-dd'),1),###10位时间int值加减法

简单的总结,date_add中参数必须是bigint类型的,并且是13位的,才能转换成timestamp。
如果看着像bigint,但也不能直接参与*1000,也会有异常。

第1种case
date_add(cast(1546831968*1000 as timestamp),1),### 输出错误,因为这么直接乘,系统不认识,必须强转成long
date_add(cast(cast(1546831968 as bigint)* 1000 as timestamp),1), ###输出正确 2019-01-08

第2种case
date_add(cast(1546831968 as timestamp),1),### 输出错误,因为不是13位的long
date_add(cast(1546831968000 as timestamp),1),### 2019-01-08 参数必须是13位的long,

第3种case
date_add(cast(ctime as timestamp),1),### 输出错误,因为ctime是10位的,需要转换成13位
date_add(cast(ctime * 1000 as timestamp),1),### 正确,因为ctime是bigint,可以直接*1000
ctime ### 572795678 即 2019-11-03 23:41:18


demo:
select from_unixtime(cast(1495037476000/1000 as bigint),'yyyy/MM/dd HH:mm:ss');
select from_unixtime(INT(substr(occurrencetime,0,10)),'yyyy-MM-dd HH:mm:ss') //即只要时间戳的前10位,并且字符串转换成int
select unix_timestamp(substr(create_time,0,19),'yyyy-MM-dd HH:mm:ss') 将日期时间字符串形式,格式为第二个参数的形式,转换成时间戳,但是该时间戳要*1000才是真的时间戳



互相转换
from_unixtime(unix_timestamp(dt,'yyyyMMdd'),'yyyy-MM-dd') yyyyMMdd 转换成 yyyy-MM-dd
to_unixtime(cast('2016-08-25' as timestamp)) 转换成10位的时间戳,字符串转换时间戳

  select from_unixtime(1546831968,'yyyyMMdd HH:mm:ss'), ### 2019-01-07 11:32:48
  from_unixtime(1546831968,'yyyyMMdd')  ### 20190107,
  '-----'
  unix_timestamp('20160825 13:02:03','yyyyMMdd HH:mm:ss'),### 1472101323
  unix_timestamp(),### now对应的时间戳
  unix_timestamp(cast(1546831968*1000 as timestamp)),###1546831968000,即时间戳可以作为参数转换成timestamp对象
  from dual;

--------------
presto format_datetime(from_unixtime(ctime),'yyyy-MM-dd HH:mm:ss')  将时间戳转换成成date类型,然后再转换成任意字符串形式
  1.将20191125进行日期加减,返回date类型
  date_add('day',-1,date '2018-04-22') ### 2018-04-21
  date_add('day',-1, date_parse('20191125','%Y%m%d'))  ### 2019-11-24 00:00:00  注意:此时返回的结果都是date类型的
  format_datetime(date类型,'yyyy-MM-dd HH:mm:ss')  ### date类型转换成任意类型字符串形式
  2.date_parse('20191125','%Y%m%d') ### 2019-11-24 00:00:00 字符串日期转换成date类型
  3.datekey2date(last_visit_time) ### 将20191125转换成2019-11-25
  4.DATEDIFF('2019-11-25','2019-11-27') ### 返回日期差多少天
  5.to_date(date_add('day',-1, date_parse('20191125','%Y%m%d'))) ### 输出2019-11-24 含义:日期20191125减去1天后,字符串格式输出
------hive
datekey2date('20170901') 输出 2017-09-01 我没有看到hive源代码里面有这个函数,所以不清楚是哪个版本有的函数,使用的时候需要测试一下该环境是否有该函数

to_date(from_unixtime(ctime)) 时间戳转换成 2019-01-07 ----其实原理是截取yyyy-MM-dd开头的字符串。因为from_unixtime(ctime)输出是yyyy-MM-dd开头的字符串,所以是可以成功转换的,即参数内不是date类型的


输入20160613 输出 2016-06-13
strDateFormat('20160613','yyyyMMdd','yyyy-MM-dd')

select date_format('2017-05-06','yyyy'); 返回2017

输入2016-06-14 输出 2016-06-13
select date_add('2016-06-14',-1)


输入2016-06-14 输出 20160613
select strDateFormat(date_add('2016-06-14',-1),'yyyy-MM-dd','yyyyMMdd')

输入20160613 输出2016-06-14
select date_add(strDateFormat('20160613','yyyyMMdd','yyyy-MM-dd'),1)


select datediff('2016-08-17','2016-08-17');//0 获取两个时间之差几天
select datediff('2016-08-17','2016-08-19');//-2 获取两个时间之差几天

select datediff('2016-08-17','2015-01-09') % 7;//5 获取两个时间之差几天%7,表示距离2015-01-09这天周五来说,今天是周几
select date_add('2016-08-17',-(datediff('2016-08-17','2015-01-09') % 7)) 返回值是与2016-08-17最近的周五,用于group by操作

select from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss'); 当前时间转换成yyyy-MM-dd HH:mm:ss格式 --- unix_timestamp表示返回当前时间戳
select date_add(cast(unix_timestamp()*1000 as timestamp),-1); 获取当前的时间戳,相当于mysql的DATE_ADD(DATE_FORMAT(NOW(),'%Y-%m-%d'),INTERVAL -1 DAY)



判断周六
datediff('2017-07-21','2017-06-04') % 7 == 5  如果2017-06-04是周一,那么判断2017-07-21是否是周6
datediff('2017-07-22','2017-06-04') % 7 == 6  如果2017-06-04是周一,那么判断2017-07-22是否是周7

获取每个月的最后一天
select interest_date,date_add(interest_date,-1) lastday //1号数据-1天就是上个月最后一天数据
from history_union_rate //所有日期数据表
where interest_date like '%-01' //获取每个月的第一天,即1号的数据

计算环比
输入当前周期内的开始时间2018-01-08,结束日期2018-01-15,因此要查询当前时间段的数据，也要查询上一个周期内的数据,即[2018-01-01，2018-01-07]
如何计算[2018-01-01，2018-01-07]呢?
where dt between date_sub($$begindatekey{-1d}-DATEDIFF(endtime-begintime)) and '$$begindatekey{-1d}'
即先计算周期,然后当前时间-1天,减去一个周期，就是2018-01-01


25.强制转换 cast as
cast(actid as String)
select from_unixtime(cast(1495037476000/1000 as bigint),'yyyy/MM/dd HH:mm:ss');
select from_unixtime(INT(substr(occurrencetime,0,10)),'yyyy-MM-dd HH:mm:ss') //即只要时间戳的前10位,并且字符串转换成int
select unix_timestamp(substr(create_time,0,19),'yyyy-MM-dd HH:mm:ss') 将日期时间字符串形式,格式为第二个参数的形式,转换成时间戳,但是该时间戳要*1000才是真的时间戳

26.json
a.SELECT get_json_object('{"store":{"fruit":\[{"weight":8,"type":"apple"},{"weight":9,"type":"pear"}],"bicycle":{"price":19.95,"color":"red"}},"email":"amy@only_for_json_udf_test.net", "owner":"amy"}', '$.owner');
打印 amy
b.hive> SELECT get_json_object('{"store":{"fruit":\[{"weight":8,"type":"apple"},{"weight":9,"type":"pear"}],"bicycle":{"price":19.95,"color":"red"}},"email":"amy@only_for_json_udf_test.net", "owner":"amy"}', '$.store.fruit\[0]');
打印 {"weight":8,"type":"apple"}
c.打印数组全部内容 --->$.store.fruit
d.获取数组内容
select get_json_object('{"store":{"fruit":[{"weight":8,"type":"apple"},{"weight":9,"type":"pear"}],"bicycle":{"price":19.95,"color":"red"}},"email":"amy@only_for_json_udf_test.net", "owner":"amy"}', '$.store.fruit[*].weight')
输出 [8,9] 数组形式
注意:如果 json上来就是数组,则使用$[*]比如get_json_object(json,'$[*].table')

d.两个数组,比如[数据库1,数据库2,数据库3] [表1,表2,表3] ,按照数组顺序,组装成数据库.表
set compatible.grammar = sparksql;
select *
from 
(
  select tables,
  get_json_object(tables,'$[*].db') db_arr,
  get_json_object(tables,'$[*].table') table_arr
  from biao
) c 
lateral view posexplode(split(db_arr, ',')) a1 as a,db 
lateral view posexplode(split(table_arr, ',')) a2 as b,tab
where a = b

注意:
解析失败的,或者没有找到节点path路径的,则返回NULL


27.sort_array(array(1, 2, 2,5, 3, 3)); 对数据排序,大多数情况下使用在group by中

28.str_to_map  将一个字符串转换成map字段类型
ql/udf/generic/GenericUDFStringToMap---返回值是Map<Object,Object>形式的

字段定义 hobby map<string,string>
Map类型如何获取数据 hobby["country"]

例如:str_to_map('a=b c=d f=e',' ','=')
解释:空格拆分成一个key=value,然后=号拆分key和value
第一个参数表示字符串内容，第二个参数表示如何拆分成一个map元素，第三个参数为如何将一个元素拆分成key=value形式

默认二参数为逗号，三参数为冒号
str_to_map(concat_ws(',',concat('key',':',value))
str_to_map(concat_ws(',',collect_set(concat_ws(':',a.寄件省份,cast(a.件量 as string))))) 

如果使用默认值,和:,那么hive直接select该字段的结果就是json类型的,但是presto输出的结果不是json,而是{aa=bb,cc=dd}形式
默认值COALESCE(jsonmap,str_to_map(concat_ws(',',concat('key',':','none')))) --注意第二个参数才是map的默认值形式
或者默认值就设置为null也没什么

demo:
insert overwrite table dim_temp.temp_user
select fu.user_mobile ,rp.userid ,  str_to_map(concat_ws(',',collect_set(concat( starttime,"#", endtime)) ),',','#') wfre
from database.preference_hour rp
left join database.user_info fu on rp.userid = fu.userid and length(user_mobile) = 11
where  rp.log_type='invest'
group by user_mobile,rp.userid
需求说明:查询每一个用户手机号码 以及 用户在一天内哪些小时是有投资偏好的
因为投资偏好是一个用户多条数据,比如
userid1  20:25-22:15
userid1  23:25-23:45
因此需要整合一个user对应的偏好变成map,key是开始时间,value是结束时间

sql说明:
a.collect_set(concat( starttime,"#", endtime))
按照userid分组,收集所有的开始时间和结束时间,用#代替key与value的分隔符
b.将用户的所有信息集合拼接成字符串,每一组用,号分割,即concat_ws(',', collect_set)
c.对集合进行拆分成map
str_to_map(map字符串,',','#'),即字符串使用,号进行拆分每一个key=value,然后key=value用#拆分

具体查询sql需求:查询属于某一个时间段内满足条件的用户
select *
from dim_temp.temp_user
LATERAL VIEW explode(preference_hour) et as k,v
where k<='21:00' and v>='21:00'
即将一个user的所有时间map进行拆分,获取k和v表示开始时间和结束时间.然后分别过滤k和v,有一组满足条件的,则都被返回该数据

29.随机数
select *
from
(
  select id,
  row_number() over (partition by 1 order by rand desc) row_number
  from
  (
    select id,rand(100) as rand ### 0-100之间随机数
    from biao
  ) a
) b
where row_number <= 10000

或者
select id,rand(100) as rand
from biao
order by rand desc
limit 1000

30.explode() --- 参见窗口函数中使用情况
可以将一根array或者map作为输入,每一个元素都作为一个单独的行,进行输出,
属于UDTF函数,可以被用于select表达式和LATERAL VIEW表达式里
a.select explode(split('a,b,c,d',',')) as col;输出四行,分别是a b c d,其中split('a,b,c,d',',')表示对字符串按照逗号拆分成数组array对象
b.SELECT explode(myMap) AS (myMapKey, myMapValue) FROM myMapTable; 用于map
c.
[{“eid”:”38”,”ex”:”affirm_time_Android”,”val”:”1”,”vid”:”31”,”vr”:”var1”},
{“eid”:”42”,”ex”:”new_comment_Android”,”val”:”1”,”vid”:”34”,”vr”:”var1”},
{“eid”:”40”,”ex”:”new_rpname_Android”,”val”:”1”,”vid”:”1”,”vr”:”var1”},
{“eid”:”19”,”ex”:”hotellistlpage_Android”,”val”:”1”,”vid”:”1”,”vr”:”var01”},
{“eid”:”29”,”ex”:”bookhotelpage_Android”,”val”:”0”,”vid”:”1”,”vr”:”var01”},
{“eid”:”17”,”ex”:”trainMode_Android”,”val”:”1”,”vid”:”1”,”vr”:”mode_Android”},
{“eid”:”44”,”ex”:”ihotelList_Android”,”val”:”1”,”vid”:”36”,”vr”:”var1”},
{“eid”:”47”,”ex”:”ihotelDetail_Android”,”val”:”0”,”vid”:”38”,”vr”:”var1”}

用explode小试牛刀一下：

select explode(split(regexp_replace(mvt,'\\[|\\]',''),'\\},\\{')) from ods where day=20160710 limit 10;1

首先对字符串mvt取消[或者],然后按照{,}进行拆分,成数组array

最后出来的结果如下： 
{“eid”:”38”,”ex”:”affirm_time_Android”,”val”:”1”,”vid”:”31”,”vr”:”var1”
“eid”:”42”,”ex”:”new_comment_Android”,”val”:”1”,”vid”:”34”,”vr”:”var1” 
“eid”:”40”,”ex”:”new_rpname_Android”,”val”:”1”,”vid”:”1”,”vr”:”var1” 
“eid”:”19”,”ex”:”hotellistlpage_Android”,”val”:”1”,”vid”:”1”,”vr”:”var01”
“eid”:”29”,”ex”:”bookhotelpage_Android”,”val”:”0”,”vid”:”1”,”vr”:”var01” 
“eid”:”17”,”ex”:”trainMode_Android”,”val”:”1”,”vid”:”1”,”vr”:”mode_Android” 
“eid”:”44”,”ex”:”ihotelList_Android”,”val”:”1”,”vid”:”36”,”vr”:”var1” 
“eid”:”47”,”ex”:”ihotelDetail_Android”,”val”:”0”,”vid”:”38”,”vr”:”var1”}

30.ateral view --- 参见窗口函数中使用情况
select *  from A LATERAL VIEW posexplode(shows) t as pos, city 拆分成2部分，一个是序号，一个是具体的拆分字符

原理
SELECT dt,myCol
from date
LATERAL VIEW explode(array()) myTab as myCol

输出结果:无。
相当于date表每一行数据，与explode的结果进行inner join。
当explode结果是空时,自然inner join是无结果的。


SELECT dt,myCol
from date
LATERAL VIEW OUTER explode(array()) myTab as myCol
输出结果:20210910	NULL
相当于date表每一行数据，与explode的结果进行left join。
当explode结果是空时,自然left join是有结果的,只是拼接的myTab表数据是null


a.语法
LATERAL VIEW udtf(expression) tableAlias AS columnAlias (‘,’ columnAlias)* 
即LATERAL VIEW 后面追加explode函数,得到新的表,别名是tableAlias,该表的字段是columnAlias,或者可能map结果的key，value,用逗号拆分即可
FROM baseTable (lateralView)*
LATERAL VIEW 语法用于from table 后面

b.Lateral view 是将一个已经存在的表,每一行数据追加一列或者若干列数据,数据的来源是Lateral view后面的将一个字段转换成多行数据的结果
c.demo
比如一个表pageAds,有两个字段,一个pageid表示页面name,adid_list表示该页面有哪些元素,是一个数组
比如2行数据
frontpage  [1，2，3]
backpage  [4，5，5]

SELECT pageid, adid
FROM pageAds LATERAL VIEW explode(split('1,2',',')) adTable AS adid;
即查询pageAds表,以及pageAds表每一行 join adTable表,2️而adTable表的熟悉是adid,内容是对adid_list进行explode的结果

输出
frontpage 1
frontpage 2
frontpage 3
backpage 1
backpage 2
backpage 3

d.结果可以作为子查询的结果,进一步被from处理
比如
select *
from
(
SELECT pageid, adid
FROM pageAds LATERAL VIEW explode(adid_list) adTable AS adid;
) a
order by adid

e.接入多个LATERAL VIEW语法,其实是在做笛卡尔乘机
比如table字段 Array<int> col1   Array<string> col2
例如
[1, 2] [a", "b", "c"]
[3, 4] [d", "e", "f"]

SELECT myCol1, myCol2 FROM baseTable
LATERAL VIEW explode(col1) myTable1 AS myCol1
LATERAL VIEW explode(col2) myTable2 AS myCol2;

或者
SELECT myCol1, myCol2 FROM baseTable
LATERAL VIEW explode(split('1,2',',')) myTable1 AS myCol1
LATERAL VIEW explode(split('a,b,c,d',',')) myTable2 AS myCol2;

笛卡尔结果是
1 "a"
1 "b"
1 "c"
2 "a"
2 "b"
2 "c"
3 "d"
3 "e"
3 "f"
4 "d"
4 "e"
4 "f"

f.Outer Lateral View
如果UDTF转换的Array是空的怎么办呢？
如果加上outer关键字，则会像left outer join 一样，还是会输出select出的列，而UDTF的输出结果是NULL。
hive> select * FROM test_lateral_view_shengli LATERAL VIEW explode(array()) C AS a ;
结果是什么都不输出。

如果加上outer关键字：
SELECT * FROM src LATERAL VIEW OUTER explode(array()) C AS a limit 10;

238 val_238 NULL
86 val_86 NULL
311 val_311 NULL
27 val_27 NULL
165 val_165 NULL
409 val_409 NULL

31.corr 相关性函数
select corr(字段1,字段2) 
from biao
针对表内所有的信息,来做相关性分析。

32.with as 用于存储临时表
with t1 as
(
   select *
   from xxxx
   where dt = '20201221'
),
t2 as
(
  select count(*) c,count(if(valid=1,1,NULL)) c2,count(if(valid!=1,1,NULL)) c3
  from t1
)
INSERT OVERWRITE TABLE xxx  PARTITION (dt='xxx')
select *
from t2

等于
select count(*) c,count(if(valid=1,1,NULL)) c2,count(if(valid!=1,1,NULL)) c3
from xxx
where dt = '20201221'

----临时表后面跟字段，不过感觉意义不大，暂时可忽略
with table1 (lable,b) as (
  select label,1 b
  from biao
  where label = 1
),
table2 (lable,b) as (
  select label,2 b
  from biao
  where label = 0
)
  select * from table1 
union all 
  select * from table2
  
33.参见源码GenericUDFInstr 相当于 indexof contain 
instr(name,brand_name) > 0
SELECT _FUNC_('Facebook', 'boo') FROM src LIMIT 1;\n" + "  5")

34.交集、并集、差集的sql形式
union all 并集
join 交集
a left join b on b is null 差集  A存在,B不存在

35.JSON_TUPLE(STRING json,STRING key1,STRING key2,...)
用于一个标准的JSON字符串中，按照输入的一组键（key1,key2,...）JSON抽取各个键指定的字符串。要求输入和输出都是String,输入必须是json。
GenericUDTFJSONTuple extends GenericUDTF 

例子:
Table：school
+------------+------------+
| Id         | json       |
+------------+------------+
| 1          | {
                "校名": "湖畔大学",
                "地址":"杭州",
                "SchoolRank": "00",
                "Class1":{
                  "Student":[{
                    "studentId":1,
                    "scoreRankIn3Year":[1,2,[3,2,6]]
                  }, {
                    "studentId":2,
                    "scoreRankIn3Year":[2,3,[4,3,1]]
                }]}
               }          |
+------------+------------+
select json_tuple(school.json,"SchoolRank","Class1") as (item0,item1) from school;
-- 本语句相当于下面的语句。
select get_json_object(school.json,"$.SchoolRank") item0,get_json_object(school.json,"$.Class1") item1 from school;
+-------+-------+
| item0 | item1 |
+-------+-------+
| 00    | {"Student":[{"studentId":1,"scoreRankIn3Year":[1,2,[3,2,6]]},{"studentId":2,"scoreRankIn3Year":[2,3,[4,3,1]]}]} |
+-------+-------+

支持多层嵌套的JSON数据解析。
select sc.Id, q.item0, q.item1
from school sc LATERAL VIEW json_tuple(sc.json,"Class1.Student.[*].studentId","Class1.Student.[0].scoreRankIn3Year") q as item0,item1;
返回结果如下。
+------------+-------+-------+
| id         | item0 | item1 |
+------------+-------+-------+
| 1          | [1,2] | [1,2,[3,2,6]] |
+------------+-------+-------+

支持包含多重嵌套的数组的JSON数据解析。
select sc.Id, q.item0, q.item1
from school sc LATERAL VIEW json_tuple(sc.json,"Class1.Student[0].scoreRankIn3Year[2]","Class1.Student[0].scoreRankIn3Year[2][1]") q as item0,item1;
返回结果如下。
+------------+-------+-------+
| id         | item0 | item1 |
+------------+-------+-------+
| 1          | [3,2,6] | 2     |
+------------+-------+-------+

二、generic
1.对case column when a then b else c end 形式进行处理
 注意:
 a类型必须是boolean类型的
 b和c必须返回值类型相同
2.a op b表示a与b进行比较大小,比较,因此一定是两个参数进行比较
3.反射的方法执行java的类
a.org.apache.hadoop.hive.ql.udf.generic.GenericUDFReflect.GenericUDFReflect
demo:
select reflect("java.net.URLDecoder", "decode",column) from biao;
SELECT reflect("java.net.URLDecoder", "decode", "%E6%B2%A1%E4%BB%80%E4%B9%88%E6%83%B3%E8%AF%B4%E7%9A%84%E5%B0%B1%E6%83%B3%E6%94%925%E4%B8%87%E5%9D%97%E9%92%B1");

函数声明,类全路径,方法名称,[所需要的参数数组集合]
@Description(name = "reflect",
  value = "_FUNC_(class,method[,arg1[,arg2..]]) calls method with reflection",

b.org.apache.hadoop.hive.ql.udf.generic.GenericUDFReflect.GenericUDFReflect2
与reflect方法不同的是,reflect方法要求第一个参数一定是一个类,而字段内容是该类的一个参数,
但是如果要针对某一个字段内容进行操作,则没办法.因此有了reflect2方法,第一个参数可以是字段内容,该字段类型是java基础类型的即可
 demo:将所有的空格替换成|字符
select reflect2("aaa		aa", "replaceAll","\\s+","|");
select reflect2(source, "replaceAll","\\s+","|");

注意:
1.在脚本中 hive <<EOF 执行的时候尽量不用双引号,要用单引号  即 select reflect2(source, 'replaceAll','\\\\s+','|');
2.在脚本中 hive <<EOF 执行的时候,要对\进行转义,即\\s+要改成\\\\s+

三、复杂对象
1.Array
a.建表语句 tag_ids_arr array<String> COMMENT 'tag_ids',
b.生成语法:
1).collect_list(cast(tag_id as string)) tag_ids_arr
2).split('hello world,zhuoru',' |,') 按照空格或者逗号拆分成array
3).array('facebook', 'com')
c.如何使用array
1).size(collect_set(age)) 计算array的size
2).collect_set(app_name)[0] 获取array的某一个元素
3).array_contains(array arr, element) 即如果element在array里面存在,则返回true。注意:array和element必须是相同的类型
demo:SELECT array_CONTAINS(ARRAY(0,1),0) returns true
参见:org.apache.hadoop.hive.ql.udf.generic.array_contains
demo:确保id的值一定不是1,2,3,4,5中的一个
where !array_contains(split('1,2,3,4,5',','),cast(id as string))
等于 where id!=1 and id !=2 and id!=3 and id!=4 and id!=5

2.named_struct
a.named_struct("tag_id",tag_id,"versions",versions)) 创建一个对象,对象的列是tag_id和versions
b.注意该对象打印出来的结果是{"tag_id":1,"versions":["v0.0.0"]} json形式,但如果cast(对象 as string) 结果不是json,而是数组形式,即[1,"v0.0.0"]
c.要实现一个udf函数,将对象转换成json。好在内部已经实现了,但好像开源官方的没有实现。
to_json(named_struct("tag_id",tag_id,"versions",versions)))

### 子查询是set,然后外层又是一个set数组，都拼凑完后,整体to_json,这样的结果不会有\转义字符
select id,
to_json(collect_set(named_struct('place_code',place_code,'place_code_strategy_json',place_code_strategy_set))) strategy_id_rank_json
from
(
select id,place_code,collect_set(named_struct('strategy_id',strategy_id,'rank',rank)) place_code_strategy_set
)
输出格式:
[{"place_code":"101","place_code_strategy_json":[{"strategy_id":1,"rank":1}]},{"place_code":"003","place_code_strategy_json":[{"strategy_id":2,"rank":1},{"strategy_id":3,"rank":2}]}]

d.如何根据named_struct生产json字符串
表字段:id tagid version
### 计算每一个id的json对象
select id,
cast(collect_set(to_json(named_struct("tag_id",tag_id,"versions",versions))) as string) json
from
(
  ### 计算每一个 id,tagid 的版本集合
  select id,tagid,collect_set(version) versions
  from biao
  group by id,tagid
) a
group by id
输出:
[{"tagid":1,"versions":["v1.0.0","v0.0.0"]},
 {"tagid":2,"versions":["v0.0.0","v1.0.0"]}]
 
注意:
因为collect_set返回值本身就是数组[],所以将其直接cast as string后,就自带[]字符串。
避免了使用concat('[',concat_ws(',',collect_set(to_json(a))),']')方式,将数组对象自己添加[],同时又添加数组元素的分隔符逗号。上面的方式比较简单。


e.转换成{}格式
to_json(named_struct("score",feature_score.score,"value",round(feature_score.value * 100,1)))
f. 转换成[]格式
select id,collect_list(to_json(labels)) as jsonArr
from
(
    select id,named_struct('index_desc', index_desc) labels
    from xx
) t1
group by id

g.动态组成json字符串,name和value是查询出来的两个字段
str_to_map(concat_ws('-', collect_set(concat(name,':',value))),'-',':') 
四、show functions
show functions; 查看全部函数
show functions like "xpath_shor*";  模糊查询以什么开头的函数
desc  FUNCTION "xpath_short"; 查看该函数的详细参数信息以及说明
