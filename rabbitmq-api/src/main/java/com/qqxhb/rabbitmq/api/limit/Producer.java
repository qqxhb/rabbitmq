package com.qqxhb.rabbitmq.api.limit;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
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
		// 必须设置消息投递模式为 消息确认模式
		channel.confirmSelect();

		String message = "Hello World QOS ";
		String exchangeName = "qos_exchange";
		String routingKey = "qos.route";
		channel.addConfirmListener(new ConfirmListener() {

			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("=======handleNack=========");
			}

			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("=======handleAck=========");
			}
		});
		for (int i = 0; i < 5; i++) {
			channel.basicPublish(exchangeName, routingKey, true, null, (message + i).getBytes());
		}
	}
}
