package com.manodrye.lakeside_hotel_server.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.manodrye.lakeside_hotel_server.dto.BookingRoomDTO;
import com.manodrye.lakeside_hotel_server.dto.RoomDTO;
import com.manodrye.lakeside_hotel_server.entity.BookingRoomEntity;
import com.manodrye.lakeside_hotel_server.entity.RoomEntity;
import com.manodrye.lakeside_hotel_server.exception.PhotoRetrievalException;
import com.manodrye.lakeside_hotel_server.exception.ResourceNotFoundException;
import com.manodrye.lakeside_hotel_server.service.IBookingRoomService;
import com.manodrye.lakeside_hotel_server.service.IRoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final IRoomService roomService;
    private final IBookingRoomService bookingRoomService;

    @PostMapping("/add/new-room")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomDTO> addNewRoom(@RequestParam("photo") MultipartFile photo, 
                                              @RequestParam("roomType") String roomType, 
                                              @RequestParam("roomPrice") BigDecimal roomPrice) {
        RoomEntity savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomDTO roomDTO = new RoomDTO(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
        
        return ResponseEntity.ok(roomDTO);
    }

    @GetMapping("/room-types")
    public List<String> getRoomTypes(){
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<RoomEntity> roomEntities = roomService.getAllRooms();
        List<RoomDTO> roomDTOs = new ArrayList<>();
        for(RoomEntity roomEntity : roomEntities){
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(roomEntity.getId());
            if(photoBytes != null && photoBytes.length > 0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomDTO roomDTO = getRoomDTO(roomEntity);
                roomDTO.setPhoto(base64Photo);
                roomDTOs.add(roomDTO);
            }
        }
        return ResponseEntity.ok(roomDTOs);
    }

    @DeleteMapping("/delete/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable Long roomId,
                                              @RequestParam(required = false) String roomType,
                                              @RequestParam(required = false) BigDecimal roomPrice,
                                              @RequestParam(required = false) MultipartFile photo) throws IOException, SerialException, SQLException {
        
        byte[] photoBytes = (photo != null && !photo.isEmpty()) ? photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = (photoBytes != null && photoBytes.length > 0) ? new SerialBlob(photoBytes) : null;
        RoomEntity roomEntity = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        roomEntity.setPhoto(photoBlob);

        RoomDTO roomDTO = getRoomDTO(roomEntity);
        return ResponseEntity.ok(roomDTO);

    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomDTO>> getRoomById(@PathVariable Long roomId) {
        Optional<RoomEntity> theRoom = roomService.getRoomById(roomId);
        return theRoom.map(room -> {
            RoomDTO roomDTO = getRoomDTO(room);
            return ResponseEntity.ok(Optional.of(roomDTO));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms(@RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                                            @RequestParam("roomType") String roomType) {
        List<RoomEntity> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomDTO> roomDTOs = new ArrayList<>();
        for (RoomEntity roomEntity : availableRooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(roomEntity.getId());
            if(photoBytes != null && photoBytes.length > 0) {
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                RoomDTO roomDTO = getRoomDTO(roomEntity);
                roomDTO.setPhoto(photoBase64);
                roomDTOs.add(roomDTO);
            }
        }
        if(roomDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(roomDTOs);
        }
    }

    private RoomDTO getRoomDTO(RoomEntity roomEntity) {
        List<BookingRoomEntity> bookingRoomEntities = bookingRoomService.getAllBookingsByRoomId(roomEntity.getId());
        List<BookingRoomDTO> bookingInfo = bookingRoomEntities.stream()
                                                           .map(booking -> new BookingRoomDTO(booking.getBookingId(), 
                                                                                            booking.getCheckInDate(), 
                                                                                            booking.getCheckOutDate(), 
                                                                                            booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = roomEntity.getPhoto();
        if(photoBlob != null){
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new RoomDTO(roomEntity.getId(), roomEntity.getRoomType(), roomEntity.getRoomPrice(), roomEntity.isBooked(), photoBytes);
    }    
}