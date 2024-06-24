package com.manodrye.lakeside_hotel_server.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.manodrye.lakeside_hotel_server.dto.BookingRoomDTO;
import com.manodrye.lakeside_hotel_server.dto.RoomDTO;
import com.manodrye.lakeside_hotel_server.entity.BookingRoomEntity;
import com.manodrye.lakeside_hotel_server.entity.RoomEntity;
import com.manodrye.lakeside_hotel_server.exception.InvalidBookingRequestException;
import com.manodrye.lakeside_hotel_server.exception.ResourceNotFoundException;
import com.manodrye.lakeside_hotel_server.service.IBookingRoomService;
import com.manodrye.lakeside_hotel_server.service.IRoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookings")
public class BookingRoomController {

    private final IBookingRoomService bookingRoomService;
    private final IRoomService roomService;

    @GetMapping("/all-bookings")    
    public ResponseEntity<List<BookingRoomDTO>> getAllBookings(){
        List<BookingRoomEntity> bookingRoomEntities = bookingRoomService.getAllBookings();
        List<BookingRoomDTO> bookingRoomDTOs = new ArrayList<>();
        for(BookingRoomEntity bookingRoomEntity : bookingRoomEntities){
            BookingRoomDTO bookingRoomDTO = getBookingDTO(bookingRoomEntity);
            bookingRoomDTOs.add(bookingRoomDTO);
        }

        return ResponseEntity.ok(bookingRoomDTOs);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try {
            BookingRoomEntity bookingRoomEntity = bookingRoomService.findByBookingConfirmationCode(confirmationCode);
            BookingRoomDTO bookingRoomDTO = getBookingDTO(bookingRoomEntity);

            return ResponseEntity.ok(bookingRoomDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/room/{roomId}/add-booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                        @RequestBody BookingRoomEntity bookingRoomEntity) {
        try {
            String confirmationCode = bookingRoomService.saveBooking(roomId, bookingRoomEntity);
            return ResponseEntity.ok("Room booked successfully! Your booking confirmation code is: " + confirmationCode);
        } catch (InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
        bookingRoomService.cancelBooking(bookingId);
    }

    private BookingRoomDTO getBookingDTO(BookingRoomEntity bookingRoomEntity) {
        RoomEntity roomEntity = roomService.getRoomById(bookingRoomEntity.getRoom().getId().get());
        RoomDTO roomDTO = new RoomDTO(roomEntity.getId(), roomEntity.getRoomType(), roomEntity.getRoomPrice());

        return new BookingRoomDTO(bookingRoomEntity.getBookingId(),
                                    bookingRoomEntity.getCheckInDate(),
                                    bookingRoomEntity.getCheckOutDate(),
                                    bookingRoomEntity.getGuestFullName(),
                                    bookingRoomEntity.getGuestEmail(),
                                    bookingRoomEntity.getNumOfAdults(),
                                    bookingRoomEntity.getNumOfChildren(),
                                    bookingRoomEntity.getTotalNumOfGuest(),
                                    bookingRoomEntity.getBookingConfirmationCode(),
                                    roomDTO);
    }
}
