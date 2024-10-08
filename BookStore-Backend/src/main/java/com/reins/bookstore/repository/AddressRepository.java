package com.reins.bookstore.repository;

import com.reins.bookstore.entity.Address;
import com.reins.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUser(User user);

    @Modifying
    void deleteAllByIdAndUser(Long id, User user);
}
