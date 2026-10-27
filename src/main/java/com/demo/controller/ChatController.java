package com.demo.controller;


import dev.langchain4j.model.chat.ChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Linda
 * @date: 2026/10/27 13:44
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatController {


    private final ChatModel chatModel;

    public ChatController(ChatModel chatModel){
        this.chatModel = chatModel;
    }


    @GetMapping
    public String chat(@RequestParam String question){
        log.info("收到的问题的总长度: {} ",question.length());
        long start = System.currentTimeMillis();
        String reply = chatModel.chat(question);
        log.info("模型响应耗时: {} ms", System.currentTimeMillis() - start);
        return reply;
    }

}
