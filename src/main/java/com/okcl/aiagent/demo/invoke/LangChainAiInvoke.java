package com.okcl.aiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class LangChainAiInvoke {
    public static void main(String[] args) {
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey("sk-b21720c1da1e4f6fb1bd856b145d19a0")
                .modelName("qwen-max")
                .build();
        String chat = qwenChatModel.chat("你好");
        System.out.println(chat);
    }
}
