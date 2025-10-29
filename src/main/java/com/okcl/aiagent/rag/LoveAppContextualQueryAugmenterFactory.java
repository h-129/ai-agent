package com.okcl.aiagent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 创建自定义RAG上下文增强器工厂
 */
public class LoveAppContextualQueryAugmenterFactory {
    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate promptTemplate = new PromptTemplate("""
                你应该输出下面的内容
                抱歉，我只能回答恋爱相关的问题，别的没有办法帮到您哦，
                有问题可以联系客服哦
                """);
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(promptTemplate)
                .build();
    }
}
