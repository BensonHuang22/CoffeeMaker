import { useState } from 'react'
import { createRecipe } from '../services/RecipesService'
import { useNavigate } from 'react-router-dom'

/** Returns true if the value is a whole number of at least 1. */
function isPositiveInteger(value) {
    return /^\d+$/.test(String(value).trim()) && parseInt(value) >= 1
}

/** Form to create a new recipe. */
const RecipeComponent = () => {

    const [name, setName] = useState("")
    const [price, setPrice] = useState("")
    const [ingredients, setIngredients] = useState([])

    const navigator = useNavigate()
    const [errors, setErrors] = useState({
        general: "",
        name: "",
        price: "",
        ingredients: "",
    })

    function addIngredient() {
        setIngredients([...ingredients, { name: "", amount: "" }])
    }

    function updateIngredient(index, field, value) {
        setIngredients(ingredients.map((ingredient, i) =>
            i === index ? { ...ingredient, [field]: value } : ingredient
        ))
    }

    function removeIngredient(index) {
        setIngredients(ingredients.filter((_, i) => i !== index))
    }

    function saveRecipe(e) {
        e.preventDefault();

        if (validateForm()) {
            const recipe = {
                name: name.trim(),
                price: parseInt(price),
                ingredients: ingredients.map(ingredient => ({
                    name: ingredient.name.trim(),
                    amount: parseInt(ingredient.amount)
                }))
            }
            console.log(recipe)

            createRecipe(recipe).then((response) => {
                console.log(response.data)
                navigator("/recipes")
            }).catch(error => {
                console.error(error)
                const errorsCopy = {... errors, general: ""}
                const status = error.response?.status
                if (status === 507) {
                    errorsCopy.general = "Recipe list is at capacity."
                } else if (status === 409) {
                    errorsCopy.general = "Duplicate recipe name."
                } else {
                    errorsCopy.general = "Could not save recipe."
                }

                setErrors(errorsCopy)
            })
        }
    }

    function validateForm() {
        let valid = true

        const errorsCopy = {... errors, general: ""}

        if (name.trim()) {
            errorsCopy.name = ""
        } else {
            errorsCopy.name = "Name is required."
            valid = false
        }

        if (isPositiveInteger(price)) {
            errorsCopy.price = ""
        } else {
            errorsCopy.price = "Price must be a positive integer."
            valid = false
        }

        const ingredientNames = ingredients.map(ingredient => ingredient.name.trim().toLowerCase())
        if (ingredients.length === 0) {
            errorsCopy.ingredients = "Recipe must have at least one ingredient."
            valid = false
        } else if (ingredientNames.some(ingredientName => !ingredientName)) {
            errorsCopy.ingredients = "Every ingredient needs a name."
            valid = false
        } else if (new Set(ingredientNames).size !== ingredientNames.length) {
            errorsCopy.ingredients = "Each ingredient may only appear once."
            valid = false
        } else if (ingredients.some(ingredient => !isPositiveInteger(ingredient.amount))) {
            errorsCopy.ingredients = "Ingredient amounts must be positive integers."
            valid = false
        } else {
            errorsCopy.ingredients = ""
        }

        setErrors(errorsCopy)
        return valid
    }

    function getGeneralErrors() {
        if (errors.general) {
            return <div className="alert alert-danger">{errors.general}</div>
        }
    }

    return (
        <div className="container">
            <br /><br />
            <div className="row">
                <div className="card col-md-8 offset-md-2">
                    <h2 className="text-center">Add Recipe</h2>

                    <div className="card-body">
                        { getGeneralErrors() }
                        <form>
                            <div className="form-group mb-2">
                                <label className="form-label">Recipe Name</label>
                                <input
                                    type="text"
                                    name="recipeName"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    className={`form-control ${errors.name ? "is-invalid":""}`}
                                />
                                {errors.name && <div className="invalid-feedback d-block">{errors.name}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Recipe Price</label>
                                <input
                                    type="number"
                                    min="1"
                                    name="recipePrice"
                                    value={price}
                                    onChange={(e) => setPrice(e.target.value)}
                                    className={`form-control ${errors.price ? "is-invalid":""}`}
                                />
                                {errors.price && <div className="invalid-feedback d-block">{errors.price}</div>}
                            </div>

                            <div className="mb-3">
                                <h4>Ingredients</h4>
                                {
                                    ingredients.map((ingredient, index) =>
                                    <div className="row mb-2 align-items-end" key={index}>
                                        <div className="col-md-5">
                                            <label className="form-label">Ingredient Name</label>
                                            <input
                                                type="text"
                                                name="ingredientName"
                                                value={ingredient.name}
                                                onChange={(e) => updateIngredient(index, "name", e.target.value)}
                                                className={`form-control ${errors.ingredients ? "is-invalid":""}`}
                                            />
                                        </div>
                                        <div className="col-md-4">
                                            <label className="form-label">Amount</label>
                                            <input
                                                type="number"
                                                min="1"
                                                name="ingredientAmount"
                                                value={ingredient.amount}
                                                onChange={(e) => updateIngredient(index, "amount", e.target.value)}
                                                className={`form-control ${errors.ingredients ? "is-invalid":""}`}
                                            />
                                        </div>
                                        <div className="col-md-3">
                                            <button type="button" className="btn btn-outline-danger" onClick={() => removeIngredient(index)}>
                                                Remove
                                            </button>
                                        </div>
                                    </div>)
                                }
                                {errors.ingredients && <div className="invalid-feedback d-block mb-2">{errors.ingredients}</div>}
                                <button type="button" className="btn btn-outline-primary" onClick={addIngredient}>
                                    Add Ingredient
                                </button>
                            </div>

                            <button type="button" className="btn btn-success me-2" onClick={(e) => saveRecipe(e)}>Add Recipe</button>
                            <button type="button" className="btn btn-secondary" onClick={() => navigator("/recipes")}>Cancel</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )

}

export default RecipeComponent
