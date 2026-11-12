package com.dovewing.rentaltracker;

/** Vehicle categories own their renter-specific deposit policies. */
public enum VehicleCategory {
    ECONOMY(true),
    PREMIUM(true),
    COMMERCIAL_VAN(false);

    private final boolean memberDepositWaiver;

    VehicleCategory(boolean memberDepositWaiver) {
        this.memberDepositWaiver = memberDepositWaiver;
    }

    public double calculateDeposit(Renter renter, double listedDeposit) {
        if (renter == null) {
            throw new IllegalArgumentException("A renter is required.");
        }
        return memberDepositWaiver && renter.getRenterType() == RenterType.MEMBER
                ? 0.0
                : listedDeposit;
    }
}
