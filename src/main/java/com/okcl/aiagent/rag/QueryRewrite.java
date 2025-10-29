package com.okcl.aiagent.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * 预检索查询重写
 */
@Component
public class QueryRewrite {
    private final QueryTransformer queryTransformer;

    public QueryRewrite(ChatModel dashscopeChatModel) {
        ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);
        //创建查询重写器
        queryTransformer = RewriteQueryTransformer.builder().chatClientBuilder(builder)
                .build();
    }

    public String doQueryWrite(String prompt) {
        Query query = new Query(prompt);
        //执行查询重写
        Query transform = queryTransformer.transform(query);
        //输出查询重写后的查询
        return transform.text();
    }
}
