package com.reins.bookstore.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String title;
  String author;

  @Column(columnDefinition = "TEXT")
  String description;

  Integer price;
  @Transient String cover;
  Integer sales;

  @ManyToMany(fetch = FetchType.EAGER)
  List<Tag> tags;

  public Book(Long id) {
    this.id = id;
  }
}
