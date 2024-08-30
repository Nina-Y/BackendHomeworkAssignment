# Shipment Discount Calculator

## Overview

The Shipment Discount Calculator is a Java application that processes shipment transactions from an input file, calculates discounts based on predefined rules, and outputs the results. The application supports shipments from two providers, LP and MR, and handles different package sizes (S, M, L).

### Setup

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Maven

### Set Up Maven

- Ensure Maven is installed and added to your system's PATH.
- Verify the installation by running: mvn -version

### Running the Application

- The application supports running with the default `input.txt` file located in the `src/main/resources` directory through the command line: mvn clean compile exec:java
- or a user-provided file path (example): mvn clean compile exec:java -Dexec.args="C:\input2.txt"
   

### Running the Tests

- To run the test:
  mvn test

### Design Decisions

- ShipmentDiscountCalculator class is the main class responsible for reading shipment transactions from an input file and outputting the results after applying discount rules.

- ShipmentDiscountRule class encapsulates the logic for calculating discounts based on shipment provider, package size, and other criteria. This class makes the code more modular, allowing easy modification or extension of discount rules without affecting the core functionality of the ShipmentDiscountCalculator.
Prices and minimum prices for shipments are initialized in a static block. This ensures that these values are loaded once and available throughout the class without redundant initializations.

- MonthlyDiscountTracking class is used to keep track of discounts and apply rules on a per-month basis. 

- Use of Java NIO for File Operations - the java.nio.file package is used for reading input files and handling file paths.

- Unit Testing with JUnit - the test class ShipmentDiscountCalculatorTest captures the output of the main application and compares it with expected output from a file. 
