package com.openclassrooms.tourguide.DTO;

import gpsUtil.location.Attraction;

public class AttractionDTO {
    private Attraction attraction;
    private double distance;
    private double rewardsPoint;

    public AttractionDTO() {
    }

    public AttractionDTO(Attraction attraction, double distance, double rewardsPoint) {
        this.attraction = attraction;
        this.distance = distance;
        this.rewardsPoint = rewardsPoint;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getRewardsPoint() {
        return rewardsPoint;
    }

    public void setRewardsPoint(double rewardsPoint) {
        this.rewardsPoint = rewardsPoint;
    }
}
