/**
 * 
 */
package com.tripmaster.tourguide.gpsService.converterDTO;

import java.util.List;

import com.tripmaster.tourguide.gpsService.model.MVisitedLocation;

import gpsUtil.location.VisitedLocation;

/**
 * Converter library VisitedLocation to model VisitedLocation.
 * @author tipikae
 * @version 1.0
 *
 */
public interface IConverterLibVisitedLocation extends IConverterLib<VisitedLocation, MVisitedLocation> {

	
	/**
	 * Convert library VisitedLocation list to model VisitedLocation list.
	 * @param libModels List<VisitedLocation>
	 * @return List<MVisitedLocation>
	 */
	List<MVisitedLocation> convertLibVisitedLocationsToMVisitedLocations(
			List<VisitedLocation> visitedLocations);
}
