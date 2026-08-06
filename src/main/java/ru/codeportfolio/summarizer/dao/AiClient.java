package ru.codeportfolio.summarizer.dao;

import org.springframework.stereotype.Component;

import org.springframework.ai.chat.client.ChatClient;

//@Transactional // если будет ошибка запросов
@Component
public class AiClient {


    private final ChatClient chatClient;

    public AiClient(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    public String get(String promt, String tasks) {
        return chatClient.prompt()
                .system(promt)
                .user(tasks)
                .call()
                .content();
    }
}
