package com.okcl.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.okcl.aiagent.constant.FileConstants;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * PDF 操作工具类
 */
@Component
public class PdfOperationTool {
    @Tool(description = "Generate a PDF file")
    public String generatePdf(@ToolParam(description = "Name of the file to save the generated PDF") String fileName,
                              @ToolParam(description = "Content to be written into the PDF") String content) {
        //创建文件目录和文件位置
        String fileDir = FileConstants.FILE_SAVE_PATH + "/pdf";
        String filePath = fileDir + "/" + fileName;
        try {
            FileUtil.mkdir(fileDir);
            try (PdfWriter pdfWriter = new PdfWriter(filePath);
                 //创建文档对象
                 PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                 Document document = new Document(pdfDocument)
            ) {
                //使用内置字体
                PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
                document.setFont(font);//设置字体
                //创建段落
                Paragraph paragraph = new Paragraph(content);
                document.add(paragraph);
            }
            return "PDF generated successfully to: " + filePath;
        } catch (IOException e) {
            return "Error generating PDF: " + e.getMessage();
        }
    }
}
