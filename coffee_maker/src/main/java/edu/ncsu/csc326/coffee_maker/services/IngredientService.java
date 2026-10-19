package edu.ncsu.csc326.coffee_maker.services;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;

import java.util.List;

public interface IngredientService {

    IngredientDto createIngredient(IngredientDto ingredientDto);

    IngredientDto getIngredientById(Long ingredientId);

    List<IngredientDto> getAllIngredients();

    void deleteIngredient(Long ingredientId);

    void deleteAllIngredients();

}
