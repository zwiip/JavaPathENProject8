package com.openclassrooms.tourguide;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.service.RewardsService;
import java.util.concurrent.ExecutorService;

@Configuration
public class TourGuideModule {
	
	@Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}

	@Bean
	public RewardsService getRewardsService(GpsUtil gpsUtil,
											RewardCentral rewardCentral,
											ExecutorService executorService) {
		return new RewardsService(gpsUtil, rewardCentral, executorService);
	}

	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
	
}
