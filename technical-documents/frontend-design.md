# CoffeeMaker Frontend Design

This document holds the frontend design for CoffeeMaker: the scenarios a user works through, the concept design for each one, the wire frames, and the reasoning behind them.

Add a section for each use case you design. The headings help with identifying the items that you are graded on.

Wire frames and other images go in `technical-documents/images/` and are linked from here.

See [CoffeeMaker Requirements](requirements.md) for the use cases and [CoffeeMaker Backend Design](backend-design.md) for the API these pages talk to.

---

### Scenarios

*Two scenarios, each taking a different path through the functionality. One should be a main flow; the other should exercise a subflow or an alternative flow.*

**Scenario 1:** *The user wants to add tea with amount of 10 as a new ingredient.*

**Scenario 2:** *User tries to add -5 of sugar as the ingredient.*

**Scenario 3:** *User update ingredient Tea to quantity 5.*

**Scenario 4:** *User update ingredient Tea to quantity -1, causing an error message.*

**Scenario 5:** *The user wants to create a new valid recipe.*

**Scenario 6:** *User tries to add a recipe with an invalid negative price.*

**Scenario 7:** *User update existing recipe.*

**Scenario 8:** *User update existing recipe's price to negative integer, causing an error message.*

### Concept Designs

*One concept design per scenario.*

**Scenario 1**

| User task                                          | System need                                             | Design consideration                                                              |
|----------------------------------------------------|---------------------------------------------------------|-----------------------------------------------------------------------------------|
| *User selects "Add ingredient" from main menu.*    | *Create a add ingredient button.*                      | *A common action in the app, so button should be present next to other options.* |
| *User enters "tea" as name of ingredient.*        | *Create a textbox for user to enter ingredient name.*  | *Keyboard has access to multiple languages.*                                    |
| *User enters 10 as initial amount of ingredient.* | *Create a textbox for user to enter ingredient amount.* | *Keybaord has access to numbers*                                                |
| *User clicks "add ingredient".*                   | *Create a button for "add ingredient"*                 | *Once button is pressed, popup shows a "ingredient added" message to UI.*       |
| *User returns to main menu.*                       | *Create a back arrow for user to return to main menu.* | *User is able to enter another ingredient before returning to main menu.*       |

**Scenario 2**

| User task                                          | System need | Design consideration                                                              |
|----------------------------------------------------| ----------- |-----------------------------------------------------------------------------------|
| *User selects "Add ingredient" from main menu.*    | *Create a add ingredient button.*                      | *A common action in the app, so button should be present next to other options.* |
| *User enters "sugar" as name of ingredient.*      | *Create a textbox for user to enter ingredient name.*  | *Keyboard has access to multiple languages.*                                    |
| *User enters -5 as initial amount of ingredient.* | *Create a textbox for user to enter ingredient amount.* | *Keybaord has access to numbers*                                                |
| *User clicks "add ingredient".*                   | *Create a button for "add ingredient"*                 | *Once button is pressed, popup shows an error message to UI.*                   |
| *User decides to return to main menu*              | *Create a back arrow for user to return to main menu.* | *User is able to enter another ingredient before returning to main menu.*       |

**Scenario 3**

| User task                                          | System need                                             | Design consideration                                                              |
|----------------------------------------------------|---------------------------------------------------------|-----------------------------------------------------------------------------------|
| *Gather information for updated ingredient.*    |                       |  |
| *Locate inventory tab on home page.*        | *Create inventory tab on home page.*  | *Inventory button should be listed with other optional pages and clearly labeled.*                                    |
| *Locate Tea and update amount to 10.* | *Display ingredient and create button to update ingredient.* | *Clearly indicate the button to update. Click on the current quantity of ingredients and input a new quantity to change.*                                                |
| *Save changes.*                   | *Add save button to commit changes to the database.*                 | *Conspicuous save button so user doesn’t forget.*       |
| *Return to home page.*                       | *Create inventory tab on home page.* | *Home page should be clearly labeled and group with other page tabs.*       |

**Scenario 4**

| User task                                          | System need | Design consideration                                                              |
|----------------------------------------------------| ----------- |-----------------------------------------------------------------------------------|
| *Gather information for updated ingredient.*    |                       |  |
| *Locate inventory tab on ahome page.*      | *Create inventory tab on home page.*  | *Inventory button should be listed with other optional pages and clearly labeled.*                                    |
| *Locate ingredient and update Tea quantity to -1 for error message.* | *Display ingredients and create buttons to update ingredients. Display error message under quantity when negative quantity is inputted.* | *Error messages should be clearly labeled. Saving should be disabled when there’s an error message.*     

**Scenario 5**

| User task                                          | System need                                             | Design consideration                                                              |
|----------------------------------------------------|---------------------------------------------------------|-----------------------------------------------------------------------------------|
| *Gather information for new recipe.*    |                       |  |
| *Locate recipe tab on home page.*        | *Create recipe tab on home page.*  | *Recipe button should be listed with other optional pages and clearly labeled.*                                    |
| *Input name of recipe, price of recipe, and each ingredient with its quantity.* | *Display fields for name, price and ingredients.* | *Clearly indicate the button to save.* |
| *Save changes.*                   | *Add save button to commit changes to the database.*                 | *Conspicuous save button so user doesn’t forget.*       |

**Scenario 6**

