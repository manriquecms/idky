package org.manriquecms.idky.web.service;

public interface KafkaService {
    public boolean createTopic(String name);
    public void producer();
    public void consumer();
}
