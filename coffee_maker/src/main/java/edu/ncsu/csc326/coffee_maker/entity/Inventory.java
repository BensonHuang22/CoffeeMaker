package edu.ncsu.csc326.coffee_maker.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory for the coffee maker. Inventory is a Data Access Object (DAO) is tied to the database using
 * Hibernate libraries. InventoryRepository provides the methods for database CRUD operations.
 *
 * @author CSC 326 Course Staff
 */
@Entity
public class Inventory {
	
	/** id for inventory entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;

    /** list of ingredients in the inventory */
    @OneToMany
    private List<Ingredient> ingredients = new ArrayList<>();
    
    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object
    }
    
    /**
     * Creates an Inventory with all fields
     * @param id inventory's id
     * @param ingredients to instantiate for inventory
     */
    public Inventory(Long id, List<Ingredient> ingredients) {
        super();
        this.id = id;
        this.ingredients = ingredients;
    }

    /**
     * Creates a fresh inventory object
     * @param id inventory's id
     */
    public Inventory(Long id) {
        super();
        this.id = id;
        this.ingredients = new LinkedList<Ingredient>();
    }


    /**
     * Returns the ID of the entry in the DB
     *
     * @return long the id of Inventory
     */
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id the ID to set for the Inventory
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get the list of ingredients in the inventory.
     *
     * @return the ingredients in the inventory.
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Set the list of ingredients in the inventory.
     *
     * @param ingredients the ingredients to set in the inventory.
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

}
