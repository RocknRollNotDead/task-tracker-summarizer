package ru.codeportfolio.summarizer.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.codeportfolio.summarizer.dto.ReportDto;
import tools.jackson.databind.ObjectMapper;

@Service
public class ReportKafkaSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ReportKafkaSender(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendRequest(ReportDto reportDto){

        String request = objectMapper.writeValueAsString(reportDto);
        kafkaTemplate.send("SUMMARIZATION_SENDING", request);
    }
}
