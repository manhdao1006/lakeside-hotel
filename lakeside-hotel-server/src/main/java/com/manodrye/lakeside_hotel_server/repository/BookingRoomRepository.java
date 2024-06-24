package com.manodrye.lakeside_hotel_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manodrye.lakeside_hotel_server.entity.BookingRoomEntity;

public interface BookingRoomRepository extends JpaRepository<BookingRoomEntity, Long>{
    
}