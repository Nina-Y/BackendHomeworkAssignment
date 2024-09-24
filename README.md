# Backend Homework Assignment

## Overview

This Java project processes parcel data and applies discount rules based on parcel size, company, and monthly limits. It handles both valid and invalid parcel entries, ensuring that discount limits are enforced within a given month (10 EUR maximum). The application supports shipments from two providers, LP and MR, and handles different package sizes (S, M, L).

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

### Program Structure
The project is organized into several core components to ensure separation of concerns, modularity, and easier maintainability. Below is an overview of the main classes and their responsibilities:

1. ParcelProcessingRunner
   Role: Entry point of the program.
2. ParcelParser
   Role: Handles parsing of the input file and categorization of parcels into valid and invalid entries.
3. ShipmentDiscountRule
   Role: Contains the core logic for applying discounts to valid parcels.
4. MonthlyDiscountTracker
   Role: Tracks the total amount of discounts applied within a calendar month. Enforces the monthly discount cap (10 EUR per calendar month).
5. Parcel
   Role: Data structure representing a parcel with essential information such as date, size, and company.
6. Enums: ParcelSize and Company
   ParcelSize: Defines valid parcel sizes (S, M, L).
   Company: Defines valid companies (LP, MR).

### Tests
Key tests include:

1. Small Parcel Discount: Verifies that small parcels (S) receive the correct discount based on the company.
2. Large Parcel LP Discount: Ensures every third large parcel (L) from LP in a month is fully discounted.
3. Monthly Discount Limit: Confirms that the total applied discounts do not exceed 10 EUR in a given month.
4. No Discount for Non-Qualifying Parcels: Ensures parcels that don’t qualify for discounts receive none.
5. Invalid Parcel Handling: Tests that parcels with invalid sizes, companies, or dates are ignored.
6. End-to-End Testing: Simulates the complete process from input parsing to output formatting, ensuring correct behavior and cross-platform compatibility.