package com.okcl.aiagent.advisor;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 违禁词过滤顾问
 */
@Component
@Slf4j
public class BannedWordsAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    private Set<String> bannedWords = new HashSet<>();
    @Value("classpath:filter/banned-words.txt")
    private Resource bannedWordsResource;

    /**
     * 初始化违禁词列表
     */
    @PostConstruct
    public void loadBannedWords() {
        try {
            log.info("Load banned words from {}", bannedWordsResource.getFilename());
            this.bannedWords = Files.lines(bannedWordsResource.getFile().toPath())
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toSet());
            log.info("Loaded {} banned words", this.bannedWords.size());
        } catch (IOException e) {
            log.error("Failed to load banned words", e);
        }
    }

    /**
     * 在处理请求之前执行
     *
     * @param advisedRequest AdvisorChain
     * @return AdvisorChain
     */
    private AdvisedRequest before(AdvisedRequest advisedRequest) {
        String userText = advisedRequest.userText();
        //将用户输入的文本进行过滤
        if (this.bannedWords.stream().anyMatch(userText::contains)) {
            log.info("检测到用户输入违禁词{}", userText);
        }
        return advisedRequest;
    }

    /**
     * 在处理请求之后执行
     *
     * @param advisedResponse AdvisorChain
     * @return AdvisorChain
     */
    private void observeAfter(AdvisedResponse advisedResponse) {
        log.info("读取到违禁词:{}", bannedWords);
        log.info("违禁词处理完成");
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        this.before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        this.observeAfter(advisedResponse);
        return chain.nextAroundCall(advisedRequest);
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        this.before(advisedRequest);
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);
        return (new MessageAggregator()).aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
