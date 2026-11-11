package com.dovewing.rentaltracker;

import java.util.ArrayList;
import java.util.List;

/** Manages the vehicles, renters and rentals for one depot. */
public class Depot {
    private String name;
    private List<Vehicle> vehicles;
    private List<Renter> renters;
    private List<Rental> rentals;

    public Depot(String name) {
        this.name = name;
        this.vehicles = new ArrayList<>();
        this.renters = new ArrayList<>();
        this.rentals = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        for (Vehicle existingVehicle : vehicles) {
            if (existingVehicle.getRegistration().equalsIgnoreCase(vehicle.getRegistration())) {
                throw new IllegalArgumentException("That registration is already in the fleet.");
            }
        }
        vehicles.add(vehicle);
    }

    public void addRenter(Renter renter) {
        renters.add(renter);
    }

    public Rental rent(Renter renter, Vehicle vehicle, int days) {
        if (renter == null || vehicle == null) {
            throw new IllegalArgumentException("A renter and a vehicle are required.");
        }
        if (!renters.contains(renter) || !vehicles.contains(vehicle)) {
            throw new IllegalArgumentException("Add the renter and vehicle to the depot first.");
        }
        if (days <= 0) {
            throw new IllegalArgumentException("Rental days must be greater than zero.");
        }
        if (!Vehicle.AVAILABLE.equals(vehicle.getStatus())) {
            throw new IllegalStateException("This vehicle is already rented.");
        }

        // Work out the price before changing the vehicle's status.
        double cost = vehicle.getDailyRate() * days;
        double deposit = vehicle.calculateDeposit(renter);

        Rental rental = new Rental(renter, vehicle, days, cost, deposit);
        rentals.add(rental);
        renter.addRentalToHistory(rental);
        vehicle.markRented();

        return rental;
    }

    public void returnVehicle(Vehicle vehicle) {
        if (vehicle == null || !vehicles.contains(vehicle)) {
            throw new IllegalArgumentException("This vehicle is not in the depot.");
        }

        for (Rental rental : rentals) {
            if (rental.getVehicle() == vehicle && Rental.ACTIVE.equals(rental.getStatus())) {
                rental.markReturned();
                vehicle.markAvailable();
                System.out.println("Returned " + vehicle.getModel() + " (" + vehicle.getRegistration() + ")");
                return;
            }
        }

        throw new IllegalStateException("There is no active rental for this vehicle.");
    }

    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }

    public List<Renter> getRenters() {
        return new ArrayList<>(renters);
    }

    public List<Rental> getRentals() {
        return new ArrayList<>(rentals);
    }

    public void printReport() {
        System.out.println();
        System.out.println("=== " + name + " - end of day ===");
        System.out.println("Fleet status:");
        for (Vehicle vehicle : vehicles) {
            System.out.println("  " + vehicle.getModel() + " (" + vehicle.getRegistration()
                    + ", " + vehicle.getCategory() + "): " + vehicle.getStatus());
        }

        System.out.println("Active rentals:");
        for (Rental rental : rentals) {
            if (Rental.ACTIVE.equals(rental.getStatus())) {
                System.out.println("  Rental #" + rental.getId() + ": "
                        + rental.getVehicle().getRegistration() + " -> "
                        + rental.getRenter().getName() + " ("
                        + rental.getDays() + " days)");
            }
        }
    }
}
