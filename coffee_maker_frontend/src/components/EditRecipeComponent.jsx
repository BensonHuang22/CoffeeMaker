import React, { useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { getRecipe, listRecipes, updateRecipe } from '../services/RecipesService'

const EditRecipeComponent = () => {
    const location = useLocation()
    const navigator = useNavigate()
    const initialRecipeId = location.state?.recipeId ?? null

    const [recipes, setRecipes] = useState([])
    const [selectedRecipeId, setSelectedRecipeId] = useState(initialRecipeId)
    const [recipe, setRecipe] = useState({
        id: '',
        name: '',
        price: '',
        ingredients: []
    })
    const [errors, setErrors] = useState({
        general: '',
        name: '',
        price: '',
        ingredients: []
    })

    useEffect(() => {
        listRecipes().then((response) => {
            setRecipes(response.data)
        }).catch((error) => {
            console.error(error)
        })
    }, [])

    useEffect(() => {
        if (!selectedRecipeId) {
            return
        }

        getRecipe(selectedRecipeId).then((response) => {
            const recipeData = response.data
            setRecipe({
                id: recipeData.id,
                name: recipeData.name,
                price: recipeData.price,
                ingredients: (recipeData.ingredients || []).map((ingredient) => ({
                    id: ingredient.id ?? null,
                    name: ingredient.name ?? '',
                    amount: ingredient.amount ?? ''
                }))
            })
            setErrors({ general: '', name: '', price: '', ingredients: [] })
        }).catch((error) => {
            console.error(error)
            setErrors((previous) => ({ ...previous, general: 'Unable to load recipe details.' }))
        })
    }, [selectedRecipeId])

    function selectRecipe(id) {
        setSelectedRecipeId(id)
    }

    function addIngredient() {
        setRecipe((previous) => ({
            ...previous,
            ingredients: [...previous.ingredients, { id: null, name: '', amount: '' }]
        }))
    }

    function removeIngredient(index) {
        setRecipe((previous) => ({
            ...previous,
            ingredients: previous.ingredients.filter((_, ingredientIndex) => ingredientIndex !== index)
        }))
    }

    function updateIngredient(index, field, value) {
        setRecipe((previous) => ({
            ...previous,
            ingredients: previous.ingredients.map((ingredient, ingredientIndex) => {
                if (ingredientIndex !== index) {
                    return ingredient
                }

                return {
                    ...ingredient,
                    [field]: field === 'amount' ? value : value
                }
            })
        }))
    }

    function validateForm() {
        let valid = true
        const errorsCopy = {
            general: '',
            name: '',
            price: '',
            ingredients: recipe.ingredients.map(() => ({ name: '', amount: '' }))
        }

        if (!recipe.name || !recipe.name.trim()) {
            errorsCopy.name = 'Name can\'t be empty.'
            valid = false
        }

        if (recipe.price === '' || Number(recipe.price) <= 0 || !Number.isInteger(Number(recipe.price))) {
            errorsCopy.price = 'Price must be a positive integer.'
            valid = false
        }

        if (!recipe.ingredients || recipe.ingredients.length === 0) {
            errorsCopy.general = 'A recipe must include at least one ingredient.'
            valid = false
        }

        recipe.ingredients.forEach((ingredient, index) => {
            const ingredientError = { name: '', amount: '' }

            if (!ingredient.name || !ingredient.name.trim()) {
                ingredientError.name = 'Ingredient name can\'t be empty.'
                valid = false
            }

            if (ingredient.amount === '' || Number(ingredient.amount) <= 0 || !Number.isInteger(Number(ingredient.amount))) {
                ingredientError.amount = 'Ingredient amount must be a positive integer.'
                valid = false
            }

            errorsCopy.ingredients[index] = ingredientError
        })

        setErrors(errorsCopy)
        return valid
    }

    function saveRecipe(e) {
        e.preventDefault()

        if (!selectedRecipeId || !validateForm()) {
            return
        }

        const payload = {
            id: recipe.id,
            name: recipe.name,
            price: Number(recipe.price),
            ingredients: recipe.ingredients.map((ingredient) => ({
                id: ingredient.id,
                name: ingredient.name,
                amount: Number(ingredient.amount)
            }))
        }

        updateRecipe(selectedRecipeId, payload).then(() => {
            navigator('/recipes')
        }).catch((error) => {
            console.error(error)
            const errorMessage = error.response?.status === 409
                ? 'A recipe with that name already exists.'
                : 'Unable to update the recipe.'
            setErrors((previous) => ({ ...previous, general: errorMessage }))
        })
    }

    function backToList() {
        setSelectedRecipeId(null)
        setRecipe({ id: '', name: '', price: '', ingredients: [] })
        setErrors({ general: '', name: '', price: '', ingredients: [] })
    }

    if (!selectedRecipeId) {
        return (
            <div className="container">
                <h2 className="text-center mt-4">Select a recipe to edit</h2>
                <table className="table table-striped table-bordered mt-4">
                    <thead>
                        <tr>
                            <th>Recipe Name</th>
                            <th>Recipe Price</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {recipes.map((recipeEntry) => (
                            <tr key={recipeEntry.id}>
                                <td>{recipeEntry.name}</td>
                                <td>{recipeEntry.price}</td>
                                <td>
                                    <button className="btn btn-primary" onClick={() => selectRecipe(recipeEntry.id)}>
                                        Edit
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        )
    }

    return (
        <div className="container">
            <br /><br />
            <div className="row">
                <div className="card col-md-8 offset-md-2">
                    <h2 className="text-center">Edit Recipe</h2>
                    <div className="card-body">
                        {errors.general && <div className="alert alert-danger">{errors.general}</div>}
                        <form>
                            <div className="form-group mb-2">
                                <label className="form-label">Recipe Name</label>
                                <input
                                    type="text"
                                    value={recipe.name}
                                    onChange={(event) => setRecipe((previous) => ({ ...previous, name: event.target.value }))}
                                    className={`form-control ${errors.name ? 'is-invalid' : ''}`}
                                />
                                {errors.name && <div className="invalid-feedback d-block">{errors.name}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Recipe Price</label>
                                <input
                                    type="number"
                                    min="1"
                                    value={recipe.price}
                                    onChange={(event) => setRecipe((previous) => ({ ...previous, price: event.target.value }))}
                                    className={`form-control ${errors.price ? 'is-invalid' : ''}`}
                                />
                                {errors.price && <div className="invalid-feedback d-block">{errors.price}</div>}
                            </div>

                            <div className="mb-3">
                                <h4>Ingredients</h4>
                                {recipe.ingredients.map((ingredient, index) => (
                                    <div key={`ingredient-${index}`} className="row mb-2 align-items-end">
                                        <div className="col-md-5">
                                            <label className="form-label">Ingredient Name</label>
                                            <input
                                                type="text"
                                                value={ingredient.name}
                                                onChange={(event) => updateIngredient(index, 'name', event.target.value)}
                                                className={`form-control ${errors.ingredients[index]?.name ? 'is-invalid' : ''}`}
                                            />
                                            {errors.ingredients[index]?.name && <div className="invalid-feedback d-block">{errors.ingredients[index].name}</div>}
                                        </div>
                                        <div className="col-md-4">
                                            <label className="form-label">Amount</label>
                                            <input
                                                type="number"
                                                min="1"
                                                value={ingredient.amount}
                                                onChange={(event) => updateIngredient(index, 'amount', event.target.value)}
                                                className={`form-control ${errors.ingredients[index]?.amount ? 'is-invalid' : ''}`}
                                            />
                                            {errors.ingredients[index]?.amount && <div className="invalid-feedback d-block">{errors.ingredients[index].amount}</div>}
                                        </div>
                                        <div className="col-md-3">
                                            <button type="button" className="btn btn-outline-danger" onClick={() => removeIngredient(index)}>
                                                Remove
                                            </button>
                                        </div>
                                    </div>
                                ))}
                                <button type="button" className="btn btn-outline-primary" onClick={addIngredient}>
                                    Add Ingredient
                                </button>
                            </div>

                            <button type="button" className="btn btn-success me-2" onClick={saveRecipe}>Update Recipe</button>
                            <button type="button" className="btn btn-secondary" onClick={backToList}>Cancel</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default EditRecipeComponent
