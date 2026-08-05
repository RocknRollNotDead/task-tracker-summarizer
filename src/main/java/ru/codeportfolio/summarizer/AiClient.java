package ru.codeportfolio.summarizer;

import org.springframework.stereotype.Service;

import org.springframework.ai.chat.client.ChatClient;

@Service
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
