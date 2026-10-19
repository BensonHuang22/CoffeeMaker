import axios from "axios"
import { API_BASE_URL } from "./ApiConfig"

const REST_API_BASE_URL = API_BASE_URL + "/api/ingredients"

/** POST Ingredient - creates a new ingredient */
export const createIngredient = (ingredient) =>
    axios.post(REST_API_BASE_URL, ingredient)