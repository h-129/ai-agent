package com.okcl.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PdfOperationToolTest {

    @Test
    void generatePdf() {
        PdfOperationTool pdfOperationTool = new PdfOperationTool();
        String fileName = "test.pdf";
        String content = "This is a test PDF.";
        String res = pdfOperationTool.generatePdf(fileName, content);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }
}