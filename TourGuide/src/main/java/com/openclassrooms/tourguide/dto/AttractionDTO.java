package com.openclassrooms.tourguide.dto;

public class AttractionDTO {
    private String attractionName;
    private double attractionLatitude;
    private double attractionLongitude;
    private double userLatitude;
    private double userLongitude;
    private double distanceInMiles;
    private double rewardsPoint;

    public AttractionDTO() { }

    public AttractionDTO(String attractionName, double attractionLatitude, double attractionLongitude, double userLatitude, double userLongitude, double distanceInMiles, double rewardsPoint) {
        this.attractionName = attractionName;
        this.attractionLatitude = attractionLatitude;
        this.attractionLongitude = attractionLongitude;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
        this.distanceInMiles = distanceInMiles;
        this.rewardsPoint = rewardsPoint;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public double getAttractionLatitude() {
        return attractionLatitude;
    }

    public void setAttractionLatitude(double attractionLatitude) {
        this.attractionLatitude = attractionLatitude;
    }

    public double getAttractionLongitude() {
        return attractionLongitude;
    }

    public void setAttractionLongitude(double attractionLongitude) {
        this.attractionLongitude = attractionLongitude;
    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public double getDistance() {
        return distanceInMiles;
    }

    public void setDistance(double distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
    }

    public double getRewardsPoint() {
        return rewardsPoint;
    }

    public void setRewardsPoint(double rewardsPoint) {
        this.rewardsPoint = rewardsPoint;
    }
}
