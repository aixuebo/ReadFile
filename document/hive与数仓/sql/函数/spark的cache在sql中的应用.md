cache table base OPTIONS ('storageLevel' 'DISK_ONLY')
select *
from biao
;

INSERT OVERWRITE TABLE `${target.table}` PARTITION (dt)
select  *
from base

相当于spark.cache().用于有大块逻辑的sql,进行缓存,避免下游重复使用的时候，中间过程的重复加工。
