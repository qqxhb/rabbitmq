package com.qqxhb.rabbitmq.api.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Producing means nothing more than sending. A program that sends messages is a
 * producer。
 * 
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		/*
		 * 1、创建一个ConnectionFactory，设置主机及端口 2、通过工厂创建连接 3、通过连接 创建通道Channel 4、声明交换机 路由
		 * 5、设置应答监听器 6、通过通道发送消息
		 */
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setVirtualHost("/");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();

		String message = "Hello World DLX ";
		String exchangeName = "dlx_exchange";
		String routingKey = "dlx.route";

		AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().deliveryMode(2).contentEncoding("UTF-8")
				.build();
		channel.basicPublish(exchangeName, routingKey, true, properties, message.getBytes());
	}
}
