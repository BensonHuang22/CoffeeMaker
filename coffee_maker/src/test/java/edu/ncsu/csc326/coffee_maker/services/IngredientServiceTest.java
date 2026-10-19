package edu.ncsu.csc326.coffee_maker.services;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class IngredientServiceTest {

    @Autowired
    private IngredientService ingredientService;

    @BeforeEach
    public void setUp() throws Exception {
        ingredientService.deleteAllIngredients();
    }

    /**
     * Tests creating an ingredient and adding it to the repository
     */
    @Test
    @Transactional
    public void testCreateIngredient() {

        IngredientDto ingredient1 = new IngredientDto("Coffee", 5);

        IngredientDto createdIngredient1 = ingredientService.createIngredient(ingredient1);
        assertAll("Ingredient contents",
                () -> assertEquals("Coffee", createdIngredient1.getName()),
                () -> assertEquals(5, createdIngredient1.getAmount()));

        IngredientDto ingredient2 = new IngredientDto("PumpkinSpice", 10);
        IngredientDto createdIngredient2 = ingredientService.createIngredient(ingredient2);
        assertAll("Ingredient contents",
                () -> assertEquals("PumpkinSpice", createdIngredient2.getName()),
                () -> assertEquals(10, createdIngredient2.getAmount()));
    }

    /**
     * Tests getting an ingredient by id
     */
    @Test
    @Transactional
    public void testGetIngredientById() {

        IngredientDto ingredient1 = new IngredientDto("Coffee", 5);

        IngredientDto createdIngredient1 = ingredientService.createIngredient(ingredient1);
        IngredientDto fetchedIngredient1 = ingredientService.getIngredientById(createdIngredient1.getId());
        assertAll("Ingredient contents",
                () -> assertEquals("Coffee", fetchedIngredient1.getName()),
                () -> assertEquals(5, fetchedIngredient1.getAmount()));

        IngredientDto ingredient2 = new IngredientDto("PumpkinSpice", 10);
        IngredientDto createdIngredient2 = ingredientService.createIngredient(ingredient2);
        IngredientDto fetchedIngredient2 = ingredientService.getIngredientById(createdIngredient2.getId());
        assertAll("Ingredient contents",
                () -> assertEquals("PumpkinSpice", fetchedIngredient2.getName()),
                () -> assertEquals(10, fetchedIngredient2.getAmount()));
    }

}