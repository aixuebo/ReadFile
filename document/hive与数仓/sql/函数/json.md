# 背景与总结


# 一、基础用法
## 1.get_json_object('字段','$.owner')
SELECT get_json_object('{"store":{"fruit":\[{"weight":8,"type":"apple"},{"weight":9,"type":"pear"}],"bicycle":{"price":19.95,"color":"red"}},"email":"amy@only_for_json_udf_test.net", "owner":"amy"}', '$.owner');
打印 amy

## 2.打印数组某一个结果
SELECT get_json_object('{"store":{"fruit":\[{"weight":8,"type":"apple"},{"weight":9,"type":"pear"}],"bicycle":{"price":19.95,"color":"red"}},"email":"amy@only_for_json_udf_test.net", "owner":"amy"}', '$.store.fruit\[0]');
打印 {"weight":8,"type":"apple"}

## 3.打印数组全部内容 --->$.store.fruit

## 4.获取数组内容 -- 很多版本是不支持该语法的
select get_json_object('{"store":{"fruit":[{"weight":8,"type":"apple"},{"weight":9,"type":"pear"}],"bicycle":{"price":19.95,"color":"red"}},"email":"amy@only_for_json_udf_test.net", "owner":"amy"}', '$.store.fruit[*].weight')
输出 [8,9] 数组形式
注意:如果 json上来就是数组,则使用$[*]比如get_json_object(json,'$[*].table')


# 二、高级用法
## 1.如何获取[]数组解析

select *
      ,get_json_object(t3.skus,'$.diffContentMap.ori_price.origin') as price_origin
      ,get_json_object(t3.skus,'$.diffContentMap.ori_price.result') as price_result
from
(
  select get_json_object(字段, '$.xxx') arr ### 先将josn中数组部分字符串提取出来
  from log
)lateral view explode(split(replace(replace(replace(arr,'},{','},,{'),'[',''),']',''),',,')) t3 as item


解析：
a = (arr,'},{','},,{')  ### },{ 转换成 },,{  即增加逗号
b = replace(a,'[','') ### 去掉[内容
c=replace(b,']','') ### 去掉]内容
split(c,',,') ### 按照,,拆分
explode()


## 2.其他逻辑案例 ：两个数组,比如[数据库1,数据库2,数据库3] [表1,表2,表3] ,按照数组顺序,组装成数据库.表
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