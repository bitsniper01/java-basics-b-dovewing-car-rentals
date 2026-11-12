package com.dovewing.rentaltracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** Stores information about a customer who rents vehicles. */
public class Renter {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final int id;
    private final String name;
    private final RenterType renterType;
    private final List<Rental> history;

    public Renter(String name, RenterType renterType) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Renter name is required.");
        }
        if (renterType == null) {
            throw new IllegalArgumentException("A renter type is required.");
        }

        this.id = NEXT_ID.getAndIncrement();
        this.name = name;
        this.renterType = renterType;
        this.history = new ArrayList<>();
    }

    void addRentalToHistory(Rental rental) {
        if (rental == null) {
            throw new IllegalArgumentException("A rental is required.");
        }
        history.add(rental);
    }

    public List<Rental> getHistory() {
        return new ArrayList<>(history);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RenterType getRenterType() {
        return renterType;
    }
}
