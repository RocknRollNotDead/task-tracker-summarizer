package ru.codeportfolio.summarizer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.codeportfolio.summarizer.dto.Status;
import ru.codeportfolio.summarizer.dto.TaskDto;
import ru.codeportfolio.summarizer.dto.UserDto;
import ru.codeportfolio.summarizer.service.AiClient;
import ru.codeportfolio.summarizer.service.SummarizerService;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AiTest {

    @Autowired
    AiClient aiClient;
    @Autowired
    private SummarizerService summarizerService;
    @Autowired
    private ObjectMapper objectMapper;

    private static String PROMT =
            """
            Сделай summary для задач юзера %s на русском языке.
            
            У него есть задачи со статусом "IN_PROGRESS", которые он не выполнил,
            а также задачи со статусом "DONE", которые он выполнил сегодня.
            Вот ты должен в summary отчёте рассказать о выполненных сегодня и невыполненных задачах.
            
            """;



/*    @Test
    void testAi() {

        String result = aiClient.get("""
                Я делаю запрос через Spring Boot.
                У меня Spring AI требует ввести значения в двух местах - "PROMT" и "text".
                Этот текст я пишу в "PROMT".
                """, """
                Мне нужно делать запросы к тебе для Summary отчётов по данным.
                Как лучше их запрашивать? Этот текст я пишу в "text".
                """);

        System.out.println(result);
    }*/



    @Test
    void testSummary() {


        var userDto = new UserDto(1L, "Юра", new ArrayList<>(List.of(
                new TaskDto("Попить воды", Status.IN_PROGRESS),
                new TaskDto("Выучить 20 слов на английском", Status.IN_PROGRESS),
                new TaskDto("сделать схему проекта с классами - граф", Status.DONE),
                new TaskDto("Пофиксить все ошибки аутентификации (исправить 500), создать тесты", Status.IN_PROGRESS),
                new TaskDto("Купить тикет", Status.DONE)
        )));

        String tasks = objectMapper.writeValueAsString(userDto.tasks());

        String result = aiClient.get(PROMT.formatted(userDto.name()), tasks);



        System.out.println(result);

    }






}
