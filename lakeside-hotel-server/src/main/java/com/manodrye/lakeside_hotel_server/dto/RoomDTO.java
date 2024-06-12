package com.manodrye.lakeside_hotel_server.dto;

import java.math.BigDecimal;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDTO {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private String photo;
    private List<BookedRoomDTO> bookings;
    
    public RoomDTO(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomDTO(Long id, String roomType, BigDecimal roomPrice, boolean isBooked, byte[] photoBytes,
            List<BookedRoomDTO> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
        this.bookings = bookings;
    }    
}
