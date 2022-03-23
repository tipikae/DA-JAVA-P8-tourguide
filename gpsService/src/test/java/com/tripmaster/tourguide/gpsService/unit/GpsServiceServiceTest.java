package com.tripmaster.tourguide.gpsService.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tripmaster.tourguide.gpsService.converters.IConverterDTOAttraction;
import com.tripmaster.tourguide.gpsService.converters.IConverterDTOLocation;
import com.tripmaster.tourguide.gpsService.converters.IConverterDTONearByAttraction;
import com.tripmaster.tourguide.gpsService.converters.IConverterDTOVisitedLocation;
import com.tripmaster.tourguide.gpsService.converters.IConverterLibAttraction;
import com.tripmaster.tourguide.gpsService.dto.AttractionDTO;
import com.tripmaster.tourguide.gpsService.dto.LocationDTO;
import com.tripmaster.tourguide.gpsService.dto.NearByAttractionDTO;
import com.tripmaster.tourguide.gpsService.dto.VisitedLocationDTO;
import com.tripmaster.tourguide.gpsService.exceptions.ConverterDTOException;
import com.tripmaster.tourguide.gpsService.exceptions.ConverterLibException;
import com.tripmaster.tourguide.gpsService.exceptions.HttpException;
import com.tripmaster.tourguide.gpsService.exceptions.UserNotFoundException;
import com.tripmaster.tourguide.gpsService.model.MAttraction;
import com.tripmaster.tourguide.gpsService.model.MLocation;
import com.tripmaster.tourguide.gpsService.model.MVisitedLocation;
import com.tripmaster.tourguide.gpsService.model.NearByAttraction;
import com.tripmaster.tourguide.gpsService.remoteServices.IUserService;
import com.tripmaster.tourguide.gpsService.repository.IVisitedLocationRepository;
import com.tripmaster.tourguide.gpsService.service.GpsServiceServiceImpl;
import com.tripmaster.tourguide.gpsService.util.INearByAttractionOperation;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;

@ExtendWith(MockitoExtension.class)
class GpsServiceServiceTest {
	
	@Mock
	private IVisitedLocationRepository visitedLocationRepository;
	
	@Mock
	private GpsUtil gpsUtil;
	
	@Mock
	private IConverterDTOAttraction attractionDTOConverter;
	
	@Mock
	private IConverterDTOLocation locationDTOConverter;
	
	@Mock
	private IConverterDTOVisitedLocation visitedLocationDTOConverter;
	
	@Mock
	private IConverterLibAttraction attractionLibConverter;
	
	@Mock
	private INearByAttractionOperation nearByAttractionOperation;
	
	@Mock
	private IConverterDTONearByAttraction nearByAttractionDTOConverter;
	
	@Mock
	private IUserService userService;
	
	@InjectMocks
	private GpsServiceServiceImpl gpsService;
	
	private static String userName;
	private static UUID userId;
	private static MLocation mLocation;
	private static Date timeVisited;
	private static MVisitedLocation mVisitedLocation;
	private static List<MVisitedLocation> mVisitedLocations;
	private static MAttraction mAttraction;
	private static List<MAttraction> mAttractions;
	private static Attraction attraction;
	private static List<Attraction> attractions;
	private static AttractionDTO attractionDTO;
	private static List<AttractionDTO> attractionDTOs;
	
	@BeforeAll
	private static void setUp() {
		userName = "username";
		userId = UUID.randomUUID();
		mLocation = new MLocation(10d, 20d);
		timeVisited = new Date();
		mVisitedLocation = new MVisitedLocation(userId, mLocation, timeVisited);
		mVisitedLocations = new ArrayList<>();
		mVisitedLocations.add(mVisitedLocation);
		mAttraction = new MAttraction("name", "city", "state", 10d, 20d);
		mAttractions = new ArrayList<>();
		mAttractions.add(mAttraction);
		attraction = new Attraction("name", "city", "state", 10d, 20d);
		attractions = new ArrayList<>();
		attractions.add(attraction);
		attractionDTO = new AttractionDTO();
		attractionDTO.setAttractionName("name");
		attractionDTOs = new ArrayList<>();
		attractionDTOs.add(attractionDTO);
	}

