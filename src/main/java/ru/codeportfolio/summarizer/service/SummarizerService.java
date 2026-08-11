package ru.codeportfolio.summarizer.service;

import org.springframework.stereotype.Service;
import ru.codeportfolio.summarizer.dao.AiClient;
import ru.codeportfolio.summarizer.dto.ReportRequestDto;
import ru.codeportfolio.summarizer.dto.UserDto;

@Service
public class SummarizerService {
    private final ReportKafkaSender reportKafkaSender;

    private static String PROMT =
            """
                    Сделай summary для задач пользователя с никнеймом "%s".
                    
                    У него есть задачи со статусом "IN_PROGRESS", которые он не выполнил,
                    а также задачи со статусом DONE, которые он выполнил сегодня.
                    Вот ты должен в summary отчёте рассказать о выполненных сегодня и невыполненных задачах.
                    
                    """;
    private final AiClient aiClient;

    public SummarizerService(ReportKafkaSender reportKafkaSender, AiClient aiClient) {
        this.reportKafkaSender = reportKafkaSender;
        this.aiClient = aiClient;
    }

    public void execute(ReportRequestDto reportRequestDto) {

        for (UserDto userDto : reportRequestDto.usersDto()) {

            String report = getReportFromAi(userDto);

            reportKafkaSender.sendRequest(userDto.id(), report);
        }

    }

    private String getReportFromAi(UserDto userDto) {

        return aiClient.get(PROMT.formatted(userDto.name()), userDto.tasks());
    }
}
