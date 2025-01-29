package com.quickparkassist.schedular;

import static org.mockito.Mockito.*;

import com.quickparkassist.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BookingStatusUpdaterTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingStatusUpdater bookingStatusUpdater;

    @Test
    void testUpdateBookingStatuses() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Act
        bookingStatusUpdater.updateBookingStatuses();

        // Assert
        verify(bookingService, times(1)).updateBookingStatuses();
    }
}

