package com.hotelmanagementsystem.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.hotelmanagementsystem.dto.Response;
import com.hotelmanagementsystem.services.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> addNewRoom(@RequestParam(required = false) MultipartFile photo,
			@RequestParam(required = false) String roomType, @RequestParam(required = false) BigDecimal roomPrice,
			@RequestParam(required = false) String roomDescription) throws IOException {

//	public ResponseEntity<Response> addNewRoom(@RequestParam MultipartFile photo,
//			@RequestParam String roomType, @RequestParam BigDecimal roomPrice,
//			@RequestParam String roomDescription) throws IOException {

	
		if (photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null
				|| roomType.isBlank()) {
			Response response = new Response();
			response.setMessage("Please provide values for all fields(photo, roomType,roomPrice)");
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Response>(roomService.addNewRoom(photo, roomType, roomPrice, roomDescription),
				HttpStatus.CREATED);
	}

	@PutMapping("/update/{roomId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> updateRoom(@PathVariable Long roomId,
			@RequestParam(required = false) MultipartFile photo, @RequestParam(required = false) String roomType,
			@RequestParam(required = false) BigDecimal roomPrice, @RequestParam(required = false) String roomDescription

	) throws IOException {
		return new ResponseEntity<Response>(roomService.updateRoom(roomId, roomDescription, roomType, roomPrice, photo),
				HttpStatus.OK);
	}

	@DeleteMapping("/delete/{roomId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> deleteRoom(@PathVariable Long roomId) {
		return new ResponseEntity<Response>(roomService.deleteRoom(roomId), HttpStatus.OK);

	}

	@GetMapping("/room-by-id/{roomId}")
	public ResponseEntity<Response> getRoomById(@PathVariable Long roomId) {
		return ResponseEntity.ok(roomService.getRoomById(roomId));
	}

	@GetMapping("/all")
	public ResponseEntity<Response> getAllRooms() {
		Response response = roomService.getAllRooms();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/types")
	public List<String> getRoomTypes() {
		return roomService.getAllRoomTypes();
	}

	@GetMapping("/all-available-rooms")
	public ResponseEntity<Response> getAvailableRooms() {
		return ResponseEntity.ok(roomService.getAllAvailableRooms());
	}

	@GetMapping("/available-rooms-by-date-and-type")
	public ResponseEntity<Response> getAvailableRoomsByDateAndType(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
			@RequestParam(required = false) String roomType) {
		if (checkInDate == null || roomType == null || roomType.isBlank() || checkOutDate == null) {
			Response response = new Response();
			response.setMessage("Please provide values for all fields(checkInDate, roomType,checkOutDate)");
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Response>(
				roomService.getAvailableRoomsByDataAndType(checkInDate, checkOutDate, roomType), HttpStatus.OK);
	}
//?checkInDate=2024-09-24&checkOutDate=2024-09-28&roomType=Famaily
}
