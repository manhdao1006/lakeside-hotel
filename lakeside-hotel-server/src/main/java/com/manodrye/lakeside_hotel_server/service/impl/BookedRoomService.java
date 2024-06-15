package com.manodrye.lakeside_hotel_server.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manodrye.lakeside_hotel_server.entity.BookedRoomEntity;
import com.manodrye.lakeside_hotel_server.service.IBookedRoomService;

@Service
public class BookedRoomService implements IBookedRoomService{

    @Override
    public List<BookedRoomEntity> getAllBookedByRoomId(Long roomId) {
        return null;
    }
    
}
