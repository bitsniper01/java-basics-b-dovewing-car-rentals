package com.dovewing.rentaltracker;

/** Records one rental of a vehicle by a renter. */
public class Rental {
    public static final String ACTIVE = "ACTIVE";
    public static final String RETURNED = "RETURNED";

    private static int nextId = 1;

    private int id;
    private Renter renter;
    private Vehicle vehicle;
    private int days;
    private double cost;
    private double deposit;
    private String status;

    public Rental(Renter renter, Vehicle vehicle, int days,
                  double cost, double deposit) {
        this.id = nextId++;
        this.renter = renter;
        this.vehicle = vehicle;
        this.days = days;
        this.cost = cost;
        this.deposit = deposit;
        this.status = ACTIVE;
    }

    public void markReturned() {
        status = RETURNED;
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

    public String getStatus() {
        return status;
    }
}
