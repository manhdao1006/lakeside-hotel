package com.manodrye.lakeside_hotel_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manodrye.lakeside_hotel_server.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>{

    Optional<RoleEntity> findByName(String name);

    boolean existsByName(RoleEntity role);

}
