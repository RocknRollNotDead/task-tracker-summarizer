package ru.codeportfolio.summarizer.service;

import org.springframework.stereotype.Service;
import ru.codeportfolio.summarizer.controller.ReportKafkaSender;
import ru.codeportfolio.summarizer.dto.ReportDto;
import ru.codeportfolio.summarizer.dto.ReportRequestDto;
import ru.codeportfolio.summarizer.dto.TaskDto;
import ru.codeportfolio.summarizer.dto.UserDto;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class SummarizerService {
    private final ReportKafkaSender reportKafkaSender;
    private final ObjectMapper objectMapper;

    private static String promt =
            """
            Сделай summary для задач юзера %s на русском языке.
            
            У него есть задачи со статусом "IN_PROGRESS", которые он не выполнил,
            а также задачи со статусом DONE, которые он выполнил сегодня.
            Вот ты должен в summary отчёте рассказать о выполненных сегодня и невыполненных задачах.
            
            Вот его задачи:
            %s
            """;

    public SummarizerService(ReportKafkaSender reportKafkaSender, ObjectMapper objectMapper) {
        this.reportKafkaSender = reportKafkaSender;
        this.objectMapper = objectMapper;
    }

    public void execute(ReportRequestDto reportRequestDto) {

        for (UserDto userDto : reportRequestDto.usersDto()) {

            String report = getReportFromAi(userDto);

            reportKafkaSender.sendRequest(objectMapper.writeValueAsString(new ReportDto(userDto.id(), report)));
        }

    }

    private String getReportFromAi(UserDto userDto) {

        String tasks = objectMapper.writeValueAsString(userDto.tasks());

        String request = promt.formatted(userDto.name(), tasks);


    }
}
