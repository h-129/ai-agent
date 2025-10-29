package com.okcl.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TerminalOperationToolTest {

    @Test
    void executeTerminalCommand() {
        TerminalOperationTool operationTool = new TerminalOperationTool();
        String command = "dir";
        String output = operationTool.executeTerminalCommand(command);
        System.out.println(output);
        Assertions.assertNotNull(output);
    }
}