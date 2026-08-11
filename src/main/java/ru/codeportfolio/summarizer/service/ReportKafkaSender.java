package ru.codeportfolio.summarizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class ReportKafkaSender {

    Logger log = LoggerFactory.getLogger(ReportKafkaSender.class);

    private final KafkaTemplate<Long, String> kafkaTemplate;

    public ReportKafkaSender(KafkaTemplate<Long, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRequest(Long userId, String report) {

        kafkaTemplate.send("SUMMARIZATION_SENDING", userId, report)
                .whenComplete((result, e) ->
                {
                    if (e != null) {
                        log.error("Error to send to kafka report for user with id {}", userId);
                    }
                });
    }
}
