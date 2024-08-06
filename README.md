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

- To run the application with a specified input file:
  mvn compile exec:java -Prun -Dexec.args="src/main/resources/input.txt"

### Running the Tests

- To run the application with a specified input file:
  mvn test

### Design Decisions

- Use of Java NIO for File Operations:
The java.nio.file package is used for reading input files and handling file paths. This decision was made to leverage the modern, efficient, and scalable I/O capabilities provided by NIO.

- Map Initialization in Static Block:
Prices and minimum prices for shipments are initialized in a static block within the ShipmentDiscountCalculator class. This ensures that these values are loaded once and available throughout the class without redundant initializations.

- ShipmentDiscountCalculator class:
ShipmentDiscountCalculator is the main class of the application. It reads shipment transactions from an input file, applies discount rules, and outputs the results. The class also maintains pricing information for different shipment sizes and providers. It tracks discounts and ensures that the correct discount is applied according to predefined rules.

- MonthlyDiscountTracking class:
The MonthlyDiscountTracker class is used to keep track of discounts and apply rules on a per-month basis. 

- Unit Testing with JUnit:
JUnit is used for unit testing. The test class ShipmentDiscountCalculatorTest captures the output of the main application and compares it with expected output from a file. 
