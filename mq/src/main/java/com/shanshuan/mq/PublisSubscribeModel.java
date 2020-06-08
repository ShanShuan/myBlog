package com.shanshuan.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description :
 * @Author : wangzifeng
 * @Createon : 2020/6/5.
 */
@Component
public class PublisSubscribeModel {
    private static final String QUEUE_NAME = "test_exchange_work1";

    private static final String QUEUE_NAME_TWO = "test_exchange_work2";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final static String EXCHANGE_NAME = "test_exchange_fanout";

    public   Connection getConnection(){
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        return connectionFactory.createConnection();

    }

    /**
     * 生产者
     * @throws IOException
     * @throws TimeoutException
     */
    public void  send() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel(false);
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        // 消息内容
        for (int i = 0; i <100 ; i++) {
            String message = i+1+"Hello World!";
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        channel.close();
        connection.close();
        //注意：消息发送到没有队列绑定的交换机时，消息将丢失，因为，交换机没有存储消息的能力，消息只能存在在队列中
    }


    public void  consumer() throws IOException, InterruptedException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel(false);
        // 声明队列
        channel.queueDeclare(QUEUE_NAME_TWO, false, false, false, null);
        channel.queueBind(QUEUE_NAME_TWO, EXCHANGE_NAME, "");
        channel.basicQos(1);
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME_TWO, autoAck, "myConsumerTag", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("body:"+new String(body));
                long deliveryTag = envelope.getDeliveryTag();
                channel.basicAck(deliveryTag, false);
            }
        });
        Thread.currentThread().join();
    }






}
