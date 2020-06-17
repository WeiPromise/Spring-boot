#!/usr/bin/env bash

CUR_DIR=$(cd `dirname "$0"`; pwd)
PROMISE_HOME=$(cd ${CUR_DIR}/..; pwd)
PID_FILE=${PROMISE_HOME}/bin/app.pid
PID=""
LOG_DIR=${PROMISE_HOME}/logs/web
OPERATION=$1

JAVA=""
if [ $JAVA_HOME"X" != "X" ];then
    JAVA=$JAVA_HOME/bin/java
else
    JAVA="java"
fi

if [[ ! -d ${LOG_DIR} ]]; then
    mkdir -p ${LOG_DIR}
fi

if [[ -f ${PID_FILE} ]]; then
    PID=$(cat ${PID_FILE})
fi


export DATAALARM_CONF_DIR=${PROMISE_HOME}/config
export DATAALARM_LIBS_DIR=${PROMISE_HOME}/libs
export DATAALARM_CLASSPATH=${DATAALARM_CONF_DIR}:${DATAALARM_LIBS_DIR}/*

MAIN_CLASS="com.promise.demo.DemoApplication"

if [[ $# -lt 1 ]];then
    echo "请输入操作类型(start|stop|restart)"
    exit 1
fi

start(){
    if [[ ! -f ${PID_FILE} ]];then
        nohup $JAVA -cp ${DATAALARM_CLASSPATH} ${MAIN_CLASS} &> ${LOG_DIR}/start.log &
        echo "promise server starting!"
    else
        echo "promise server is already running, since the PID file existed."
    fi
}

stop(){
    kill -9 ${PID} &> /dev/null
    if [[ $? -ne 0 ]];then
        echo "promise server not start!"
    else
        echo "promise server stopped!"
    fi
    rm -f ${PID_FILE}
}

if [[ ${OPERATION} == "start" ]];then
    start
    sleep 2
    echo "启动成功"
elif [[ ${OPERATION} == "stop" ]];then
    stop
    echo "关闭成功"
elif [[ ${OPERATION} == "restart" ]];then
    stop
    sleep 2
    start
    sleep 2
    echo "启动成功"
fi