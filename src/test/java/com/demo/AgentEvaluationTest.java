package com.demo;


import com.demo.ai.Assistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: Linda
 * @date: 2026/10/30 14:47
 * @description:
 */
@SpringBootTest
class AgentEvaluationTest {

    @Autowired
    private Assistant assistant;

    @Test
    void checkCacheExpireTime() {
        String answer = assistant.chat("eval-1", "本项目的缓存过期时间是多少？");
        System.out.println("Q: 缓存过期时间 | A: " + answer);
        // 人工看输出，确认包含 37
    }

    @Test
    void checkSystemMessage() {
        String answer = assistant.chat("eval-2", "今天天气怎么样？");
        System.out.println("Q: 天气 | A: " + answer);
        // 人工看输出，确认说的是"没找到"而不是编造天气
    }

    @Test
    void checkTimeToolInvoked() {
        String answer = assistant.chat("eval-3", "现在几点了？");
        System.out.println("Q: 时间 | A: " + answer);
        // 人工看输出，确认是真实时间
    }
}
