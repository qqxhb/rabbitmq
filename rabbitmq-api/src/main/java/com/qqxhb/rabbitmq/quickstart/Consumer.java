package com.qqxhb.rabbitmq.quickstart;

import com.qqxhb.rabbitmq.api.consumer.MyConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
		
		channel.basicConsume(queueName, true,new MyConsumer(channel));
	}
}
