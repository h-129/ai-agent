package com.okcl.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileOperationToolTest {


    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "test.txt";
        String s = fileOperationTool.readFile(fileName);
        System.out.println(s);
        Assertions.assertNotNull(s);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "test.txt";
        String s = fileOperationTool.writeFile(fileName, "Hello World");
        Assertions.assertNotNull(s);
    }
}