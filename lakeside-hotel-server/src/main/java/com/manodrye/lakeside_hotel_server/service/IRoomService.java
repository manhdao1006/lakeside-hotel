package com.manodrye.lakeside_hotel_server.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.manodrye.lakeside_hotel_server.entity.RoomEntity;

public interface IRoomService {
    
    RoomEntity addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice);

    List<String> getAllRoomTypes();

    List<RoomEntity> getAllRooms();

    byte[] getRoomPhotoByRoomId(Long roomId);

    void deleteRoom(Long roomId);

    RoomEntity updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes);
    
    Optional<RoomEntity> getRoomById(Long roomId);

    List<RoomEntity> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}