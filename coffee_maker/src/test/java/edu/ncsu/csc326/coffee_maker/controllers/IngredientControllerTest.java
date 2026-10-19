package edu.ncsu.csc326.coffee_maker.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ncsu.csc326.coffee_maker.services.impl.IngredientServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.transaction.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import edu.ncsu.csc326.coffee_maker.dto.IngredientDto;
import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.services.IngredientService;

@SpringBootTest
@AutoConfigureMockMvc
public class IngredientControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc mvc;
    @Autowired
    private IngredientServiceImpl ingredientServiceImpl;

    /**
     * Tests create ingredient
     * @throws Exception
     */
    @Test
    @Transactional
    void testCreateIngredient() throws Exception {
        IngredientDto ingredient1 = new IngredientDto("Coffee", 5);

        mvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(ingredient1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value("5"))
                .andExpect(jsonPath("$.name").value("Coffee"));
    }

    @Test
    @Transactional
    void testGetIngredient() throws Exception {
        IngredientDto ingredient1 = ingredientServiceImpl.createIngredient(new IngredientDto("Coffee", 5));

        mvc.perform(get("/api/ingredients/" + ingredient1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(5))
                .andExpect(jsonPath("$.name").value("Coffee"));
    }

}