package com.manodrye.lakeside_hotel_server.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.manodrye.lakeside_hotel_server.entity.RoleEntity;
import com.manodrye.lakeside_hotel_server.entity.UserEntity;
import com.manodrye.lakeside_hotel_server.exception.UserAlreadyExistsException;
import com.manodrye.lakeside_hotel_server.repository.RoleRepository;
import com.manodrye.lakeside_hotel_server.repository.UserRepository;
import com.manodrye.lakeside_hotel_server.service.IUserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(UserEntity userEntity) {
        if (userRepository.existsByEmail(userEntity.getEmail())) {
            throw new UserAlreadyExistsException(userEntity.getEmail() + " already exists");
        }

        // encode password
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        RoleEntity userRole = roleRepository.findByName("ROLE_USER").get();
        userEntity.setRoles(Collections.singletonList(userRole));

        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        UserEntity theUser = getUser(email);
        if (theUser != null) {
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public UserEntity getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
