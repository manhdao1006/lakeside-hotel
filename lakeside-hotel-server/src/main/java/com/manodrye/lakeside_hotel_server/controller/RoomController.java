package com.manodrye.lakeside_hotel_server.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.manodrye.lakeside_hotel_server.dto.RoomDTO;
import com.manodrye.lakeside_hotel_server.entity.RoomEntity;
import com.manodrye.lakeside_hotel_server.service.IRoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final IRoomService roomService;

    @PostMapping("/add/new-room")    
    public ResponseEntity<RoomDTO> addNewRoom(@RequestParam("photo") MultipartFile photo, 
                                              @RequestParam("roomType") String roomType, 
                                              @RequestParam("roomPrice") BigDecimal roomPrice){
        RoomEntity savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomDTO roomDTO = new RoomDTO(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
        
        return ResponseEntity.ok(roomDTO);
    }
}