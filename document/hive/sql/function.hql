hive支持的数据类型
boolean、tinyint、smallint、int、bigint、float、double、decimal、string、varchar、ts、date、binary

一、普通UDF
1.concat_ws
例如ELECT concat_ws('.', 'www', array('facebook', 'com')) FROM src LIMIT 1
 返回www.facebook.com
 
连接字符串concat(string A, string B…)

2.collect_set,collect_list 该函数要配合group by语法
例如 collect_set(age),返回array,类似set,包含所有不重复的age信息
collect_list是按照出现的顺序展现结果
size(collect_set(age)),即array的值是可以放到size函数里面的
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
5.exp(double) 表示e的几次方,e表示2.718281828459045
6.E(),返回e的值2.718281828459045
7.abs(double) 获取正数,例如abs(-5.3),返回5.3
8.ascii(String) 对string进行getBytes()后,获取第一个字节对应的ascii码
如果参数为null,或者"",则返回0

9. ceil(5.3) = 6 or ceiling(5.3) = 6

10.base64(Bytes),将字节数组转换成Text对象,使用Base64.encodeBase64(bytes)方法反编译
       将字节数组按照base64反编译成字符串
11.floor(5.3) = 5 向下取整
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
注意,在脚本中 hive <<EOF 执行的时候,要对\进行转义,即\\s+要改成\\\\s+
16.rlike(string,regexp) 校验string是否匹配正则表达式
   regexp(string,regexp) 校验string是否匹配正则表达式
   case when status like '%余额不足%'
17.regexp_extract(string,regexp,index) 返回匹配正则表达式的第index个group,默认index可以省略,默认为1
例如:
regexp_extract('100-200', '(\\d+)-(\\d+)', 1) will return '100'  例子从100-200中获取第一个分组100
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

25.时间函数
输入20160613 输出 2016-06-13
strDateFormat('20160613','yyyyMMdd','yyyy-MM-dd')

select date_format('2017-05-06','yyyy'); 返回2017

输入2016-06-14 输出 2016-06-13
select date_add('2016-06-14',-1)

输入2016-06-14 输出 20160613
select strDateFormat(date_add('2016-06-14',-1),'yyyy-MM-dd','yyyyMMdd')

输入20160613 输出2016-06-14
select date_add(strDateFormat('20160613','yyyyMMdd','yyyy-MM-dd'),1)


select date_add('2016-08-17',1) //2016-08-18 对时间进行累加
select date_add('2016-08-17',1) //2016-08-16 对时间进行减少
select datediff('2016-08-17','2016-08-17');//0 获取两个时间之差几天
select datediff('2016-08-17','2016-08-19');//-2 获取两个时间之差几天

select datediff('2016-08-17','2015-01-09') % 7;//5 获取两个时间之差几天%7,表示距离2015-01-09这天周五来说,今天是周几
select date_add('2016-08-17',-(datediff('2016-08-17','2015-01-09') % 7)) 返回值是与2016-08-17最近的周五,用于group by操作

select to_date(from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss')); 获取当前的时间,相当于mysql的DATE_ADD(DATE_FORMAT(NOW(),'%Y-%m-%d'),INTERVAL -1 DAY)
select date_add(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),-1);

获取每个月的最后一天
select interest_date,date_add(interest_date,-1) lastday //1号数据-1天就是上个月最后一天数据
from history_union_rate //所有日期数据表
where interest_date like '%-01' //获取每个月的第一天,即1号的数据

25.强制转换 cast as
cast(actid as String)
select from_unixtime(cast(1495037476000/1000 as bigint),'yyyy/MM/dd HH:mm:ss');
select from_unixtime(INT(substr(occurrencetime,0,10)),'yyyy-MM-dd HH:mm:ss') //即只要时间戳的前10位,并且字符串转换成int

select unix_timestamp(substr(create_time,0,19),'yyyy-MM-dd HH:mm:ss') 将日期时间字符串形式,格式为第二个参数的形式,转换成时间戳,但是该时间戳要*1000才是真的时间戳

26.json
SELECT get_json_object('{"store":{"fruit":\[{"weight":8,"type":"apple"},{"weight":9,"type":"pear"}],"bicycle":{"price":19.95,"color":"red"}},"email":"amy@only_for_json_udf_test.net", "owner":"amy"}', '$.owner');
打印 amy
hive> SELECT get_json_object('{"store":{"fruit":\[{"weight":8,"type":"apple"},{"weight":9,"type":"pear"}],"bicycle":{"price":19.95,"color":"red"}},"email":"amy@only_for_json_udf_test.net", "owner":"amy"}', '$.store.fruit\[0]');
打印 {"weight":8,"type":"apple"}

27.sort_array(array(1, 2, 2,5, 3, 3)); 对数据排序,大多数情况下使用在group by中

28.str_to_map  将一个字符串转换成map字段类型
例如:str_to_map('a=b c=d f=e',' ','=')
解释:空格拆分成一个key=value,然后=号拆分key和value

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



29.explode() 可以将一根array或者map作为输入,每一个元素都作为一个单独的行,进行输出,
属于UDTF函数,可以被用于select表达式和LATERAL VIEW表达式里
a.select explode(split('a,b,c,d',',')) as col;输出四行,分别是a b c d,其中plit('a,b,c,d',',')表示对字符串按照逗号拆分成数组array对象
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

select explode(split(regexp_replace(mvt,'\\[|\\]',''),'\\},\\{')) from ods_mvt_hourly where day=20160710 limit 10;1

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

30.ateral view
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
FROM pageAds LATERAL VIEW explode(adid_list) adTable AS adid;
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

三、show functions
show functions; 查看全部函数
show functions like "xpath_shor*";  模糊查询以什么开头的函数
desc  FUNCTION "xpath_short"; 查看该函数的详细参数信息以及说明
