#!/bin/bash

curDir=$(cd `dirname $0`;pwd)
projectDir=$(cd ${curDir}/..;pwd)
profileActive=prod

checkReturnValue(){
    returnValue=$1
    if [[ ! 0 -eq ${returnValue} ]];then
        echo "exception"
        exit 1
    fi
}

if [[ 0 -eq $# ]]; then
    cd ${projectDir}
    mvn -Dmaven.test.skip=true -DskipTests=true clean install -P${profileActive}
    checkReturnValue $?
    cd -
elif [[ 1 -eq $# ]];then
    module=$1
    cd ${projectDir}
    mvn -Dmaven.test.skip=true -DskipTests=true clean package -pl ${module} -am
    checkReturnValue $?
    cd -
else
    echo "Usage : $0 [module name]"
    exit 1
fi