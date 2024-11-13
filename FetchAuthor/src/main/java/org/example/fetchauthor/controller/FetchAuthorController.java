package org.example.fetchauthor.controller;

import org.example.fetchauthor.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FetchAuthorController {

  @Autowired BookRepository bookRepository;

  @GetMapping("/author")
  public String fetchAuthor(@RequestParam String title) {
    return bookRepository.findAuthorByTitle(title);
    //    if (book.equals("book1")) {
    //      return "author1";
    //    } else if (book.equals("book2")) {
    //      return "author2";
    //    } else {
    //      return "author3";
    //    }
  }
}
