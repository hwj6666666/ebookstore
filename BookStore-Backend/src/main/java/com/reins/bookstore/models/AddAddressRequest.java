package com.reins.bookstore.models;

import lombok.Data;

@Data
public class AddAddressRequest {
    String address;
    String receiver;
    String tel;
}
