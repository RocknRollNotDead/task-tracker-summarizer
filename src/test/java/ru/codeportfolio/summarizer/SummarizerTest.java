package ru.codeportfolio.summarizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codeportfolio.summarizer.dao.AiClient;
import ru.codeportfolio.summarizer.dto.ReportRequestDto;
import ru.codeportfolio.summarizer.dto.UserDto;
import ru.codeportfolio.summarizer.service.ReportKafkaSender;
import ru.codeportfolio.summarizer.service.SummarizerService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SummarizerTest {

    @Mock
    private ReportKafkaSender reportKafkaSender;

    @Mock
    private AiClient aiClient;

    @InjectMocks
    private SummarizerService summarizerService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "username1", String.valueOf(List.of("task1", "task2")));
    }

    @Test
    void shouldSendReportToKafkaOneTime() {
        String expectedReport = "отчёт 1";
        when(aiClient.get(anyString(), any())).thenReturn(expectedReport);

        ReportRequestDto request = new ReportRequestDto(List.of(userDto));

        summarizerService.execute(request);

        verify(aiClient, times(1)).get(anyString(), eq(userDto.tasks()));
        verify(reportKafkaSender, times(1)).sendRequest(userDto.id(), expectedReport);
        verifyNoMoreInteractions(aiClient, reportKafkaSender);
    }

    @Test
    void shouldSendReportFor2Users() {
        UserDto secondUser = new UserDto(2L, "username2", String.valueOf(List.of("task3")));

        when(aiClient.get(anyString(), eq(userDto.tasks()))).thenReturn("report1");
        when(aiClient.get(anyString(), eq(secondUser.tasks()))).thenReturn("report2");

        ReportRequestDto request = new ReportRequestDto(List.of(userDto, secondUser));

        summarizerService.execute(request);

        verify(reportKafkaSender).sendRequest(userDto.id(), "report1");
        verify(reportKafkaSender).sendRequest(secondUser.id(), "report2");
        verify(aiClient, times(2)).get(anyString(), any());
    }

    @Test
    void notShouldSendReport() {
        ReportRequestDto request = new ReportRequestDto(List.of());

        summarizerService.execute(request);

        verify(aiClient, never()).get(anyString(), any());
        verify(reportKafkaSender, never()).sendRequest(any(), anyString());
    }

}
