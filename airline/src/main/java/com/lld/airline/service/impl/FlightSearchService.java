//package com.lld.airline.service.impl;
//
//import com.lld.airline.model.FlightModel;
//
//import java.util.List;
//
//public class FlightSearchService {
//    private final FlightRepository flightRepository;
//
//    public FlightSearchService(FlightRepository flightRepository) {
//        this.flightRepository = flightRepository;
//    }
//
//    public List<FlightModel> searchFlights(String source, String destination, LocalDate travelDate, int passengerCount) {
//        // 1️⃣ Fetch Direct Flights
//        List<FlightModel> directFlights = flightRepository.findDirectFlights(source, destination, travelDate, passengerCount);
//
//        // 2️⃣ Fetch Connecting Flights (One Stop)
//        List<FlightModel> connectingFlights = flightRepository.findConnectingFlights(source, destination, travelDate, passengerCount);
//
//        // 3️⃣ Combine results and return
//        List<FlightModel> allFlights = new ArrayList<>();
//        allFlights.addAll(directFlights);
//        allFlights.addAll(connectingFlights);
//
//        // Sort by price (Cheapest first)
//        allFlights.sort(Comparator.comparing(FlightModel::getPrice));
//
//        return allFlights;
//    }
//}
