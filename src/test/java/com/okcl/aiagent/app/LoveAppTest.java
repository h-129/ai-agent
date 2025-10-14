package com.okcl.aiagent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@Slf4j
class LoveAppTest {
    @Resource
    private LoveApp loveApp;

    @Test
    void contextLoads() {
        String chatId = UUID.randomUUID().toString();
        //第一轮
        String message = "你好，我是野猪佩奇";
        String answer = loveApp.doChat(message, chatId);
        //第二轮
        message = "我想让另一半(野猪亨利)更爱我";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        //第三轮
        message = "我的另一半叫什么名字，刚刚和你说过";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String message = "我叫小王,现在单身,想找对象,希望对象能给我一个好听的名字";
        String chatId = UUID.randomUUID().toString();
        log.info("请求id:{}", chatId);
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }
}