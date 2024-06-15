package com.manodrye.lakeside_hotel_server.service;

import java.util.List;

import com.manodrye.lakeside_hotel_server.entity.BookedRoomEntity;

public interface IBookedRoomService {

    List<BookedRoomEntity> getAllBookedByRoomId(Long roomId);
    
}