package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.services.InventoryService;
import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;

/**
 * Tests InventoryController on its own, without the rest of the application.
 *
 * This class and InventoryControllerTest test the same two endpoints on purpose.
 * InventoryControllerTest starts the whole application with @SpringBootTest and
 * talks to a real MySQL database, so it proves that the controller, the service,
 * the repository, and the database all work together.  That takes several seconds
 * and needs MySQL running.
 *
 * This class starts only Spring's web layer with @WebMvcTest and hands the
 * controller a Mockito mock in place of the real InventoryService, using an
 * annotation called @MockitoBean.  Nothing here touches a database.  It tests
 * less of the system, and in exchange it can do three things the other test
 * cannot:
 *
 *   1. decide exactly what the service returns, so the controller can be checked
 *      against values that would be awkward to arrange in a real database;
 *   2. inspect what the controller handed down to the service, which is the only
 *      way to prove the JSON request body was bound to an InventoryDto correctly;
 *   3. make the service fail on command, so error handling can be tested at all.
 *
 * Neither style replaces the other.  Reach for this one while working on a
 * controller: it starts its Spring context roughly ten times faster than
 * InventoryControllerTest does, and it passes with no MySQL running at all, so it
 * still works before the database is set up.  Keep an integration test around too,
 * to catch the wiring mistakes that a mock will happily hide.
 *
 * Note @MockitoBean and not @MockBean.  They do the same job, but @MockBean was
 * deprecated in Spring Boot 3.4, so it is what older tutorials will show you.
 *
 * @author CSC 326 Course Staff
 */
@WebMvcTest(InventoryController.class)
public class InventoryControllerWebMvcTest {

	/** Mock MVC for testing the controller without a running server. */
	@Autowired
	private MockMvc mvc;

	/**
	 * Stands in for the real InventoryService.  @WebMvcTest does not create the
	 * application's @Service beans, so without this the controller would have
	 * nothing to call and the test context would fail to start.
	 */
	@MockitoBean
	private InventoryService inventoryService;


	/**
	 * Tests that GET returns inventory ingredients as JSON.
	 *
	 * @throws Exception if the request fails
	 */
	@Test
	public void testGetInventory() throws Exception {
		InventoryDto inventory = new InventoryDto();
		inventory.setId(1L);
		inventory.getIngredients().add(new IngredientDto("Coffee", 20));
		inventory.getIngredients().add(new IngredientDto("Milk", 10));

		when(inventoryService.getInventory()).thenReturn(inventory);

		mvc.perform(get("/api/inventory"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.ingredients.length()").value(2))
				.andExpect(jsonPath("$.ingredients[0].name").value("Coffee"))
				.andExpect(jsonPath("$.ingredients[0].amount").value(20))
				.andExpect(jsonPath("$.ingredients[1].name").value("Milk"))
				.andExpect(jsonPath("$.ingredients[1].amount").value(10));

		verify(inventoryService).getInventory();
	}

	/**
	 * Tests that PUT passes submitted additions to the service and returns its result.
	 *
	 * @throws Exception if the request fails
	 */
	@Test
	public void testUpdateInventoryPassesRequestBodyToService() throws Exception {
		InventoryDto additions = new InventoryDto();
		additions.getIngredients().add(new IngredientDto("Coffee", 5));
		additions.getIngredients().add(new IngredientDto("Milk", 3));

		InventoryDto updated = new InventoryDto();
		updated.setId(1L);
		updated.getIngredients().add(new IngredientDto("Coffee", 25));
		updated.getIngredients().add(new IngredientDto("Milk", 13));

		when(inventoryService.updateInventory(any(InventoryDto.class)))
				.thenReturn(updated);

		mvc.perform(put("/api/inventory")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(additions))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ingredients.length()").value(2))
				.andExpect(jsonPath("$.ingredients[0].name").value("Coffee"))
				.andExpect(jsonPath("$.ingredients[0].amount").value(25))
				.andExpect(jsonPath("$.ingredients[1].name").value("Milk"))
				.andExpect(jsonPath("$.ingredients[1].amount").value(13));

		ArgumentCaptor<InventoryDto> sentToService =
				ArgumentCaptor.forClass(InventoryDto.class);

		verify(inventoryService).updateInventory(sentToService.capture());

		InventoryDto captured = sentToService.getValue();

		assertEquals(2, captured.getIngredients().size());

		assertAll("Additions passed to the service",
				() -> assertEquals("Coffee",
						captured.getIngredients().get(0).getName()),
				() -> assertEquals(5,
						captured.getIngredients().get(0).getAmount()),
				() -> assertEquals("Milk",
						captured.getIngredients().get(1).getName()),
				() -> assertEquals(3,
						captured.getIngredients().get(1).getAmount()));
	}

	/**
	 * Tests that PUT returns 404 when inventory is missing.
	 *
	 * @throws Exception if the request fails
	 */
	@Test
	public void testUpdateInventoryWhenInventoryIsMissing() throws Exception {
		InventoryDto additions = new InventoryDto();
		additions.getIngredients().add(new IngredientDto("Coffee", 5));

		when(inventoryService.updateInventory(any(InventoryDto.class)))
				.thenThrow(new ResourceNotFoundException(
						"Inventory does not exist."));

		mvc.perform(put("/api/inventory")
						.contentType(MediaType.APPLICATION_JSON)
						.content(TestUtils.asJsonString(additions))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		verify(inventoryService).updateInventory(any(InventoryDto.class));
	}
}
