package com.vuemov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "analytics_events")
@CompoundIndex(name = "type_timestamp_idx", def = "{'eventType': 1, 'timestamp': -1}")
public class AnalyticsEvent {
    @Id
    private String id;

    @Indexed
    private String eventType;

    private LocalDateTime timestamp;

    private String ip;

    private String userAgent;

    private String deviceType;

    private String browser;

    private String os;

    private int screenWidth;

    private int screenHeight;

    private String path;

    private String movieSlug;

    private String userId;

    private String referrer;

    private String country;

    private String city;

    private String region;
}
