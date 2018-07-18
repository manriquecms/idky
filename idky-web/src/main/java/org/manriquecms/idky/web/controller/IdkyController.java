package org.manriquecms.idky.web.controller;

import org.manriquecms.idky.web.controller.model.BasicResponse;
import org.manriquecms.idky.web.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class IdkyController {

    @Autowired
    KafkaService kafkaService;

    @RequestMapping(method = RequestMethod.GET, path = "/ping",produces = {MediaType.APPLICATION_JSON_VALUE})
    public BasicResponse ping(){
        return new BasicResponse("pong");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/kafka/topic",produces = {MediaType.APPLICATION_JSON_VALUE})
    public BasicResponse createTopic(@RequestParam String topicName){
        return new BasicResponse(kafkaService.createTopic(topicName));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/producer",produces = {MediaType.APPLICATION_JSON_VALUE})
    public BasicResponse producer(){
        kafkaService.producer();
        return new BasicResponse("OK: "+UUID.randomUUID());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/consumer",produces = {MediaType.APPLICATION_JSON_VALUE})
    public BasicResponse consumer(){
        kafkaService.consumer();
        return new BasicResponse("OK: "+UUID.randomUUID());
    }
}
