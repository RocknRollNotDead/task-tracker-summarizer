package ru.codeportfolio.summarizer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.codeportfolio.summarizer.dao.AiClient;
import ru.codeportfolio.summarizer.dto.UserDto;
import tools.jackson.databind.ObjectMapper;

// тест для пинга нейросети по api
@SpringBootTest
class AiTest {

    @Autowired
    AiClient aiClient;
    @Autowired
    private ObjectMapper objectMapper;

    private static String PROMT =
            """
            Сделай summary для задач юзера %s на русском языке.
            
            У него есть задачи со статусом "IN_PROGRESS", которые он не выполнил,
            а также задачи со статусом "DONE", которые он выполнил сегодня.
            Вот ты должен в summary отчёте рассказать о выполненных сегодня и невыполненных задачах.
            
            """;

    @Test
    void testSummary() {


        var userDto = new UserDto(1L, "Юра", """
                {name: "Попить воды", status: "IN_PROGRESS"},
                {name: "Выучить 20 слов на английском", status: "DONE"},
                {name: "сделать схему проекта с классами - граф", status: "IN_PROGRESS"},
                {name: "Пофиксить все ошибки аутентификации (исправить 500), создать тесты", status: "IN_PROGRESS"},
                {name: "Купить тикет", status: "DONE"}
                """
        );

        String tasks = objectMapper.writeValueAsString(userDto.tasks());

        String result = aiClient.get(PROMT.formatted(userDto.name()), tasks);



        System.out.println(result);

    }






}
