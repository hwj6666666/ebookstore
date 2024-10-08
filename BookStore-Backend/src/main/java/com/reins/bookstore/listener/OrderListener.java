package com.reins.bookstore.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reins.bookstore.models.ApiResponseBase;
import com.reins.bookstore.models.OrderInfo;
import com.reins.bookstore.models.OrderMessage;
import com.reins.bookstore.service.OrderService;
import com.reins.bookstore.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

  @Autowired private OrderService orderService;

  @Autowired private KafkaTemplate<String, ApiResponseBase> kafkaTemplate;

  @KafkaListener(topics = "orderTopic")
  public void listen(String message) throws Exception {
    ObjectMapper mapper=new ObjectMapper();
    OrderMessage orderMessage = mapper.readValue(message, OrderMessage.class);

    ApiResponseBase response = orderService.placeOrder(orderMessage.getUserId(), orderMessage.getOrderInfo());
//    kafkaTemplate.send("orderResponseTopic", response);
  }
}
