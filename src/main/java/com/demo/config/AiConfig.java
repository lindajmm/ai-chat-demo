package com.demo.config;


import com.demo.ai.Assistant;
import com.demo.rag.DocumentIngestor;
import com.demo.tool.TimeTools;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolErrorHandlerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Linda
 * @date: 2026/10/27 14:22
 * @description:
 */
@Slf4j
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
                .logRequests(true)
                .logResponses(true)
                .build();
        return model;
    }

  /*  @Bean
    public Assistant assistant(ChatModel chatModel){
        return AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .chatMemoryProvider(new ChatMemoryProvider() {
                    @Override
                    public ChatMemory get(Object memoryId) {
                        System.out.println("为会话创建记忆: " + memoryId);
//                        MessageWindowChatMemory.withMaxMessages(10))
                        return MessageWindowChatMemory.withMaxMessages(3);
                    }
                })
                .build();
    }*/

    @Bean
    public Assistant assistant(ChatModel model, DocumentIngestor ingestor) {
        ContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(ingestor.getEmbeddingStore())
                .embeddingModel(ingestor.getEmbeddingModel())  // 需要新增 getter
                .maxResults(3)
                .build();

        return AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(retriever)
                .tools(new TimeTools())
                .toolExecutionErrorHandler((error, context) -> {
                    // 1. 记录完整异常到日志，方便你排查
                    log.error("工具执行失败，上下文: {}", context, error);
                    // 2. 返回给模型的文本：简短、安全、不暴露内部细节
                    return ToolErrorHandlerResult.text("工具执行时发生错误，请稍后重试或联系管理员。");
                })// 新增这一行
                .build();
    }
}
