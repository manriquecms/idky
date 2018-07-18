#!/bin/bash
DIR="$( cd "$(dirname "$0")" ; pwd -P )"
cd $DIR

docker run -it --rm --link zookeeper:confluent/zookeeper zookeeper zkCli.sh -server zookeeper

cd - > /dev/null
