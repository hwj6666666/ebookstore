package com.reins.bookstore.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cover")
@Data
public class BookCover {
  @Id private Integer bookId;
  private String cover;
}
