package com.lld.trainbooking.dao;

import com.lld.trainbooking.model.RouteSegment;
import com.lld.trainbooking.model.Station;
import com.lld.trainbooking.model.Train;
import lombok.AllArgsConstructor;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for RouteSegment-related database operations
 */
@AllArgsConstructor
public class RouteSegmentRepository {
    // Simulating database with in-memory storage
    private static final List<RouteSegment> routeSegments = new ArrayList<>();
    private static final TrainRepository trainRepository = new TrainRepository();
    private static final StationRepository stationRepository = new StationRepository();

    static {
        // Initialize with some sample data
        Train train1 = trainRepository.findById(1L);
        Train train2 = trainRepository.findById(2L);
        Train train3 = trainRepository.findById(3L);

        // Train 1 route: A -> B -> C -> D -> E -> M -> N -> Z
        addSegment(1L, train1, "A", 1, null, LocalTime.of(10, 0), 0);
        addSegment(2L, train1, "B", 2, LocalTime.of(10, 30), LocalTime.of(10, 35), 50);
        addSegment(3L, train1, "C", 3, LocalTime.of(11, 0), LocalTime.of(11, 5), 100);
        addSegment(4L, train1, "D", 4, LocalTime.of(11, 30), LocalTime.of(11, 35), 150);
        addSegment(5L, train1, "E", 5, LocalTime.of(12, 0), LocalTime.of(12, 5), 200);
        addSegment(6L, train1, "M", 6, LocalTime.of(12, 30), LocalTime.of(12, 35), 250);
        addSegment(7L, train1, "N", 7, LocalTime.of(13, 0), LocalTime.of(13, 5), 300);
        addSegment(8L, train1, "Z", 8, LocalTime.of(13, 30), null, 350);

        // Train 2 route: A -> C -> D -> M -> Z
        addSegment(9L, train2, "A", 1, null, LocalTime.of(11, 0), 0);
        addSegment(10L, train2, "C", 2, LocalTime.of(11, 45), LocalTime.of(11, 50), 100);
        addSegment(11L, train2, "D", 3, LocalTime.of(12, 15), LocalTime.of(12, 20), 150);
        addSegment(12L, train2, "M", 4, LocalTime.of(13, 0), LocalTime.of(13, 5), 250);
        addSegment(13L, train2, "Z", 5, LocalTime.of(14, 0), null, 350);

        // Train 3 route: A -> B -> E -> M -> Z (doesn't go through D)
        addSegment(14L, train3, "A", 1, null, LocalTime.of(9, 0), 0);
        addSegment(15L, train3, "B", 2, LocalTime.of(9, 30), LocalTime.of(9, 35), 50);
        addSegment(16L, train3, "E", 3, LocalTime.of(10, 15), LocalTime.of(10, 20), 200);
        addSegment(17L, train3, "M", 4, LocalTime.of(10, 50), LocalTime.of(10, 55), 250);
        addSegment(18L, train3, "Z", 5, LocalTime.of(11, 30), null, 350);
    }

    private static void addSegment(Long id, Train train, String stationCode, int sequenceNumber,
                                   LocalTime arrivalTime, LocalTime departureTime, int distanceFromOrigin) {
        Station station = stationRepository.findByCode(stationCode);
        routeSegments.add(RouteSegment.builder()
                .id(id)
                .train(train)
                .station(station)
                .sequenceNumber(sequenceNumber)
                .arrivalTime(arrivalTime)
                .departureTime(departureTime)
                .distanceFromOrigin(distanceFromOrigin)
                .build());
    }

    public List<RouteSegment> findByTrainId(Long trainId) {
        return routeSegments.stream()
                .filter(segment -> segment.getTrain().getId().equals(trainId))
                .sorted(Comparator.comparing(RouteSegment::getSequenceNumber))
                .collect(Collectors.toList());
    }

    public List<RouteSegment> findAll() {
        return new ArrayList<>(routeSegments);
    }

    public List<Train> findTrainsConnectingStations(String sourceStationCode, String destinationStationCode) {
        Map<Long, List<RouteSegment>> segmentsByTrain = routeSegments.stream()
                .collect(Collectors.groupingBy(segment -> segment.getTrain().getId()));

        Set<Long> validTrainIds = new HashSet<>();

        for (Map.Entry<Long, List<RouteSegment>> entry : segmentsByTrain.entrySet()) {
            List<RouteSegment> trainSegments = entry.getValue();
            trainSegments.sort(Comparator.comparing(RouteSegment::getSequenceNumber));

            boolean sourceFound = false;
            boolean destinationFoundAfterSource = false;

            for (RouteSegment segment : trainSegments) {
                if (segment.getStation().getCode().equals(sourceStationCode)) {
                    sourceFound = true;
                } else if (sourceFound && segment.getStation().getCode().equals(destinationStationCode)) {
                    destinationFoundAfterSource = true;
                    break;
                }
            }

            if (sourceFound && destinationFoundAfterSource) {
                validTrainIds.add(entry.getKey());
            }
        }

        return validTrainIds.stream()
                .map(trainId -> trainRepository.findById(trainId))
                .collect(Collectors.toList());
    }

    public RouteSegment findByTrainAndStation(Long trainId, String stationCode) {
        return routeSegments.stream()
                .filter(segment -> segment.getTrain().getId().equals(trainId) &&
                        segment.getStation().getCode().equals(stationCode))
                .findFirst()
                .orElse(null);
    }
}