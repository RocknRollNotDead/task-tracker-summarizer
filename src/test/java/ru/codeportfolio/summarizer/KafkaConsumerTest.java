package ru.codeportfolio.summarizer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codeportfolio.summarizer.controller.KafkaConsumer;
import ru.codeportfolio.summarizer.dto.ReportRequestDto;
import ru.codeportfolio.summarizer.dto.UserDto;
import ru.codeportfolio.summarizer.service.SummarizerService;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    private SummarizerService summarizerService;

    @Mock
    private ObjectMapper objectMapper;

    private KafkaConsumer kafkaConsumer;

    @Test
    void shouldParseJson() {
        kafkaConsumer = new KafkaConsumer(summarizerService, objectMapper);

        String json = "{\"usersDto\":[]}";
        ReportRequestDto reportRequestDto = new ReportRequestDto(List.of());
        when(objectMapper.readValue(eq(json), eq(ReportRequestDto.class))).thenReturn(reportRequestDto);

        kafkaConsumer.consume(json);

        verify(objectMapper).readValue(json, ReportRequestDto.class);
        verify(summarizerService).execute(reportRequestDto);
    }

    @Test
    void notShouldParseJson() {
        kafkaConsumer = new KafkaConsumer(summarizerService, objectMapper);

        String invalidJson = "invalid json";
        when(objectMapper.readValue(eq(invalidJson), eq(ReportRequestDto.class)))
                .thenThrow(new RuntimeException("parse error"));

        try {
            kafkaConsumer.consume(invalidJson);
        } catch (RuntimeException ignore) {
        }

        verify(summarizerService, never()).execute(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldDeserializeRequestReport() {
        kafkaConsumer = new KafkaConsumer(summarizerService, objectMapper);

        ReportRequestDto users = new ReportRequestDto(
                List.of(
                        new UserDto(1L, "username1", "task1, task2, анжуманя"),
                        new UserDto(2L, "username2", "task1, task2, анжуманя")
                ));

        String json = String.valueOf(users);

        when(objectMapper.readValue(json, ReportRequestDto.class)).thenReturn(users);

        kafkaConsumer.consume(json);

        verify(summarizerService).execute(users);
    }
}
