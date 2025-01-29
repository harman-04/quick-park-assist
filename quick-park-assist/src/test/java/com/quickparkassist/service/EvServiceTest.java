package com.quickparkassist.service;

import com.quickparkassist.model.Evmodel;
import com.quickparkassist.repository.EvRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EvServiceTest {

    @InjectMocks
    private EvService evService;

    @Mock
    private EvRepository evRepository;

    private Evmodel testReservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        testReservation = new Evmodel();
        testReservation.setId(1L);
        testReservation.setLocation("Test Location");
    }

    @Test
    void testGetAllReservations() {
        List<Evmodel> reservations = new ArrayList<>();
        reservations.add(testReservation);
        
        when(evRepository.findAll()).thenReturn(reservations);

        List<Evmodel> result = evService.getAllReservations();

        assertEquals(1, result.size());
        assertEquals(testReservation, result.get(0));
    }

    @Test
    void testGetReservationById_Found() {
        when(evRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        Evmodel result = evService.getReservationById(1L);

        assertEquals(testReservation, result);
    }

    @Test
    void testGetReservationById_NotFound() {
        when(evRepository.findById(1L)).thenReturn(Optional.empty());

        Evmodel result = evService.getReservationById(1L);

        assertNull(result);
    }

    @Test
    void testSaveReservation() {
        when(evRepository.save(any(Evmodel.class))).thenReturn(testReservation);

        Evmodel result = evService.saveReservation(testReservation);

        assertEquals(testReservation, result);
        verify(evRepository).save(testReservation);
    }

    @Test
    void testDeleteReservation() {
        Long reservationId = 1L;

        evService.deleteReservation(reservationId);

        verify(evRepository).deleteById(reservationId);
    }
}