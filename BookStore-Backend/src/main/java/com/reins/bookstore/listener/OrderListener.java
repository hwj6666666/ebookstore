package com.reins.bookstore.listener;

import static com.reins.bookstore.constants.Messages.PROCESS_ORDER_SUCCEED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reins.bookstore.models.ApiResponseBase;
import com.reins.bookstore.models.OrderMessage;
import com.reins.bookstore.service.OrderService;
import com.reins.bookstore.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

  @Autowired private OrderService orderService;

  @Autowired private KafkaTemplate<String, ApiResponseBase> kafkaTemplate;
  @Autowired private WebSocketServer ws;

  @KafkaListener(topics = "orderTopic")
  public void listen(String message) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    OrderMessage orderMessage = mapper.readValue(message, OrderMessage.class);

    Thread.sleep(3000);

    ApiResponseBase response =
        orderService.placeOrder(orderMessage.getUserId(), orderMessage.getOrderInfo());
    ws.sendMessageToUser(orderMessage.getUserId().toString(), PROCESS_ORDER_SUCCEED);
  }
}
