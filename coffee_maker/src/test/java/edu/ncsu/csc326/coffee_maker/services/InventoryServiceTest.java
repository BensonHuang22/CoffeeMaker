package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import jakarta.persistence.EntityManager;

/**
 * Tests InventoryServiceImpl.
 *
 * @author CSC 326 Course Staff
 */
@SpringBootTest
@Transactional
public class InventoryServiceTest {
	
	/** Reference to InventoryService (and InventoryServiceImpl). */
	@Autowired
	private InventoryService inventoryService;
	
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
	 * Tests InventoryService.createInventory().
	 */
	@Test
	public void testCreateInventory() {
		InventoryDto inventoryDto = new InventoryDto();

		InventoryDto created = inventoryService.createInventory(inventoryDto);

		assertNotNull(created.getId());

		entityManager.flush();
		entityManager.clear();

		Inventory stored = entityManager.find(Inventory.class, created.getId());

		assertNotNull(stored);
		assertAll("Stored inventory",
				() -> assertEquals(created.getId(), stored.getId()),
				() -> assertTrue(stored.getIngredients().isEmpty()));
	}
	
	/**
	 * Tests InventoryService.updateInventory()
	 */
	@Test
	public void testUpdateInventory() {
		Ingredient coffee = new Ingredient();
		coffee.setName("UC5_TEST_COFFEE");
		coffee.setAmount(20);
		entityManager.persist(coffee);

		Ingredient milk = new Ingredient();
		milk.setName("UC5_TEST_MILK");
		milk.setAmount(10);
		entityManager.persist(milk);

		Inventory inventory = new Inventory();
		inventory.getIngredients().add(coffee);
		inventory.getIngredients().add(milk);
		entityManager.persist(inventory);

		entityManager.flush();
		entityManager.clear();

		// The request contains additions, not replacement totals.
		IngredientDto coffeeAddition = new IngredientDto();
		coffeeAddition.setName("UC5_TEST_COFFEE");
		coffeeAddition.setAmount(5);

		IngredientDto milkAddition = new IngredientDto();
		milkAddition.setName("UC5_TEST_MILK");
		milkAddition.setAmount(3);

		InventoryDto additions = new InventoryDto();
		additions.getIngredients().add(coffeeAddition);
		additions.getIngredients().add(milkAddition);

		inventoryService.updateInventory(additions);

		// Reload to verify the quantities were actually persisted.
		entityManager.flush();
		entityManager.clear();

		InventoryDto updated = inventoryService.getInventory();

		Map<String, Integer> amounts = updated.getIngredients().stream()
				.collect(Collectors.toMap(
						IngredientDto::getName,
						IngredientDto::getAmount));

		assertAll("Updated inventory",
				() -> assertEquals(2, amounts.size()),
				() -> assertEquals(Integer.valueOf(25),
						amounts.get("UC5_TEST_COFFEE")),
				() -> assertEquals(Integer.valueOf(13),
						amounts.get("UC5_TEST_MILK")));
	}
}
