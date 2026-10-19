package edu.ncsu.csc326.coffee_maker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class InventoryTest {

    /**
     * Checks that the constructor retains the ID and supplied ingredients.
     */
    @Test
    public void testConstructorWithIngredients() {
        Ingredient coffee = new Ingredient("Coffee", 20);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(coffee);

        Inventory inventory = new Inventory(7L, ingredients);

        assertEquals(Long.valueOf(7L), inventory.getId());
        assertEquals(1, inventory.getIngredients().size());
        assertEquals("Coffee",
                inventory.getIngredients().get(0).getName());
        assertEquals(Integer.valueOf(20),
                inventory.getIngredients().get(0).getAmount());
    }


    /**
     * Checks that the ID-only constructor creates an empty, usable list.
     */
    @Test
    public void testConstructorWithId() {
        Inventory inventory = new Inventory(8L);

        assertEquals(Long.valueOf(8L), inventory.getId());
        assertNotNull(inventory.getIngredients());
        assertTrue(inventory.getIngredients().isEmpty());

        inventory.getIngredients().add(new Ingredient("Milk", 10));

        assertEquals(1, inventory.getIngredients().size());
        assertEquals("Milk",
                inventory.getIngredients().get(0).getName());
    }


    /**
     * Checks that setting ingredients replaces the previous collection.
     */
    @Test
    public void testSetIngredients() {
        Inventory inventory = new Inventory();
        inventory.getIngredients().add(new Ingredient("Coffee", 20));

        List<Ingredient> replacement = new ArrayList<>();
        replacement.add(new Ingredient("Tea", 5));

        inventory.setIngredients(replacement);

        assertEquals(1, inventory.getIngredients().size());
        assertEquals("Tea",
                inventory.getIngredients().get(0).getName());
        assertEquals(Integer.valueOf(5),
                inventory.getIngredients().get(0).getAmount());
    }
}
