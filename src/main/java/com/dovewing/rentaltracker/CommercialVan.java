package com.dovewing.rentaltracker;


/* A commercial van is a type of vehicle.*/
public class CommercialVan extends Vehicle {

    public CommercialVan(String model, String reg, double dailyRate, double deposit) {
        super(model, reg, dailyRate, deposit, "COMMERCIAL_VAN");
    }

    @Override
    public double calculateDeposit(Renter renter) {
        // TODO: Replace this message when the business confirms the van rules.
        throw new IllegalStateException(
                "Commercial van deposit rules have not been confirmed yet.");
    }
}
