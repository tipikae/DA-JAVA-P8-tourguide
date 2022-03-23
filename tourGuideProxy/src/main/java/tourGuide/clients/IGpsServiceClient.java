/**
 * 
 */
package tourGuide.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tourGuide.model.Attraction;
import tourGuide.model.NearByAttraction;
import tourGuide.model.VisitedLocation;

/**
 * Feign client for GpsService.
 * @author tipikae
 * @version 1.0
 *
 */
@FeignClient(name = "GpsService", url = "http://localhost:8081/gpsservice")
public interface IGpsServiceClient {

	/**
	 * Mapping for getAttractions.
	 * @return List<Attraction>
	 */
	@RequestMapping(value = "/attractions", method = RequestMethod.GET)
	List<Attraction> getAttractions();
	
	/**
	 * Mapping for getUserLocation.
	 * @param userId UUID
	 * @return VisitedLocation
	 */
	@RequestMapping(value = "/userlocation", method = RequestMethod.GET)
	VisitedLocation getUserLocation(@RequestParam("userId") UUID userId);
	
	/**
	 * Get NearByAttractions of a user.
	 * @param userName String
	 * @return List<NearByAttraction>
	 */
	@RequestMapping(value = "/nearbyattractions/{userName}")
	List<NearByAttraction> getNearByAttractions(@PathVariable("userName") String userName);
}
