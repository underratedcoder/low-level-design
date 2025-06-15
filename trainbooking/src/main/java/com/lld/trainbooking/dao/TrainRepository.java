package com.lld.trainbooking.dao;

import com.lld.trainbooking.model.Station;
import com.lld.trainbooking.model.Train;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for Train-related database operations
 */
@AllArgsConstructor
public class TrainRepository {
    // Simulating database with in-memory storage
    private static final Map<Long, Train> trainsById = new HashMap<>();
    private static final StationRepository stationRepository = new StationRepository();

    static {
        // Initialize with some sample data
        Station stationA = stationRepository.findByCode("A");
        Station stationZ = stationRepository.findByCode("Z");

        trainsById.put(1L, Train.builder()
                .id(1L)
                .trainNumber("12345")
                .name("Express Train 1")
                .sourceStation(stationA)
                .destinationStation(stationZ)
                .active(true)
                .build());

        trainsById.put(2L, Train.builder()
                .id(2L)
                .trainNumber("67890")
                .name("Superfast Train 2")
                .sourceStation(stationA)
                .destinationStation(stationZ)
                .active(true)
                .build());

        trainsById.put(3L, Train.builder()
                .id(3L)
                .trainNumber("11121")
                .name("Local Train 3")
                .sourceStation(stationA)
                .destinationStation(stationZ)
                .active(true)
                .build());
    }

    public Train findById(Long id) {
        return trainsById.get(id);
    }

    public List<Train> findAll() {
        return new ArrayList<>(trainsById.values());
    }
}