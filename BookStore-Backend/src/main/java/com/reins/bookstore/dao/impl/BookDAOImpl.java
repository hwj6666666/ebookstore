package com.reins.bookstore.dao.impl;

import com.reins.bookstore.dao.BookDAO;
import com.reins.bookstore.dto.BookDTO;
import com.reins.bookstore.entity.Book;
import com.reins.bookstore.entity.CartItem;
import com.reins.bookstore.entity.Tag;
import com.reins.bookstore.repository.BookRepository;
import com.reins.bookstore.repository.TagRepository;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class BookDAOImpl implements BookDAO {
  private static final Logger logger = LoggerFactory.getLogger(BookDAOImpl.class);
  private static final String BOOK_CACHE_KEY_PREFIX = "book_";
  @Autowired BookRepository bookRepository;
  @Autowired TagRepository tagRepository;
  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Override
  public Page<Book> searchBooksByKeyword(String keyword, Integer pageIndex, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageIndex, pageSize);
    String wrapped = "%" + keyword + "%";
    return bookRepository.findAllByAuthorLikeOrTitleLike(wrapped, wrapped, pageable);
  }

  @Override
  public Page<Book> searchBooksByTagAndKeyword(
      String tag, String keyword, Integer pageIndex, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageIndex, pageSize);
    String wrapped = "%" + keyword + "%";
    Tag tagEntity = tagRepository.findByName(tag);
    return bookRepository.findAllByAuthorLikeOrTitleLikeAndTagsContaining(
        wrapped, wrapped, tagEntity, pageable);
  }

  @Override
  public BookDTO getById(Long id) {
    String cacheKey = BOOK_CACHE_KEY_PREFIX + id;
    BookDTO bookDTO = null;

    // 尝试从 Redis 获取
    try {
      bookDTO = (BookDTO) redisTemplate.opsForValue().get(cacheKey);
      if (bookDTO != null) {
        logger.info("Cache hit for book id: {}", id);
        return bookDTO;
      }
    } catch (Exception e) {
      logger.error("Failed to get book from cache, falling back to database", e);
    }

    // 从数据库获取书籍信息
    Book book = bookRepository.findById(id).orElse(null);
    if (book != null) {
      List<String> tagNames = book.getTags().stream().map(Tag::getName).collect(Collectors.toList());
      bookDTO = new BookDTO(
              book.getId(),
              book.getTitle(),
              book.getAuthor(),
              book.getDescription(),
              book.getPrice(),
              book.getCover(),
              book.getSales(),
              tagNames
      );

      // 尝试将结果存入 Redis
      try {
        redisTemplate.opsForValue().set(cacheKey, bookDTO, 10, TimeUnit.MINUTES);
        logger.info("Cache set for book id: {}", id);
      } catch (Exception e) {
        logger.error("Failed to set book to cache", e);
      }
    }

    return bookDTO;
  }

  @Override
  @Transactional
  public void updateSales(List<CartItem> cartItems) {
    for (CartItem cartItem : cartItems) {
      Book book = cartItem.getBook();
      if (book == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }

      // 更新销售数量
      book.setSales(book.getSales() + cartItem.getNumber());
      bookRepository.save(book);

      // 获取 BookDTO
      List<String> tagNames =
          book.getTags().stream().map(Tag::getName).collect(Collectors.toList());
      BookDTO bookDTO =
          new BookDTO(
              book.getId(),
              book.getTitle(),
              book.getAuthor(),
              book.getDescription(),
              book.getPrice(),
              book.getCover(),
              book.getSales(),
              tagNames);

      String cacheKey = BOOK_CACHE_KEY_PREFIX + book.getId();
      try {
        // 删除 Redis 缓存
        redisTemplate.delete(cacheKey);
        logger.info("Cache deleted for book id: {}", book.getId());
      } catch (Exception e) {
        logger.error("Failed to delete cache for book id: {}", book.getId(), e);
      }

      // 尝试将 BookDTO 放入 Redis 缓存
      try {
        redisTemplate.opsForValue().set(cacheKey, bookDTO);
        logger.info("Cache set for book id: {}", book.getId());
      } catch (Exception e) {
        logger.error("Failed to set book to cache", e);
      }
    }
  }

  @Override
  public List<Book> getTop10BestsellingBooks() {
    return bookRepository.findTop10ByOrderBySalesDesc();
  }

  @Override
  public List<Tag> getAllTags() {
    return tagRepository.findAll();
  }
}
