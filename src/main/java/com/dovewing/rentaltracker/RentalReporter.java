package com.dovewing.rentaltracker;

/** Formats rental and depot results for the console. */
final class RentalReporter {
    void printRental(Rental rental) {
        System.out.printf("Rented %s (%s) to %s for %d days, cost GHS %.2f, deposit GHS %.2f%n",
                rental.getVehicle().getModel(), rental.getVehicle().getRegistration(),
                rental.getRenter().getName(), rental.getDays(),
                rental.getCost(), rental.getDeposit());
    }

    void printReturn(Vehicle vehicle) {
        System.out.println("Returned " + vehicle.getModel() + " (" + vehicle.getRegistration() + ")");
    }

    void printRentalDeclined(IllegalStateException exception) {
        System.out.println("Rental declined: " + exception.getMessage());
    }

    void printDepotReport(Depot depot) {
        System.out.println();
        System.out.println("=== " + depot.getName() + " - end of day ===");
        System.out.println("Fleet status:");
        for (Vehicle vehicle : depot.getVehicles()) {
            System.out.println("  " + vehicle.getModel() + " (" + vehicle.getRegistration()
                    + ", " + vehicle.getCategory() + "): " + vehicle.getStatus());
        }

        System.out.println("Active rentals:");
        for (Rental rental : depot.getRentals()) {
            if (rental.getStatus() == RentalStatus.ACTIVE) {
                System.out.println("  Rental #" + rental.getId() + ": "
                        + rental.getVehicle().getRegistration() + " -> "
                        + rental.getRenter().getName() + " ("
                        + rental.getDays() + " days)");
            }
        }
    }

    void printRentalHistory(Renter renter) {
        System.out.println("Rental history for " + renter.getName() + ":");
        for (Rental rental : renter.getHistory()) {
            System.out.println("  " + rental.getVehicle().getModel() + " - "
                    + rental.getStatus());
        }
    }
}
