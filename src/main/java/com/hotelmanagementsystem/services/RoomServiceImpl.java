package com.hotelmanagementsystem.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotelmanagementsystem.dto.Response;
import com.hotelmanagementsystem.dto.RoomDTO;
import com.hotelmanagementsystem.entity.Room;
import com.hotelmanagementsystem.exception.OurException;
import com.hotelmanagementsystem.repo.RoomRepository;

@Service
public class RoomServiceImpl implements RoomService {


    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;
  
    @Autowired
    private FileService fileService;

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) throws IOException {
        Response response = new Response();
           String imageUrl = fileService.uploadImage( photo);
            Room room = new Room();
           room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = modelMapper.map(savedRoom, RoomDTO.class);
            response.setMessage("successful");
            response.setRoom(roomDTO);
        return response;
    }
    
    
    @Override
    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) throws IOException {
        Response response = new Response();
            String imageUrl = null;
            if (photo != null && !photo.isEmpty()) {
            	 imageUrl = fileService.uploadImage(photo);
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomDescription(description);
            if (imageUrl != null) room.setRoomPhotoUrl(imageUrl);

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = modelMapper.map(updatedRoom, RoomDTO.class);
            response.setRoom(roomDTO);
        return response;
    }
    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            roomRepository.deleteById(roomId);
            response.setMessage("Deleted Succesfully");
        return response;
    }
    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            RoomDTO roomDTO = modelMapper.map(room, RoomDTO.class);
            response.setRoom(roomDTO);
        return response;
    }
    @Override
    public Response getAllRooms() {
        Response response = new Response();
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = roomList.stream().map(room->modelMapper.map(room, RoomDTO.class)).collect(Collectors.toList());
            response.setRoomList(roomDTOList);
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = availableRooms.stream().map(room->modelMapper.map(room, RoomDTO.class)).collect(Collectors.toList());
            response.setRoomList(roomDTOList);
        return response;
    }
    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = roomList.stream().map(room->modelMapper.map(room, RoomDTO.class)).collect(Collectors.toList());
            response.setRoomList(roomDTOList);
        return response;
    }
}
