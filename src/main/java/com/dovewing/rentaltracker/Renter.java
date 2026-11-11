package com.dovewing.rentaltracker;

import java.util.ArrayList;
import java.util.List;

/** Stores information about a customer who rents vehicles. */
public class Renter {
    public static final String CASUAL = "CASUAL";
    public static final String MEMBER = "MEMBER";

    private static int nextId = 1;

    private int id;
    private String name;
    private String kind;
    private List<Rental> history;

    public Renter(String name, String kind) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Renter name is required.");
        }
        if (!CASUAL.equals(kind) && !MEMBER.equals(kind)) {
            throw new IllegalArgumentException("Renter must be CASUAL or MEMBER.");
        }

        this.id = nextId++;
        this.name = name;
        this.kind = kind;
        this.history = new ArrayList<>();
    }

    public void addRentalToHistory(Rental rental) {
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

    public String getKind() {
        return kind;
    }
}
