package com.manodrye.lakeside_hotel_server.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.manodrye.lakeside_hotel_server.entity.RoomEntity;

public interface IRoomService {
    
    RoomEntity addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice);
    List<String> getAllRoomTypes();
    List<RoomEntity> getAllRooms();
    byte[] getRoomPhotoByRoomId(Long roomId);
    void deleteRoom(Long roomId);
}