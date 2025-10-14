package com.okcl.aiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FileBasedChatMemory implements ChatMemory {
    //实例化一个kryo序列化对象
    private static final Kryo kryo = new Kryo();

    //静态代码块 初始化kryo对象
    static {
        // 设置kryo对象是否需要注册
        kryo.setRegistrationRequired(false);
        //设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    // 为每个会话ID添加锁机制
    private final ConcurrentHashMap<String, Object> conversationLocks = new ConcurrentHashMap<>();
    //指定存储路径
    private final String BASE_DIR;

    //构造函数 初始化存储路径
    public FileBasedChatMemory(String dir) {
        this.BASE_DIR = dir;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 添加消息
     *
     * @param conversationId 会话ID
     * @param message        消息
     */
    @Override
    public void add(String conversationId, Message message) {
        saveConversationMessages(conversationId, List.of(message));
    }

    /**
     * 添加消息
     *
     * @param conversationId 会话ID
     * @param messages       消息列表
     */
    @Override
    public void add(String conversationId, List<Message> messages) {
        //先读再写
        List<Message> messageList = getOrCreateConversationMessages(conversationId);
        messageList.addAll(messages);
        saveConversationMessages(conversationId, messageList);
    }

    /**
     * 获取指定会话ID的最新N条消息
     *
     * @param conversationId 会话ID
     * @param lastN          最新N条消息数
     * @return 最新N条消息列表
     */
    @Override
    public List<Message> get(String conversationId, int lastN) {
        //先取出所有的消息列表
        List<Message> messageList = getOrCreateConversationMessages(conversationId);
        return messageList.stream().skip(Math.max(0, messageList.size() - lastN)).toList();
    }

    /**
     * 清空指定会话ID的会话信息
     *
     * @param conversationId 会话ID
     */
    @Override
    public void clear(String conversationId) {
        File conversationFile = getConversationFile(conversationId);
        if (conversationFile.exists()) {
            conversationFile.delete();
        }
    }

    //每个会话文件单独保存
    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }

    //获取或创建会话消息的列表
    private List<Message> getOrCreateConversationMessages(String conversationId) {
        synchronized (conversationLocks.computeIfAbsent(conversationId, k -> new Object())) {
            File file = getConversationFile(conversationId);
            List<Message> messages = new ArrayList<>();
            if (file.exists()) {
                try (Input input = new Input(new FileInputStream(file))) {
                    messages = kryo.readObject(input, ArrayList.class);
                } catch (IOException e) {
                    log.error("Error reading conversation file: {}", e.getMessage());
                }
            }
            return messages;
        }
    }

    //保存会话信息
    private void saveConversationMessages(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output fos = new Output(new FileOutputStream(file))) {
            kryo.writeObject(fos, messages);
        } catch (IOException e) {
            log.error("Error writing conversation file: {}", e.getMessage());
        }
    }
}
