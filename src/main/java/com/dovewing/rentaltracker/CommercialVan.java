package com.dovewing.rentaltracker;

/** A commercial van uses its configured deposit for every renter type. */
public class CommercialVan extends Vehicle {

    public CommercialVan(String model, String reg, double dailyRate, double deposit) {
        super(model, reg, dailyRate, deposit, VehicleCategory.COMMERCIAL_VAN);
    }
}
