
package edu.ncsu.csc326.coffee_maker.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.mapper.InventoryMapper;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


/**
 * Implementation of the InventoryService interface.
 *
 * @author CSC 326 Course Staff
 */
@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
	
	/** Connection to the repository to work with the DAO + database */
	@Autowired
	private InventoryRepository inventoryRepository;

	/**
	 * Creates the inventory.
	 * @param inventoryDto inventory to create
	 * @return updated inventory after creation
	 */
	@Override
	public InventoryDto createInventory(InventoryDto inventoryDto) {
		Inventory inventory = InventoryMapper.mapToInventory(inventoryDto);
		Inventory savedInventory = inventoryRepository.save(inventory);
		return InventoryMapper.mapToInventoryDto(savedInventory);
	}

	/**
	 * Returns the single Inventory row, if one has been created.  Inventory is
	 * treated as a singleton, so it is located by reading the only row rather than
	 * by a hard-coded id.  Assuming a particular id would tie this method's
	 * correctness to the database's AUTO_INCREMENT counter, which keeps climbing
	 * after deletes and is not reset when a transaction rolls back.0
	 * @return the single Inventory, or an empty Optional if none exists yet
	 */
	private Optional<Inventory> findSingleInventory() {
		return inventoryRepository.findAll().stream().findFirst();
	}

	/**
	 * Returns the single inventory.
	 * @return the single inventory
	 */
	@Override
	public InventoryDto getInventory() {
		Optional<Inventory> inventory = findSingleInventory();
		if (inventory.isEmpty()) {
			return createInventory(new InventoryDto());
		}
		return InventoryMapper.mapToInventoryDto(inventory.get());
	}

	/**
	 * Adds the submitted amounts to existing inventory ingredients.
	 *
	 * @param inventoryDto ingredient amounts to add
	 * @return the updated inventory
	 * @throws ResourceNotFoundException if the inventory or ingredient is missing
	 * @throws ResponseStatusException if an addition is invalid
	 */
	@Override
	public InventoryDto updateInventory(InventoryDto inventoryDto) {
		if (inventoryDto == null || inventoryDto.getIngredients() == null) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "An ingredient list is required.");
		}

		Inventory inventory = findSingleInventory().orElseThrow(
				() -> new ResourceNotFoundException("Inventory does not exist."));

		Map<String, Ingredient> ingredientsByType = new HashMap<>();

		for (Ingredient ingredient : inventory.getIngredients()) {
			ingredientsByType.put(ingredient.getName(), ingredient);
		}

		Set<String> seenTypes = new HashSet<>();
		Map<String, Integer> updatedAmounts = new HashMap<>();

		// Validate every entry before changing any inventory quantities.
		for (IngredientDto addition : inventoryDto.getIngredients()) {
			if (addition == null
					|| addition.getName() == null
					|| addition.getName().isBlank()) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "An ingredient name is required.");
			}

			String ingredientType = addition.getName();
			Integer amountToAdd = addition.getAmount();

			if (amountToAdd == null || amountToAdd <= 0) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"Ingredient amounts must be positive integers.");
			}

			if (!seenTypes.add(ingredientType)) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"An ingredient may only appear once per update.");
			}

			Ingredient existingIngredient = ingredientsByType.get(ingredientType);

			if (existingIngredient == null) {
				throw new ResourceNotFoundException(
						"Ingredient does not exist in inventory: " + ingredientType);
			}

			int updatedAmount;

			try {
				updatedAmount = Math.addExact(
						existingIngredient.getAmount(), amountToAdd);
			} catch (ArithmeticException e) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"The resulting ingredient amount is too large.");
			}

			updatedAmounts.put(ingredientType, updatedAmount);
		}

		// Every entry is valid, so apply the calculated totals.
		for (Map.Entry<String, Integer> entry : updatedAmounts.entrySet()) {
			ingredientsByType.get(entry.getKey()).setAmount(entry.getValue());
		}

		Inventory savedInventory = inventoryRepository.save(inventory);
		return InventoryMapper.mapToInventoryDto(savedInventory);
	}


}
