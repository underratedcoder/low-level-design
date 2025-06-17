package com.lld.parking.quick;

import com.lld.parking.quick.enums.PaymentMethod;
import com.lld.parking.quick.enums.VehicleType;
import com.lld.parking.quick.model.*;
import com.lld.parking.quick.service.ParkingService;

import java.util.*;

public class ParkingLotApp {
    public static void main(String[] args) throws InterruptedException {
        // Setup parking lot with 2 floors, each with 2 CAR, 2 BIKE, 1 TRUCK spots
        List<Floor> floors = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Map<VehicleType, List<ParkingSpot>> map = new HashMap<>();
            map.put(VehicleType.CAR, Arrays.asList(new ParkingSpot(1, VehicleType.CAR, false, null), new ParkingSpot(2, VehicleType.CAR, false, null)));
            map.put(VehicleType.BIKE, Arrays.asList(new ParkingSpot(3, VehicleType.BIKE, false, null), new ParkingSpot(4, VehicleType.BIKE, false, null)));
            map.put(VehicleType.TRUCK, List.of(new ParkingSpot(5, VehicleType.TRUCK, false, null)));
            floors.add(new Floor(i, map));
        }

        ParkingService service = new ParkingService(floors);

        EntryGate entryGate = new EntryGate(1);
        ExitGate exitGate = new ExitGate(1);

        Vehicle v1 = new Vehicle("KA01AB1234", VehicleType.CAR);
        ParkingTicket ticket = service.parkVehicle(v1, entryGate);
        System.out.println("Ticket issued: " + ticket);

        // Simulate delay
        Thread.sleep(2000); // 2 seconds

        PaymentReceipt receipt = service.exitVehicle(ticket.getTicketId(), exitGate, PaymentMethod.UPI);
        System.out.println("Payment Receipt: " + receipt);
    }
}
