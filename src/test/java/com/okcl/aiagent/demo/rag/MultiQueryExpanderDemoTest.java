package com.okcl.aiagent.demo.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MultiQueryExpanderDemoTest {
    @Resource
    private MultiQueryExpanderDemo multiQueryExpanderDemo;

    @Test
    void expand() {
        List<Query> expand = multiQueryExpanderDemo.expand("如何提高自我 confidence");
        expand.forEach(System.out::println);
    }
}