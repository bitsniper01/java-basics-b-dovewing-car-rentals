## Dovewing Car Rentals - Diagnosis

### Code Base Overview 
Assessment performed on the Dovewing Car Rental fleet tracking system. Application runs without runtime errors during normal operation but has several design, business and logic flaws. These issues found affect data integrity, the business and operational reliability and user trust.


## Findings (most serious first)

| What is wrong | Where (class / method) | Root cause | Business risk | Severity |
|---|---|---|---|---|
| The same vehicle can be rented to two people at once. | `Depot.rent()`  | The method changes the status to `RENTED`, but it never checks whether the vehicle is already rented. | Two customers may arrive expecting the same car. This can lead to complaints, refunds and lost trust. | **Critical** |
| A return can close more than one rental record for the same vehicle. | `Depot.returnVehicle()`  | The loop marks every active rental with the same registration as returned. | The records may show that several customers returned one vehicle. | **High** |
| Zero or negative rental days are accepted. | `Depot.rent()` | There is no check that `days` is greater than zero. | The business could create a rental with an incorrect or negative charge. | **High** |
| An unknown vehicle category can create a rental with zero cost. | `Depot.rent()`  | The code only handles `ECONOMY` and `PREMIUM`, but it still creates a rental after the `if` statements. | A typing mistake or new category could create an incorrect rental record. | **High** |
| Important fields can be changed from anywhere. | `Vehicle`, `Renter` and `Rental` fields  | Most fields are `public`, so another class can change the price or status directly. | Vehicle and rental information can disagree, making the report unreliable. | **High** |
| Money uses `double`. | `Vehicle` and `Rental` fields; calculations in `Depot.rent()` | `double` is a general decimal number type and can have small rounding differences. | A larger system may show small differences in money calculations. | **Medium** |
| Categories and statuses are written as normal text. | `Vehicle.category`, `Vehicle.status`, `Renter.kind`, `Rental.status` | The program compares strings such as `"MEMBER"` and `"ACTIVE"`. A spelling mistake can make a rule fail. | A booking may receive the wrong deposit or appear under the wrong report heading. | **Medium** |
| The same pricing code is repeated for economy and premium vehicles. | `Depot.rent()`  | Both branches calculate cost and deposit in almost exactly the same way. | A future change may be added to one branch but forgotten in the other. | **Low** |
| Lists can be changed directly by other classes. | `Depot.vehicles`, `Depot.renters`, `Renter.history`, `Depot.getRentals()` | The lists are public or returned directly. | A caller could remove a vehicle or edit the rental records without following the normal process. | **Medium** |
| Some variable names are too short to explain their purpose. | `RentalTracker.main()` (`d`, `vitz` is fine, but `d` is unclear); `Depot.rent()` (`r`, `v`, `rt`) | Short names save typing but make the code harder for a new developer to read. | New team members may misunderstand the code and introduce mistakes. | **Low** |

## Severity guide

- **Critical:** Can directly cause the business operations to stop.
- **High:** Can create incorrect rental or return records.
- **Medium:** Can make information or money less reliable, especially as the program grows.
- **Low:** Makes the code harder to read or update, but is less likely to stop today's rental process.

## Main problem to fix first

The double-booking problem is the first priority. Before a rental is saved, `Depot.rent()` must check that the vehicle status is `AVAILABLE`. It should only change the status and save the rental after the checks and price calculation succeed.

## Additional Expansion Notes

The business has said that it expects to add commercial vans with their own deposit rules. Those rules have not been supplied. A  rebuild will includes a `CommercialVan` class, but it does not guess the deposit amount or member discount. CommercialVan rentals are blocked until the business confirms the rule.
- **Repository:** [etornam-ahiataku/dovewing-car-rental-lab](https://github.com/etornam-ahiataku/java-basics-b-dovewing-car-rentals)

- **Maintainer:** Etornam Ahiataku
