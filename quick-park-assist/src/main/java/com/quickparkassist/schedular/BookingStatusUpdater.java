package com.quickparkassist.schedular;



import com.quickparkassist.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookingStatusUpdater {

    @Autowired
    private BookingService bookingService;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void updateBookingStatuses() {
        bookingService.updateBookingStatuses();
    }
}
