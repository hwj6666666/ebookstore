package com.reins.bookstore.repository;

import com.reins.bookstore.entity.BookCover;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookCoverRepository extends MongoRepository<BookCover, Integer> {
  Optional<BookCover> findByBookId(Integer bookId);
}
