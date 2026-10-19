import React, { useEffect, useState } from 'react'
import { listRecipes, deleteRecipe } from '../services/RecipesService'
import { useNavigate } from 'react-router-dom'

/** Lists all the recipes and provide the option to create a new recipe
 * and delete an existing recipe.
 */
const ListRecipesComponent = () => {

    const [recipes, setRecipes] = useState([])
    const [selectedRecipeId, setSelectedRecipeId] = useState(null)

    const navigator = useNavigate();

    useEffect(() => {
        getAllRecipes()
    }, [])

    function getAllRecipes() {
        listRecipes().then((response) => {
            setRecipes(response.data)
        }).catch(error => {
            console.error(error)
        })
    }

    /** Returns the recipe's ingredients in alphabetical order by name. */
    function sortedIngredients(recipe) {
        return [...(recipe.ingredients ?? [])].sort((a, b) =>
            a.name.localeCompare(b.name, undefined, { sensitivity: 'base' })
        )
    }

    function addNewRecipe() {
        navigator('/add-recipe')
    }

    function editRecipe() {
        if (!selectedRecipeId) {
            return
        }

        navigator('/edit-recipe', { state: { recipeId: selectedRecipeId } })
    }

    function removeRecipe(id) {
        console.log(id)

        deleteRecipe(id).then((response) => {
            getAllRecipes()
            if (selectedRecipeId === id) {
                setSelectedRecipeId(null)
            }
        }).catch(error => {
            console.error(error)
        })
    }

    return (
        <div className="container">
            <h2 className="text-center">List of Recipes</h2>
            <button className="btn btn-primary mb-2" onClick={ addNewRecipe }>Add Recipe</button>
            {
                recipes.map(recipe =>
                <table className="table table-striped table-bordered mb-4" key={recipe.id}
                    style={{tableLayout: 'fixed', width: '100%'}}
                >
                    <thead>
                        <tr>
                            <th style={{width: '20%'}}>Recipe Name</th>
                            <th style={{width: '12%'}}>Recipe Price</th>
                            {
                                sortedIngredients(recipe).map((ingredient, index) =>
                                <th key={ingredient.id ?? index}>Amount {ingredient.name}</th>)
                            }
                            <th style={{width: '20%'}}>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>{recipe.name}</td>
                            <td>{recipe.price}</td>
                            {
                                sortedIngredients(recipe).map((ingredient, index) =>
                                <td key={ingredient.id ?? index}>{ingredient.amount}</td>)
                            }
                            <td>
                                <button className="btn btn-danger" onClick={() => removeRecipe(recipe.id)}
                                    style={{marginLeft: '10px'}}
                                >Delete</button>
                            </td>
                        </tr>
                    </tbody>
                </table>)
            }
        </div>
    )

}

export default ListRecipesComponent