package com.manodrye.lakeside_hotel_server.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manodrye.lakeside_hotel_server.entity.BookingRoomEntity;
import com.manodrye.lakeside_hotel_server.entity.RoomEntity;
import com.manodrye.lakeside_hotel_server.exception.InvalidBookingRequestException;
import com.manodrye.lakeside_hotel_server.repository.BookingRoomRepository;
import com.manodrye.lakeside_hotel_server.repository.RoomRepository;
import com.manodrye.lakeside_hotel_server.service.IBookingRoomService;
import com.manodrye.lakeside_hotel_server.service.IRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingRoomService implements IBookingRoomService{

    private final BookingRoomRepository bookingRoomRepository;
    private final RoomRepository roomRepository;
    private final IRoomService roomService;

    @Override
    public List<BookingRoomEntity> getAllBookingsByRoomId(Long roomId) {
        return bookingRoomRepository.findByRoomId(roomId);
    }

    @Override
    public List<BookingRoomEntity> getAllBookings() {
        return bookingRoomRepository.findAll();
    }

    @Override
    public BookingRoomEntity findByBookingConfirmationCode(String confirmationCode) {
        return bookingRoomRepository.findByBookingConfirmationCode(confirmationCode);
    }

    @Override
    public String saveBooking(Long roomId, BookingRoomEntity bookingRoomEntity) {
        if(bookingRoomEntity.getCheckOutDate().isBefore(bookingRoomEntity.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }

        RoomEntity roomEntity = roomService.getRoomById(roomId).get();
        List<BookingRoomEntity> existingBookings = roomEntity.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRoomEntity, existingBookings);
        if(roomIsAvailable){
            roomEntity.addBooking(bookingRoomEntity);
            bookingRoomRepository.save(bookingRoomEntity);
        } else {
            throw new InvalidBookingRequestException("Sorry! This room is not available for the selected dates");
        }

        return bookingRoomEntity.getBookingConfirmationCode();
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRoomRepository.deleteById(bookingId);
    }

    private boolean roomIsAvailable(BookingRoomEntity bookingRoomEntity, List<BookingRoomEntity> existingBookings) {
        return existingBookings.stream()
                                .noneMatch(existingBooking -> bookingRoomEntity.getCheckInDate().equals(existingBooking.getCheckInDate())

                                                            || bookingRoomEntity.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())

                                                            || (bookingRoomEntity.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                                            && bookingRoomEntity.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))

                                                            || (bookingRoomEntity.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                                            && bookingRoomEntity.getCheckOutDate().equals(existingBooking.getCheckOutDate()))

                                                            || (bookingRoomEntity.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                                            && bookingRoomEntity.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                                            || (bookingRoomEntity.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                                            && bookingRoomEntity.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                                            || (bookingRoomEntity.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                                            && bookingRoomEntity.getCheckOutDate().equals(bookingRoomEntity.getCheckInDate()))
                                    );
    }
}
