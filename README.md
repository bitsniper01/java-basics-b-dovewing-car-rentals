# Dovewing Car Rentals — Design Notes

This is a simple Java rebuild of the Dovewing Car Rental Tracker program. The aim is to keep the original business behaviour while making the model safer and easier to extend.

## What you need

- Java JDK 17 or newer
- An IDE such as IntelliJ IDEA, Eclipse, or VS Code, or a terminal
- Maven can be used to compile the project

## Project folders

```text
dovewing-car-rentals/
  DIAGNOSIS.md
  UML.md
  README.md
  NOTES.md
  pom.xml
  src/
    main/java/com/dovewing/rentaltracker/
      CommercialVan.java
      Depot.java
      Rental.java
      RentalReporter.java
      RentalStatus.java
      RentalTracker.java
      RentalTrackerDemo.java
      Renter.java
      RenterType.java
      Vehicle.java
      VehicleCategory.java
      VehicleStatus.java
    test/java/com/dovewing/rentaltracker/
      RentalTrackerTest.java
```

Each public class is stored in its own file, and the classes are organised under the `com.dovewing.rentaltracker` package.

## Run the program

The project is plain Java with no external dependencies. JDK 21 or higher is recommended.

### Directly with the JDK

```bash
javac -d out src/main/java/com/dovewing/rentaltracker/*.java
java -cp out com.dovewing.rentaltracker.RentalTracker
```

### With Maven

```bash
mvn compile
java -cp target/classes com.dovewing.rentaltracker.RentalTracker
```

The program demonstrates two normal rentals, the return of the Prado, and a rejected second rental attempt for the Vitz. It then prints the end-of-day report and Ama's rental history.

## Run the simple tests

The tests are written in normal Java, so you do not need JUnit or any additional framework.

```bash
javac -d out src/main/java/com/dovewing/rentaltracker/*.java src/test/java/com/dovewing/rentaltracker/RentalTrackerTest.java
java -cp out com.dovewing.rentaltracker.RentalTrackerTest
```

You should see:

```text
All simple tests passed.
```

## Original rules

- Rental cost is the daily rate multiplied by the number of days.
- Casual customers pay the vehicle's listed deposit.
- Members do not pay a deposit for ordinary cars in the original example.
- Returning a vehicle makes it available again.
- The end-of-day report shows the fleet and any active rentals.

## Fixes

- A vehicle that is already rented cannot be rented again.
- Rental days must be greater than zero.
- A vehicle must belong to the depot before it can be rented.
- Important fields are private, and getters are used to read them.
- Customer rental history now contains rental records, including returned rentals.
- Vehicle category and status are represented as enums rather than raw strings.
- The available/rented invariant is enforced so a vehicle cannot be both available and rented at once.

## Added feature

- The extra feature is the customer rental-history list. It helps the front desk answer questions about previous rentals.

## Commercial van rule

The `CommercialVan` type is included and follows the business rule used by the project: the configured deposit is charged for every renter type. This is implemented through the vehicle category policy rather than a placeholder exception.

## Related documentation

See `DIAGNOSIS.md` for the findings about the inherited code and `UML.md` for the class design notes for both the original structure and the rebuilt design.
