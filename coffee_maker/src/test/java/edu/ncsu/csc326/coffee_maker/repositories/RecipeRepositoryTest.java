package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import edu.ncsu.csc326.coffee_maker.entity.Recipe;

/**
 * Tests Recipe repository.  Uses the real database - not an embedded one.
 *
 * @author CSC 326 Course Staff
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RecipeRepositoryTest {
	
	/** Reference to recipe repository */
	@Autowired
	private RecipeRepository recipeRepository;
	
	/** Reference to EntityManager */
	@Autowired
	private TestEntityManager testEntityManager;
	
	/** Coffee recipe */
	private Recipe recipe1;
	/** Latte recipe */
	private Recipe recipe2;

	/**
	 * Sets up the test case.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		recipeRepository.deleteAll();
		
		recipe1 = new Recipe("Coffee", 100);
		recipe1.addIngredient(new Ingredient("Coffee", 3));
		recipe1.addIngredient(new Ingredient("Sugar", 2));
		recipe1.addIngredient(new Ingredient("Milk", 1));
		recipe1.addIngredient(new Ingredient("Chocolate", 0));

		recipe2 = new Recipe("Latte", 100);
		recipe2.addIngredient(new Ingredient("Coffee", 3));
		recipe2.addIngredient(new Ingredient("Sugar", 3));
		recipe2.addIngredient(new Ingredient("Milk", 3));
		recipe2.addIngredient(new Ingredient("Chocolate", 0));

		recipeRepository.save(recipe1);
		recipeRepository.save(recipe2);
		
		//Push the inserts to the database and empty the persistence context.  Without
		//this, a test below could be handed the same in-memory object that save()
		//just returned and would pass even if nothing reached the database.
		testEntityManager.flush();
		testEntityManager.clear();
	}

	/**
	 * Tests RecipeRepository.findByName().  Use this test as the model for the
	 * repository tests you write for the remaining operations.
	 */
	@Test
	public void testFindRecipeByName() {
		Recipe fetchedRecipe = recipeRepository.findByName("Latte").orElseThrow();

		assertAll("Recipe contents",
				() -> assertEquals("Latte", fetchedRecipe.getName()),
				() -> assertEquals(100, fetchedRecipe.getPrice()),
				() -> assertEquals("Coffee", fetchedRecipe.getIngredients().get(0).getName()),
				() -> assertEquals(3, fetchedRecipe.getIngredients().get(0).getAmount()),
				() -> assertEquals("Sugar", fetchedRecipe.getIngredients().get(1).getName()),
				() -> assertEquals(3, fetchedRecipe.getIngredients().get(1).getAmount()),
				() -> assertEquals("Milk", fetchedRecipe.getIngredients().get(2).getName()),
				() -> assertEquals(3, fetchedRecipe.getIngredients().get(2).getAmount()),
				() -> assertEquals("Chocolate", fetchedRecipe.getIngredients().get(3).getName()),
				() -> assertEquals(0, fetchedRecipe.getIngredients().get(3).getAmount())
		);
		// Covers the empty branch
		org.junit.jupiter.api.Assertions.assertTrue(recipeRepository.findByName("NonExistent").isEmpty());
	}

	/**
	 * Tests retrieving every recipe from the repository.
	 */
	@Test
	public void testGetAllRecipes() {
		java.util.List<Recipe> recipes = recipeRepository.findAll();
		assertEquals(2, recipes.size());

		// Standard assertions
		org.junit.jupiter.api.Assertions.assertTrue(recipes.contains(recipe1));
		org.junit.jupiter.api.Assertions.assertTrue(recipes.contains(recipe2));

		// Exercise Recipe equals() and hashCode() branches
		org.junit.jupiter.api.Assertions.assertEquals(recipe1, recipe1); // same reference
		org.junit.jupiter.api.Assertions.assertNotEquals(null, recipe1); // null target
		org.junit.jupiter.api.Assertions.assertNotEquals("Not a Recipe", recipe1); // different class
		org.junit.jupiter.api.Assertions.assertNotEquals(recipe1, recipe2); // different names

		Recipe match = new Recipe("Coffee", 100, recipe1.getIngredients());
		org.junit.jupiter.api.Assertions.assertEquals(recipe1, match); // same name
		org.junit.jupiter.api.Assertions.assertEquals(recipe1.hashCode(), match.hashCode());

		Recipe empty1 = new Recipe();
		Recipe empty2 = new Recipe();
		org.junit.jupiter.api.Assertions.assertEquals(empty1, empty2); // both names null
		org.junit.jupiter.api.Assertions.assertNotEquals(empty1, recipe1); // null name vs non-null name
		org.junit.jupiter.api.Assertions.assertNotEquals(recipe1, empty1); // not null vs null name
	}

	/**
	 * Tests updating a recipe already in the repository.
	 */
	@Test
	public void testUpdateRecipe() {
		Recipe fetchedRecipe = recipeRepository.findById(recipe1.getId()).orElseThrow();
		fetchedRecipe.setPrice(75);
		recipeRepository.save(fetchedRecipe);

		testEntityManager.flush();
		testEntityManager.clear();

		Recipe updatedRecipe = recipeRepository.findById(recipe1.getId()).orElseThrow();
		assertEquals(75, updatedRecipe.getPrice());

		fetchedRecipe = recipeRepository.findById(recipe1.getId()).orElseThrow();
		fetchedRecipe.setName("Americano");
		recipeRepository.save(fetchedRecipe);

		testEntityManager.flush();
		testEntityManager.clear();

		updatedRecipe = recipeRepository.findById(recipe1.getId()).orElseThrow();
		assertEquals("Americano", updatedRecipe.getName());

		fetchedRecipe = recipeRepository.findById(recipe1.getId()).orElseThrow();
		int oldIngListSize = fetchedRecipe.getIngredients().size();
		fetchedRecipe.removeIngredient(fetchedRecipe.getIngredients().getFirst());
		recipeRepository.save(fetchedRecipe);


		testEntityManager.flush();
		testEntityManager.clear();

		int newIngListSize = fetchedRecipe.getIngredients().size();
		assertTrue(newIngListSize < oldIngListSize);
	}

	/**
	 * Tests deleting a recipe from the repository.
	 */
	@Test
	public void testDeleteRecipe() {
		recipeRepository.delete(recipe2);

		testEntityManager.flush();
		testEntityManager.clear();

		// Covers empty Optional for deleted entity and present Optional for retained entity
		org.junit.jupiter.api.Assertions.assertTrue(recipeRepository.findByName("Latte").isEmpty());
		org.junit.jupiter.api.Assertions.assertTrue(recipeRepository.findByName("Coffee").isPresent());

		recipeRepository.delete(recipe1);

		testEntityManager.flush();
		testEntityManager.clear();

		// Covers empty Optional for deleted entity and present Optional for retained entity
		org.junit.jupiter.api.Assertions.assertTrue(recipeRepository.findByName("Latte").isEmpty());
		org.junit.jupiter.api.Assertions.assertTrue(recipeRepository.findByName("Coffee").isEmpty());
	}

}
