package com.manodrye.lakeside_hotel_server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manodrye.lakeside_hotel_server.entity.BookingRoomEntity;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoomEntity, Long>{

    List<BookingRoomEntity> findByRoomId(Long roomId);
    
    Optional<BookingRoomEntity> findByBookingConfirmationCode(String confirmationCode);

    List<BookingRoomEntity> findByGuestEmail(String email);
    
}