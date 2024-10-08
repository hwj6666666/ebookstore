package com.reins.bookstore.dao.impl;

import com.reins.bookstore.dao.UserDAO;
import com.reins.bookstore.entity.Address;
import com.reins.bookstore.entity.User;
import com.reins.bookstore.entity.UserAuth;
import com.reins.bookstore.models.AddAddressRequest;
import com.reins.bookstore.repository.AddressRepository;
import com.reins.bookstore.repository.UserAuthRepository;
import com.reins.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAOImpl implements UserDAO {
    @Autowired
    UserAuthRepository userAuthRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public UserAuth getAuth(String username) {
        return userAuthRepository.findByUsername(username);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Long getUserBalance(Long id) {
        User user = getById(id);
        if (user == null) {
            return null;
        }
        return user.getBalance();
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        User user = new User(userId);
        return addressRepository.findAllByUser(user);
    }

    @Override
    public void addUserAddress(Long userId, AddAddressRequest request) {
        User user = new User(userId);
        Address address = new Address(null,
                request.getAddress(),
                request.getTel(),
                request.getReceiver(),
                user
        );
        addressRepository.save(address);
    }

    @Override
    public void deleteUserAddress(Long userId, Long addressId) {
        User user = new User(userId);
        addressRepository.deleteAllByIdAndUser(addressId, user);
    }

    @Override
    public void updateUserBalance(Long id, Long balance) {
        User user = getById(id);
        if (user != null) {
            user.setBalance(balance);
            userRepository.save(user);
        }
    }

    @Override
    public void updateUserPassword(Long id, String password) {
        UserAuth userAuth = userAuthRepository.findById(id).orElse(null);
        if (userAuth != null) {
            userAuth.setPassword(password);
            userAuthRepository.save(userAuth);
        }
    }

    @Override
    public void updateUserIntroduction(Long id, String introduction) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setIntroduction(introduction);
            userRepository.save(user);
        }
    }

    @Override
    public String updateUserAvatar(Long id, String avatar) {
        User user = userRepository.findById(id).orElse(null);
        String oldAvatar = null;
        if (user != null) {
            oldAvatar = user.getAvatar();
            user.setAvatar(avatar);
            userRepository.save(user);
        }
        return oldAvatar;
    }
}
