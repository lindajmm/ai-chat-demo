package com.demo.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface Assistant {
//    @SystemMessage("你是一个知识库助手。请严格基于提供的参考信息回答问题。如果参考信息中没有相关内容，直接说“文档中没有找到相关信息”，不要编造。")
    String chat(@MemoryId String sessionId, @UserMessage String message);
}
