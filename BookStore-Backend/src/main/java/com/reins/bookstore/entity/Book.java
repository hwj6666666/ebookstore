package com.reins.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    String cover;
    Integer sales;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Tag> tags;

    public Book(Long id) {
        this.id = id;
    }
}
