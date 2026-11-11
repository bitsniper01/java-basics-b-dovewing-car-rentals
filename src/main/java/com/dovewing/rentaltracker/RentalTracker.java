package com.dovewing.rentaltracker;

/** Starts the Dovewing Car Rentals example program. */
public class RentalTracker {

    public static void main(String[] args) {
        Depot depot = new Depot("Dovewing Car Rentals");

        Vehicle vitz = new Vehicle("Toyota Vitz", "GT-1234-20", 150.00,
                500.00, Vehicle.ECONOMY);
        Vehicle prado = new Vehicle("Toyota Prado", "GR-5678-22", 600.00,
                2000.00, Vehicle.PREMIUM);

        depot.addVehicle(vitz);
        depot.addVehicle(prado);

        Renter kofi = new Renter("Kofi Mensah", Renter.CASUAL);
        Renter ama = new Renter("Ama Owusu", Renter.MEMBER);
        depot.addRenter(kofi);
        depot.addRenter(ama);

        // A normal day: two rentals, followed by the return of the Prado.
        printRental(depot.rent(kofi, vitz, 3));
        printRental(depot.rent(ama, prado, 2));
        depot.returnVehicle(prado);

        // This booking is rejected because Kofi still has the Vitz.
        try {
            depot.rent(ama, vitz, 2);
        } catch (IllegalStateException exception) {
            System.out.println("Rental declined: " + exception.getMessage());
        }

        depot.printReport();

        // Extra feature: show Ama's rental history, including returned rentals.
        System.out.println("Rental history for " + ama.getName() + ":");
        for (Rental rental : ama.getHistory()) {
            System.out.println("  " + rental.getVehicle().getModel() + " - "
                    + rental.getStatus());
        }
    }

    private static void printRental(Rental rental) {
        System.out.printf("Rented %s (%s) to %s for %d days, cost GHS %.2f, deposit GHS %.2f%n",
                rental.getVehicle().getModel(), rental.getVehicle().getRegistration(),
                rental.getRenter().getName(), rental.getDays(),
                rental.getCost(), rental.getDeposit());
    }
}
