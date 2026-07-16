package com.vuemov.service;

import com.vuemov.model.AnalyticsEvent;
import com.vuemov.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final MongoTemplate mongoTemplate;

    public void logEvent(AnalyticsEvent event) {
        event.setTimestamp(LocalDateTime.now());
        analyticsRepository.save(event);
    }

    public Map<String, Object> getDashboardStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime yesterdayStart = now.toLocalDate().minusDays(1).atStartOfDay();
        LocalDateTime weekStart = now.toLocalDate().minusDays(6).atStartOfDay();
        LocalDateTime monthStart = now.toLocalDate().minusDays(29).atStartOfDay();

        long todayVisits = analyticsRepository.countByEventTypeAndTimestampBetween("page_view", todayStart, now);
        long yesterdayVisits = analyticsRepository.countByEventTypeAndTimestampBetween("page_view", yesterdayStart, todayStart);
        long weekVisits = analyticsRepository.countByEventTypeAndTimestampBetween("page_view", weekStart, now);
        long monthVisits = analyticsRepository.countByEventTypeAndTimestampBetween("page_view", monthStart, now);
        long todayMovieViews = analyticsRepository.countByEventTypeAndTimestampBetween("movie_view", todayStart, now);

        Set<String> todayIps = new HashSet<>();
        analyticsRepository.findByTimestampBetween(todayStart, now)
                .forEach(e -> { if (e.getIp() != null) todayIps.add(e.getIp()); });

        long totalEvents = analyticsRepository.count();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("todayVisits", todayVisits);
        stats.put("yesterdayVisits", yesterdayVisits);
        stats.put("weekVisits", weekVisits);
        stats.put("monthVisits", monthVisits);
        stats.put("todayMovieViews", todayMovieViews);
        stats.put("todayUniqueUsers", todayIps.size());
        stats.put("totalEvents", totalEvents);
        return stats;
    }

    public List<Map<String, Object>> getDeviceStats() {
        return aggregateByField("deviceType");
    }

    public List<Map<String, Object>> getBrowserStats() {
        return aggregateByField("browser");
    }

    public List<Map<String, Object>> getOsStats() {
        return aggregateByField("os");
    }

    public List<Map<String, Object>> getTopMovies(int limit) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("eventType").is("movie_view")),
                Aggregation.group("movieSlug").count().as("views"),
                Aggregation.sort(Sort.Direction.DESC, "views"),
                Aggregation.limit(limit)
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "analytics_events", Map.class);
        List<Map<String, Object>> output = new ArrayList<>();
        for (Map doc : results.getMappedResults()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("slug", doc.get("_id"));
            item.put("views", doc.get("views"));
            output.add(item);
        }
        return output;
    }

    public List<Map<String, Object>> getRecentActivity(int limit) {
        List<AnalyticsEvent> events = analyticsRepository.findByEventTypeOrderByTimestampDesc(
                "movie_view", PageRequest.of(0, limit));

        if (events.isEmpty()) {
            events = analyticsRepository.findTopByOrderByTimestampDesc(PageRequest.of(0, limit));
        }

        List<Map<String, Object>> activity = new ArrayList<>();
        for (AnalyticsEvent event : events) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("eventType", event.getEventType());
            item.put("path", event.getPath());
            item.put("movieSlug", event.getMovieSlug());
            item.put("deviceType", event.getDeviceType());
            item.put("browser", event.getBrowser());
            item.put("os", event.getOs());
            item.put("timestamp", event.getTimestamp());
            activity.add(item);
        }
        return activity;
    }

    public List<Map<String, Object>> getVisitTrend(int days) {
        LocalDateTime now = LocalDateTime.now();
        List<Map<String, Object>> trend = new ArrayList<>();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = now.toLocalDate().minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

            long visits = analyticsRepository.countByEventTypeAndTimestampBetween("page_view", dayStart, dayEnd);
            long movieViews = analyticsRepository.countByEventTypeAndTimestampBetween("movie_view", dayStart, dayEnd);

            Map<String, Object> dayData = new LinkedHashMap<>();
            dayData.put("date", date.toString());
            dayData.put("label", date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, new Locale("vi", "VN")));
            dayData.put("visits", visits);
            dayData.put("movieViews", movieViews);
            trend.add(dayData);
        }
        return trend;
    }

    private List<Map<String, Object>> aggregateByField(String field) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group(field).count().as("count"),
                Aggregation.sort(Sort.Direction.DESC, "count")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "analytics_events", Map.class);
        List<Map<String, Object>> output = new ArrayList<>();
        for (Map doc : results.getMappedResults()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", doc.get("_id") != null ? doc.get("_id") : "Unknown");
            item.put("count", doc.get("count"));
            output.add(item);
        }
        return output;
    }
}
