package com.abigtomato.example.work;

import com.abigtomato.example.util.ConnectionUtil;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Recv {

    private final static String QUEUE_NAME = "work_queue";

    public static void main(String[] argv) throws Exception {
        // 获取到连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取通道
        final Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 设置每个消费者同时只能处理一条消息
        channel.basicQos(1);

        // 定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                log.info("recv1: " + new String(body));
                try {
                    // 模拟完成任务的耗时：1000ms
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 手动ACK
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        // 监听队列。
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}