package com.demo.config;


import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Linda
 * @date: 2026/10/27 14:22
 * @description:
 */
@Configuration
public class AiConfig {

    @Value("${ai.dashscope.api-key}")
    private String apiKey;
    @Value("${ai.dashscope.base-url}")
    private String baseUrl;
    @Value("${ai.dashscope.model-name}")
    private String modelName;
    @Value("${ai.dashscope.temperature}")
    private Double temperature;


    @Bean
    public ChatModel chatModel(){
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .temperature(temperature)
//                .logRequests(true)
//                .logResponses(true)
                .build();
        return model;
    }
}
