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
            return new GeoResult("Localhost", "", "", 0, 0, "");
        }

        GeoResult cached = cache.get(ip);
        if (cached != null) {
            return cached;
        }

        try {
            String url = "http://ip-api.com/json/" + ip + "?fields=country,regionName,city,lat,lon,status";
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
                    double lat = json.path("lat").asDouble(0);
                    double lon = json.path("lon").asDouble(0);

                    String address = reverseGeocode(lat, lon);

                    GeoResult result = new GeoResult(country, region, city, lat, lon, address);
                    cache.put(ip, result);
                    return result;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to geolocate IP {}: {}", ip, e.getMessage());
        }

        return new GeoResult("", "", "", 0, 0, "");
    }

    private String reverseGeocode(double lat, double lon) {
        if (lat == 0 && lon == 0) {
            return "";
        }

        try {
            String url = String.format(
                    "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&accept-language=vi",
                    lat, lon
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(3))
                    .header("User-Agent", "VueMov-Backend/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode json = objectMapper.readTree(response.body());
                JsonNode address = json.path("address");

                String ward = address.path("suburb").asText("");
                if (ward.isEmpty()) ward = address.path("township").asText("");
                if (ward.isEmpty()) ward = address.path("village").asText("");

                String district = address.path("county").asText("");
                if (district.isEmpty()) district = address.path("district").asText("");

                String province = address.path("state").asText("");

                StringBuilder sb = new StringBuilder();
                if (!ward.isEmpty()) sb.append(ward);
                if (!district.isEmpty()) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(district);
                }
                if (!province.isEmpty()) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(province);
                }

                return sb.toString();
            }
        } catch (Exception e) {
            log.warn("Failed to reverse geocode ({}, {}): {}", lat, lon, e.getMessage());
        }

        return "";
    }

    public GeoResult getLocationFromCoords(double lat, double lon) {
        if (lat == 0 && lon == 0) {
            return new GeoResult("", "", "", 0, 0, "");
        }

        String address = reverseGeocode(lat, lon);
        String country = "";
        String region = "";
        String city = "";

        if (!address.isEmpty()) {
            String[] parts = address.split(", ");
            if (parts.length >= 3) {
                city = parts[parts.length - 3].trim();
                region = parts[parts.length - 2].trim();
                country = parts[parts.length - 1].trim();
            } else if (parts.length == 2) {
                region = parts[0].trim();
                country = parts[1].trim();
            } else if (parts.length == 1) {
                country = parts[0].trim();
            }
        }

        return new GeoResult(country, region, city, lat, lon, address);
    }

    public static class GeoResult {
        private final String country;
        private final String region;
        private final String city;
        private final double latitude;
        private final double longitude;
        private final String address;

        public GeoResult(String country, String region, String city, double latitude, double longitude, String address) {
            this.country = country;
            this.region = region;
            this.city = city;
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
        }

        public String getCountry() { return country; }
        public String getRegion() { return region; }
        public String getCity() { return city; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public String getAddress() { return address; }
    }
}
