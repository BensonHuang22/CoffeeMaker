package edu.ncsu.csc326.coffee_maker.services.impl;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.mapper.IngredientMapper;
import edu.ncsu.csc326.coffee_maker.repositories.IngredientRepository;
import edu.ncsu.csc326.coffee_maker.repositories.InventoryRepository;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Creates an Ingredient with the given information.
     * @param ingredientDto ingredient to create
     * @return created ingredient
     */
    @Transactional
    public IngredientDto createIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = IngredientMapper.mapToIngredient(ingredientDto);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        Inventory inventory = inventoryRepository.findAll().stream().findFirst().orElseGet(Inventory::new);

        inventory.getIngredients().add(savedIngredient);
        inventoryRepository.save(inventory);

        return IngredientMapper.mapToIngredientDto(savedIngredient);
    }


    /**
     * Returns the ingredient with the given id.
     * @param ingredientId ingredient's id
     * @return the ingredient with the given id
     * @throws ResourceNotFoundException if the ingredient doesn't exist
     */
    @Override
    public IngredientDto getIngredientById(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ingredient does not exist with id " + ingredientId));

        return IngredientMapper.mapToIngredientDto(ingredient);
    }

    /**
     * Returns a list of all the ingredients
     * @return all the ingredients
     */
    @Override
    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream().map(IngredientMapper::mapToIngredientDto).collect(Collectors.toList());
    }

    /**
     * Deletes the ingredient with the given id
     * @param ingredientId ingredient's id
     * @throws ResourceNotFoundException if the ingredient doesn't exist
     */
    @Override
    public void deleteIngredient(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(
                () -> new ResourceNotFoundException("Ingredient does not exist with id " + ingredientId)
        );

        ingredientRepository.delete(ingredient);
    }

    /**
     * Deletes all ingredients
     */
    @Override
    public void deleteAllIngredients() {
        ingredientRepository.deleteAll();
    }
}
