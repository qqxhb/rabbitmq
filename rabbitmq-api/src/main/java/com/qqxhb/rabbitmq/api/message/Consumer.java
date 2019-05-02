package com.qqxhb.rabbitmq.api.message;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer {

	public static void main(String[] args) throws Exception {

		/*
		 * 1、创建一个ConnectionFactory，设置主机及端口 
		 * 2、通过工厂创建连接 
		 * 3、通过连接 创建通道Channel 
		 * 4、通过通道声明队列
		 * 5、创建消费者 
		 * 6、给通道设置消费者及队列 
		 * 7、获取消息
		 */
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setVirtualHost("/");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		String queueName = "quickstart";
		channel.queueDeclare(queueName, true, false, false, null);

		QueueingConsumer consumer = new QueueingConsumer(channel);

		channel.basicConsume(queueName, true, consumer);

		while (true) {
			Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("message：" + msg);
			BasicProperties properties = delivery.getProperties();
			System.out.println("propertie of encoding：" + properties.getContentEncoding());
			System.out.println("my headers：" + properties.getHeaders());
		}
	}
}
