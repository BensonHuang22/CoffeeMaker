import { useEffect, useState } from 'react'
import { getInventory, updateInventory } from '../services/InventoryService'

/**
 * Displays current inventory and a form for adding ingredient quantities.
 *
 * @returns {import('react').ReactElement} the inventory page
 */
const InventoryComponent = () => {

    /** Current ingredients and quantities returned by the backend. */
    const [ingredients, setIngredients] = useState([])

    /** User addition amounts stored as strings, keyed by ingredient ID. */
    const [additions, setAdditions] = useState({})

    /** Confirmation message displayed after a successful inventory update. */
    const [successMessage, setSuccessMessage] = useState("")

    /** Whether an update request is in progress */
    const [isSaving, setIsSaving] = useState(false)

    /** Whether the initial inventory request is still loading. */
    const [isLoading, setIsLoading] = useState(true)

    /** General errors and ingredient-specific validation errors keyed by ID. */
    const [errors, setErrors] = useState({
        general: "",
        ingredients: {}
    })

    /**
     * Loads inventory when the component mounts and initializes blank addition
     * inputs. Displays a general error if the request fails.
     */
    useEffect(() => {
        getInventory()
            .then((response) => {
                const currentIngredients = response.data.ingredients

                setIngredients(currentIngredients)

                const emptyAdditions = {}

                currentIngredients.forEach((ingredient) => {
                    emptyAdditions[ingredient.id] = ""
                })

                setAdditions(emptyAdditions)
            })
            .catch(() => {
                setErrors({
                    general: "Unable to load inventory. Please try again.",
                    ingredients: {}
                })
            })
            .finally(() => {
                setIsLoading(false)
            })
    }, [])

    /**
     * Validates and submits nonblank ingredient additions to the backend.
     * On success, displays updated quantities, clears inputs, and shows confirmation.
     * On failure, preserves the inputs and displays an error.
     *
     * @param {import('react').FormEvent<HTMLFormElement>} event form submission event
     * @returns {Promise<void>} resolves after submission handling finishes
     */
    async function modifyInventory(event) {
        event.preventDefault()

        if (isSaving) {
            return
        }

        setSuccessMessage("")

        if (!validateForm()) {
            return
        }

        const inventory = {
            ingredients: ingredients
                .filter((ingredient) =>
                    (additions[ingredient.id] ?? "").trim() !== ""
                )
                .map((ingredient) => ({
                    name: ingredient.name,
                    amount: Number(additions[ingredient.id].trim())
                }))
        }

        setIsSaving(true)

        try {
            const response = await updateInventory(inventory)
            const updatedIngredients = response.data.ingredients

            setIngredients(updatedIngredients)

            const emptyAdditions = {}

            updatedIngredients.forEach((ingredient) => {
                emptyAdditions[ingredient.id] = ""
            })

            setAdditions(emptyAdditions)
            setErrors({ general: "", ingredients: {} })
            setSuccessMessage("Inventory updated successfully.")
        } catch (error) {
            let message

            if (!error.response) {
                message =
                    "Unable to confirm the update. Check your connection and reload inventory before trying again."
            } else if (error.response.status === 400) {
                message =
                    "The update was rejected. Check the amounts; a resulting inventory quantity may be too large."
            } else if (error.response.status === 404) {
                message =
                    "An ingredient or the inventory could not be found. Reload the page before trying again."
            } else {
                message =
                    "Unable to confirm the update. Reload inventory before trying again."
            }

            setErrors({
                general: message,
                ingredients: {}
            })
        } finally {
            setIsSaving(false)
        }
    }
    /**
     * Updates the value of an ingredient's addition input.
     *
     * @param {number} ingredientId ID of the ingredient
     * @param {string} value New value for the addition
     */
    function handleAdditionChange(ingredientId, value) {
        setSuccessMessage("")

        setAdditions((previous) => ({
            ...previous,
            [ingredientId]: value
        }))
    }

    /**
     * Validates all addition inputs and updates the displayed errors.
     * Blank inputs are skipped. At least one addition is required, and every
     * entered amount must be a positive whole number within Java's integer range.
     *
     * @returns {boolean} true if all inputs are valid; otherwise false
     */
    function validateForm() {
        const nextErrors = {
            general: "",
            ingredients: {}
        }

        let hasAddition = false

        for (const ingredient of ingredients) {
            const value = (additions[ingredient.id] ?? "").trim()

            if (value === "") {
                continue
            }

            hasAddition = true

            const amount = Number(value)

            if (
                !/^\d+$/.test(value) ||
                !Number.isSafeInteger(amount) ||
                amount <= 0 ||
                amount > 2147483647
            ) {
                nextErrors.ingredients[ingredient.id] =
                    "Enter a positive whole number no greater than 2147483647."
            }
        }

        if (!hasAddition) {
            nextErrors.general = "Enter an amount to add for at least one ingredient."
        }

        setErrors(nextErrors)

        return (
            nextErrors.general === "" &&
            Object.keys(nextErrors.ingredients).length === 0
        )
    }

    /**
     * Creates the general error message display when an error exists.
     *
     * @returns {import('react').ReactElement|undefined} error display or undefined
     */
    function getGeneralErrors() {
        if (errors.general) {
            return <div className="p-3 mb-2 bg-danger text-white">{errors.general}</div>
        }
    }

    return (
        <div className="container">
            <br /><br />
            <div className="row">
                <div className="card col-md-6 offset-md-3">
                    <h2 className="text-center">Inventory</h2>

                    <div className="card-body">
                        { getGeneralErrors() }

                        {isLoading && (
                            <p role="status">Loading inventory...</p>
                        )}

                        {successMessage && (
                            <div className="alert alert-success" role="status">
                                {successMessage}
                            </div>
                        )}

                        <form onSubmit={modifyInventory}>
                            {ingredients.map((ingredient) => (
                                <div className="form-group mb-3" key={ingredient.id}>
                                    <label
                                        className="form-label"
                                        htmlFor={`addition-${ingredient.id}`}
                                    >
                                        {ingredient.name} — Current amount: {ingredient.amount}
                                    </label>

                                    <input
                                        id={`addition-${ingredient.id}`}
                                        type="number"
                                        min="1"
                                        max="2147483647"
                                        step="1"
                                        disabled={isSaving}
                                        inputMode="numeric"
                                        placeholder="Enter amount to add"
                                        value={additions[ingredient.id] ?? ""}
                                        onChange={(event) =>
                                            handleAdditionChange(
                                                ingredient.id,
                                                event.target.value
                                            )
                                        }
                                        className={`form-control ${
                                            errors.ingredients[ingredient.id]
                                                ? "is-invalid"
                                                : ""
                                        }`}
                                        aria-invalid={
                                            Boolean(errors.ingredients[ingredient.id])
                                        }
                                        aria-describedby={
                                            errors.ingredients[ingredient.id]
                                                ? `error-${ingredient.id}`
                                                : undefined
                                        }
                                    />

                                    {errors.ingredients[ingredient.id] && (
                                        <div
                                            id={`error-${ingredient.id}`}
                                            className="invalid-feedback"
                                        >
                                            {errors.ingredients[ingredient.id]}
                                        </div>
                                    )}
                                </div>
                            ))}

                            {!isLoading && !errors.general && ingredients.length === 0 && (
                                <p>No ingredients are available to update.</p>
                            )}

                            <button
                                type="submit"
                                className="btn btn-success"
                                disabled={isLoading || isSaving || ingredients.length === 0}
                            >
                                {isSaving ? "Updating..." : "Update Inventory"}
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default InventoryComponent