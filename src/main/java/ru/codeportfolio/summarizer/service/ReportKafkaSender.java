package ru.codeportfolio.summarizer.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReportKafkaSender {

    private final KafkaTemplate<Long, String> kafkaTemplate;

    public ReportKafkaSender(KafkaTemplate<Long, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRequest(Long userId, String report) {

        kafkaTemplate.send("SUMMARIZATION_SENDING", userId, report);
    }
}
