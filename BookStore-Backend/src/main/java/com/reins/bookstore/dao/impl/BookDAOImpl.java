package com.reins.bookstore.dao.impl;

import com.reins.bookstore.dao.BookDAO;
import com.reins.bookstore.dto.BookDTO;
import com.reins.bookstore.entity.Book;
import com.reins.bookstore.entity.BookCover; // 引入 BookCover
import com.reins.bookstore.entity.CartItem;
import com.reins.bookstore.entity.Tag;
import com.reins.bookstore.repository.BookCoverRepository;
import com.reins.bookstore.repository.BookRepository;
import com.reins.bookstore.repository.TagRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
  @Autowired BookCoverRepository bookCoverRepository; // 注入 BookCoverRepository
  @Autowired TagRepository tagRepository;
  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Override
  public Page<Book> searchBooksByKeyword(String keyword, Integer pageIndex, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageIndex, pageSize);
    String wrapped = "%" + keyword + "%";
    // 查询书籍
    Page<Book> booksPage = bookRepository.findAllByAuthorLikeOrTitleLike(wrapped, wrapped, pageable);

    // 为每本书填充 cover 字段
    List<Book> bookList = booksPage.getContent().stream().map(book -> {
      // 使用 Optional 和 isPresent 查找 bookCover
      Optional<BookCover> bookCover = bookCoverRepository.findByBookId(book.getId().intValue());

      // 判断是否存在 cover
      if (bookCover.isPresent()) {
        // 如果存在，设置 cover
        book.setCover(bookCover.get().getCover());
        logger.info("Cover found for book id: {}", book.getId());
      } else {
        // 如果不存在，设置 cover 为 null
        book.setCover(null);
        logger.info("Cover not found for book id: {}", book.getId());
      }

      return book;
    }).collect(Collectors.toList());

    // 返回分页的 Book 对象
    return new PageImpl<>(bookList, pageable, booksPage.getTotalElements());
  }

  @Override
  public Page<Book> searchBooksByTagAndKeyword(String tag, String keyword, Integer pageIndex, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageIndex, pageSize);
    String wrapped = "%" + keyword + "%";
    Tag tagEntity = tagRepository.findByName(tag);

    // 查询书籍
    Page<Book> booksPage = bookRepository.findAllByAuthorLikeOrTitleLikeAndTagsContaining(wrapped, wrapped, tagEntity, pageable);

    // 为每本书填充 cover 字段
    List<Book> bookList = booksPage.getContent().stream().map(book -> {
      // 使用 Optional 和 isPresent 查找 bookCover
      Optional<BookCover> bookCover = bookCoverRepository.findByBookId(book.getId().intValue());

      // 判断是否存在 cover
      if (bookCover.isPresent()) {
        // 如果存在，设置 cover
        book.setCover(bookCover.get().getCover());
        logger.info("Cover found for book id: {}", book.getId());
      } else {
        // 如果不存在，设置 cover 为 null
        book.setCover(null);
        logger.info("Cover not found for book id: {}", book.getId());
      }

      return book;
    }).collect(Collectors.toList());

    // 返回分页的 Book 对象
    return new PageImpl<>(bookList, pageable, booksPage.getTotalElements());
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
      // 从 MongoDB 获取 cover 信息
      Optional<BookCover> bookCover = bookCoverRepository.findByBookId(id.intValue());
      String cover = (bookCover.isPresent()) ? bookCover.get().getCover() : null;

      // 获取标签信息
      List<String> tagNames = book.getTags().stream().map(Tag::getName).collect(Collectors.toList());

      // 创建 BookDTO 对象
      bookDTO = new BookDTO(
              book.getId(),
              book.getTitle(),
              book.getAuthor(),
              book.getDescription(),
              book.getPrice(),
              cover, // 从 MongoDB 获取 cover
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

      // 从 MongoDB 获取 cover 信息
      Optional<BookCover> bookCover = bookCoverRepository.findByBookId(book.getId().intValue());
      String cover = (bookCover.isPresent()) ? bookCover.get().getCover() : null;

      // 获取标签信息
      List<String> tagNames = book.getTags().stream().map(Tag::getName).collect(Collectors.toList());

      // 创建新的 BookDTO 对象
      BookDTO bookDTO = new BookDTO(
              book.getId(),
              book.getTitle(),
              book.getAuthor(),
              book.getDescription(),
              book.getPrice(),
              cover, // 从 MongoDB 获取 cover
              book.getSales(),
              tagNames
      );

      String cacheKey = BOOK_CACHE_KEY_PREFIX + book.getId();
      try {
        // 删除旧的 Redis 缓存
        redisTemplate.delete(cacheKey);
        logger.info("Cache deleted for book id: {}", book.getId());
      } catch (Exception e) {
        logger.error("Failed to delete cache for book id: {}", book.getId(), e);
      }

      // 尝试将新的 BookDTO 存入 Redis
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
    // 获取销量排名前10的书籍
    List<Book> topBooks = bookRepository.findTop10ByOrderBySalesDesc();

    // 为每本书填充 cover 字段
    topBooks = topBooks.stream().map(book -> {
      // 使用 Optional 和 isPresent 查找 bookCover
      Optional<BookCover> bookCover = bookCoverRepository.findByBookId(book.getId().intValue());

      // 判断是否存在 cover
      if (bookCover.isPresent()) {
        // 如果存在，设置 cover
        book.setCover(bookCover.get().getCover());
        logger.info("Cover found for book id: {}", book.getId());
      } else {
        // 如果不存在，设置 cover 为 null
        book.setCover(null);
        logger.info("Cover not found for book id: {}", book.getId());
      }

      return book;
    }).collect(Collectors.toList());

    return topBooks;
  }

  @Override
  public List<Tag> getAllTags() {
    return tagRepository.findAll();
  }
}
