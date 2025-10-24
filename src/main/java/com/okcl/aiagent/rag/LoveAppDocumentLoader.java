package com.okcl.aiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 加载文档
 */
@Component
@Slf4j
public class LoveAppDocumentLoader {
    private final ResourcePatternResolver resourcePatternResolver;

    public LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public List<Document> loadMarkdowns() {
        List<Document> allDocuments = new ArrayList<>();
        //加载多篇
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath*:document/*.md");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                //添加元信息状体
                String status = null;
                if (filename != null) {
                    status = filename.substring(filename.length() - 6, filename.length() - 4);
                }
                MarkdownDocumentReaderConfig markdownDocumentReaderConfig = null;
                if (filename != null) {
                    markdownDocumentReaderConfig = MarkdownDocumentReaderConfig.builder()
                            .withHorizontalRuleCreateDocument(true)
                            .withIncludeCodeBlock(false)
                            .withIncludeBlockquote(false)
                            .withAdditionalMetadata("filename", filename)
                            //添加元信息状态
                            .withAdditionalMetadata("status", status)
                            .build();
                }
                MarkdownDocumentReader markdownDocumentReader = new MarkdownDocumentReader(resource, markdownDocumentReaderConfig);
                List<Document> documents = markdownDocumentReader.get();
                allDocuments.addAll(documents);
            }
        } catch (IOException e) {
            log.warn("Failed to read document: {}", e.getMessage());
        }
        return allDocuments;
    }
}
