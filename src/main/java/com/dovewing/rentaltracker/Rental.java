package com.dovewing.rentaltracker;

import java.util.concurrent.atomic.AtomicInteger;

/** Records one rental of a vehicle by a renter. */
public class Rental {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final int id;
    private final Renter renter;
    private final Vehicle vehicle;
    private final int days;
    private final double cost;
    private final double deposit;
    private RentalStatus status;

    Rental(Renter renter, Vehicle vehicle, int days) {
        if (renter == null || vehicle == null) {
            throw new IllegalArgumentException("A renter and a vehicle are required.");
        }
        if (days <= 0) {
            throw new IllegalArgumentException("Rental days must be greater than zero.");
        }
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new IllegalStateException("This vehicle is already rented.");
        }

        double calculatedCost = vehicle.getDailyRate() * days;
        double calculatedDeposit = vehicle.calculateDeposit(renter);
        if (!Double.isFinite(calculatedCost) || !Double.isFinite(calculatedDeposit)) {
            throw new IllegalArgumentException("Rental cost and deposit must be finite.");
        }

        this.id = NEXT_ID.getAndIncrement();
        this.renter = renter;
        this.vehicle = vehicle;
        this.days = days;
        this.cost = calculatedCost;
        this.deposit = calculatedDeposit;
        this.status = RentalStatus.ACTIVE;
    }

    void markReturned() {
        if (status != RentalStatus.ACTIVE) {
            throw new IllegalStateException("This rental has already been returned.");
        }
        status = RentalStatus.RETURNED;
    }

    public int getId() {
        return id;
    }

    public Renter getRenter() {
        return renter;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getDays() {
        return days;
    }

    public double getCost() {
        return cost;
    }

    public double getDeposit() {
        return deposit;
    }

    public RentalStatus getStatus() {
        return status;
    }
}
