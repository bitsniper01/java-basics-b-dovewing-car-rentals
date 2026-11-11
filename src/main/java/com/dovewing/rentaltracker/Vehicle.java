package com.dovewing.rentaltracker;

/** Stores information about a vehicle in the rental fleet. */
public class Vehicle {
    public static final String ECONOMY = "ECONOMY";
    public static final String PREMIUM = "PREMIUM";
    public static final String AVAILABLE = "AVAILABLE";
    public static final String RENTED = "RENTED";

    private static int nextId = 1;

    private int id;
    private String model;
    private String registration;
    private double dailyRate;
    private double deposit;
    private String category;
    private String status;

    public Vehicle(String model, String reg, double dailyRate,
                   double deposit, String category) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Vehicle model is required.");
        }
        if (reg == null || reg.isBlank()) {
            throw new IllegalArgumentException("Vehicle registration is required.");
        }
        if (dailyRate < 0 || deposit < 0) {
            throw new IllegalArgumentException("Rate and deposit cannot be negative.");
        }

        this.id = nextId++;
        this.model = model;
        this.registration = reg;
        this.dailyRate = dailyRate;
        this.deposit = deposit;
        this.category = category;
        this.status = AVAILABLE;
    }

    /** Applies the original deposit rule for cars. */
    public double calculateDeposit(Renter renter) {
        if (Renter.MEMBER.equals(renter.getKind())) {
            return 0.0;
        }
        return deposit;
    }

    public void markRented() {
        status = RENTED;
    }

    public void markAvailable() {
        status = AVAILABLE;
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

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }
}
