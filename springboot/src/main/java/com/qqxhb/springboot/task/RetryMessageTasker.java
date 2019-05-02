package com.qqxhb.springboot.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qqxhb.springboot.constant.Constants;
import com.qqxhb.springboot.entity.BrokerMessageLog;
import com.qqxhb.springboot.entity.Order;
import com.qqxhb.springboot.mapper.BrokerMessageLogMapper;
import com.qqxhb.springboot.producer.RabbitOrderSender;
import com.qqxhb.springboot.utils.FastJsonConvertUtil;

@Component
public class RetryMessageTasker {

	
	@Autowired
	private RabbitOrderSender rabbitOrderSender;
	
	@Autowired
	private BrokerMessageLogMapper brokerMessageLogMapper;
	
	@Scheduled(initialDelay = 3000, fixedDelay = 10000)
	public void reSend(){
		System.err.println("---------------定时任务开始---------------");
		//pull status = 0 and timeout message 
		List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
		list.forEach(messageLog -> {
			if(messageLog.getTryCount() >= 3){
				//update fail message 
				brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), Constants.ORDER_SEND_FAILURE, new Date());
			} else {
				// resend 
				brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(),  new Date());
				Order reSendOrder = FastJsonConvertUtil.convertJSONToObject(messageLog.getMessage(), Order.class);
				try {
					rabbitOrderSender.sendOrder(reSendOrder);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("-----------异常处理-----------");
				}
			}			
		});
		
	}
	
	
}
