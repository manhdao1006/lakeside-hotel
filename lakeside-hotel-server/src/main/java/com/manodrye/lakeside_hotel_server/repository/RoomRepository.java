package com.manodrye.lakeside_hotel_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.manodrye.lakeside_hotel_server.entity.RoomEntity;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long>{
    @Query("SELECT DISTINCT r.roomType FROM RoomEntity r")
    List<String> findDistinctRoomTypes();
}