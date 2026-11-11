package com.dovewing.rentaltracker;

/** Small tests that can be run without downloading a testing library. */
public class RentalTrackerTest {

    public static void main(String[] args) {
        testVehicleCannotBeRentedTwice();
        testMemberDepositIsWaived();
        testReturnedRentalStaysInHistory();
        testInvalidRentalDaysAreRejected();
        testVanWaitsForDepositRules();
        System.out.println("All simple tests passed.");
    }

    private static void testVehicleCannotBeRentedTwice() {
        Depot depot = new Depot("Test Depot");
        Vehicle car = new Vehicle("Toyota Vitz", "AA-1111-22", 150, 500, Vehicle.ECONOMY);
        Renter first = new Renter("First Customer", Renter.CASUAL);
        Renter second = new Renter("Second Customer", Renter.MEMBER);
        depot.addVehicle(car);
        depot.addRenter(first);
        depot.addRenter(second);

        depot.rent(first, car, 2);
        boolean bookingWasRejected = false;
        try {
            depot.rent(second, car, 2);
        } catch (IllegalStateException exception) {
            bookingWasRejected = true;
        }

        check(bookingWasRejected, "A second booking should be rejected.");
        check(depot.getRentals().size() == 1, "Only one rental should be recorded.");
    }

    private static void testMemberDepositIsWaived() {
        Depot depot = new Depot("Test Depot");
        Vehicle car = new Vehicle("Toyota Vitz", "AA-2222-22", 150, 500, Vehicle.ECONOMY);
        Renter member = new Renter("Member Customer", Renter.MEMBER);
        depot.addVehicle(car);
        depot.addRenter(member);

        Rental rental = depot.rent(member, car, 2);
        check(rental.getDeposit() == 0.0, "A member should not pay a deposit for a car.");
    }

    private static void testReturnedRentalStaysInHistory() {
        Depot depot = new Depot("Test Depot");
        Vehicle car = new Vehicle("Toyota Vitz", "AA-3333-22", 150, 500, Vehicle.ECONOMY);
        Renter renter = new Renter("Test Customer", Renter.CASUAL);
        depot.addVehicle(car);
        depot.addRenter(renter);

        depot.rent(renter, car, 1);
        depot.returnVehicle(car);

        check(renter.getHistory().size() == 1, "Rental history should keep returned rentals.");
        check(Vehicle.AVAILABLE.equals(car.getStatus()), "Returned vehicle should be available.");
    }


    private static void testInvalidRentalDaysAreRejected() {
        Depot depot = new Depot("Test Depot");
        Vehicle car = new Vehicle("Toyota Vitz", "AA-5555-22", 150, 500, Vehicle.ECONOMY);
        Renter renter = new Renter("Test Customer", Renter.CASUAL);
        depot.addVehicle(car);
        depot.addRenter(renter);

        boolean bookingWasRejected = false;
        try {
            depot.rent(renter, car, 0);
        } catch (IllegalArgumentException exception) {
            bookingWasRejected = true;
        }

        check(bookingWasRejected, "A rental with zero days should be rejected.");
        check(depot.getRentals().isEmpty(), "Invalid rental should not be saved.");
    }

    private static void testVanWaitsForDepositRules() {
        Depot depot = new Depot("Test Depot");
        CommercialVan van = new CommercialVan("Delivery Van", "AA-4444-22", 300, 1000);
        Renter renter = new Renter("Test Customer", Renter.CASUAL);
        depot.addVehicle(van);
        depot.addRenter(renter);

        boolean bookingWasBlocked = false;
        try {
            depot.rent(renter, van, 1);
        } catch (IllegalStateException exception) {
            bookingWasBlocked = exception.getMessage().contains("not been confirmed");
        }

        check(bookingWasBlocked, "Van booking should wait for the agreed deposit rules.");
        check(Vehicle.AVAILABLE.equals(van.getStatus()), "Blocked van should remain available.");
        check(depot.getRentals().isEmpty(), "Blocked van should not create a rental record.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
