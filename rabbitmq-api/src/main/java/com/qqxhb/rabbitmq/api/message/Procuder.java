package com.qqxhb.rabbitmq.api.message;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Producing means nothing more than sending. A program that sends messages is a
 * producer。
 * 
 */
public class Procuder {

	public static void main(String[] args) throws Exception {
		/*
		 * 1、创建一个ConnectionFactory，设置主机及端口 
		 * 2、通过工厂创建连接 
		 * 3、通过连接 创建通道Channel 
		 * 4、通过通道发送消息
		 * 5、关闭资源
		 */
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setVirtualHost("/");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		String message = "Hello world.";
		Map<String, Object> headers = new HashMap<>();
		headers.put("myh", 666);
		/*
		 * 自定义一些配置信息
		 */
		BasicProperties properties = new BasicProperties().builder().deliveryMode(2).contentEncoding("UTF-8")
				.expiration("10000").headers(headers).build();
		for (int i = 0; i < 3; i++) {
			channel.basicPublish("", "quickstart", properties, message.getBytes());
		}

		channel.close();
		connection.close();
	}
}
