package com.dovewing.rentaltracker;

import java.util.ArrayList;
import java.util.List;

/** Manages the vehicles, renters and rentals for one depot. */
public class Depot {
    private final String name;
    private final List<Vehicle> vehicles;
    private final List<Renter> renters;
    private final List<Rental> rentals;

    public Depot(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Depot name is required.");
        }
        this.name = name;
        this.vehicles = new ArrayList<>();
        this.renters = new ArrayList<>();
        this.rentals = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("A vehicle is required.");
        }
        for (Vehicle existingVehicle : vehicles) {
            if (existingVehicle.getRegistration().equalsIgnoreCase(vehicle.getRegistration())) {
                throw new IllegalArgumentException("That registration is already in the fleet.");
            }
        }
        vehicles.add(vehicle);
    }

    public void addRenter(Renter renter) {
        if (renter == null) {
            throw new IllegalArgumentException("A renter is required.");
        }
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
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new IllegalStateException("This vehicle is already rented.");
        }

        Rental rental = new Rental(renter, vehicle, days);
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
            if (rental.getVehicle() == vehicle && rental.getStatus() == RentalStatus.ACTIVE) {
                rental.markReturned();
                vehicle.markAvailable();
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

    public String getName() {
        return name;
    }
}
