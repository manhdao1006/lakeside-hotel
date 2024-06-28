package com.manodrye.lakeside_hotel_server.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.manodrye.lakeside_hotel_server.entity.RoomEntity;
import com.manodrye.lakeside_hotel_server.repository.RoomRepository;
import com.manodrye.lakeside_hotel_server.service.IRoomService;
import com.manodrye.lakeside_hotel_server.exception.ResourceNotFoundException;
import com.manodrye.lakeside_hotel_server.exception.InternalServerException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;

    @Override
    public RoomEntity addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomType(roomType);
        roomEntity.setRoomPrice(roomPrice);
        if (!file.isEmpty()) {
            byte[] photoBytes;
            try {
                photoBytes = file.getBytes();
                Blob photBlob;
                photBlob = new SerialBlob(photoBytes);
                roomEntity.setPhoto(photBlob);
            } catch (IOException | SQLException e) {
            }
        }
        return roomRepository.save(roomEntity);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) {
        Optional<RoomEntity> theRoom = roomRepository.findById(roomId);
        if (theRoom.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, Room not found!");
        }
        Blob photBlob = theRoom.get().getPhoto();
        if (photBlob != null) {
            try {
                return photBlob.getBytes(1, (int) photBlob.length());
            } catch (SQLException e) {
            }
        }
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<RoomEntity> theRoom = roomRepository.findById(roomId);
        if (theRoom.isPresent()) {
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    public RoomEntity updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        
        if (roomType != null) {
            roomEntity.setRoomType(roomType);
        }
        if (roomPrice != null) {
            roomEntity.setRoomPrice(roomPrice);
        }
        if (photoBytes != null && photoBytes.length > 0) {
            try {
                roomEntity.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException e) {
                throw new InternalServerException("Error updating room");
            }
        }
        
        return roomRepository.save(roomEntity);
    }

    @Override
    public Optional<RoomEntity> getRoomById(Long roomId) {
        return Optional.of(roomRepository.findById(roomId).get());
    }    

    @Override
    public List<RoomEntity> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
    }
}