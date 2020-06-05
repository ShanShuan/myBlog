# Getting Started

#工作模式
##简单模式
![图示](https://github.com/ShanShuan/myBlog/blob/master/mq/src/main/resources/static/simple.jpg)<br>
P：消息的生产者<br>
C：消息的消费者<br>
生产者发送消息到队列<br>
  ```
    @Autowired
    RabbitTemplate rabbitTemplate;

    、、、、
     ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
     Connection connection = connectionFactory.createConnection();
     Channel channel = connection.createChannel(true);//开始事务确认机制
     try {
         for (int i = 0; i <100 ; i++) {
              /**
              * 队列名字
              * 如果我们声明一个持久队列(该队列将在服务器重启后继续存在)，则为true。
              * 如果我们声明一个排他队列(仅限于此连接)，则为true。
              * 如果我们声明一个自动删除队列(服务器将在不再使用时删除它)，则为true
              * 队列的其他属性(构造参数)
              */
              // 声明（创建）队列
             channel.queueDeclare(QUEUE_NAME, true, false, false, null);
             String message = (i+1)+"Hello World!";
             channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
             System.out.println(" [x] Sent '" + message + "'");
             channel.txCommit();//提交事务
         }
     }catch (Exception e){
         channel.txRollback();//回退事务
         logger.error("发送小心出现错误",e);
     }
     //关闭通道和连接
     channel.close();
     connection.close();

  ```
消费者消费事务
```
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(true);
        boolean autoAck = false;
        final int[] count = {0};
        channel.basicConsume(QUEUE_NAME, autoAck, "myConsumerTag", new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        System.out.println("body:"+new String(body));
                        count[0]++;
                        System.out.println(count[0]);
                        channel.txCommit();
                    }
        });
        Thread.currentThread().join();
```
connection.createChannel(true); true表示开启事务 ，需要确认(channel.txCommit())后消息才被删除；false 相反。<br>
#WORK 模式
![图示](https://github.com/ShanShuan/myBlog/blob/master/mq/src/main/resources/static/work.jpg)<br>
一个生产者、2个消费者。<br>
一个消息只能被一个消费者获取。<br>
<br>
## -轮询不公平
消费者1：<br>
```
ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(true);
        boolean autoAck = false;
        final int[] count = {0};
        channel.basicConsume(QUEUE_NAME, autoAck, "myConsumerTag", new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        System.out.println("body:"+new String(body));
                        long deliveryTag = envelope.getDeliveryTag();
                        channel.basicAck(deliveryTag, false);
                        count[0]++;
                        System.out.println(count[0]);
                        channel.txCommit();
                    }
        });
        Thread.currentThread().join();
```
消费者2 同上所示<br>
生成者同简单模式 加一个for循环发送多条消息<br>

测试结果：<br>
1、消费者1和消费者2获取到的消息内容是不同的，同一个消息只能被一个消费者获取。<br>
2、消费者1和消费者2获取到的消息的数量是相同的，一个是消费奇数号消息，一个是偶数。<br>
*　其实，这样是不合理的，因为消费者1线程停顿的时间短。应该是消费者1要比消费者2获取到的消息多才对。
RabbitMQ 默认将消息顺序发送给下一个消费者，这样，每个消费者会得到相同数量的消息。即轮询（round-robin）分发消息
* 怎样才能做到按照每个消费者的能力分配消息呢？联合使用 Qos 和 Acknowledge 就可以做到<br>
basicQos 方法设置了当前信道最大预获取（prefetch）消息数量为1。消息从队列异步推送给消费者，消费者的 ack 也是异步发送给队列，从队列的视角去看，总是会有一批消息已推送但尚未获得 ack 确认，Qos 的 prefetchCount 参数就是用来限制这批未确认消息数量的。设为1时，队列只有在收到消费者发回的上一条消息 ack 确认后，
才会向该消费者发送下一条消息。prefetchCount 的默认值为0，即没有限制，队列会将所有消息尽快发给消费者<br>

为了解决这个问题，我们使用basicQos( prefetchCount = 1)方法，来限制RabbitMQ只发不超过1条的消息给同一个消费者。当消息处理完毕后，有了反馈，才会进行第二次发送。
还有一点需要注意，使用公平分发，必须关闭自动应答，改为手动应答

## -能者多劳
* //同一时刻服务器只会发一条消息给消费者<br>
  channel.basicQos(1);
* //开启这行 表示使用手动确认模式<br>
  channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
