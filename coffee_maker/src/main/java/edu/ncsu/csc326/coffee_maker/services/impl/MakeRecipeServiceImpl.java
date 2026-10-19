package edu.ncsu.csc326.coffee_maker.services.impl;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.entity.Recipe;
import edu.ncsu.csc326.coffee_maker.mapper.InventoryMapper;
import edu.ncsu.csc326.coffee_maker.mapper.RecipeMapper;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.services.MakeRecipeService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static edu.ncsu.csc326.coffee_maker.mapper.IngredientMapper.mapToIngredient;

/**
 * Implementation of the MakeRecipeService interface.
 *
 * @author CSC 326 Course Staff
 */

@Service
@Transactional
public class MakeRecipeServiceImpl implements MakeRecipeService {
	
	/** Connection to the repository to work with the DAO + database */
	@Autowired
	private InventoryRepository inventoryRepository;
    @Autowired
    private InventoryService inventoryService;

	/**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param inventoryDto
     * 			  current inventory
     * @param recipeDto
     *            recipe to make
     * @return updated inventory 
     */
	@Override
	@Transactional
	public boolean makeRecipe(InventoryDto inventoryDto, RecipeDto recipeDto) {

		Inventory inventory = inventoryRepository.findAll()
				.stream()
				.findFirst()
				.orElse(null);

		if (inventory == null) {
			return false;
		}

		// Check ALL ingredients before changing anything
		for (Ingredient recipeIngredient : recipeDto.getIngredients()) {
			boolean hasEnough = false;

			for (Ingredient inventoryIngredient : inventory.getIngredients()) {
				if (recipeIngredient.getName().equals(inventoryIngredient.getName())) {

					if (inventoryIngredient.getAmount() >= recipeIngredient.getAmount()) {
						hasEnough = true;
					}
					break;
				}
			}

			if (!hasEnough) {
				return false;
			}
		}

		// All ingredients are valid, so subtract them
		for (Ingredient recipeIngredient : recipeDto.getIngredients()) {

			for (Ingredient inventoryIngredient : inventory.getIngredients()) {

				if (recipeIngredient.getName().equals(inventoryIngredient.getName())) {

					inventoryIngredient.setAmount(inventoryIngredient.getAmount() - recipeIngredient.getAmount());
					break;
				}
			}
		}

		inventoryRepository.save(inventory);

		return true;
	}
}