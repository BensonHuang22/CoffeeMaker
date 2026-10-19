package edu.ncsu.csc326.coffee_maker.controllers;
import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    /**
     * API call to get an ingredient from repository
     * @param id id of ingredient to get
     * @return the response of the API call
     */
    @GetMapping("{id}")
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable("id") Long id) {
        IngredientDto ingredientDto = ingredientService.getIngredientById(id);
        return ResponseEntity.ok(ingredientDto);
    }

    /**
     * API  call to create an ingredient and add it to the repository
     * @param ingredientDto the ingredient to add
     * @return the response of the API call
     */
    @PostMapping
    public ResponseEntity<IngredientDto> createIngredient(@RequestBody IngredientDto ingredientDto) {
        IngredientDto savedIngredientDto = ingredientService.createIngredient(ingredientDto);
        return ResponseEntity.ok(savedIngredientDto);
    }
}