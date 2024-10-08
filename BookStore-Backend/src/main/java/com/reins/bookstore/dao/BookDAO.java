package com.reins.bookstore.dao;

import com.reins.bookstore.entity.Book;
import com.reins.bookstore.entity.CartItem;
import com.reins.bookstore.entity.Tag;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookDAO {
    Page<Book> searchBooksByKeyword(String keyword, Integer pageIndex, Integer pageSize);

    Page<Book> searchBooksByTagAndKeyword(String tag, String keyword, Integer pageIndex, Integer pageSize);

    Book getById(Long id);

    void updateSales(List<CartItem> cartItems);

    List<Book> getTop10BestsellingBooks();

    List<Tag> getAllTags();
}
