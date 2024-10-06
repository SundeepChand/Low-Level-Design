package org.sundeep.bookmyshow;

import org.sundeep.bookmyshow.models.*;
import org.sundeep.bookmyshow.services.BookingService;
import org.sundeep.bookmyshow.services.ShowService;
import org.sundeep.bookmyshow.services.VenueService;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ShowService showService = new ShowService();
        BookingService bookingService = new BookingService();
        VenueService venueService = new VenueService();
        initialiseShows(showService, venueService);

        List<Venue> availableVenues = venueService.getVenuesForShowInCity("Rockstar Movie", City.BENGALURU);
        List<Seat> pickedSeats = availableVenues.getFirst().getSlots().getFirst().getAvailableSeats().subList(0, 2);

        List<Seat> seatsToBook = new LinkedList<>();
        for (Seat seat: pickedSeats) {
            seatsToBook.add(new Seat(seat.getId(), seat.getPrice(), seat.getCategory()));
        }

        Booking booking1 = bookingService.createBooking(
                availableVenues.getFirst().getSlots().getFirst(),
                seatsToBook);
        System.out.println("Initiated booking 1: " + booking1.toString());

        booking1.getPayment().setPaymentStatus(PaymentStatus.SUCCESS);
        Booking booking1AfterPayment = bookingService.updateBookingStatusAfterPayment(booking1.getId(), booking1.getPayment());
        System.out.println("Booking 1 after payment: " + booking1AfterPayment.toString());
    }

    private static void initialiseShows(ShowService showService, VenueService venueService) {
        Show rockstarMovie = new Show(1, "Rockstar Movie", Duration.ofHours(2));
        showService.addShow(City.BENGALURU, rockstarMovie);
        List<Seat> seatConfig1 = new LinkedList<>(Arrays.asList(
                new Seat(1, 200, SeatCategory.STANDARD),
                new Seat(2, 200, SeatCategory.STANDARD),
                new Seat(3, 200, SeatCategory.STANDARD),
                new Seat(4, 350, SeatCategory.PREMIUM),
                new Seat(5, 350, SeatCategory.PREMIUM),
                new Seat(6, 350, SeatCategory.PREMIUM)
        ));
        Hall h1 = new Hall(1, seatConfig1);
        Hall h2 = new Hall(2, seatConfig1);

        Slot s1 = new Slot(1, rockstarMovie, h1, ZonedDateTime.now());

        venueService.addVenue(
            new Venue(
                1,
                "Indiranagar",
                City.BENGALURU,
                new LinkedList<>(Arrays.asList(h1, h2)),
                List.of(s1)
            )
        );
    }
}
