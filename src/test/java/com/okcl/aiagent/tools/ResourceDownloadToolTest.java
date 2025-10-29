package com.okcl.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String url = "https://web-home-www.oss-cn-beijing.aliyuncs.com/dudu.png";
        String s = resourceDownloadTool.downloadResource(url, "dudu.png");
        System.out.println(s);
        Assertions.assertNotNull(s);
    }
}