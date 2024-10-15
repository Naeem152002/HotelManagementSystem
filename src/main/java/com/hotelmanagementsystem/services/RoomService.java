package com.hotelmanagementsystem.services;

import org.springframework.web.multipart.MultipartFile;

import com.hotelmanagementsystem.dto.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description)throws IOException;

    List<String> getAllRoomTypes();

    Response getAllRooms();

    Response deleteRoom(Long roomId);

    Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo)throws IOException;

    Response getRoomById(Long roomId);

    Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    Response getAllAvailableRooms();
}
