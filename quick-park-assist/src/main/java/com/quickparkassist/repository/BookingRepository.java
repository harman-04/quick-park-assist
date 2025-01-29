package com.quickparkassist.repository;


import com.quickparkassist.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByMobileNumber(String mobileNumber);
    List<Booking> findByStatus(Booking.Status status);
    List<Booking> findByStatusIn(List<String> statuses);

    // Find bookings by userId
    List<Booking> findByUserId(Long userId);

    // Fetch bookings by user ID and a list of statuses without Simulating
    List<Booking> findByUserIdAndStatusIn(Long userId, List<Booking.Status> statuses);

    boolean existsByMobileNumberAndUserId(String mobileNumber, Long userId);

}
