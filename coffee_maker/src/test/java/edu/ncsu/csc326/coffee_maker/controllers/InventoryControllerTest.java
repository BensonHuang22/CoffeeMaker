package edu.ncsu.csc326.coffee_maker.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import jakarta.persistence.EntityManager;
import java.util.List;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
/**
 * Tests the InventoryController REST API through MockMvc.
 *
 * @author Brandon Carvajal
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InventoryControllerTest {
		
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

	/**
	 * Tests the GET /api/inventory endpoint.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	public void testGetInventory() throws Exception {
		mvc.perform(get("/api/inventory"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.ingredients").isArray())
				.andExpect(jsonPath("$.ingredients").isEmpty());
	}

	/**
	 * Tests the PUT /api/inventory endpoint.
	 * @throws Exception if issue when running the test.
	 */
	@Test
	public void testUpdateInventory() throws Exception {
		// Prepare an existing ingredient with 20 units.
		Ingredient coffee = new Ingredient();
		coffee.setName("UC5_CONTROLLER_COFFEE");
		coffee.setAmount(20);
		entityManager.persist(coffee);

		Inventory inventory = new Inventory();
		inventory.getIngredients().add(coffee);
		entityManager.persist(inventory);

		entityManager.flush();
		entityManager.clear();

		// Confirm the starting inventory through the API.
		mvc.perform(get("/api/inventory"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients.length()").value(1))
				.andExpect(jsonPath("$.ingredients[0].name")
						.value("UC5_CONTROLLER_COFFEE"))
				.andExpect(jsonPath("$.ingredients[0].amount").value(20));

		// Request an addition of 5, not a replacement total.
		IngredientDto addition = new IngredientDto();
		addition.setName("UC5_CONTROLLER_COFFEE");
		addition.setAmount(5);

		InventoryDto additions = new InventoryDto();
		additions.getIngredients().add(addition);

		mvc.perform(put("/api/inventory")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(additions))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients.length()").value(1))
				.andExpect(jsonPath("$.ingredients[0].name")
						.value("UC5_CONTROLLER_COFFEE"))
				.andExpect(jsonPath("$.ingredients[0].amount").value(25));

		// Reload to verify the update reached the database.
		entityManager.flush();
		entityManager.clear();

		mvc.perform(get("/api/inventory"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients.length()").value(1))
				.andExpect(jsonPath("$.ingredients[0].name")
						.value("UC5_CONTROLLER_COFFEE"))
				.andExpect(jsonPath("$.ingredients[0].amount").value(25));
	}
	
}
