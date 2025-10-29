package com.okcl.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.okcl.aiagent.constant.FileConstants;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 资源下载工具类
 */
@Component
public class ResourceDownloadTool {
    @Tool(description = "Download a resource from a URL")
    public String downloadResource(@ToolParam(description = "URL of the resource to download") String url,
                                   @ToolParam(description = "Name of the downloaded file") String fileName) {
        String saveDir = FileConstants.FILE_SAVE_PATH + "/download";
        try {
            //创建目录
            FileUtil.mkdir(saveDir);
            //文件保存路径
            String filePath = saveDir + "/" + fileName;
            //下载文件
            HttpUtil.downloadFile(url, new File(filePath));
            return "File downloaded successfully to: " + filePath;
        } catch (Exception e) {
            return "Downloading error" + e.getMessage();
        }
    }
}