	@Test
	void getAttractionsReturnsListWhenOk() throws ConverterDTOException, ConverterLibException {
		when(gpsUtil.getAttractions()).thenReturn(attractions);
		when(attractionLibConverter.convertLibAttractionsToMAttractions(anyList()))
			.thenReturn(mAttractions);
		when(attractionDTOConverter.convertAttractionsToDTos(anyList())).thenReturn(attractionDTOs);
		assertEquals(1, gpsService.getAttractions().size());
	}

	/*@Test
	void getUserLocationReturnsVisitedLocationWhenOk() {
		when(gpsUtil.getUserLocation(any(UUID.class))).thenReturn(visitedLocation);
		assertEquals(10d, gpsService.getUserLocation(userName).location.latitude);
	}

	@Test
	void getUserLocationThrowsExceptionWhenUserNotFound() {
		when(gpsUtil.getUserLocation(any(UUID.class))).thenReturn(visitedLocation);
		assertEquals(10d, gpsService.getUserLocation(userName).location.latitude);
	}*/
	
	@Test
	void getAllUsersLastLocationReturnsMapWhenOK() throws ConverterDTOException {
		Map<UUID, List<MVisitedLocation>> allUsersVisitedLocations = new HashMap<>();
		allUsersVisitedLocations.put(userId, mVisitedLocations);
		LocationDTO locationDTO = new LocationDTO();
		locationDTO.setLatitude(10d);
		when(visitedLocationRepository.findAll()).thenReturn(allUsersVisitedLocations);
		when(locationDTOConverter.convertEntityToDTO(any(MLocation.class))).thenReturn(locationDTO);
		assertEquals(mVisitedLocation.getLocation().getLatitude(), 
				gpsService.getAllUsersLastLocation().get(userId).getLatitude());
	}
	
	@Test
	void getUserVisitedLocationsReturnsListWhenOk() throws UserNotFoundException, ConverterDTOException {
		List<VisitedLocationDTO> visitedLocationDTOs = new ArrayList<>();
		LocationDTO locationDTO = new LocationDTO();
		locationDTO.setLongitude(20d);
		VisitedLocationDTO visitedLocationDTO = new VisitedLocationDTO();
		visitedLocationDTO.setLocation(locationDTO);
		visitedLocationDTOs.add(visitedLocationDTO);
		when(visitedLocationRepository.findByUserId(any(UUID.class)))
			.thenReturn(Optional.of(mVisitedLocations));
		when(visitedLocationDTOConverter.convertVisitedLocationsToDTOs(anyList()))
			.thenReturn(visitedLocationDTOs);
		assertEquals(20d, gpsService.getUserVisitedLocations(userId).get(0).getLocation().getLongitude());
	}
	
	@Test
	void getUserVisitedLocationsThrowsExceptionWhenUserNotFound() throws UserNotFoundException {
		when(visitedLocationRepository.findByUserId(any(UUID.class))).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () ->
				gpsService.getUserVisitedLocations(userId));
	}
	
	@Test
	void getNearbyAttractionsReturnsListWhenOk() 
			throws UserNotFoundException, ConverterLibException, HttpException, ConverterDTOException {
		NearByAttraction nearByAttraction = new NearByAttraction("name", mLocation, mLocation, 10, 100);
		List<NearByAttraction> nearByAttractions = new ArrayList<>();
		nearByAttractions.add(nearByAttraction);
		NearByAttractionDTO nearByAttractionDTO = 
				new NearByAttractionDTO("name", attractionDTO, attractionDTO, 10, 100);
		List<NearByAttractionDTO> nearByAttractionDTOs = new ArrayList<>();
		nearByAttractionDTOs.add(nearByAttractionDTO);
		when(userService.getUserId(anyString())).thenReturn(userId);
		when(visitedLocationRepository.findByUserId(any(UUID.class)))
			.thenReturn(Optional.of(mVisitedLocations));
		when(nearByAttractionOperation.getNearByAttractions(any(UUID.class))).thenReturn(nearByAttractions);
		when(nearByAttractionDTOConverter.convertNearByAttractionsToDTOs(anyList()))
			.thenReturn(nearByAttractionDTOs);
		assertEquals(1, gpsService.getNearByAttractions(userName).size());
	}
	
	@Test
	void getNearbyAttractionsThrowsExceptionWhenUserNotFound() throws HttpException {
		when(userService.getUserId(anyString())).thenReturn(userId);
		when(visitedLocationRepository.findByUserId(any(UUID.class))).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () ->
				gpsService.getNearByAttractions(userName));
	}

}
