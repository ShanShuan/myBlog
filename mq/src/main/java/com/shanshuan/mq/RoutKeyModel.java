package com.shanshuan.mq;

import com.rabbitmq.client.*;
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
public class RoutKeyModel {
    private static final String EXCHANGE_NAME = "test_exchange_direct";
    private static final String QUEUE_NAME_TWO = "queue_name_two";
    private static final String QUEUE_NAME = "queue_name";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);//声明exchange 交换机
        // 消息内容
        for (int i = 0; i <10 ; i++) {
            String message = i+1+"Hello World!";
            channel.basicPublish(EXCHANGE_NAME, "insert", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        channel.close();
        connection.close();
    }

    public  void comsumer() throws IOException, InterruptedException {
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);



        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "delete");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "update");

        channel.basicQos(1);
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, "myConsumerTag", new DefaultConsumer(channel) {
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



    public  void comsumer2() throws IOException, InterruptedException {
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);
        channel.queueDeclare(QUEUE_NAME_TWO, false, false, false, null);



        channel.queueBind(QUEUE_NAME_TWO, EXCHANGE_NAME, "delete");
        channel.queueBind(QUEUE_NAME_TWO, EXCHANGE_NAME, "update");
        channel.queueBind(QUEUE_NAME_TWO, EXCHANGE_NAME, "insert");

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
