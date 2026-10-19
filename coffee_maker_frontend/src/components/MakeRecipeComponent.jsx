import React, { useEffect, useState } from 'react'
import { listRecipes } from '../services/RecipesService'
import { makeRecipe } from '../services/MakeRecipeService'

/** Provides functionality to make a recipe, pay for it, and receive change.*/
const MakeRecipeComponent  = () => {

    const [recipes, setRecipes] = useState([])
    const [amtPaid, setAmtPaid] = useState([])
    const [change, setChange] = useState(null)

    const [errors, setErrors] = useState( {
        general: ""
    })

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

    function craftRecipe(name) {
        setChange(null)

        setErrors({
            general: ""
        })

        if (validateForm()) {
            makeRecipe(name, Number(amtPaid))
                .then((response) => {
                    setChange(response.data)
                    setAmtPaid("")
                    setErrors({
                        general: ""
                    })
                })
                .catch((error) => {
                    console.error(error)

                    const errorsCopy = {
                        general: ""
                    }

                    if (error.response?.status === 409) {
                        errorsCopy.general = "Insufficient payment."
                    } else if (error.response?.status === 400) {
                        errorsCopy.general = "Insufficient inventory."
                    } else {
                        errorsCopy.general = "Unable to make beverage."
                    }

                    setErrors(errorsCopy)
                })
        }
    }

    function validateForm() {
        let valid = true
        const errorsCopy = {
            general: ""
        }

        const payment = String(amtPaid).trim()
        const paymentNumber = Number(payment)

        if (
            payment === "" ||
            !/^\d+$/.test(payment) ||
            !Number.isSafeInteger(paymentNumber) ||
            paymentNumber <= 0
        ) {
            errorsCopy.general =
                "Payment must be a non-negative integer."
            valid = false
        }

        setErrors(errorsCopy)
        return valid
    }

    function getGeneralErrors() {
        if (errors.general) {
            return <div className="p-3 mb-2 bg-danger text-white">{errors.general}</div>
        }
    }

    return (
        <div className="container">
            <h2 className="text-center">List of Recipes</h2>
            { getGeneralErrors() }
            {change !== null && (
                <div className="alert alert-success">
                    Beverage is ready! Your change is ${change}.
                </div>
            )}
            <br /><br />
            <div className="card-body">
                <form>
                    <div className="form-group mb-2">
                        <label className="form-label">Amount Paid</label>
                        <input
                            type="text"
                            name="amtPaid"
                            placeholder="How much are you paying?"
                            value={amtPaid}
                            onChange={(e) => setAmtPaid(e.target.value)}
                            className={`form-control ${errors.general ? "is-invalid":""}`}
                        >
                        </input>
                        <label className="form-label">Change: {change}</label>
                    </div>
                </form>
            </div>

            <table className="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Recipe Name</th>
                        <th>Recipe Price</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        recipes.map(recipe => 
                        <tr key={recipe.id}>
                            <td>{recipe.name}</td>
                            <td>{recipe.price}</td>
                            <td>
                                <button
                                    className="btn btn-primary"
                                    onClick={() => craftRecipe(recipe.name)}
                                >Make Recipe</button>
                            </td>
                        </tr>)
                    }
                </tbody>
            </table>
        </div>
    )

}

export default MakeRecipeComponent