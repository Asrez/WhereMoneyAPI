package com.asrez.wheremoney.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "session")
public class SessionConfig {

    private int maxLoginAttempts;
    private long banForInHours;

    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }

    public void setMaxLoginAttempts(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }

    public long getBanForInHours() {
        return banForInHours;
    }

    public void setBanForInHours(long banForInHours) {
        this.banForInHours = banForInHours;
    }
}
