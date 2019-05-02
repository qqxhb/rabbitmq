package com.qqxhb.rabbitmq.api.returnlistener;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;

/**
 * Producing means nothing more than sending. A program that sends messages is a
 * producer。
 * 
 */
public class Producer {

	public static void main(String[] args) throws Exception {
		/*
		 * 1、创建一个ConnectionFactory，设置主机及端口
		 * 2、通过工厂创建连接
		 * 3、通过连接 创建通道Channel
		 * 4、声明交换机 路由
		 * 5、设置结果监听器
		 * 6、通过通道发送消息
		 * 需要监听结果 固没有关闭资源操作
		 */
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setVirtualHost("/");
		
		Connection connection = factory.newConnection();
		
		Channel channel = connection.createChannel();
		String message = "Hello world Return Listener";
		String exchangeName = "return_listener_exchange";
		String routingKey = "return.listener";
		String routingKeyError = "error_listener";
		channel.addReturnListener(new ReturnListener() {
			
			@Override
			public void handleReturn(int replyCode,
		            String replyText,
		            String exchange,
		            String routingKey,
		            AMQP.BasicProperties properties,
		            byte[] body)
					throws IOException {
				System.out.println("=========Return Listener============");
				System.out.println("replyCode:"+replyCode);
				System.out.println("replyText:"+replyText);
				System.out.println("exchange:"+exchange);
				System.out.println("routingKey:"+routingKey);
				System.out.println("properties:"+properties);
				System.out.println("body:"+ new String(body));
				
			}
		});
		
		channel.basicPublish(exchangeName, routingKey, true,null, message.getBytes());
		channel.basicPublish(exchangeName, routingKeyError, true,null, message.getBytes());
	}
}
