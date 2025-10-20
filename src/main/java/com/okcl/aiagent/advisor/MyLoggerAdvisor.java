package com.okcl.aiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public int getOrder() {
        return 0;
    }

    /**
     * 在处理请求之前执行的前置方法
     *
     * @param request 包含用户文本的请求对象
     * @return 返回原始请求对象
     */
    private AdvisedRequest before(AdvisedRequest request) {
        // 记录AI请求的用户文本信息
        log.info("AI Request: {}", request.userText());
        return request;
    }


    /**
     * 观察AI响应后处理方法
     *
     * @param advisedResponse AI建议响应对象，包含AI生成的结果信息
     */
    private void observeAfter(AdvisedResponse advisedResponse) {
        // 记录AI响应结果中的文本输出内容
        log.info("AI response: {}", advisedResponse.response().getResult().getOutput().getText());
    }

    /**
     * 在处理请求之后执行
     *
     * @param advisedRequest AdvisedRequest
     * @param chain          CallAroundAdvisorChain
     * @return AdvisedResponse
     */
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        this.before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        this.observeAfter(advisedResponse);
        return advisedResponse;
    }

    /**
     * 在处理请求之后执行
     *
     * @param advisedRequest AdvisedRequest
     * @param chain          StreamAroundAdvisorChain
     * @return Flux<AdvisedResponse>
     */
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        this.before(advisedRequest);
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);
        return (new MessageAggregator()).aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }
}
