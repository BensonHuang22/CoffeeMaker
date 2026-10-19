import { useState } from 'react'
import { createIngredient } from '../services/IngredientService'


const AddIngredientComponent = () => {

    const [name, setName] = useState("")
    const [amount, setAmount] = useState("")

    const [successMessage, setSuccessMessage] = useState("")

    const [errors, setErrors] = useState({
        general: "",
        name: "",
        amount: ""
    })

    function validateForm() {

        let valid = true

        const errorsCopy = {
            general: "",
            name: "",
            amount: ""
        }

        if (!name.trim()) {
            errorsCopy.name = "Name is required."
            valid = false
        }

        const amountNumber = Number(amount)

        if (
            !/^\d+$/.test(amount) ||
            !Number.isSafeInteger(amountNumber) ||
            amountNumber <= 0
        ) {
            errorsCopy.amount =
                "Amount must be a positive integer."

            valid = false
        }

        setErrors(errorsCopy)

        return valid
    }

    function saveIngredient(e) {

        e.preventDefault()

        if (validateForm()) {

            const ingredient = {
                name: name.trim(),
                amount: Number(amount)
            }

            createIngredient(ingredient)
                .then((response) => {

                    console.log(response.data)

                    setName("")
                    setAmount("")

                    setSuccessMessage("Ingredient added successfully.")

                    setErrors({
                        general: "",
                        name: "",
                        amount: ""
                    })

                })
                .catch((error) => {

                    console.error(error)

                    const errorsCopy = {
                        general: "",
                        name: "",
                        amount: ""
                    }

                    if (error.response?.status === 409) {
                        errorsCopy.general =
                            "An ingredient with that name already exists."
                    } else {
                        errorsCopy.general =
                            "Unable to add ingredient."
                    }

                    setErrors(errorsCopy)
                })
        }
    }

    return (
        <div className="container">
            <br /><br />

            <div className="row">
                <div className="card col-md-6 offset-md-3">

                    <h2 className="text-center">
                        Add Ingredient
                    </h2>

                    <div className="card-body">

                        {errors.general && (
                            <div className="p-3 mb-2 bg-danger text-white">
                                {errors.general}
                            </div>
                        )}

                        {successMessage && (
                            <div className="alert alert-success">
                                {successMessage}
                            </div>
                        )}

                        <form onSubmit={saveIngredient}>

                            <div className="form-group mb-2">

                                <label className="form-label">
                                    Ingredient Name
                                </label>

                                <input
                                    type="text"
                                    placeholder="Enter Ingredient Name"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    className={`form-control ${
                                        errors.name ? "is-invalid" : ""
                                    }`}
                                />

                                {errors.name &&
                                    <div className="invalid-feedback">
                                        {errors.name}
                                    </div>
                                }

                            </div>


                            <div className="form-group mb-2">

                                <label className="form-label">
                                    Initial Amount
                                </label>

                                <input
                                    type="text"
                                    placeholder="Enter Initial Amount"
                                    value={amount}
                                    onChange={(e) => setAmount(e.target.value)}
                                    className={`form-control ${
                                        errors.amount ? "is-invalid" : ""
                                    }`}
                                />

                                {errors.amount &&
                                    <div className="invalid-feedback">
                                        {errors.amount}
                                    </div>
                                }

                            </div>


                            <button
                                type="submit"
                                className="btn btn-success"
                            >
                                Add Ingredient
                            </button>

                        </form>

                    </div>
                </div>
            </div>
        </div>
    )
}

export default AddIngredientComponent

