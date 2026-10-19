# CoffeeMaker System Tests

## Testing Overview

See [CoffeeMaker Setup](../setup) for first-time configuration.

  * Start MySQL. 
  * Drop the `coffee_maker` database.
  * Run `CoffeeMakerApplication` from IntelliJ and confirm the console reports the application started on port 8080.  
  * In a terminal in `coffee_maker_frontend`, run `npm run dev`
  * Browse to [http://localhost:3000](http://localhost:3000).  
  

## Requirements Coverage

TODO - update!

| Use Case | Tests | Covered? |
| -------- | ----- | -------- |
| [UC1 Display Menu](../requirements#uc1) | | |
| [UC2 Add Recipe](../requirements#uc2) | `addRecipeValid1`, `addRecipeNegativePrice1` | Partial |
| [UC3 Delete Recipe](../requirements#uc3) | | |
| [UC4 Edit Recipe](../requirements#uc4) | | |
| [UC5 Update Inventory](../requirements#uc5) | | |
| [UC6 Order Beverage](../requirements#uc6) | | |

## Test Data Summary

TODO - update!

## Test Plan

| Test ID | Description | Expected Results | Actual Results |
| ------- | ----------- | ---------------- | -------------- |
| `addRecipeValid1` | **Preconditions:** CoffeeMaker is running and no recipe named "Coffee" exists.<br><br>1. Browse to [http://localhost:3000](http://localhost:3000).<br>2. Click **Add a Recipe**.<br>3. Enter name `Coffee`, price `50`, coffee `3`, milk `1`, sugar `1`, chocolate `0`.<br>4. Click **Submit**. | The recipe is created and a success message is displayed.  "Coffee" appears in the recipe list on the main menu. | |
| `addRecipeNegativePrice1` | **Preconditions:** CoffeeMaker is running and no recipe named "Mocha" exists.<br><br>1. Browse to [http://localhost:3000](http://localhost:3000).<br>2. Click **Add a Recipe**.<br>3. Enter name `Mocha`, price `-10`, coffee `3`, milk `1`, sugar `1`, chocolate `2`.<br>4. Click **Submit**. | The recipe is **not** created.  An error message indicates the price must not be negative.  "Mocha" does not appear in the recipe list. | |