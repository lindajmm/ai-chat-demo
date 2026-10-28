package com.demo;


import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.List;


/**
 * @author: Linda
 * @date: 2026/10/27 11:54
 * @description:
 */
public class FirstCall {
    public static void main(String[] args) {
        String apiKey = System.getenv("OPENAI_API_KEY");
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .modelName("qwen-plus")
                .temperature(0.7)
                .build();


        String helloWorld = model.chat("用一句话解释什么是Java的接口");
        System.out.println(helloWorld);



        ChatResponse response = model.chat(List.of(
                UserMessage.from("你好，介绍下自己")
        ));
        System.out.println(response.aiMessage());
    }
}
