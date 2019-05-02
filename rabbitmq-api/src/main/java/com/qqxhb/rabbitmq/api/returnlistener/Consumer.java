package com.qqxhb.rabbitmq.api.returnlistener;

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
		 * 4、通过通道声明交换机
		 * 5、通过通道声明队列
		 * 6、通过通道声明绑定交换机和队列
		 * 7、创建消费者队列
		 * 8、给通道设置消费者及队列
		 * 9、获取消息
		 */
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setVirtualHost("/");
		
		Connection connection = factory.newConnection();
		
		Channel channel = connection.createChannel();
		String exchangeName = "return_listener_exchange";
		String routingKey = "return.#";
		String queueName = "return_queue";
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		channel.basicConsume(queueName, true,consumer);
		
		while (true) {
			Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println(msg);
		}
	}
}
