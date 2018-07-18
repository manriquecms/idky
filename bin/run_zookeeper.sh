#!/bin/bash
DIR="$( cd "$(dirname "$0")" ; pwd -P )"
cd $DIR

EXISTS=$(docker ps -q -a --filter name=zookeeper)
if [ -n "$EXISTS" ];then
	docker rm -f $EXISTS
fi

docker run -d --name zookeeper -p 2181:2181 confluent/zookeeper

cd - > /dev/null
