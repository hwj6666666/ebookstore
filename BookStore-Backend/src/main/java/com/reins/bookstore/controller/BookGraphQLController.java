package com.reins.bookstore.controller;

import com.reins.bookstore.dto.GraphQLBook;
import com.reins.bookstore.entity.Book;
import com.reins.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class BookGraphQLController {
  @Autowired BookRepository bookRepository;

  @QueryMapping
  public GraphQLBook getBookByTitle(@Argument String title) {
    System.out.println("getGraphQLBookByTitle: " + title);

    Book book = bookRepository.findBookByTitle(title);
    if (book == null) {
      return null;
    }
    GraphQLBook graphQLBook = new GraphQLBook();
    graphQLBook.setTitle(book.getTitle());
    graphQLBook.setAuthor(book.getAuthor());
    graphQLBook.setPrice(book.getPrice());
    return graphQLBook;
  }
}
