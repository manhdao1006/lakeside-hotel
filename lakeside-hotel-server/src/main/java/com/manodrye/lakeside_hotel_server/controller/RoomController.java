package com.manodrye.lakeside_hotel_server.controller;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.manodrye.lakeside_hotel_server.dto.BookedRoomDTO;
import com.manodrye.lakeside_hotel_server.dto.RoomDTO;
import com.manodrye.lakeside_hotel_server.entity.BookedRoomEntity;
import com.manodrye.lakeside_hotel_server.entity.RoomEntity;
import com.manodrye.lakeside_hotel_server.service.IBookedRoomService;
import com.manodrye.lakeside_hotel_server.service.IRoomService;
import com.manodrye.lakeside_hotel_server.exception.PhotoRetrievalException;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final IRoomService roomService;
    private final IBookedRoomService bookedRoomService;

    @PostMapping("/add/new-room")    
    public ResponseEntity<RoomDTO> addNewRoom(@RequestParam("photo") MultipartFile photo, 
                                              @RequestParam("roomType") String roomType, 
                                              @RequestParam("roomPrice") BigDecimal roomPrice){
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
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private RoomDTO getRoomDTO(RoomEntity roomEntity) {
        List<BookedRoomEntity> bookedRoomEntities = bookedRoomService.getAllBookedByRoomId(roomEntity.getId());
        // List<BookedRoomDTO> bookedInfo = bookedRoomEntities.stream()
        //                                                    .map(booked -> new BookedRoomDTO(booked.getBookingId(), 
        //                                                                                     booked.getCheckInDate(), 
        //                                                                                     booked.getCheckOutDate(), 
        //                                                                                     booked.getBookingConfirmationCode())).toList();
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