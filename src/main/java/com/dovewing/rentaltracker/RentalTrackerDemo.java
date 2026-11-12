package com.dovewing.rentaltracker;

/** Builds and runs the example day independently of the domain model. */
final class RentalTrackerDemo {
    private final RentalReporter reporter = new RentalReporter();

    void run() {
        Depot depot = new Depot("Dovewing Car Rentals");

        Vehicle vitz = new Vehicle("Toyota Vitz", "GT-1234-20", 150.00,
                500.00, VehicleCategory.ECONOMY);
        Vehicle prado = new Vehicle("Toyota Prado", "GR-5678-22", 600.00,
                2000.00, VehicleCategory.PREMIUM);

        depot.addVehicle(vitz);
        depot.addVehicle(prado);

        Renter kofi = new Renter("Kofi Mensah", RenterType.CASUAL);
        Renter ama = new Renter("Ama Owusu", RenterType.MEMBER);
        depot.addRenter(kofi);
        depot.addRenter(ama);

        reporter.printRental(depot.rent(kofi, vitz, 3));
        reporter.printRental(depot.rent(ama, prado, 2));
        depot.returnVehicle(prado);
        reporter.printReturn(prado);

        try {
            depot.rent(ama, vitz, 2);
        } catch (IllegalStateException exception) {
            reporter.printRentalDeclined(exception);
        }

        reporter.printDepotReport(depot);
        reporter.printRentalHistory(ama);
    }
}
