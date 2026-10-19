UML Diagram Differences - 
The AI’s UML diagram was much smaller than the example UML diagram. Aside from the lack of certain classes, a major difference was how the AI created two separate Ingredient classes, one for Inventory and one for Recipe. This would make it harder for recipes and inventory to interact with each other since their ingredients classes are slightly different. Additionally, RecipeServiceImpl had a field for RecipeRepository where it stored  the recipe data which was different from my UML diagram. I’m going to keep the current UML to keep things simple. 

Sequence Diagram -
I think I’ll be going with the AI’s version of the sequence diagram because it’s much more detailed. It has loops and conditional statements to check for valid ingredients and if an ingredient already exists before making a new ingredient for a recipe. Although the return to the user is incorrect as it should be a response entity, that can be easily fixed so overall the AI’s diagram is better.

Endpoint Design -
The AI created additional endpoints for functionality such as listing recipes, getting a specific recipe, and listing available ingredients. I can incorporate those additional API endpoints into my design, but for creating a new recipe, we had very similar ideas so I won’t be changing that. AI is very good for creating a good breadth of different calls that I didn’t think of. 


