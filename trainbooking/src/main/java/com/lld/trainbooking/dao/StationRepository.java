package com.lld.trainbooking.dao;// Repositories

import com.lld.trainbooking.enums.ClassType;
import com.lld.trainbooking.model.RouteSegment;
import com.lld.trainbooking.model.Station;
import com.lld.trainbooking.model.Train;
import com.lld.trainbooking.model.TravelClass;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repository for Station-related database operations
 */
@AllArgsConstructor
public class StationRepository {
    // Simulating database with in-memory storage
    private static final Map<String, Station> stationsByCode = new HashMap<>();

    static {
        // Initialize with some sample data
        stationsByCode.put("A", Station.builder().id(1L).code("A").name("Station A").city("City A").build());
        stationsByCode.put("B", Station.builder().id(2L).code("B").name("Station B").city("City B").build());
        stationsByCode.put("C", Station.builder().id(3L).code("C").name("Station C").city("City C").build());
        stationsByCode.put("D", Station.builder().id(4L).code("D").name("Station D").city("City D").build());
        stationsByCode.put("E", Station.builder().id(5L).code("E").name("Station E").city("City E").build());
        stationsByCode.put("M", Station.builder().id(6L).code("M").name("Station M").city("City M").build());
        stationsByCode.put("N", Station.builder().id(7L).code("N").name("Station N").city("City N").build());
        stationsByCode.put("Z", Station.builder().id(8L).code("Z").name("Station Z").city("City Z").build());
    }

    public Station findByCode(String code) {
        return stationsByCode.get(code);
    }

    public List<Station> findAll() {
        return new ArrayList<>(stationsByCode.values());
    }
}
