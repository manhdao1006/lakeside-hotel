package com.manodrye.lakeside_hotel_server.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.manodrye.lakeside_hotel_server.entity.RoomEntity;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long>{
    @Query("SELECT DISTINCT r.roomType FROM RoomEntity r")
    List<String> findDistinctRoomTypes();

    @Query(" SELECT r FROM RoomEntity r " +
            " WHERE r.roomType LIKE %:roomType% " +
            " AND r.id NOT IN (" +
            "  SELECT br.room.id FROM BookingRoomEntity br " +
            "  WHERE ((br.checkInDate <= :checkOutDate) AND (br.checkOutDate >= :checkInDate))" +
            ")")
    List<RoomEntity> findAvailableRoomsByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}