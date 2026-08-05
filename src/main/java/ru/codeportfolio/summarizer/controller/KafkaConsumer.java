package ru.codeportfolio.summarizer.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import ru.codeportfolio.summarizer.dto.ReportRequestDto;
import ru.codeportfolio.summarizer.service.SummarizerService;
import tools.jackson.databind.ObjectMapper;


@Controller
public class KafkaConsumer {


    private final SummarizerService summarizerService;
    private final ObjectMapper objectMapper;

    public KafkaConsumer(SummarizerService summarizerService, ObjectMapper objectMapper) {
        this.summarizerService = summarizerService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "REPORT_REQUEST", groupId = "id")
    public void consume(String json){
        ReportRequestDto reportRequestDto = objectMapper.readValue(json, ReportRequestDto.class);
        summarizerService.execute(reportRequestDto);
    }


}
