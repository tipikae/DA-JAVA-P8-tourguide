/**
 * 
 */
package com.tripmaster.tourguide.gpsService.service;

import java.util.List;
import java.util.UUID;

import com.tripmaster.tourguide.gpsService.exceptions.CustomNumberFormatException;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

/**
 * Interface for accessing gpsUtil lib.
 * @author tipikae
 * @version 1.0
 *
 */
public interface IGpsServiceService {

	/**
	 * Get all attractions.
	 * @return List<Attraction>
	 */
	List<Attraction> getAttractions();
	
	/**
	 * Get user current location.
	 * @param userId UUID
	 * @return VisitedLocation
	 */
	VisitedLocation getUserLocation(UUID userId) throws CustomNumberFormatException;
}
