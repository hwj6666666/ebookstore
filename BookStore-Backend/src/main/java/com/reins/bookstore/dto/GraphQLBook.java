package com.reins.bookstore.dto;

import lombok.Data;

@Data
public class GraphQLBook {
  private String title;
  private String author;
  private Integer price;
}
