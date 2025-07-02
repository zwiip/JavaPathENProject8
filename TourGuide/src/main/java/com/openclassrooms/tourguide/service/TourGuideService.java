package com.openclassrooms.tourguide.service;

import com.openclassrooms.tourguide.DTO.AttractionDTO;
import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.tracker.Tracker;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {
	private final Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;
	private final ExecutorService executorService;

	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService, @Qualifier("executorServiceBean") ExecutorService executorService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;
		this.executorService = executorService;
		
		Locale.setDefault(Locale.US);

		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(User user) {
		return (user.getVisitedLocations().isEmpty()) ? user.getLastVisitedLocation()
				: trackUserLocation(user).join();
	}

	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	public User getUserByUIID(UUID userId) {
		List<User> users = getAllUsers();
		for (User user : users) {
			if (user.getUserId().equals(userId)) {
				return user;
			}
		}
		return null;
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	/**
	 * Retrieves the user's current location and adds it to his locations history.
	 * Triggers the reward calculation process.
	 * @param user the current user whose location is being tracked.
	 * @return a CompletableFuture object that will fill with the user location's history
	 */
	public CompletableFuture<VisitedLocation> trackUserLocation(User user) {
		return CompletableFuture
			.supplyAsync(() -> gpsUtil.getUserLocation(user.getUserId()), executorService)
			.thenApplyAsync(visitedLocation -> {
				user.addToVisitedLocations(visitedLocation);
				rewardsService.calculateRewards(user);
				return visitedLocation;
			}, executorService);
	}

	/**
	 * Fetch the attractions and the User rewardsPoints,
	 * Calculate the distance between user and attractions,
	 * Sort by distance in miles and return the five closest.
	 * @param visitedLocation the user location
	 * @return a list of 5 AttractionDTO objects
	 * 			(attraction's name, latitude and longitude, and the user's latitude, longitude and rewardPoints for this attraction.)
	 */
	public List<AttractionDTO> getNearByAttractions(VisitedLocation visitedLocation) {
		List<AttractionDTO> nearbyAttractions = new ArrayList<>();
		List<Attraction> attractions = gpsUtil.getAttractions();
		User user = getUserByUIID(visitedLocation.userId);
		rewardsService.calculateRewards(user);

		for (Attraction attraction : attractions) {
			double rewardPoints = 0;

			for (UserReward reward : user.getUserRewards()) {
				if (reward.attraction.attractionName.equals(attraction.attractionName)) {
					rewardPoints = reward.getRewardPoints();
					break;
				}
			}

			AttractionDTO attractionDTO = new AttractionDTO(
					attraction.attractionName,
					attraction.latitude,
					attraction.longitude,
					visitedLocation.location.latitude,
					visitedLocation.location.longitude,
					rewardsService.getDistance(attraction, visitedLocation.location),
					rewardPoints
			);
			nearbyAttractions.add(attractionDTO);
		}

		return nearbyAttractions.stream()
				.sorted(Comparator.comparingDouble(AttractionDTO::getDistance))
				.limit(5)
				.toList();
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				tracker.stopTracking();
			}
		});
	}

	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();

	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

}
