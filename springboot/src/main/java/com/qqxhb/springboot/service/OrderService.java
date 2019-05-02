package com.qqxhb.springboot.service;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qqxhb.springboot.constant.Constants;
import com.qqxhb.springboot.entity.BrokerMessageLog;
import com.qqxhb.springboot.entity.Order;
import com.qqxhb.springboot.mapper.BrokerMessageLogMapper;
import com.qqxhb.springboot.mapper.OrderMapper;
import com.qqxhb.springboot.producer.RabbitOrderSender;
import com.qqxhb.springboot.utils.FastJsonConvertUtil;

@Service
public class OrderService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private BrokerMessageLogMapper brokerMessageLogMapper;
	
	@Autowired
	private RabbitOrderSender rabbitOrderSender;
	
	
	public void createOrder(Order order) throws Exception {
		// order current time 
		Date orderTime = new Date();
		// order insert
		orderMapper.insert(order);
		// log insert
		BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
		brokerMessageLog.setMessageId(order.getMessageId());
		//save order message as json
		brokerMessageLog.setMessage(FastJsonConvertUtil.convertObjectToJSON(order));
		brokerMessageLog.setStatus(Constants.ORDER_SENDING);
		brokerMessageLog.setNextRetry(DateUtils.addMinutes(orderTime, Constants.ORDER_TIMEOUT));
		brokerMessageLog.setCreateTime(new Date());
		brokerMessageLog.setUpdateTime(new Date());
		brokerMessageLogMapper.insert(brokerMessageLog);
		// order message sender
		rabbitOrderSender.sendOrder(order);
	}
	
}
