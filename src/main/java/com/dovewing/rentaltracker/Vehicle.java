package com.dovewing.rentaltracker;

import java.util.concurrent.atomic.AtomicInteger;

/** Stores information about a vehicle in the rental fleet. */
public class Vehicle {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final int id;
    private final String model;
    private final String registration;
    private final double dailyRate;
    private final double deposit;
    private final VehicleCategory category;
    private VehicleStatus status;

    public Vehicle(String model, String reg, double dailyRate,
                   double deposit, VehicleCategory category) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Vehicle model is required.");
        }
        if (reg == null || reg.isBlank()) {
            throw new IllegalArgumentException("Vehicle registration is required.");
        }
        if (!Double.isFinite(dailyRate) || !Double.isFinite(deposit)
                || dailyRate < 0 || deposit < 0) {
            throw new IllegalArgumentException("Rate and deposit must be finite and cannot be negative.");
        }
        if (category == null) {
            throw new IllegalArgumentException("A vehicle category is required.");
        }

        this.id = NEXT_ID.getAndIncrement();
        this.model = model;
        this.registration = reg;
        this.dailyRate = dailyRate;
        this.deposit = deposit;
        this.category = category;
        this.status = VehicleStatus.AVAILABLE;
    }

    /** Delegates deposit calculation to the vehicle category's policy. */
    public double calculateDeposit(Renter renter) {
        return category.calculateDeposit(renter, deposit);
    }

    void markRented() {
        if (status != VehicleStatus.AVAILABLE) {
            throw new IllegalStateException("This vehicle is already rented.");
        }
        status = VehicleStatus.RENTED;
    }

    void markAvailable() {
        if (status != VehicleStatus.RENTED) {
            throw new IllegalStateException("This vehicle is not rented.");
        }
        status = VehicleStatus.AVAILABLE;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getRegistration() {
        return registration;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public double getDeposit() {
        return deposit;
    }

    public VehicleCategory getCategory() {
        return category;
    }

    public VehicleStatus getStatus() {
        return status;
    }
}
