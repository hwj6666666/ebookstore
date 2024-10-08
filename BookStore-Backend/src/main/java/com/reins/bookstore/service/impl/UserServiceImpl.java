package com.reins.bookstore.service.impl;

import com.reins.bookstore.constants.Messages;
import com.reins.bookstore.dao.UserDAO;
import com.reins.bookstore.entity.Address;
import com.reins.bookstore.entity.User;
import com.reins.bookstore.models.AddAddressRequest;
import com.reins.bookstore.models.ApiResponseBase;
import com.reins.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDAO userDAO;

    @Override
    public User getUser(Long userId) {
        return userDAO.getById(userId);
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        return userDAO.getUserAddresses(userId);
    }

    @Override
    public ApiResponseBase addUserAddresses(Long userId, AddAddressRequest addAddressRequest) {
        userDAO.addUserAddress(userId, addAddressRequest);
        return ApiResponseBase.succeed(Messages.MODIFY_SUCCEED);
    }

    @Override
    @Transactional
    public ApiResponseBase deleteUserAddress(Long userId, Long addressId) {
        userDAO.deleteUserAddress(userId, addressId);
        return ApiResponseBase.succeed(Messages.DELETE_SUCCEED);
    }

    @Override
    @Transactional
    public ApiResponseBase changeMyPassword(Long userId, String password) {
        userDAO.updateUserPassword(userId, password);
        return ApiResponseBase.succeed(Messages.MODIFY_SUCCEED);
    }

    @Override
    @Transactional
    public ApiResponseBase changeMyIntroduction(Long userId, String introduction) {
        userDAO.updateUserIntroduction(userId, introduction);
        return ApiResponseBase.succeed(Messages.MODIFY_SUCCEED);
    }

    @Override
    @Transactional
    public String changeMyAvatar(Long userId, String avatarPath) {
        return userDAO.updateUserAvatar(userId, avatarPath);
    }
}
