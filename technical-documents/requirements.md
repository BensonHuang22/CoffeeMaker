# CoffeeMaker Requirements

CoffeeMaker is an online beverage ordering system, similar in spirit to GrubHub. The requirements below are written as use cases — a way of modeling requirements from the user's perspective. Each use case describes the preconditions needed to reach it, the main flow of functionality, and alternative/error paths.

## Use Case 1: Display Menu

Display a menu of options for working with the CoffeeMaker system for a user.

**Main Flow**
1. A user goes to the CoffeeMaker website.
2. A user selects one of the following menu options:
   - Add Recipe [UC2]
   - Delete Recipe [UC3]
   - Edit Recipe [UC4]
   - Update Inventory [UC5]
   - Order Beverage [UC6]

## Use Case 2: Add Recipe

**Preconditions:** User selected the Add Recipe option from the menu [UC1]

**Main Flow**
1. A user selects the option to add a recipe.
2. The user enters the name, price, units coffee, units tea, units sugar, units milk, and units chocolate that make up the recipe. The user clicks Submit. The recipe is saved. [Too Many] [Invalid Price] [Invalid Unit] [Duplicate]
3. The user returns to the main menu [UC1].

**Alternative Flows**
- **[Too Many]** If there are already three recipes in the system, then a new recipe may not be added. An error message is printed, and the recipe is not added.
- **[Invalid Price]** The price must be a positive integer. If the price is not a positive integer, an error message is printed and the user is returned to the form to edit. The recipe is not added.
- **[Invalid Unit]** The units of coffee, sugar, milk, and chocolate must be positive integers. If any unit value is not a positive integer, an error message is printed and the user is returned to the form to edit. The recipe is not added.
- **[Duplicate]** If the name of the new recipe already exists in the system, an error message is printed and the user is returned to the form to edit. The recipe is not added.

## Use Case 3: Delete Recipe

**Preconditions:** User selected the Delete Recipe option from the menu [UC1]

**Main Flow**
1. The user will be shown a list of all recipes in the system.
2. The user may select one or more recipes that they want to delete.
3. The user selects the option to delete the recipes.
4. The selected recipes are deleted and a success message is displayed. [Cannot Delete]
5. The user returns to the main menu [UC1].

**Alternative Flows**
- **[Cannot Delete]** If the recipe is deleted by a concurrent user before it's selected for deletion, an error message is printed and the user is returned to the delete recipe page with an updated list of recipes.

## Use Case 4: Edit Recipe

**Preconditions:** User selected the Edit Recipe option from the menu [UC1]

**Main Flow**
1. The user will be shown a list of all recipes in the system.
2. The user may select one recipe that they want to edit. [E1]
3. After selecting a recipe, the form will be pre-filled with all existing values (price, units coffee, units sugar, units milk, and units chocolate) for the selected recipe.
4. The user edits the price, units coffee, units tea, units sugar, units milk, and units chocolate that make up the recipe. [Cannot Edit] [Invalid Price] [Invalid Unit]
5. The user returns to the main menu [UC1].

**Alternative Flows**
- **[Cannot Edit]** If the recipe is deleted by a concurrent user before it's selected for edit, an error message is printed and the user is returned to the edit recipe page with an updated list of recipes.
- **[Invalid Price]** The price must be a positive integer. If the price is not a positive integer, an error message is printed and the user is returned to the form to edit. The recipe is not added.
- **[Invalid Unit]** The units of coffee, sugar, milk, and chocolate must be positive integers. If any unit value is not a positive integer, an error message is printed and the user is returned to the form to edit. The recipe is not added.

## Use Case 5: Add Inventory

**Preconditions:** User selected the Add Inventory option from the menu [UC1]

**Main Flow**
1. The user will be shown a listing of the current inventory of ingredients in the CoffeeMaker.
2. The user enters the amount of inventory to add for each ingredient. [Invalid Unit]
3. The current inventory values are updated after inventory is added.
4. The user returns to the main menu [UC1].

**Alternative Flows**
- **[Invalid Unit]** The units of coffee, sugar, milk, and chocolate must be positive integers. If any unit value is not an integer, an error message is printed and the user is returned to the form to edit. No inventory is added.

## Use Case 6: Order Beverage

**Preconditions:** User selected the Order Beverage option from the menu [UC1]

**Main Flow**
1. The user will select the beverage they want to purchase.
2. The user will enter the amount of money they will use to purchase the beverage.
3. The user requests the beverage.
4. The system will check if there are enough ingredients in the inventory to make the selected beverage. [Insufficient Ingredients]
5. The system will check if there is enough money to pay for the beverage. [Insufficient Payment] [Invalid Payment]
6. If there are enough ingredients and the money paid is sufficient, the ingredients to make the purchased beverage will be removed from the inventory.
7. A message indicating that the beverage is ready and the user's change will be displayed.
8. The user may return to the main menu [UC1].

**Alternative Flows**
- **[Insufficient Ingredients]** If there is not enough inventory to make the beverage, an error message is printed, the user's money is returned, and the user is returned to the purchase beverage page. No inventory is used.
- **[Insufficient Payment]** If the user does not enter enough money, an error message is printed, their money is returned, and the user is returned to the purchase beverage page. No inventory is used.
- **[Invalid Payment]** If the payment entered is not numeric or not an integer, an error message is printed, the user's money is returned (if the input was numeric), and the user is returned to the purchase beverage page. No inventory is used.
