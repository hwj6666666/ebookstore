package com.reins.bookstore.dao;

import com.reins.bookstore.entity.Address;
import com.reins.bookstore.entity.User;
import com.reins.bookstore.entity.UserAuth;
import com.reins.bookstore.models.AddAddressRequest;

import java.util.List;

public interface UserDAO {
    UserAuth getAuth(String username);

    User getById(Long id);

    Long getUserBalance(Long id);

    List<Address> getUserAddresses(Long userId);

    void addUserAddress(Long userId, AddAddressRequest request);

    void deleteUserAddress(Long userId, Long addressId);

    void updateUserBalance(Long id, Long balance);

    void updateUserPassword(Long id, String password);

    void updateUserIntroduction(Long id, String introduction);

    String updateUserAvatar(Long id, String avatar);
}
