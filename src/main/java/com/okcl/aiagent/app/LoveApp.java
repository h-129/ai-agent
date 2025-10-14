package com.okcl.aiagent.app;

import com.okcl.aiagent.advisor.MyLoggerAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveApp {
    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题;" +
            "围绕单身、恋爱、已婚三种状态提问:单身状态询问社交圈拓展及追求心仪对象的困扰;" +
            "恋爱状态询问沟通、习惯差异引发的矛盾;已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";
    private final ChatClient chatClient;

    /**
     * 初始化 AI 客户端
     *
     * @param dashscopeChatModel 阿里灵积模型
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        //初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();
        //基于文件的对话记忆
//        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
//        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        chatClient = ChatClient.builder(dashscopeChatModel).
                defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
                )
                .build();
    }

    /**
     * AI基础对话 支持多轮会话
     *
     * @param message 用户输入信息
     * @param chatId  会话ID
     * @return AI输出信息
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = null;
        if (chatResponse != null) {
            content = chatResponse.getResult().getOutput().getText();
        }
        log.info("content: {}", content);
        return content;
    }

    /**
     * AI基础对话 结构化输出
     *
     * @param message 用户输入信息
     * @param chatId  会话ID
     * @return AI输出信息
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport entity = chatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话生成恋爱结果,标题为{用户名}的恋爱报告,内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        log.info("entity: {}", entity);
        return entity;
    }

    //定义一个类
    record LoveReport(String title, List<String> suggestions) {

    }


}
