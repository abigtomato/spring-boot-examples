package com.abigtomato.example.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {

    public static Connection getConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();

        // 设置主机端口
        factory.setHost("172.16.116.100");
        factory.setPort(5672);

        // 设置账号信息
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/abigtomato");

        return factory.newConnection();
    }
}
