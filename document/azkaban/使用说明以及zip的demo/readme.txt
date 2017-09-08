azkaban使用
一、为了并行,因此要设置多个job
二、需求是将money_record_0到15这16个表从mysql导入到hive中
因此设置16个job.这里只是简单举例了两个
account_balance_0.job和account_balance_1.job----注意一定是job结尾,这样azkaban就会解析他

里面的内容是type= command,表示使用linux原生命令执行
command = sh /server/app/sqoop/sqoop_mysql_money_record16_import.sh 0 表示执行的具体的命令

三、etl_done_m2h_money_record16该任务用于监控是否全部任务都执行完
因此该任务只是简单的echo输出而已,但是要配置依赖关系,将所有的job的文件名字配置进来即可。

四、在azkaban上创建project,以及将这些文件打包成zip,上传到azkaban,因此就可以手动调用了。

五、如何自动调用
参见 azkaban_job_run_once.sh 脚本
sh azkaban_job_run_once.sh etl_done_jlc_m2h_money_record16 maming
其中etl_done_jlc_m2h_money_record16 是我在azkaban上的project内部
maming指代group,具体情况可以设置为公司名字的简称

六、具体详细参见zip包

------------------------------------------------------
