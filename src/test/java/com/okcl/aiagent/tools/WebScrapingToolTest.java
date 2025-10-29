package com.okcl.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WebScrapingToolTest {
    @Test
    void scrapeWebPage() {
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String url = "http://www.okcl.cn";
        String s = webScrapingTool.scrapeWebPage(url);
        System.out.println(s);
        Assertions.assertNotNull(s);
    }
}