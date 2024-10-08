package com.reins.bookstore.service;

import com.reins.bookstore.entity.Address;
import com.reins.bookstore.entity.User;
import com.reins.bookstore.models.AddAddressRequest;
import com.reins.bookstore.models.ApiResponseBase;

import java.util.List;

public interface UserService {
    User getUser(Long userId);

    List<Address> getUserAddresses(Long userId);

    ApiResponseBase addUserAddresses(Long userId, AddAddressRequest addAddressRequest);

    ApiResponseBase deleteUserAddress(Long userId, Long addressId);

    ApiResponseBase changeMyPassword(Long userId, String password);

    ApiResponseBase changeMyIntroduction(Long userId, String password);

    String changeMyAvatar(Long userId, String avatarPath);
}
