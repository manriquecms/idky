#!/bin/bash
DIR="$( cd "$(dirname "$0")" ; pwd -P )"
cd $DIR

EXISTS=$(docker ps -a -q --filter name=kafka)
if [ -n "$EXISTS" ];then
    docker rm -f $EXISTS
fi

docker run -d --name kafka -p 9092:9092 --link zookeeper confluent/kafka

cd - > /dev/null
