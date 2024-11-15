package com.colak.springtutorial.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserActivity {
    private String userId;
    private String activityType;
    private String activityTime;
    private String ipAddress;
    private String deviceType;
    private String location;
    private String browserType;
    private String operatingSystem;

    // Constructor
    public UserActivity(String userId, String activityType, String activityTime, String ipAddress, String deviceType, String location, String browserType, String operatingSystem) {
        this.userId = userId;
        this.activityType = activityType;
        this.activityTime = activityTime;
        this.ipAddress = ipAddress;
        this.deviceType = deviceType;
        this.location = location;
        this.browserType = browserType;
        this.operatingSystem = operatingSystem;
    }
}

