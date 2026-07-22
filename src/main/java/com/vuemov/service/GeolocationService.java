package com.vuemov.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class GeolocationService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Map<String, GeoResult> cache = new ConcurrentHashMap<>();

    public GeolocationService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public GeoResult getLocation(String ip) {
        if (ip == null || ip.isEmpty() || ip.equals("127.0.0.1") || ip.equals("::1") || ip.startsWith("192.168.") || ip.startsWith("10.")) {
            return new GeoResult("Localhost", "", "");
        }

        GeoResult cached = cache.get(ip);
        if (cached != null) {
            return cached;
        }

        try {
            String url = "http://ip-api.com/json/" + ip + "?fields=country,regionName,city,status";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode json = objectMapper.readTree(response.body());
                if ("success".equals(json.path("status").asText())) {
                    String country = json.path("country").asText("");
                    String region = json.path("regionName").asText("");
                    String city = json.path("city").asText("");

                    GeoResult result = new GeoResult(country, region, city);
                    cache.put(ip, result);
                    return result;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to geolocate IP {}: {}", ip, e.getMessage());
        }

        return new GeoResult("", "", "");
    }

    public static class GeoResult {
        private final String country;
        private final String region;
        private final String city;

        public GeoResult(String country, String region, String city) {
            this.country = country;
            this.region = region;
            this.city = city;
        }

        public String getCountry() { return country; }
        public String getRegion() { return region; }
        public String getCity() { return city; }
    }
}
