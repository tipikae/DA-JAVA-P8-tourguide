/**
 * 
 */
package com.tripmaster.tourguide.rewardsCentral.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rewardCentral.RewardCentral;

/**
 * RewardCentral service implementation.
 * @author tipikae
 * @version 1.0
 *
 */
@Service
public class RewardCentralServiceImpl implements IRewardCentralService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RewardCentralServiceImpl.class);
	
	@Autowired
	private RewardCentral rewardCentral;

	@Override
	public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
		LOGGER.debug("getAttractionRewardPoints: attractionId=" + attractionId
				+ ", userId=" + userId);
		return rewardCentral.getAttractionRewardPoints(attractionId, userId);
	}

}
