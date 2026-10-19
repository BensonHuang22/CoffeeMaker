## Static Design - UML Diagram



I added a recipe class that uses a list of ingredients to store it's recipe. The functions in each of the recipe classes remain the same, but the underlying implementation will have to change due to how ingredients work. I changed the necessary Dto, Service, and Controller classes for recipe to suit the different fields. 


## Endpoint Design

POST /recipes/

**Name of function**: RecipeController.createRecipe(recipeDto:RecipeDto)

**Brief Description**:
This API endpoint creates a recipe if one doesn't already exist with that name .

**Body Parameters**:
recipeDto: Recipe object

{\
	   // start of Recipe object\
     // ingredients are not limited so temporarily labeled as 1, 2\
	  'name': ‘Coffee’,\
	  'price': ‘5’, \
	  'ingredient1': ‘1’,\
    'ingredient2': ‘3’,\
}

**HTTP Response Status Codes**:
| Status Code | Description |
| :--- | :--- |
| `200` | OK |
| `404` | Name of recipe already exists

Status: 200 OK\
{\
    ‘Id’: '1',\
	  'name': ‘Coffee’,\
	  'price': ‘5’, \
	  'ingredient1': ‘1’,\
    'ingredient2': ‘3’,\
}

Status: 404
{\
    Invalid name error message \
}


PUT /recipes/update

**Name of function**: RecipeController.updateRecipe(recipeDto:RecipeDto)

**Brief Description**:
This API endpoint updates the recipe with the same name.

**Body Parameters**:
recipeDto: Recipe object

{\
	   // start of Recipe object\
     // ingredients are not limited so temporarily labeled as 1, 2\
	  'name': ‘Coffee’,\
	  'price': ‘5’, \
	  'ingredient1': ‘1’,\
    'ingredient2': ‘3’,\
}

**HTTP Response Status Codes**:
| Status Code | Description |
| :--- | :--- |
| `200` | OK |
| `404` | Name doesn't match existing recipe |

Status: 200 OK\
{\
    ‘Id’: '1',\
	  'name': ‘Coffee’,\
	  'price': ‘5’, \
	  'ingredient1': ‘1’,\
    'ingredient2': ‘3’,\
}

Status: 404
{\
    Invalid name error message \
}















---------------------------------------------------
Sequence Diagram
---------------------------------------------------
This sequence diagram models the createRecipe functionality. The API call already exists, but we have to change the flow of control to make up for the fact that it now uses a list of ingredients. The RecipeService now calls the IngredientService to get an ingredient object to put in it's list. 
In the scenario we have 
1) A user submitting information for a new recipe
2) The program checks if a recipe with that name already exists. If it does it'll return an error response.
4) If name is unique create the recipe by using IngredientService to create the Ingredient objects.
   



