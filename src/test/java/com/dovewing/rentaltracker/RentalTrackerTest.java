package com.dovewing.rentaltracker;

/** Small tests that can be run without downloading a testing library. */
public class RentalTrackerTest {

    public static void main(String[] args) {
        testVehicleCannotBeRentedTwice();
        testMemberDepositIsWaivedForCars();
        testVanDepositIsChargedForEveryRenter();
        testReturnedRentalStaysInHistory();
        testInvalidRentalDaysAreRejected();
        testInvalidConstructionInputsAreRejected();
        testRentalRejectsUnavailableVehicle();
        System.out.println("All simple tests passed.");
    }

    private static void testVehicleCannotBeRentedTwice() {
        Depot depot = new Depot("Test Depot");
        Vehicle car = new Vehicle("Toyota Vitz", "AA-1111-22", 150, 500,
                VehicleCategory.ECONOMY);
        Renter first = new Renter("First Customer", RenterType.CASUAL);
        Renter second = new Renter("Second Customer", RenterType.MEMBER);
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
        check(car.getStatus() == VehicleStatus.RENTED, "The vehicle should remain rented.");
        check(first.getHistory().size() == 1, "Only the first renter should have a rental record.");
        assertAvailabilityInvariant(depot);
    }

    private static void testMemberDepositIsWaivedForCars() {
        Depot depot = new Depot("Test Depot");
        Vehicle car = new Vehicle("Toyota Vitz", "AA-2222-22", 150, 500,
                VehicleCategory.ECONOMY);
        Renter member = new Renter("Member Customer", RenterType.MEMBER);
        Renter casual = new Renter("Casual Customer", RenterType.CASUAL);
        depot.addVehicle(car);
        depot.addRenter(member);
        depot.addRenter(casual);

        Rental memberRental = depot.rent(member, car, 2);
        check(memberRental.getDeposit() == 0.0, "A member should not pay a car deposit.");
        depot.returnVehicle(car);
        Rental casualRental = depot.rent(casual, car, 1);
        check(casualRental.getDeposit() == 500.0, "A casual renter should pay the listed car deposit.");
    }

    private static void testVanDepositIsChargedForEveryRenter() {
        Depot depot = new Depot("Test Depot");
        CommercialVan van = new CommercialVan("Delivery Van", "AA-4444-22", 300, 1000);
        Renter member = new Renter("Member Customer", RenterType.MEMBER);
        depot.addVehicle(van);
        depot.addRenter(member);

        Rental rental = depot.rent(member, van, 1);
        check(rental.getDeposit() == 1000.0, "A member should pay the configured van deposit.");
        check(van.getStatus() == VehicleStatus.RENTED, "The van should be rented.");
        assertAvailabilityInvariant(depot);
    }

    private static void testReturnedRentalStaysInHistory() {
        Depot depot = new Depot("Test Depot");
        Vehicle car = new Vehicle("Toyota Vitz", "AA-3333-22", 150, 500,
                VehicleCategory.ECONOMY);
        Renter renter = new Renter("Test Customer", RenterType.CASUAL);
        depot.addVehicle(car);
        depot.addRenter(renter);

        Rental rental = depot.rent(renter, car, 1);
        depot.returnVehicle(car);

        check(renter.getHistory().size() == 1, "Rental history should keep returned rentals.");
        check(rental.getStatus() == RentalStatus.RETURNED, "The rental should be returned.");
        check(car.getStatus() == VehicleStatus.AVAILABLE, "Returned vehicle should be available.");
        assertAvailabilityInvariant(depot);
    }

    private static void testInvalidRentalDaysAreRejected() {
        Depot depot = new Depot("Test Depot");
        Vehicle car = new Vehicle("Toyota Vitz", "AA-5555-22", 150, 500,
                VehicleCategory.ECONOMY);
        Renter renter = new Renter("Test Customer", RenterType.CASUAL);
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
        check(car.getStatus() == VehicleStatus.AVAILABLE, "Invalid rental should not change vehicle state.");
        assertAvailabilityInvariant(depot);
    }

    private static void testInvalidConstructionInputsAreRejected() {
        expectIllegalArgument(() -> new Vehicle("Car", "AA-0000-00", 10, 20, null),
                "A missing vehicle category should be rejected.");
        expectIllegalArgument(() -> new Vehicle("Car", "AA-0000-01", Double.NaN, 20,
                VehicleCategory.ECONOMY), "A non-finite rate should be rejected.");
        expectIllegalArgument(() -> new Renter("Customer", null),
                "A missing renter type should be rejected.");
        expectIllegalArgument(() -> new Rental(
                new Renter("Customer", RenterType.CASUAL),
                new Vehicle("Car", "AA-0000-02", 10, 20, VehicleCategory.ECONOMY), 0),
                "A rental with invalid days should be rejected.");
    }

    private static void testRentalRejectsUnavailableVehicle() {
        Vehicle car = new Vehicle("Toyota Vitz", "AA-6666-22", 150, 500,
                VehicleCategory.ECONOMY);
        Renter renter = new Renter("Test Customer", RenterType.CASUAL);
        car.markRented();

        boolean rentalWasRejected = false;
        try {
            new Rental(renter, car, 1);
        } catch (IllegalStateException exception) {
            rentalWasRejected = true;
        }

        check(rentalWasRejected, "A rental record must not be created for an unavailable vehicle.");
        check(car.getStatus() == VehicleStatus.RENTED, "Rejected rental must not change vehicle state.");
    }

    private static void assertAvailabilityInvariant(Depot depot) {
        for (Vehicle vehicle : depot.getVehicles()) {
            long activeRentals = depot.getRentals().stream()
                    .filter(rental -> rental.getVehicle() == vehicle)
                    .filter(rental -> rental.getStatus() == RentalStatus.ACTIVE)
                    .count();
            check((vehicle.getStatus() == VehicleStatus.RENTED) == (activeRentals == 1),
                    "A vehicle must be rented exactly when it has one active rental.");
        }
    }

    private static void expectIllegalArgument(Runnable action, String message) {
        boolean rejected = false;
        try {
            action.run();
        } catch (IllegalArgumentException exception) {
            rejected = true;
        }
        check(rejected, message);
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
