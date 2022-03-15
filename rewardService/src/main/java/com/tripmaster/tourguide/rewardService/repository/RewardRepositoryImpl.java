/**
 * 
 */
package com.tripmaster.tourguide.rewardService.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.tripmaster.tourguide.rewardService.model.Reward;

/**
 * Reward repository.
 * @author tipikae
 * @version 1.0
 *
 */
@Repository
public class RewardRepositoryImpl implements IRewardRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RewardRepositoryImpl.class);
	
	private static Map<UUID, List<Reward>> internalRewards = new HashMap<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reward save(Reward reward) {
		LOGGER.debug("save");
		
		UUID userId = reward.getVisitedLocation().getUserId();
		if(internalRewards.containsKey(userId)) {
			List<Reward> rewards = internalRewards.get(userId);
			rewards.add(reward);
			internalRewards.replace(userId, rewards);
		} else {
			List<Reward> rewards = new ArrayList<>();
			rewards.add(reward);
			internalRewards.put(userId, rewards);
		}
		
		return reward;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<List<Reward>> findByUserId(UUID userId) {
		LOGGER.debug("findByUserId: userId=" + userId);
		
		if(internalRewards.containsKey(userId)) {
			return Optional.of(internalRewards.get(userId));
		}
		
		return Optional.empty();
	}

}
