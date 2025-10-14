package com.okcl.aiagent;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@SpringBootTest
class AiAgentApplicationTests {
    /**
     * 测试kryo的序列化
     */
    @Test
    public void testKryo() throws FileNotFoundException {
        Kryo kryo = new Kryo();
        kryo.register(SomeClass.class);

        SomeClass object = new SomeClass();
        object.value = "Hello Kryo!";

        Output output = new Output(new FileOutputStream("F:\\javaTest\\kryo.txt"));
        kryo.writeObject(output, object);
        output.close();

        Input input = new Input(new FileInputStream("F:\\javaTest\\kryo.txt"));
        SomeClass object2 = kryo.readObject(input, SomeClass.class);
        System.out.println(object2.value);
        input.close();
    }

    static public class SomeClass {
        String value;
    }
}
