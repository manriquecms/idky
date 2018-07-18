#!/bin/bash
DIR="$( cd "$(dirname "$0")" ; pwd -P )"
cd $DIR

EXISTS=$(docker ps -q -a --filter name=schema-registry)
if [ -n "$EXISTS" ];then
	docker rm -f $EXISTS
fi
# -e SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS="PLAINTEXT://kafka:9092" -e SCHEMA_REGISTRY_KAFKASTORE_CONNECTION_URL="zookeeper:2181"
docker run -d --name schema-registry -p 0.0.0.0:8081:8081 --link zookeeper \
    --link kafka confluent/schema-registry

cd - > /dev/null
