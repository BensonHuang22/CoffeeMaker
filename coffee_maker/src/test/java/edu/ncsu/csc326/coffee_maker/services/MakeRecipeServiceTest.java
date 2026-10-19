package edu.ncsu.csc326.coffee_maker.services;


import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests makeRecipeService
 */
@SpringBootTest
@Transactional
public class MakeRecipeServiceTest {

    /** Reference to RecipeService (and RecipeServiceImpl). */
    @Autowired
    private RecipeService recipeService;

    /** Reference to InventoryService (and InventoryServiceImpl). */
    @Autowired
    private InventoryService inventoryService;

    /** Reference to MakeRecipeService (and MakeRecipeServiceImpl). */
    @Autowired
    private MakeRecipeService makeRecipeService;

    /** Reference to EntityManager */
    @Autowired
    private EntityManager entityManager;

    /**
     * Sets up the test case.  Inventory is treated as a singleton, so the table is
     * emptied before each test.
     * @throws java.lang.Exception if error
     */
    @BeforeEach
    public void setUp() throws Exception {
        List<Inventory> inventories = entityManager.createQuery(
                        "SELECT i FROM Inventory i", Inventory.class)
                .getResultList();

        for (Inventory inventory : inventories) {
            inventory.getIngredients().clear();
        }
        entityManager.flush();

        for (Inventory inventory : inventories) {
            entityManager.remove(inventory);
        }
        entityManager.flush();
        entityManager.clear();
    }

    /** Test makeRecipe() */
    @Test
    public void testMakeRecipe(){
        List<Ingredient> ingredients = new LinkedList<>();
        ingredients.add(new Ingredient("Espresso", 2));
        ingredients.add(new Ingredient("Milk", 1));
        RecipeDto recipeDto = new RecipeDto("Coffee", 50, ingredients);

        recipeService.createRecipe(recipeDto);

        Ingredient espresso = new Ingredient();
        espresso.setName("Espresso");
        espresso.setAmount(20);
        entityManager.persist(espresso);

        Ingredient milk = new Ingredient();
        milk.setName("Milk");
        milk.setAmount(20);
        entityManager.persist(milk);

        Inventory inventory = new Inventory();
        inventory.getIngredients().add(espresso);
        inventory.getIngredients().add(milk);
        entityManager.persist(inventory);

        InventoryDto inventoryDto = inventoryService.getInventory();

        entityManager.flush();
        entityManager.clear();

        makeRecipeService.makeRecipe(inventoryDto, recipeDto);

        // Push changes to database and remove cached entities
        entityManager.flush();
        entityManager.clear();

        // Grab the updated inventory from the database
        Inventory updatedInventory = entityManager.createQuery(
                        "SELECT i FROM Inventory i", Inventory.class)
                .getSingleResult();

        Map<String, Integer> amounts = updatedInventory.getIngredients().stream()
                .collect(Collectors.toMap(
                        Ingredient::getName,
                        Ingredient::getAmount));

        assertAll("Updated inventory",
                () -> assertEquals(2, amounts.size()),
                () -> assertEquals(Integer.valueOf(18),
                        amounts.get("Espresso")),
                () -> assertEquals(Integer.valueOf(19),
                        amounts.get("Milk")));

    }

}
