package com.hotelmanagementsystem.services;

import com.hotelmanagementsystem.dto.Response;
import com.hotelmanagementsystem.entity.Booking;

public interface BookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);

}
