package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.entity.Inventory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;

/**
 * Tests InventoryRepository.  Uses the real database - not an embedded one.
 *
 * @author CSC 326 Course Staff
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class InventoryRepositoryTest {
	
	/** Reference to inventory repository */
	@Autowired
	private InventoryRepository inventoryRepository;
	
	/** Reference to EntityManager */
	@Autowired
	private TestEntityManager testEntityManager;
	
	/** Reference to inventory */
	private Inventory inventory;

	/**
	 * Sets up the test case.  Inventory is treated as a singleton, so the table is
	 * emptied before each test.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		List<Inventory> inventories = inventoryRepository.findAll();

		for (Inventory existing : inventories) {
			existing.getIngredients().clear();
		}
		testEntityManager.flush();

		inventoryRepository.deleteAll();
		testEntityManager.flush();
		testEntityManager.clear();

		Ingredient coffee = new Ingredient();
		coffee.setName("UC5_REPO_COFFEE");
		coffee.setAmount(20);
		testEntityManager.persist(coffee);

		Ingredient milk = new Ingredient();
		milk.setName("UC5_REPO_MILK");
		milk.setAmount(14);
		testEntityManager.persist(milk);

		inventory = new Inventory();
		inventory.getIngredients().add(coffee);
		inventory.getIngredients().add(milk);
		inventory = inventoryRepository.save(inventory);

		// Force the test to retrieve data from the database.
		testEntityManager.flush();
		testEntityManager.clear();
	}


	/**
	 * Test saving the inventory and retrieving from the repository.
	 */
	@Test
	public void testSaveAndGetInventory() {
		Inventory fetchedInventory = inventoryRepository
				.findById(inventory.getId())
				.orElseThrow();

		Map<String, Integer> amounts = fetchedInventory.getIngredients()
				.stream()
				.collect(Collectors.toMap(
						Ingredient::getName,
						Ingredient::getAmount));

		assertAll("Inventory contents",
				() -> assertEquals(inventory.getId(),
						fetchedInventory.getId()),
				() -> assertEquals(2, amounts.size()),
				() -> assertEquals(Integer.valueOf(20),
						amounts.get("UC5_REPO_COFFEE")),
				() -> assertEquals(Integer.valueOf(14),
						amounts.get("UC5_REPO_MILK")));
	}
	
	/**
	 * Tests updating the inventory
	 */
	@Test
	public void testUpdateInventory() {
		Inventory fetchedInventory = inventoryRepository
				.findById(inventory.getId())
				.orElseThrow();

		for (Ingredient ingredient : fetchedInventory.getIngredients()) {
			if ("UC5_REPO_COFFEE".equals(ingredient.getName())) {
				ingredient.setAmount(25);
			}
		}

		inventoryRepository.save(fetchedInventory);

		testEntityManager.flush();
		testEntityManager.clear();

		Inventory updatedInventory = inventoryRepository
				.findById(inventory.getId())
				.orElseThrow();

		Map<String, Integer> amounts = updatedInventory.getIngredients()
				.stream()
				.collect(Collectors.toMap(
						Ingredient::getName,
						Ingredient::getAmount));

		assertAll("Updated inventory",
				() -> assertEquals(2, amounts.size()),
				() -> assertEquals(Integer.valueOf(25),
						amounts.get("UC5_REPO_COFFEE")),
				() -> assertEquals(Integer.valueOf(14),
						amounts.get("UC5_REPO_MILK")));
	}
}
