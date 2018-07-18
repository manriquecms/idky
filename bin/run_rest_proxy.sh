#!/bin/bash
DIR="$( cd "$(dirname "$0")" ; pwd -P )"
cd $DIR

EXISTS=$(docker ps -q -a --filter name=rest-proxy)
if [ -n "$EXISTS" ];then
	docker rm -f $EXISTS
fi

docker run -d --name rest-proxy -p 8082:8082 --link zookeeper \
    --link kafka --link schema-registry:schema-registry confluent/rest-proxy

cd - > /dev/null
