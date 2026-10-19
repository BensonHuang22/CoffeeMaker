package edu.ncsu.csc326.coffee_maker.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.entity.Ingredient;
import edu.ncsu.csc326.coffee_maker.services.RecipeService;

@WebMvcTest(RecipeController.class)
public class RecipeControllerWebMvcTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    public void testCreateRecipeInsufficientStorage() throws Exception {
        List<Ingredient> ingredients = new LinkedList<>();
        ingredients.add(new Ingredient("Coffee", 2));
        ingredients.add(new Ingredient("Milk", 1));
        ingredients.add(new Ingredient("Sugar", 1));
        ingredients.add(new Ingredient("Chocolate", 3));
        RecipeDto newRecipe =
                new RecipeDto("Mocha", 200, ingredients);

        when(recipeService.isDuplicateName("Mocha"))
                .thenReturn(false);

        when(recipeService.getAllRecipes())
                .thenReturn(List.of(new RecipeDto(), new RecipeDto(), new RecipeDto()));

        mvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(newRecipe))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInsufficientStorage());
    }
}