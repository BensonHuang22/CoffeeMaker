package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RecipeControllerTest {
	/** Mock MVC for testing controller */
	@Autowired
	private MockMvc mvc;

	/** Reference to EntityManager */
	@Autowired
	private EntityManager entityManager;

	/** Reference to RecipeService */
	@Autowired
	private RecipeService recipeService;

	/**
	 * Sets up the test case.
	 */
	@BeforeEach
	public void setUp() {
		entityManager.createQuery("DELETE FROM Recipe").executeUpdate();
	}

	@Test
	public void testGetRecipes() throws Exception {
		String recipe = mvc.perform(get("/api/recipes"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertFalse(recipe.contains("Mocha"));
	}

	@Test
	public void testCreateRecipe() throws Exception {
		List<Ingredient> ingredients = new LinkedList<>();
		ingredients.add(new Ingredient("Coffee", 2));
		ingredients.add(new Ingredient("Milk", 1));
		ingredients.add(new Ingredient("Sugar", 1));
		ingredients.add(new Ingredient("Chocolate", 3));
		RecipeDto recipeDto = new RecipeDto("Mocha", 200, ingredients);

		// Testing 200 ok case
		mvc.perform(post("/api/recipes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(recipeDto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Mocha"))
				.andExpect(jsonPath("$.price").value(200))
				.andExpect(jsonPath("$.ingredients[0].name").value("Coffee"))
				.andExpect(jsonPath("$.ingredients[0].amount").value(2))
				.andExpect(jsonPath("$.ingredients[1].name").value("Milk"))
				.andExpect(jsonPath("$.ingredients[1].amount").value(1))
				.andExpect(jsonPath("$.ingredients[2].name").value("Sugar"))
				.andExpect(jsonPath("$.ingredients[2].amount").value(1))
				.andExpect(jsonPath("$.ingredients[3].name").value("Chocolate"))
				.andExpect(jsonPath("$.ingredients[3].amount").value(3));

		// Testing 409 conflict case where there's a duplicate request
		mvc.perform(post("/api/recipes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(recipeDto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict());
	}

	/**
	 * Tests GET /api/recipes/{name} to verify:
	 * 1. An existing recipe returns 200 OK with the correct JSON fields.
	 * 2. A non-existent recipe returns 404 NOT FOUND.
	 *
	 * @throws Exception if an error occurs during mock requests
	 */
	@Test
	public void testGetRecipe() throws Exception {
		// 1. Create a recipe via POST so it exists in the database
		List<Ingredient> ingredients = new LinkedList<>();
		ingredients.add(new Ingredient("Coffee", 2));
		ingredients.add(new Ingredient("Milk", 1));
		ingredients.add(new Ingredient("Sugar", 1));
		ingredients.add(new Ingredient("Chocolate", 3));
		final RecipeDto recipeDto = new RecipeDto("Mocha", 200, ingredients);
		mvc.perform(post("/api/recipes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(recipeDto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// 2. Test GET /api/recipes/{name} for the existing recipe
		mvc.perform(get("/api/recipes/Mocha")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Mocha"))
				.andExpect(jsonPath("$.price").value(200))
				.andExpect(jsonPath("$.ingredients[0].name").value("Coffee"))
				.andExpect(jsonPath("$.ingredients[0].amount").value(2))
				.andExpect(jsonPath("$.ingredients[1].name").value("Milk"))
				.andExpect(jsonPath("$.ingredients[1].amount").value(1))
				.andExpect(jsonPath("$.ingredients[2].name").value("Sugar"))
				.andExpect(jsonPath("$.ingredients[2].amount").value(1))
				.andExpect(jsonPath("$.ingredients[3].name").value("Chocolate"))
				.andExpect(jsonPath("$.ingredients[3].amount").value(3));

		// 3. Test GET /api/recipes/{name} for a recipe that does not exist
		mvc.perform(get("/api/recipes/Latte")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	/**
	 * Tests DELETE /api/recipes/{id} to verify:
	 * 1. An existing recipe can be deleted successfully (200 OK).
	 * 2. A subsequent GET request for the deleted recipe returns 404 NOT FOUND.
	 *
	 * @throws Exception if an error occurs during mock requests
	 */
	@Test
	public void testDeleteRecipe() throws Exception {
		// 1. Create a recipe via POST
		List<Ingredient> ingredients = new LinkedList<>();
		ingredients.add(new Ingredient("Coffee", 3));
		final RecipeDto recipeDto = new RecipeDto("Espresso", 100, ingredients);
		mvc.perform(post("/api/recipes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(recipeDto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Fetch the saved recipe ID via EntityManager or repository/service
		RecipeDto saved = recipeService.getRecipeByName("Espresso");
		Long id = saved.getId();

		// 2. Perform DELETE /api/recipes/{id}
		mvc.perform(delete("/api/recipes/" + id)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		// 3. Verify it is gone with a follow-up GET
		mvc.perform(get("/api/recipes/Espresso")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

}
