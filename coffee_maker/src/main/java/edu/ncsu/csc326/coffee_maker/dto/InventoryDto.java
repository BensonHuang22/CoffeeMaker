package edu.ncsu.csc326.coffee_maker.dto;

import java.util.ArrayList;
import java.util.List;


/**
 * Inventory for the coffee maker.
 *
 * @author CSC 326 Course Staff
 * @author bpcarvaj
 */
public class InventoryDto {

	/** id for inventory entry */
    private Long    id;


	/** Ingredients in the inventory. */
	private List<IngredientDto> ingredients = new ArrayList<>();

	/**
	 * Creates an empty inventory DTO.
	 */
	public InventoryDto() {
	}

	/**
	 * Creates an inventory DTO with an ID and ingredients.
	 *
	 * @param id the inventory ID
	 * @param ingredients the ingredients in the inventory
	 */
	public InventoryDto(Long id, List<IngredientDto> ingredients) {
		this.id = id;
		this.ingredients = ingredients;
	}


	/**
	 * Returns the ID of the entry in the DB
	 *
	 * @return long
	 */
	public Long getId () {
		return id;
	}

	/**
	 * Set the ID of the Inventory (Used by Hibernate)
	 *
	 * @param id
	 *            the ID
	 */
	public void setId ( final Long id ) {
		this.id = id;
	}

	/**
	 * Gets the ingredients.
	 *
	 * @return the ingredients in the inventory
	 */
	public List<IngredientDto> getIngredients() {
		return this.ingredients;
	}

	/**
	 * Sets the ingredients.
	 *
	 * @param ingredients the ingredients in the inventory
	 */
	public void setIngredients(List<IngredientDto> ingredients) {
		this.ingredients = ingredients;
	}


    
}
