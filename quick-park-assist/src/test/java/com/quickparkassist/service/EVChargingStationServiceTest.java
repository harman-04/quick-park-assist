package com.quickparkassist.service;

import com.quickparkassist.model.EVChargingStation;
import com.quickparkassist.repository.EVChargingStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EVChargingStationServiceTest {

    @InjectMocks
    private EVChargingStationService chargingStationService;

    @Mock
    private EVChargingStationRepository chargingStationRepository;

    private EVChargingStation testStation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        testStation = new EVChargingStation();
        testStation.setId(1L);
        testStation.setName("Super Charger");
        testStation.setLocation("123 Electric Ave");
    }

    @Test
    void testGetAllStations() {
        List<EVChargingStation> stations = new ArrayList<>();
        stations.add(testStation);

        when(chargingStationRepository.findAll()).thenReturn(stations);

        List<EVChargingStation> result = chargingStationService.getAllStations();

        assertEquals(1, result.size());
        assertEquals(testStation, result.get(0));
    }

    @Test
    void testGetStationById_Found() {
        when(chargingStationRepository.findById(1L)).thenReturn(Optional.of(testStation));

        Optional<EVChargingStation> result = chargingStationService.getStationById(1L);

        assertEquals(testStation, result.get());
    }

    @Test
    void testGetStationById_NotFound() {
        when(chargingStationRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<EVChargingStation> result = chargingStationService.getStationById(1L);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testSaveStation() {
        when(chargingStationRepository.save(any(EVChargingStation.class))).thenReturn(testStation);

        EVChargingStation result = chargingStationService.saveStation(testStation);

        assertEquals(testStation, result);
        verify(chargingStationRepository).save(testStation);
    }

    @Test
    void testDeleteStation() {
        Long stationId = 1L;

        chargingStationService.deleteStation(stationId);

        verify(chargingStationRepository).deleteById(stationId);
    }
}