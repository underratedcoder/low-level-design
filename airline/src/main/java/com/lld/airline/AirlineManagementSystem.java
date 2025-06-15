package com.lld.airline;


import com.lld.airline.controller.BookingController;
import com.lld.airline.enums.PaymentMethodType;
import com.lld.airline.model.Booking;
import com.lld.airline.model.User;

public class AirlineManagementSystem {
    public static void main(String[] args) {
        BookingController controller = new BookingController();
        
        User user = new User();
        user.setUserId(1L);
        user.setName("John Doe");

        Booking booking = new Booking();
        booking.setBookingId(1001L);
        booking.setUserId(1L);
        booking.setNoOfTravelers(2);

        // Create Booking
        //Booking newBooking = controller.createBooking(user, booking);
        //System.out.println("Booking Confirmed: " + newBooking);

        // Process Payment
       // boolean paymentStatus = controller.processPayment(PaymentMethodType.CARD, 500.0);
        //System.out.println("Payment Successful: " + paymentStatus);
    }
}
