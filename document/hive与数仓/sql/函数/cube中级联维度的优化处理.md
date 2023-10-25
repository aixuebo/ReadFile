# 背景与总结
1.维度有dim1以及一二三级城市。四个维度。
此时需要grouping sets的组合较多，为了简化代码，将级联内容转换成一个字段。

# 缺点与优点
优点:代码可读性好。
缺点:数据膨胀，比如三级级联字段，一条数据膨胀成3条，会增加cube的计算量。

# 方案
## 1.cube结果表
select coalesce(dim1, -1) dim1,##-1表示"全部"
coalesce(city_id, -1) dim1,
sum(指标1)
from
(
  ### cube初始化，不允许有null
  select coalesce(dim1, 0) dim1,
  coalesce(city_id, 0) dim1,
  指标1
  from
  (
    select dim1,
    一级城市 as city_id,
    指标1
    from 表 
  union all 
    select dim1,
    二级城市 as city_id,
    指标1
    from 表 
  union all
    select dim1,
    三级城市 as city_id,
    指标1
    from 表 
  ) t 
) t
group by dim1,city
grouping sets
(
(),(dim1),(city),(dim1,city)
)
## 2.cube结果表，关联城市维度，补充一二三级维度属性信息
假设有一个 id、一二三级id的维度表，即任意一个id，都可以找到层级维度全路径。

select 
from 1
join dim_city
on 1.city_id = dim_city.city_id
where (1.city_id = -1 or dim_city.city_id is not null) ### 去除脏数据，城市不在维度表里。比如1中城市有id=555，但其实维度表没有这个信息，则过滤掉。
