package com.manodrye.lakeside_hotel_server.service;

import java.util.List;

import com.manodrye.lakeside_hotel_server.entity.BookingRoomEntity;

public interface IBookingRoomService {

    List<BookingRoomEntity> getAllBookingsByRoomId(Long roomId);

    List<BookingRoomEntity> getAllBookings();

    BookingRoomEntity findByBookingConfirmationCode(String confirmationCode);

	String saveBooking(Long roomId, BookingRoomEntity bookingRoomEntity);

    void cancelBooking(Long bookingId);

    List<BookingRoomEntity> getBookingsByUserEmail(String email);
    
}