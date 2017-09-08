#!/bin/bash
if [ $# -ne 2 ];then
echo "Usage: azkaban_daily_m2h.sh {project_name} {group_name}"
exit 0
fi

project_name=$1
group_name=$2
tmp_output=/tmp/${project_name}_tmp_log
host=10.2.19.62:8081 ###azkaban服务
metamap_host=10.2.19.62:8088 ####ETL服务


# 访问azkaban,获取session id
curl -k -X POST --data "username=azkaban&password=xxxx&action=login" http://${host} > ${tmp_output} ###将sessionId输出到文件中
echo result is `cat ${tmp_output}`
session_id=`cat ${tmp_output} | grep session | awk -F\" '!/[{}]/{print $(NF-1)}'` ###解析sessionId
echo "we got session id : $session_id"


# 
# 阻塞检察某个execution的进度
#
function check_exec_status(){
	execid=$1
	sleep 2m ##每次休息2秒
	#查看前execution的执行状态，完成后退出
	status=RUNNING
	until [ $status == '"SUCCEEDED"' ] ###如果该任务不是成功的,则一直循环
	do
		。。。。。。
		sleep 2m	
	done
}

# 遍历执行project下的flow,即每一个子job
curl -k --get --data "session.id=${session_id}&ajax=fetchprojectflows&project=${project_name}" http://${host}/manager > ${tmp_output}
cat ${tmp_output}
for flow in `cat ${tmp_output} |  JSON.sh -b | grep flowId |  awk '{gsub("\"","",$2);print($2)}'` ##获取每一个flow子job的id
do
        echo "flow is $flow, ready to execute"
        ###请求该flow的任务执行情况
        curl -k --get --data "session.id=${session_id}" --data 'ajax=executeFlow' --data "project=${project_name}" --data "flow=${flow}" --data "failureAction=finishPossible" http://${host}/executor >${tmp_output}${flow}
        execid=`cat ${tmp_output}${flow} | JSON.sh -b| grep execid | awk '{print($2)}'` ###返回执行结果
	echo "got execid : ${execid}"
	check_exec_status ${execid} ###校验执行结果
echo "$flow execution done"
done

###说明该脚本执行成功
echo "all flows for project ${project_name} has been executed"
rm -vf ${tmp_output}
