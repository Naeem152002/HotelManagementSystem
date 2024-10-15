package com.hotelmanagementsystem.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hotelmanagementsystem.dto.BookingDTO;
import com.hotelmanagementsystem.dto.Response;
import com.hotelmanagementsystem.dto.RoomDTO;
import com.hotelmanagementsystem.dto.UserDTO;
import com.hotelmanagementsystem.entity.Booking;
import com.hotelmanagementsystem.entity.Room;
import com.hotelmanagementsystem.entity.User;
import com.hotelmanagementsystem.exception.OurException;
import com.hotelmanagementsystem.repo.BookingRepository;
import com.hotelmanagementsystem.repo.RoomRepository;
import com.hotelmanagementsystem.repo.UserRepository;
import com.hotelmanagementsystem.utils.Utils;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper; 


    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {

        Response response = new Response();
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date must come after check out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<Booking> existingBookings = room.getBookings();

            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new OurException("Room not Available for selected date range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setMessage("Successfully Save");
            response.setBookingConfirmationCode(bookingConfirmationCode);
        return response;
    }


    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {

        Response response = new Response();

            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new OurException("Booking Not Found"));
           BookingDTO bookingDTO=modelMapper.map(booking, BookingDTO.class);
           if(true) {
           bookingDTO.setUser(modelMapper.map(booking.getUser(), UserDTO.class));
           }if(booking.getRoom()!=null) {
        	 bookingDTO.setRoom(modelMapper.map(booking.getRoom(), RoomDTO.class));  
           }
            response.setBooking(bookingDTO);
        return response;
    }

    @Override
    public Response getAllBookings() {

        Response response = new Response();
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = bookingList.stream().map(booking->modelMapper.map(booking, BookingDTO.class)).collect(Collectors.toList());
            response.setBookingList(bookingDTOList);
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {

        Response response = new Response();
            bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking Does Not Exist"));
            bookingRepository.deleteById(bookingId);
            response.setMessage("Successfully Cancel Booking");
        return response;
    }


    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {

        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
