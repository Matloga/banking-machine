# Banking Machine (Java console app)

## Requirements
- Java 17+ (JDK)
- Maven

## Build
mvn clean package

This creates a runnable JAR in `target/banking-machine-1.0-SNAPSHOT.jar`.

## Run
java -jar target/banking-machine-1.0-SNAPSHOT.jar

The application will load saved state from `bank.db` (if present) and allow you to:
- Create customers
- Create accounts
- Deposit, withdraw, transfer
- View balances and transactions
- Save state to `bank.db`

Data persistence uses Java serialization to `bank.db`. For production you'd replace this with a proper DB.

## Notes / Next steps
- Add authentication (PINs) per account
- Replace serialization with SQLite / H2 or another DB
- Add unit tests (JUnit)
- Add concurrency handling for multi-threaded access
- Add interest computation / scheduled jobs
