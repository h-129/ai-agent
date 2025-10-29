package com.okcl.aiagent.Registration;

import com.okcl.aiagent.tools.*;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集中注册工具类
 */
@Configuration
public class ToolRegistration {
    @Resource
    private WebSearchTool webSearchTool;
    @Resource
    private ResourceDownloadTool resourceDownloadTool;
    @Resource
    private FileOperationTool fileOperationTool;
    @Resource
    private WebScrapingTool webScrapingTool;
    @Resource
    private TerminalOperationTool terminalOperationTool;
    @Resource
    private PdfOperationTool pdfOperationTool;

    @Bean
    public ToolCallback[] allTools() {
        return ToolCallbacks.from(
                webSearchTool,
                fileOperationTool,
                webScrapingTool,
                terminalOperationTool,
                pdfOperationTool,
                resourceDownloadTool
        );
    }
}
