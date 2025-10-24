package com.okcl.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LoveAppDocumentLoaderTest {
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;
    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;
    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    @Test
    void loadMarkdowns() {
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        List<Document> documents1 = myTokenTextSplitter.splitDocuments(documents);
        //自动填充元信息
        List<Document> documents2 = myKeywordEnricher.enrichDocuments(documents1);
        documents.forEach(System.out::println);
        documents1.forEach(System.out::println);
        documents2.forEach(System.out::println);
    }
}