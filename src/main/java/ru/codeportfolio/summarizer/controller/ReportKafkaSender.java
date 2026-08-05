package ru.codeportfolio.summarizer.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class ReportKafkaSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ReportKafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRequest(String string){
        kafkaTemplate.send("SUMMARIZATION_SENDING", string);
    }
}
