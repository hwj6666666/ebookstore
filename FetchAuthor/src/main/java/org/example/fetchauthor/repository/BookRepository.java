package org.example.fetchauthor.repository;

import java.util.List;
import org.example.fetchauthor.entity.Book;
import org.example.fetchauthor.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
  Page<Book> findAllByAuthorLikeOrTitleLike(String author, String title, Pageable pageable);

  @Query(
      "SELECT b FROM Book b JOIN b.tags t WHERE (b.author LIKE %:author% OR b.title LIKE %:title%) AND t = :tag")
  Page<Book> findAllByAuthorLikeOrTitleLikeAndTagsContaining(
      @Param("author") String author,
      @Param("title") String title,
      @Param("tag") Tag tag,
      Pageable pageable);

  List<Book> findTop10ByOrderBySalesDesc();

  @Query("SELECT b.author FROM Book b WHERE b.title = :title")
  String findAuthorByTitle(@Param("title") String title);
}
