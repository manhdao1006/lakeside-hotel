package com.manodrye.lakeside_hotel_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manodrye.lakeside_hotel_server.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

}
