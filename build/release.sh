#!/bin/bash

CUR_DIR=$(cd `dirname "$0"`; pwd)
PROMISE_HOME=$(cd ${CUR_DIR}/..; pwd)

profileActive=prod
buildVersion=$1

if [[ ! "${buildVersion}" =~ ^1\.[0-9]+\.[0-9]+$ ]] ; then
    echo "Usage : $0 [version(1.x.x)]"
    exit 1
fi

ergodic(){
    for file in ` ls $1`
    do
        if [[ -f $1 ]];then
            dos2unix $1 &> /dev/null
        elif [[ -d $1"/"${file} ]];then
            ergodic $1"/"${file}
        else
            local path=$1"/"${file}
            dos2unix ${path} &> /dev/null
        fi
    done
}

VERSION=$(echo ${buildVersion} | cut -d . -f1)

# package
cd ${PROMISE_HOME}
mvn clean package -Dmaven.test.skip=true -P${profileActive}; buildResult="$?"

if [[ ${buildResult} -ne 0 ]]; then
    echo -e "\nFailed to package project."
    exit 1
fi

BUILD_DIR=${PROMISE_HOME}/target/promise-${buildVersion}
#if [[ -d "${BUILD_DIR}" ]]; then
#    rm -fr ${BUILD_DIR}
#fi
echo "Create build directory: ${BUILD_DIR}"
mkdir -p ${BUILD_DIR}/logs/web

echo "Copying README ..."
cp ${PROMISE_HOME}/README.md ${BUILD_DIR}
echo "Copying bin ..."
cp -r ${PROMISE_HOME}/promise-workspace/bin ${BUILD_DIR}
if [[ -f ${PROMISE_HOME}/promise-workspace/bin/app.pid ]];then
    rm -rf ${PROMISE_HOME}/promise-workspace/bin/app.pid
fi
echo "Copying init ..."
cp -r ${PROMISE_HOME}/promise-workspace/init ${BUILD_DIR}

echo "Copying libs ..."
cp -r ${PROMISE_HOME}/promise-web/target/libs ${BUILD_DIR}
echo "Copying web ..."
cp ${PROMISE_HOME}/promise-web/target/promise-web-1.jar ${BUILD_DIR}/libs
echo "Copying config ..."
cp -r ${PROMISE_HOME}/promise-web/target/config ${BUILD_DIR}
echo "Copying data ..."
cp ${PROMISE_HOME}/promise-db/target/classes/*.sql ${BUILD_DIR}/config

ergodic ${BUILD_DIR}/bin
ergodic ${BUILD_DIR}/config

cd ${PROMISE_HOME}/target
echo "Starting build package ..."
tar czvf promise-${buildVersion}.tar.gz promise-${buildVersion} &> /dev/null
echo "${PROMISE_HOME}/target/promise-${buildVersion}.tar.gz"
echo "###########################"
echo "Build package successfully."
echo "###########################"