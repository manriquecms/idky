package org.manriquecms.idky.web.service;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.manriquecms.idky.web.model.User;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Log4j2
public class KafkaServiceImpl implements KafkaService{

    @Override
    public boolean createTopic(String name) {
        return false;
    }

    @Override
    public void producer() {
        Properties config = new Properties();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("schema.registry.url", "http://localhost:8081");
        config.put("acks", "all");
        config.put("retries", 0);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);

        KafkaProducer producer = new KafkaProducer(config);
        User u = User.newBuilder().setName("cesar").setPassword("mypass").build();
        ProducerRecord<String,User> producerRecord = new ProducerRecord<>("prueba", UUID.randomUUID().toString(),u);
        Future<RecordMetadata> fut = producer.send(producerRecord);

        try {

            log.info(fut.get(10, TimeUnit.SECONDS).toString());
        } catch (InterruptedException|ExecutionException |TimeoutException e) {
            e.printStackTrace();
        } finally {
            producer.close(1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void consumer() {
        Properties config = new Properties();
        try {
            config.put("client.id", InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        config.put("group.id", "foo");
        config.put("bootstrap.servers", "localhost:9092");
        config.put("schema.registry.url", "http://localhost:8081");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);

        KafkaConsumer<String,User> consumer = new KafkaConsumer<String, User>(config);
        consumer.subscribe(Collections.singletonList("prueba"));

        try {
            ConsumerRecords<String, User> records = consumer.poll(Long.MAX_VALUE);
            records.forEach(rec -> {
                log.info(rec.key() + rec.value());
            });

            consumer.commitSync();


        } catch (CommitFailedException e) {
            // application-specific rollback of processed records
        }

    }
}
