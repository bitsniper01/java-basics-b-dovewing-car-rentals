# Dovewing Car Rentals — Design Notes

This is a simple rebuild of the `Dovewing Car Rental Tracker` program.

## What you need

- Java JDK 17 or newer
- An IDE such as IntelliJ IDEA, Eclipse or VS Code, or a terminal
- Maven is can be used to run it

## Project folders

```text
dovewing-rental-tracker/
  DIAGNOSIS.md
  UML-DESIGNS.md
  README.md
  NOTES.md
  src/
    main/java/com/dovewing/rentaltracker/
      RentalTracker.java
      Depot.java
      Vehicle.java
      CommercialVan.java
      Renter.java
      Rental.java
    test/java/com/dovewing/rentaltracker/
      RentalTrackerTest.java
```

Each public class has been moved into a file with the same name.

## Run the program

The program is in plain Java (no external dependancies) JDK 21 or higher is required:

**Directly with the JDK**

```bash
javac -d out src/main/java/com/dovewing/rentaltracker/*.java
java -cp out com.dovewing.rentaltracker.RentalTracker
```

**With Maven:**

```bash
mvn compile
mvn exec:java -Dexec.mainClass=RentalTracker
```


The program shows two normal rentals, the return of the Prado, and the rejected attempt to rent the Vitz a second time. It then prints the end-of-day report and Ama's rental history.

## Run the simple tests

The tests use normal Java code, so you do not need to install JUnit or Maven or any external framework to test it:

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
- Members do not pay a deposit for the ordinary cars in the original example.
- Returning a vehicle makes it available again.
- The end-of-day report shows the fleet and any active rentals.

## Fixes

- A vehicle that is already rented cannot be rented again.
- Rental days must be greater than zero.
- A vehicle must belong to the depot before it can be rented.
- Important fields are private, and getters are used to read them.
- Customer rental history now contains rental records, including returned rentals.

## Added feature

- The extra feature is the customer rental-history list. It helps the front desk answer questions about previous rentals.

## Commercial vans — awaiting business rules

The `CommercialVan` class is already included. Its deposit calculation is deliberately not completed because the business has not yet provided the van deposit rules. For now, trying to rent a commercial van shows a clear message and does not create a rental.


See `DIAGNOSIS.md` for the findings about the inherited code and `UML.md` for the Class Designs for old code structure and the updated code all in ``Mermaid`` code
