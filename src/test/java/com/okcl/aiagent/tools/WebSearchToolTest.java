package com.okcl.aiagent.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebSearchToolTest {
    @Resource
    private WebSearchTool webSearchTool;

    @Test
    void search() {
        String search = webSearchTool.search("如何提高自控力");
        System.out.println(search);
        Assertions.assertNotNull(search);
    }
}