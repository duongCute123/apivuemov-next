package com.vuemov.repository;

import com.vuemov.model.AnalyticsEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsRepository extends MongoRepository<AnalyticsEvent, String> {

    long countByTimestampBetween(LocalDateTime start, LocalDateTime end);

    long countByEventTypeAndTimestampBetween(String eventType, LocalDateTime start, LocalDateTime end);

    @Query(value = "{ 'timestamp': { $gte: ?0, $lte: ?1 } }", fields = "{ 'ip': 1 }")
    List<AnalyticsEvent> findDistinctIpsBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "{ 'timestamp': { $gte: ?0, $lte: ?1 } }", fields = "{ 'deviceType': 1 }")
    List<AnalyticsEvent> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "{ 'eventType': 'movie_view', 'timestamp': { $gte: ?0, $lte: ?1 } }", fields = "{ 'movieSlug': 1 }")
    List<AnalyticsEvent> findMovieViewsBetween(LocalDateTime start, LocalDateTime end);

    List<AnalyticsEvent> findTopByOrderByTimestampDesc(org.springframework.data.domain.Pageable pageable);

    @Query(value = "{ 'eventType': ?0 }", sort = "{ 'timestamp': -1 }")
    List<AnalyticsEvent> findByEventTypeOrderByTimestampDesc(String eventType, org.springframework.data.domain.Pageable pageable);
}
