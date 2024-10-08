package com.reins.bookstore.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderMessage {
  private Long userId;
  private OrderInfo orderInfo;

  public OrderMessage(Long userId, OrderInfo orderInfo) {
    this.userId = userId;
    this.orderInfo = orderInfo;
  }
}
