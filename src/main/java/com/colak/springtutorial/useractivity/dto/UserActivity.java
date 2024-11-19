package com.colak.springtutorial.useractivity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserActivity {
    private String userId;
    private String activityType;
    private String activityTime;
    private String ipAddress;
    private String deviceType;
    private String location;
    private String browserType;
    private String operatingSystem;
}

