package edu.ncsu.csc326.coffee_maker.dto;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

import java.util.LinkedList;
import java.util.List;

/**
 * Used to transfer Recipe data between the client and server.
 * This class will serve as the response in the REST API.
 *
 * @author CSC 326 Course Staff
 */
public class RecipeDto {

	/** Recipe id */
	private Long id;

	/** Recipe name */
	private String name;

	/** Recipe price */
	private Integer price;

	/** Amount of ingredients */
	List<Ingredient> ingredientList;

	/**
	 * Creates a default recipe for the coffee maker.
	 */
	public RecipeDto () {
		this.ingredientList = new LinkedList<Ingredient>();
	}

	/**
	 * Creates a recipe from a name and price
	 *
	 * @param name recipe name
	 * @param price recipe price
	 */
	public RecipeDto(String name, Integer price) {
		super();
		setName(name);
		setPrice(price);
		setIngredientList(new LinkedList<Ingredient>());
	}

	/**
	 * Creates a recipe from all the fields
	 *
	 * @param name recipe name
	 * @param price recipe price
	 * @param ingredients recipe ingredients
	 */
	public RecipeDto(String name, Integer price, List<Ingredient> ingredients) {
		super();
		setName(name);
		setPrice(price);
		setIngredientList(ingredients);
	}

	/**
	 * Creates a recipe from all the fields including id
	 *
	 * @param id recipe id
	 * @param name recipe name
	 * @param price recipe price
	 * @param ingredients recipe ingredients
	 */
	public RecipeDto(Long id, String name, Integer price, List<Ingredient> ingredients) {
		setId(id);
		setName(name);
		setPrice(price);
		setIngredientList(ingredients);
	}

	/**
	 * Gets the full list of ingredients for the recipe
	 *
	 * @return list of ingredients
	 */
	public List<Ingredient> getIngredients() {
		return ingredientList;
	}

	/**
	 * Sets the list of ingredients for the recipe
	 *
	 * @param ingredients list to set the inner field
	 */
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredientList = ingredients;
	}

	/**
	 * Sets the list of ingredients for the recipe
	 *
	 * @param ingredients list to set the inner field
	 */
	public void setIngredientList(List<Ingredient> ingredients) {
		this.ingredientList = ingredients;
	}

	/**
	 * Adds an ingredient to the list
	 *
	 * @param ingredient to add
	 */
	public void addIngredient(Ingredient ingredient) {
		this.ingredientList.add(ingredient);
	}

	/**
	 * Removes an ingredient to the list
	 *
	 * @param ingredient to remove
	 */
	public void removeIngredient(Ingredient ingredient) {
		this.ingredientList.remove(ingredient);
	}

	/**
	 * Get the ID of the Recipe
	 *
	 * @return the ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the ID of the Recipe
	 *
	 * @param id
	 *            the ID
	 */
	public void setId ( final Long id ) {
		this.id = id;
	}


	/**
	 * Returns name of the recipe.
	 *
	 * @return Returns the name.
	 */
	public String getName () {
		return name;
	}

	/**
	 * Sets the recipe name.
	 *
	 * @param name
	 *            The name to set.
	 */
	public void setName ( final String name ) {
		this.name = name;
	}

	/**
	 * Returns the price of the recipe.
	 *
	 * @return Returns the price.
	 */
	public Integer getPrice () {
		return price;
	}

	/**
	 * Sets the recipe price.
	 *
	 * @param price
	 *            The price to set.
	 */
	public void setPrice ( final Integer price ) {
		this.price = price;
	}

}
