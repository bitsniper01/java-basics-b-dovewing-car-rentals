# UML Class Diagrams

Mermaid class diagrams for the original code and updated/rebuild code.

## 1. Inherited design — original program

```mermaid
classDiagram
    class RentalTracker {
        +main(String[] args) void
    }
    class Depot {
        +String name
        +List~Vehicle~ vehicles
        +List~Renter~ renters
        -List~Rental~ rentals
        +addVehicle(Vehicle) void
        +addRenter(Renter) void
        +rent(Renter, Vehicle, int) void
        +returnVehicle(Vehicle) void
        +getRentals() List~Rental~
        +printReport() void
    }
    class Vehicle {
        +String model
        +String reg
        +double dailyRate
        +double deposit
        +String category
        +String status
        +int id
    }
    class Renter {
        +String name
        +String kind
        +int id
        +List~String~ history
    }
    class Rental {
        +int id
        +String renterName
        +String vehicleReg
        +int days
        +double cost
        +double deposit
        +String status
    }
    RentalTracker --> Depot : starts the program
    Depot "1" o-- "many" Vehicle : keeps the fleet
    Depot "1" o-- "many" Renter : keeps customers
    Depot "1" o-- "many" Rental : keeps rental records
    Rental ..> Vehicle : stores registration text
    Rental ..> Renter : stores name text
```

### What is difficult about the original design?

Most of the work is placed inside `Depot`. Many fields are public, and `Rental` only stores a vehicle registration and renter name as text. The program also does not stop someone from renting a vehicle that is already out.

## 2. New design - Updated program

```mermaid
classDiagram
    class RentalTracker {
        +main(String[] args) void
        -printRental(Rental) void
    }
    class Depot {
        -String name
        -List~Vehicle~ vehicles
        -List~Renter~ renters
        -List~Rental~ rentals
        +addVehicle(Vehicle) void
        +addRenter(Renter) void
        +rent(Renter, Vehicle, int) Rental
        +returnVehicle(Vehicle) void
        +printReport() void
    }
    class Vehicle {
        -int id
        -String model
        -String reg
        -double dailyRate
        -double deposit
        -String category
        -String status
        +calculateDeposit(Renter) double
        +markRented() void
        +markAvailable() void
        +getStatus() String
    }
    class CommercialVan {
        +calculateDeposit(Renter) double
    }
    class Renter {
        -int id
        -String name
        -String kind
        -List~Rental~ history
        +addRentalToHistory(Rental) void
        +getHistory() List~Rental~
    }
    class Rental {
        -int id
        -Renter renter
        -Vehicle vehicle
        -int days
        -double cost
        -double deposit
        -String status
        +markReturned() void
    }

    RentalTracker --> Depot : uses
    Depot "1" o-- "many" Vehicle : manages
    Depot "1" o-- "many" Renter : manages
    Depot "1" o-- "many" Rental : records
    Vehicle <|-- CommercialVan : extends
    Rental --> Vehicle : rented vehicle
    Rental --> Renter : customer
    Renter "1" --> "many" Rental : rental history
```

### What each class does

| Class | Responsibility |
|---|---|
| `RentalTracker` | Starts the program and shows the example day. |
| `Depot` | Adds vehicles and renters, handles renting and returning, and prints the report. |
| `Vehicle` | Stores common vehicle details and the normal car deposit rule. |
| `CommercialVan` | Represents a van and provides a separate place for its future deposit rule. |
| `Renter` | Stores customer details and their rental history. |
| `Rental` | Stores the details and status of one rental. |

## 3. Design notes and conclusion

Please refer to the `NOTES.md` in the dir for the notes in the update

- The main fix is in `Depot.rent()`: it checks that the vehicle is available before saving a rental. It also checks that the renter and vehicle have been added to the depot and that the number of days is positive.

+ The extra feature is customer rental history. `Renter` stores the actual `Rental` objects, so returned rentals remain available for a later customer enquiry.

#### Note for Commercial Vans

 * The business has not supplied the commercial-van deposit rules. So that will be updates when the rule comes up.

**Conclusion:** the rebuilt prevents the main booking mistake in the original. Each class has a clear job, and the commercial-van deposit decision has a clear place to be added once the business provides the details.
