package com.manodrye.lakeside_hotel_server.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manodrye.lakeside_hotel_server.entity.BookingRoomEntity;
import com.manodrye.lakeside_hotel_server.service.IBookingRoomService;

@Service
public class BookingRoomService implements IBookingRoomService{

    @Override
    public List<BookingRoomEntity> getAllBookedByRoomId(Long roomId) {
        return null;
    }

    @Override
    public List<BookingRoomEntity> getAllBookings() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllBookings'");
    }

    @Override
    public BookingRoomEntity findByBookingConfirmationCode(String confirmationCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByBookingConfirmationCode'");
    }

    @Override
    public String saveBooking(Long roomId, BookingRoomEntity bookingRoomEntity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveBooking'");
    }

    @Override
    public void cancelBooking(Long bookingId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelBooking'");
    }
    
}
