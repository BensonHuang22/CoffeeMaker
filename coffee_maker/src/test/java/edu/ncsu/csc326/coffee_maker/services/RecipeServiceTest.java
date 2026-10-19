package edu.ncsu.csc326.coffee_maker.services;

import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest

class RecipeServiceTest {

    /** Reference to RecipeService (and RecipeServiceImpl). */
    @Autowired
    private RecipeService recipeService;

    /** Reference to EntityManager */
    @Autowired
    private EntityManager entityManager;

    /**
     * Sets up the test case.
     * @throws java.lang.Exception if error
     */
    @BeforeEach
    public void setUp() throws Exception {
        entityManager.createQuery("DELETE FROM Recipe").executeUpdate();
    }

    @Test
    void createRecipe() {
        List<Ingredient> ingredients = new LinkedList<>();
        ingredients.add(new Ingredient("Coffee", 2));
        ingredients.add(new Ingredient("Milk", 1));
        ingredients.add(new Ingredient("Sugar", 1));
        RecipeDto recipeDto = new RecipeDto("Coffee", 50, ingredients);

        recipeService.createRecipe(recipeDto);

        entityManager.flush();
        entityManager.clear();

        RecipeDto createdRecipe = recipeService.getRecipeByName("Coffee");
        assertAll("Recipe contents",
                () -> assertEquals("Coffee", createdRecipe.getName()),
                () -> assertEquals(50, createdRecipe.getPrice()),
                () -> assertEquals(3, createdRecipe.getIngredients().size()),
                () -> assertEquals("Coffee", createdRecipe.getIngredients().get(0).getName()),
                () -> assertEquals(2, createdRecipe.getIngredients().get(0).getAmount()),
                () -> assertEquals("Milk", createdRecipe.getIngredients().get(1).getName()),
                () -> assertEquals(1, createdRecipe.getIngredients().get(1).getAmount()),
                () -> assertEquals("Sugar", createdRecipe.getIngredients().get(2).getName()),
                () -> assertEquals(1, createdRecipe.getIngredients().get(2).getAmount()));

        // Exercise the name-not-found branch
        assertThrows(RuntimeException.class, () -> recipeService.getRecipeByName("NonExistentRecipe"));
    }

    @Test
    void isDuplicateName() {
        List<Ingredient> ingredients = new LinkedList<>();
        ingredients.add(new Ingredient("Coffee", 2));
        ingredients.add(new Ingredient("Milk", 2));
        ingredients.add(new Ingredient("Sugar", 1));
        RecipeDto recipeDto = new RecipeDto("Latte", 55, ingredients);
        recipeService.createRecipe(recipeDto);

        entityManager.flush();
        entityManager.clear();

        // Check both a name that exists and one that does not
        assertTrue(recipeService.isDuplicateName("Latte"));
        assertFalse(recipeService.isDuplicateName("Cappuccino"));
    }

    @Test
    void getAllRecipes() {
        List<Ingredient> ing1 = new LinkedList<>();
        ing1.add(new Ingredient("Coffee", 2));
        ing1.add(new Ingredient("Milk", 1));
        ing1.add(new Ingredient("Sugar", 1));
        RecipeDto r1 = new RecipeDto("Coffee", 50, ing1);

        List<Ingredient> ing2 = new LinkedList<>();
        ing2.add(new Ingredient("Coffee", 3));
        ing2.add(new Ingredient("Milk", 2));
        ing2.add(new Ingredient("Sugar", 2));
        ing2.add(new Ingredient("Chocolate", 2));
        RecipeDto r2 = new RecipeDto("Mocha", 75, ing2);

        recipeService.createRecipe(r1);
        recipeService.createRecipe(r2);

        entityManager.flush();
        entityManager.clear();

        // Create two recipes; assert both come back
        List<RecipeDto> recipes = recipeService.getAllRecipes();
        assertEquals(2, recipes.size());
    }

    @Test
    void updateRecipe() {
        List<Ingredient> ingredients = new LinkedList<>();
        ingredients.add(new Ingredient("Coffee", 1));
        ingredients.add(new Ingredient("Milk", 2));
        ingredients.add(new Ingredient("Sugar", 1));
        RecipeDto recipeDto = new RecipeDto("Latte", 60, ingredients);
        recipeService.createRecipe(recipeDto);

        entityManager.flush();
        entityManager.clear();

        RecipeDto saved = recipeService.getRecipeByName("Latte");
        Long id = saved.getId();

        // Change only milk (from 2 to 5); keep other fields
        List<Ingredient> updatedIngredients = new LinkedList<>();
        updatedIngredients.add(new Ingredient("Coffee", 1));
        updatedIngredients.add(new Ingredient("Milk", 5));
        updatedIngredients.add(new Ingredient("Sugar", 1));
        RecipeDto updatedDto = new RecipeDto("Latte", 60, updatedIngredients);
        recipeService.updateRecipe(id, updatedDto);

        entityManager.flush();
        entityManager.clear();

        RecipeDto retrieved = recipeService.getRecipeById(id);
        assertAll("Updated Recipe contents",
                () -> assertEquals("Latte", retrieved.getName()),
                () -> assertEquals(60, retrieved.getPrice()),
                () -> assertEquals(3, retrieved.getIngredients().size()),
                () -> assertEquals("Coffee", retrieved.getIngredients().get(0).getName()),
                () -> assertEquals(1, retrieved.getIngredients().get(0).getAmount()),
                () -> assertEquals("Milk", retrieved.getIngredients().get(1).getName()),
                () -> assertEquals(5, retrieved.getIngredients().get(1).getAmount()),
                () -> assertEquals("Sugar", retrieved.getIngredients().get(2).getName()),
                () -> assertEquals(1, retrieved.getIngredients().get(2).getAmount())
        );

        assertThrows(RuntimeException.class, () -> recipeService.updateRecipe(999999L, updatedDto));
        assertThrows(RuntimeException.class, () -> recipeService.getRecipeById(999999L));
    }

    @Test
    void deleteRecipe() {
        List<Ingredient> ing1 = new LinkedList<>();
        ing1.add(new Ingredient("Coffee", 3));
        RecipeDto r1 = new RecipeDto("Espresso", 40, ing1);

        List<Ingredient> ing2 = new LinkedList<>();
        ing2.add(new Ingredient("Coffee", 2));
        RecipeDto r2 = new RecipeDto("Americano", 45, ing2);

        recipeService.createRecipe(r1);
        recipeService.createRecipe(r2);

        entityManager.flush();
        entityManager.clear();

        RecipeDto savedR1 = recipeService.getRecipeByName("Espresso");
        recipeService.deleteRecipe(savedR1.getId());

        entityManager.flush();
        entityManager.clear();

        // Verify the deleted recipe is gone
        assertFalse(recipeService.isDuplicateName("Espresso"));

        // Verify the other recipe remains
        List<RecipeDto> remaining = recipeService.getAllRecipes();
        assertEquals(1, remaining.size());
        assertEquals("Americano", remaining.get(0).getName());
        assertThrows(RuntimeException.class, () -> recipeService.deleteRecipe(999999L));
    }

}