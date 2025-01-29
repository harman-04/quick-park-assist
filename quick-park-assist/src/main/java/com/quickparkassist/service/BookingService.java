package com.quickparkassist.service;


import com.quickparkassist.model.Booking;
import com.quickparkassist.model.Spot;
import com.quickparkassist.repository.BookingRepository;
import com.quickparkassist.repository.SpotRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SpotRepository spotRepository;

    // Method to fetch bookings by mobile number
    public List<Booking> findByMobileNumber(String mobileNumber) {
        return bookingRepository.findByMobileNumber(mobileNumber);
    }
    // Add the findById method
    public Optional<Booking> findById(Long bookingId) {
        return bookingRepository.findById(bookingId);  // Calls the repository's findById
    }
    public List<Booking> findAllBookings() {
        return bookingRepository.findAll(); // Assuming you have a BookingRepository to interact with the DB
    }
    public List<Booking> findByStatus(Booking.Status status) {
        return bookingRepository.findByStatus(status);
    }
    // Save a booking
    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }
    public List<Booking> getBookingsByStatus(List<String> statuses) {
        return bookingRepository.findByStatusIn(statuses);
    }

    public List<Booking> findBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // Fetch bookings by user ID and a list of statuses without stimulating
    public List<Booking> findBookingsByUserIdAndStatuses(Long userId, List<Booking.Status> statuses) {
        return bookingRepository.findByUserIdAndStatusIn(userId, statuses);
    }


    public void updateBookingStatusTOCancel(Long id, Booking.Status status) {
    Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
    try {
       // Booking.Status statusEnum = Booking.Status.valueOf(status.toUpperCase()); // Convert String to Enum
        booking.setStatus(status);
        bookingRepository.save(booking);
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid status: " + status);
    }
}
    public boolean existsByMobileNumberAndUserId(String mobileNumber, Long userId) {
        return bookingRepository.existsByMobileNumberAndUserId(mobileNumber, userId);
    }


    public void updateBookingStatuses() {
        List<Booking> activeBookings = bookingRepository.findByStatus(Booking.Status.CONFIRMED);

        LocalDateTime now = LocalDateTime.now();
        for (Booking booking : activeBookings) {
            LocalDateTime endTime = booking.getStartTime().plusHours(Long.parseLong(booking.getDuration()));
            if (now.isAfter(endTime)) {
                booking.setStatus(Booking.Status.COMPLETED);
                bookingRepository.save(booking);

                // Free up the associated parking spot
                Spot spot = booking.getSpot();
                if (spot != null) {
                    spot.setAvailability("YES");
                    spotRepository.save(spot);
                }
            }

        }
    }

}

