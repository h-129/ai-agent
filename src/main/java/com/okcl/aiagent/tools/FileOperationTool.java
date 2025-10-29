package com.okcl.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.okcl.aiagent.constant.FileConstants;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 文件操作工具类
 */
@Component
public class FileOperationTool {
    private final String FILE_DIR = FileConstants.FILE_SAVE_PATH + "/file";

    /**
     * 读取文件内容
     *
     * @param fileName
     * @return
     */
    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of file to read") String fileName) {
        String filePath = FILE_DIR + "/" + fileName;
        String s = null;
        try {
            s = FileUtil.readUtf8String(filePath);
            return s;
        } catch (Exception e) {
            return "Error reading file" + e.getMessage();
        }
    }

    /**
     * 将内容写入文件
     *
     * @param fileName
     * @param content
     * @return
     */
    public String writeFile(@ToolParam(description = "Name of file to write") String fileName,
                            @ToolParam(description = "Content to write") String content) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            //先创建目录
            FileUtil.mkdir(FILE_DIR);
            //进行写入
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to: " + filePath;
        } catch (Exception e) {
            return "Error writing file" + e.getMessage();
        }
    }
}