| User task                                          | System need | Design consideration                                                              |
|----------------------------------------------------| ----------- |-----------------------------------------------------------------------------------|
| *Gather information for new recipe.*    |                       |  |
| *Locate recipe tab on home page.*        | *Create recipe tab on home page.*  | *Recipe button should be listed with other optional pages and clearly labeled.*                                    |
| *Input name of recipe, negative price of recipe, and each ingredient with its quantity. * | *Display fields for name, price and ingredients.* | *Clearly indicate the button to save.* |
| *Try to save changes and display error message.*                   | *No changes are saved, error message is displayed and user is returned to main menu.*                 | *Message should be clearly visible and labeled.*       |

**Scenario 7**

| User task                                          | System need                                             | Design consideration                                                              |
|----------------------------------------------------|---------------------------------------------------------|-----------------------------------------------------------------------------------|
| *Gather information for updated recipe.*    |                       |  |
| *Locate recipe tab on home page.*        | *Create recipe tab on home page.*  | *Recipe button should be listed with other optional pages and clearly labeled.*                                    |
| *Locate Latte and update price to 100.* | *Display recipe information and create button to update recipe.* | *Clearly indicate the button to update. Click on the current recipe information and input new information to change.*                                                |
| *Save changes.*                   | *Add save button to commit changes to the database and return user to main menu.*                 | *Conspicuous save button so user doesn’t forget.*       |

**Scenario 8**

| User task                                          | System need                                             | Design consideration                                                              |
|----------------------------------------------------|---------------------------------------------------------|-----------------------------------------------------------------------------------|
| *Gather information for updated recipe.*    |                       |  |
| *Locate recipe tab on home page.*        | *Create recipe tab on home page.*  | *Recipe button should be listed with other optional pages and clearly labeled.*                                    |
| *Locate Latte and update price to -100, causing an error message.* | *Display recipe information and create button to update recipe. Display error message when negative number is entered* | *Clearly indicate the button to update. Click on the current recipe information and input new information to change.*  |
| *Save changes.*                   | *No changes are saved, error message is displayed and user is returned to main menu.*                 | *Message should be clearly visible and labeled.*      |


### Wire Frames

*One wire frame and flow/navigation map for the use case. Start from the menu and follow the actions you designed to the other pages.*

*Annotate the wire frame to show which parts each scenario touches â€” for example, blue triangles numbered by step for Scenario 1 and red squares for Scenario 2.*

[UC7 wire frame](images/Wire_frame_UC_7.png)

### Design Explanation

*How the wire frame satisfies the requirements, and how it addresses the design and usability considerations. Point at specific parts of the design rather than restating the checklist.*

*We started and ended our wireframe from the main menu of our front end, therefore it can be completed start to finish. Each step of the wireframe can be traced from the last, ensuring that it shows the exact flow of a user using our application. We have covered the alternative flows in the wireframe; the only possible alternative would be a duplicate name. The interfaces are balanced and nothing looks off center and everything is symmetric. The interfaces have enough white space on the bottom and between each button and tab. The field labels and text are aligned. The length of a text field is big enough for both a name and amount of ingredients. We do not have any dropdowns, radio buttons, checkboxes, or steppers, we use textboxes for both of our fields. Every element of the design is useful in both of the scenarios. Our successful wireframe flow is conveyed by blue triangles while our failure information is conveyed by red rectangles. Every form control has a visible label that explains what the user’s action is. All of our buttons have text that explains the meaning of them. Our headings each display one page, the ingredients page is the one displayed in our wireframes. Every interactive element can be reached and operated with a keyboard and mouse. The buttons and tabs are contrasted form the background with different color and text. The design is based off of our original Coffee Maker application and therefore matches the same controls and wordings as the conventions used elsewhere in the application. The design works with arbitrary ingredients, such as tea in our scenario 1 example, not just with the original four.

Individual Portion: The wireframe's design shows the flow user inputs to complete each of the use cases. For use case 2, the button to add a new recipe is clearly labeled and the next frame shows what page the button leads to. Then depending on the validity of the user input the page will either display and error or allow you add the recipe to the database. If you try to add a recipe with invalid fields the user will be returned to the main menu. The design of the wireframe is to maximum readability by reducing the number of elements so the few important elements stand out. For use case 4, the display of recipes can be directed edited to change the recipes. This avoid having to load an entire page just to edit a recipe, when all the information is already provided in the recipe page.*

### Requirements Assumptions

Interpretations you made where the requirements did not provide specific information. For example, a maximum number of units of an ingredient that can go in a recipe. If you made none, say so.

*We made an assumption that the names of the Ingredient entered will only contain alphabetical characters, and would not contain numbers.*

---

## Static Pages

*Which components in `coffee_maker_frontend/src/components/` implement this design, which routes were added to `App.jsx`, and which menu entries were added to `HeaderComponent.jsx`.*

| Use case | Component | Route | In the menu? |
| -------- | --------- | ----- | ------------ |
| UC5 | | | |
| UC7 | '../component/IngredientsComponent' | '../ingredients' | 'Ingredients' entry next to 'Inventory' and 'Recipe' menu buttons |

We assumed that a user can only edit one recipe at a time, and that removing all ingredients during an edit is treated the same as the "No Ingredients" alternative flow from Add Recipe.

### Static Pages

| Use case | Component | Route | In the menu? |
|---|---|---|---|
| UC2 | `../component/RecipeComponent` | `../recipes/` | "Add Recipe" entry accessible from the Recipe menu |
