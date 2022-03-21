package com.tripmaster.tourguide.rewardService.unit;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tripmaster.tourguide.rewardService.controller.RewardServiceController;
import com.tripmaster.tourguide.rewardService.dto.RewardDTO;
import com.tripmaster.tourguide.rewardService.exceptions.UserNotFoundException;
import com.tripmaster.tourguide.rewardService.service.IRewardServiceService;

@WebMvcTest(RewardServiceController.class)
class RewardServiceControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private IRewardServiceService rewardService;
	
	private static String root;
	private static UUID userId;
	private static int points;
	private static RewardDTO rewardDTO;
	private static List<RewardDTO> rewardDTOs;
	
	@BeforeAll
	private static void setUp() {
		root = "/rewardservice";
		points = 100;
		userId = UUID.randomUUID();
		rewardDTO = new RewardDTO();
		rewardDTO.setRewardPoints(points);
		rewardDTOs = new ArrayList<>();
		rewardDTOs.add(rewardDTO);
	}
	
	@Test
	void calculate() {
		
	}
	
	@Test
	void getUserRewardsReturnsListWhenOk() throws UserNotFoundException, Exception {
		when(rewardService.getUserRewards(anyString())).thenReturn(rewardDTOs);
		mockMvc.perform(get(root + "/rewards/" + userId.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].rewardPoints", is(points)));
	}
	
	@Test
	void getUserRewardsReturns404WhenUserNotFound() throws Exception {
		doThrow(UserNotFoundException.class).when(rewardService).getUserRewards(anyString());
		mockMvc.perform(get(root + "/rewards/" + userId.toString()))
			.andExpect(status().is(404));
	}
	
	@Test
	void getUserRewardsReturns400WhenArgInvalid() throws Exception {
		mockMvc.perform(get(root + "/rewards/ "))
			.andExpect(status().is(400));
	}
	
	@Test
	void getUserRewardsPointsReturnsIntWhenOk() throws Exception {
		when(rewardService.getUserRewardsPoints(any(UUID.class))).thenReturn(points);
		mockMvc.perform(get(root + "/points/" + userId.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is(points)));
	}
	
	@Test
	void getUserRewardsPointsReturns404WhenUserNotFound() throws Exception {
		doThrow(UserNotFoundException.class).when(rewardService).getUserRewardsPoints(any(UUID.class));
		mockMvc.perform(get(root + "/points/" + userId.toString()))
			.andExpect(status().is(404));
	}
	
	@Test
	void getUserRewardsPointsReturns400WhenArgInvalid() throws Exception {
		mockMvc.perform(get(root + "/points/ "))
			.andExpect(status().is(400));
	}

}
