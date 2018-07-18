run_kafka:
	bin/run_kafka.sh
run_zookeeper:
	bin/run_zookeeper.sh
run_zookeeper_cli:
	bin/run_zookeeper_cli.sh
run_schema_registry:
	bin/run_schema_registry.sh
run_rest_proxy:
	bin/run_rest_proxy.sh
run_all_kafka: run_zookeeper run_kafka run_schema_registry run_rest_proxy

create_kafka_topic:
	bin/create_kafka_topic.sh
