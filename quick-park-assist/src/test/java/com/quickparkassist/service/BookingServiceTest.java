package com.quickparkassist.service;

import com.quickparkassist.model.Booking;
import com.quickparkassist.model.Spot;
import com.quickparkassist.repository.BookingRepository;
import com.quickparkassist.repository.SpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SpotRepository spotRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindByMobileNumber() {
        String mobileNumber = "1234567890";
        List<Booking> bookings = new ArrayList<>();
        when(bookingRepository.findByMobileNumber(mobileNumber)).thenReturn(bookings);

        List<Booking> result = bookingService.findByMobileNumber(mobileNumber);

        assertEquals(bookings, result);
        verify(bookingRepository, times(1)).findByMobileNumber(mobileNumber);
    }

    @Test
    void testFindById() {
        Long bookingId = 1L;
        Optional<Booking> booking = Optional.of(new Booking());
        when(bookingRepository.findById(bookingId)).thenReturn(booking);

        Optional<Booking> result = bookingService.findById(bookingId);

        assertEquals(booking, result);
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void testFindAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.findAllBookings();

        assertEquals(bookings, result);
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testFindByStatus() {
        Booking.Status status = Booking.Status.CONFIRMED;
        List<Booking> bookings = new ArrayList<>();
        when(bookingRepository.findByStatus(status)).thenReturn(bookings);

        List<Booking> result = bookingService.findByStatus(status);

        assertEquals(bookings, result);
        verify(bookingRepository, times(1)).findByStatus(status);
    }

    @Test
    void testSaveBooking() {
        Booking booking = new Booking();

        bookingService.saveBooking(booking);

        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testGetBookingsByStatus() {
        //List<String> statuses = List.of("CONFIRMED", "PENDING");
        List<String> statuses = Arrays.asList("CONFIRMED", "PENDING");
        List<Booking> bookings = new ArrayList<>();
        when(bookingRepository.findByStatusIn(statuses)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByStatus(statuses);

        assertEquals(bookings, result);
        verify(bookingRepository, times(1)).findByStatusIn(statuses);
    }

    @Test
    void testFindBookingsByUserId() {
        Long userId = 1L;
        List<Booking> bookings = new ArrayList<>();
        when(bookingRepository.findByUserId(userId)).thenReturn(bookings);

        List<Booking> result = bookingService.findBookingsByUserId(userId);

        assertEquals(bookings, result);
        verify(bookingRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindBookingsByUserIdAndStatuses() {
        Long userId = 1L;
        List<Booking.Status> statuses = Arrays.asList(Booking.Status.CONFIRMED);
        List<Booking> bookings = new ArrayList<>();
        when(bookingRepository.findByUserIdAndStatusIn(userId, statuses)).thenReturn(bookings);

        List<Booking> result = bookingService.findBookingsByUserIdAndStatuses(userId, statuses);

        assertEquals(bookings, result);
        verify(bookingRepository, times(1)).findByUserIdAndStatusIn(userId, statuses);
    }

    @Test
    void testUpdateBookingStatusToCancel() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        bookingService.updateBookingStatusTOCancel(bookingId, Booking.Status.CANCELLED);

        assertEquals(Booking.Status.CANCELLED, booking.getStatus());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testUpdateBookingStatusToCancel_InvalidStatus() {
        Long bookingId = 1L;

        // Mock Booking object
        Booking booking = mock(Booking.class);

        // Mock the findById to return the mocked booking
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // Mock the behavior of setStatus to throw IllegalArgumentException
        doThrow(new IllegalArgumentException("Invalid status")).when(booking).setStatus(any());

        // Test the service method
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.updateBookingStatusTOCancel(bookingId, Booking.Status.CANCELLED);
        });

        // Assert the exception message
        assertEquals("Invalid status: CANCELLED", exception.getMessage());

        // Verify that save is never called due to the exception
        verify(bookingRepository, never()).save(booking);
    }


    @Test
    void testExistsByMobileNumberAndUserId() {
        String mobileNumber = "1234567890";
        Long userId = 1L;
        when(bookingRepository.existsByMobileNumberAndUserId(mobileNumber, userId)).thenReturn(true);

        boolean result = bookingService.existsByMobileNumberAndUserId(mobileNumber, userId);

        assertTrue(result);
        verify(bookingRepository, times(1)).existsByMobileNumberAndUserId(mobileNumber, userId);
    }

    @Test
    void testUpdateBookingStatuses() {
        Booking booking = new Booking();
        booking.setStatus(Booking.Status.CONFIRMED);
        booking.setStartTime(LocalDateTime.now().minusHours(2));
        booking.setDuration("1");

        Spot spot = new Spot();
        booking.setSpot(spot);

        List<Booking> bookings = Arrays.asList(booking);
        when(bookingRepository.findByStatus(Booking.Status.CONFIRMED)).thenReturn(bookings);

        bookingService.updateBookingStatuses();

        assertEquals(Booking.Status.COMPLETED, booking.getStatus());
        assertEquals("YES", spot.getAvailability());
        verify(bookingRepository, times(1)).save(booking);
        verify(spotRepository, times(1)).save(spot);
    }
    @Test
    void testUpdateBookingStatuses_EndTimeNotPassed() {
        Booking booking = new Booking();
        booking.setStatus(Booking.Status.CONFIRMED);
        booking.setStartTime(LocalDateTime.now().plusHours(2)); // End time in the future
        booking.setDuration("1");

        Spot spot = new Spot();
        booking.setSpot(spot);

        List<Booking> bookings = Arrays.asList(booking);
        when(bookingRepository.findByStatus(Booking.Status.CONFIRMED)).thenReturn(bookings);

        bookingService.updateBookingStatuses();

        assertEquals(Booking.Status.CONFIRMED, booking.getStatus()); // Status should remain unchanged
        assertNull(spot.getAvailability()); // Spot availability should remain unchanged
        verify(bookingRepository, never()).save(booking); // No save call for booking
        verify(spotRepository, never()).save(spot); // No save call for spot
    }
    @Test
    void testUpdateBookingStatuses_SpotIsNull() {
        Booking booking = new Booking();
        booking.setStatus(Booking.Status.CONFIRMED);
        booking.setStartTime(LocalDateTime.now().minusHours(2)); // End time in the past
        booking.setDuration("1");

        // No associated spot
        booking.setSpot(null);

        List<Booking> bookings = Arrays.asList(booking);
        when(bookingRepository.findByStatus(Booking.Status.CONFIRMED)).thenReturn(bookings);

        bookingService.updateBookingStatuses();

        assertEquals(Booking.Status.COMPLETED, booking.getStatus()); // Status should be updated
        verify(bookingRepository, times(1)).save(booking); // Save call for booking
        verify(spotRepository, never()).save(any()); // No save call for spot
    }



}
