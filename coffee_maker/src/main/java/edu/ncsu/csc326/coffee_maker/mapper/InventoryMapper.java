package edu.ncsu.csc326.coffee_maker.mapper;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;

/**
 * Converts between InventoryDto and Inventory entity.
 *
 * @author CSC 326 Course Staff
 */
public class InventoryMapper {

	/**
	 * Converts an Inventory entity to InventoryDto
	 * @param inventory Inventory to convert
	 * @return InventoryDto object
	 */
	public static InventoryDto mapToInventoryDto(Inventory inventory) {
		InventoryDto inventoryDto = new InventoryDto();
		inventoryDto.setId(inventory.getId());

		for (Ingredient ingredient : inventory.getIngredients()) {
			inventoryDto.getIngredients().add(
					IngredientMapper.mapToIngredientDto(ingredient));
		}

		return inventoryDto;
				
	}
	
	/**
	 * Converts an InventoryDto to an Inventory entity
	 * @param inventoryDto InventoryDto to convert
	 * @return Inventory entity
	 */
	public static Inventory mapToInventory(InventoryDto inventoryDto) {
		Inventory inventory = new Inventory();
		inventory.setId(inventoryDto.getId());

		for (IngredientDto ingredientDto : inventoryDto.getIngredients()) {
			inventory.getIngredients().add(
					IngredientMapper.mapToIngredient(ingredientDto));
		}

		return inventory;
	}
}
