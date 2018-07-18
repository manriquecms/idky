#!/bin/bash
DIR="$( cd "$(dirname "$0")" ; pwd -P )"
cd $DIR

docker exec kafka /usr/bin/kafka-topics --zookeeper zookeeper:2181 --create --topic $TOPIC --partitions 1 --replication-factor 1

cd - > /dev/null
