package com.manodrye.lakeside_hotel_server.service;

import java.util.List;

import com.manodrye.lakeside_hotel_server.entity.UserEntity;

public interface IUserService {

    UserEntity registerUser(UserEntity userEntity);

    List<UserEntity> getUsers();

    void deleteUser(String email);

    UserEntity getUser(String email);
}
