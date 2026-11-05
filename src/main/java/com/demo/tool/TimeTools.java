package com.demo.tool;


import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: Linda
 * @date: 2026/10/29 16:26
 * @description:
 */
public class TimeTools {
    @Tool("查询当前日期和时间，返回格式化后的字符串")
    public String getCurrentTime(
            @P("时区标识，例如 Asia/Shanghai，不传则默认系统时区") String timezone) {
        // 简化处理：不真正解析 timezone，只返回系统时间
        // 目的是验证模型是否调用了工具，而不是做时区库
        if (timezone != null && timezone.contains("invalid")) {
            throw new IllegalArgumentException("不支持的时区格式");
        }
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
