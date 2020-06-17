#!/usr/bin/env bash

CUR_DIR=$(cd `dirname "$0"`; pwd)
PROMISE_HOME=$(cd ${CUR_DIR}/..; pwd)

sed -i 's/[ ]*$//g' ${PROMISE_HOME}/config/application.properties
homeArg=`cat ${PROMISE_HOME}/config/application.properties | grep -v '#'| grep promise.home= | awk -F'=' '{print $2}'`
if [[ -z ${homeArg} || ${homeArg} != ${PROMISE_HOME} ]];then
sed -i 's#promise.home='${homeArg}'#promise.home='${PROMISE_HOME}'#' ${PROMISE_HOME}/config/application.properties
fi


MYSQL_HOSTNAME=`cat ${PROMISE_HOME}/config/application.properties | grep -v '#'| grep promise.mysql.host= | awk -F'=' '{print $2}'`
MYSQL_PORT=`cat ${PROMISE_HOME}/config/application.properties | grep -v '#'| grep promise.mysql.port= | awk -F'=' '{print $2}'`
MYSQL_USERNAME=`cat ${PROMISE_HOME}/config/application.properties | grep -v '#'| grep promise.mysql.username= | awk -F'=' '{print $2}'`
MYSQL_PASSWORD=`cat ${PROMISE_HOME}/config/application.properties | grep -v '#'| grep promise.mysql.password= | awk -F'=' '{print $2}'`
MYSQL_DATABASE=`cat ${PROMISE_HOME}/config/application.properties | grep -v '#'| grep promise.databasename= | awk -F'=' '{print $2}'`
mysql -h ${MYSQL_HOSTNAME} -P${MYSQL_PORT} -u${MYSQL_USERNAME} -p${MYSQL_PASSWORD} -e "CREATE DATABASE IF NOT EXISTS ${MYSQL_DATABASE};"
