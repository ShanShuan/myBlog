package com.shanshuan.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description :
 * @Author : wangzifeng
 * @Createon : 2020/6/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PublisSubscribeTest {

    @Autowired
    PublisSubscribeModel publisSubscribeModel;

    @Test
    public  void send() throws IOException, TimeoutException {
        publisSubscribeModel.send();
    }

    @Test
    public void consumer() throws IOException, TimeoutException, InterruptedException {
        publisSubscribeModel.consumer();
    }

    @Test
    public void consumer2() throws IOException, TimeoutException, InterruptedException {
        publisSubscribeModel.consumer();
    }
}
