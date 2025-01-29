package com.quickparkassist.service;

import com.quickparkassist.model.Spot;
import com.quickparkassist.repository.SpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

 class TestSpotService {

    @Mock
    private SpotRepository spotRepository;

    private SpotService spotService;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.initMocks(this);
        spotService = new SpotService();
        spotService.setSpotRepository(spotRepository);
    }

    @Test
     void testGetAvailableSpots() {

        Spot spot1 = new Spot();
        spot1.setSpotId(1L);
        spot1.setLocation("Location 1");
        spot1.setAvailability("yes");

        Spot spot2 = new Spot();
        spot2.setSpotId(2L);
        spot2.setLocation("Location 2");
        spot2.setAvailability("yes");

        List<Spot> mockSpots = Arrays.asList(spot1, spot2);


        when(spotRepository.findByAvailability("yes")).thenReturn(mockSpots);


        List<Spot> result = spotService.getAvailableSpots();


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("yes", result.get(0).getAvailability());
        assertEquals("yes", result.get(1).getAvailability());
    }

    @Test
     void testGetUnavailableSpots() {

        Spot spot1 = new Spot();
        spot1.setSpotId(1L);
        spot1.setLocation("Location 1");
        spot1.setAvailability("no");

        List<Spot> mockSpots = Collections.singletonList(spot1);


        when(spotRepository.findByAvailability("no")).thenReturn(mockSpots);


        List<Spot> result = spotService.getUnavailableSpots();


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("no", result.get(0).getAvailability());
    }

    @Test
     void testGetSpotsByLocationAndAvailability() {

        Spot spot1 = new Spot();
        spot1.setSpotId(1L);
        spot1.setLocation("Location 1");
        spot1.setSpotType("Type 1");
        spot1.setAvailability("yes");

        List<Spot> mockSpots = Collections.singletonList(spot1);


        when(spotRepository.findByLocationAndSpotTypeAndAvailability("Location 1", "Type 1", "yes"))
                .thenReturn(mockSpots);


        List<Spot> result = spotService.getSpotsByLocationAndAvailability("Location 1", "Type 1", "yes");


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Location 1", result.get(0).getLocation());
        assertEquals("Type 1", result.get(0).getSpotType());
        assertEquals("yes", result.get(0).getAvailability());
    }

    @Test
     void testGetSpotById() {

        Spot spot = new Spot();
        spot.setSpotId(1L);
        spot.setLocation("Location 1");


        when(spotRepository.findById(1L)).thenReturn(Optional.of(spot));


        Spot result = spotService.getSpotById(1L);


        assertNotNull(result);
        assertEquals(1L, result.getSpotId());
        assertEquals("Location 1", result.getLocation());
    }

    @Test
     void testSaveSpot() {

        Spot spot = new Spot();
        spot.setSpotId(1L);
        spot.setLocation("Location 1");


        spotService.saveSpot(spot);

        verify(spotRepository, times(1)).save(spot);
    }

    @Test
     void testRemoveSpot() {

        Long spotId = 1L;

        spotService.removeSpot(spotId);

        verify(spotRepository, times(1)).deleteById(spotId);
    }
    @Test
    void testGetSpotsByLocation() {
       // Arrange
       String location = "Downtown";
       List<Spot> expectedSpots = new ArrayList<>();
       expectedSpots.add(new Spot(/* initialize as needed */));

       when(spotRepository.findByLocation(location)).thenReturn(expectedSpots);

       // Act
       List<Spot> actualSpots = spotService.getSpotsByLocation(location);

       // Assert
       assertEquals(expectedSpots, actualSpots);
       verify(spotRepository, times(1)).findByLocation(location);
    }

    @Test
    void testGetAllSpotsOrderedByLocation() {
       // Arrange
       List<Spot> expectedSpots = new ArrayList<>();
       expectedSpots.add(new Spot(/* initialize as needed */));

       when(spotRepository.findAllByOrderByLocationAsc()).thenReturn(expectedSpots);

       // Act
       List<Spot> actualSpots = spotService.getAllSpotsOrderedByLocation();

       // Assert
       assertEquals(expectedSpots, actualSpots);
       verify(spotRepository, times(1)).findAllByOrderByLocationAsc();
    }

    @Test
    void testGetSpotsByType() {
       // Arrange
       String spotType = "Parking";
       List<Spot> expectedSpots = new ArrayList<>();
       expectedSpots.add(new Spot(/* initialize as needed */));

       when(spotRepository.findBySpotType(spotType)).thenReturn(expectedSpots);

       // Act
       List<Spot> actualSpots = spotService.getSpotsByType(spotType);

       // Assert
       assertEquals(expectedSpots, actualSpots);
       verify(spotRepository, times(1)).findBySpotType(spotType);
    }

    @Test
    void testGetAvailableSpotsByUserId() {
       // Arrange
       Long userId = 1L;
       List<Spot> expectedSpots = new ArrayList<>();
       expectedSpots.add(new Spot(/* initialize as needed */));

       when(spotRepository.findByUserId(userId)).thenReturn(expectedSpots);

       // Act
       List<Spot> actualSpots = spotService.getAvailableSpotsByUserId(userId);

       // Assert
       assertEquals(expectedSpots, actualSpots);
       verify(spotRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetAvailableSpotsByUser_Id() {
       // Arrange
       Long userId = 1L;
       List<Spot> expectedSpots = new ArrayList<>();
       expectedSpots.add(new Spot(/* initialize as needed */));

       when(spotRepository.findByUserIdAndAvailability(userId, "yes")).thenReturn(expectedSpots);

       // Act
       List<Spot> actualSpots = spotService.getAvailableSpotsByUser_Id(userId);

       // Assert
       assertEquals(expectedSpots, actualSpots);
       verify(spotRepository, times(1)).findByUserIdAndAvailability(userId, "yes");
    }

    @Test
    void testGetUnavailableSpotsByUserId() {
       // Arrange
       Long userId = 1L;
       List<Spot> expectedSpots = new ArrayList<>();
       expectedSpots.add(new Spot(/* initialize as needed */));

       when(spotRepository.findByUserIdAndAvailability(userId, "no")).thenReturn(expectedSpots);

       // Act
       List<Spot> actualSpots = spotService.getUnavailableSpotsByUserId(userId);

       // Assert
       assertEquals(expectedSpots, actualSpots);
       verify(spotRepository, times(1)).findByUserIdAndAvailability(userId, "no");
    }

    @Test
    void testUpdateSpot() {
       // Arrange
       Spot spotToUpdate = new Spot(/* initialize fields as needed */);

       // Act
       spotService.updateSpot(spotToUpdate);

       // Assert
       verify(spotRepository, times(1)).save(spotToUpdate);  // Verify that save was called with the correct spot instance.
    }
}
