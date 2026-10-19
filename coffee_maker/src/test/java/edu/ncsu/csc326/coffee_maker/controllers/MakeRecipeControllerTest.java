package edu.ncsu.csc326.coffee_maker.controllers;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the MakeRecipeController REST API through MockMvc.
 *
 * @author Benson Huang
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MakeRecipeControllerTest {
    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc mvc;

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
        entityManager.createQuery("DELETE FROM Recipe").executeUpdate();

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


    @Test
    public void makeRecipe() throws Exception {
        Ingredient espresso = new Ingredient();
        espresso.setName("Espresso");
        espresso.setAmount(4);
        entityManager.persist(espresso);

        Ingredient milk = new Ingredient();
        milk.setName("Milk");
        milk.setAmount(2);
        entityManager.persist(milk);

        Inventory inventory = new Inventory();
        inventory.getIngredients().add(espresso);
        inventory.getIngredients().add(milk);
        entityManager.persist(inventory);

        entityManager.flush();
        entityManager.clear();

        List<Ingredient> ingredients = new LinkedList<>();
        ingredients.add(new Ingredient("Espresso", 2));
        ingredients.add(new Ingredient("Milk", 1));
        RecipeDto recipeDto = new RecipeDto("Coffee", 50, ingredients);

        mvc.perform(post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(recipeDto))
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(post("/api/makerecipe/Coffee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(100))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(50));

            entityManager.flush();
            entityManager.clear();

        mvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients.length()").value(2))
                .andExpect(jsonPath("$.ingredients[0].name")
                        .value("Espresso"))
                .andExpect(jsonPath("$.ingredients[0].amount").value(2))
                .andExpect(jsonPath("$.ingredients[1].name")
                .value("Milk"))
                .andExpect(jsonPath("$.ingredients[1].amount").value(1));

        // invalid price
        mvc.perform(post("/api/makerecipe/Coffee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(49))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        // reducing quantity to 0
        mvc.perform(post("/api/makerecipe/Coffee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(100))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(50));

        // invalid quantity
        mvc.perform(post("/api/makerecipe/Coffee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(50))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
