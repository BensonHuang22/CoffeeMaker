package edu.ncsu.csc326.coffee_maker.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.LinkedList;
import java.util.List;

/**
 * Recipe for the coffee maker. Recipe is a Data Access Object (DAO) is tied to the database using
 * Hibernate libraries. RecipeRepository provides the methods for database CRUD operations.
 *
 * @author CSC 326 Course Staff
 */
@Entity
@Table(name = "recipes")
public class Recipe {

    /** Recipe id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Recipe name */
    private String name;

    /** Recipe price */
    private Integer price;

    /** Amount of ingredients */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Ingredient> ingredientList;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.ingredientList = new LinkedList<Ingredient>();
    }

    /**
     * Creates a recipe from a name and price
     *
     * @param name recipe name
     * @param price recipe price
     */
    public Recipe(String name, Integer price) {
    	this.name = name;
        this.price = price;
    	this.ingredientList = new LinkedList<Ingredient>();
    }

    /**
     * Creates a recipe from all the fields
     *
     * @param name recipe name
     * @param price recipe price
     */
    public Recipe(String name, Integer price, List<Ingredient> ingredients) {
        this.name = name;
        this.price = price;
        setIngredientList(ingredients);
    }

    /**
     * Creates a recipe from all the fields including id
     *
     * @param id recipe id
     * @param name recipe name
     * @param price recipe price
     * @param ingredients recipe ingredients
     */
    public Recipe(Long id, String name, Integer price, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        setIngredientList(ingredients);
    }

    /**
     * Gets the full list of ingredients for the recipe
     *
     * @return list of ingredients
     */
    public List<Ingredient> getIngredients() {
        return ingredientList;
    }

    /**
     * Sets the list of ingredients for the recipe
     *
     * @param ingredients list to set the inner field
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredientList = ingredients;
    }

    /**
     * Sets the list of ingredients for the recipe
     *
     * @param ingredients list to set the inner field
     */
    public void setIngredientList(List<Ingredient> ingredients) {
        this.ingredientList = ingredients;
    }

    /**
     * Adds an ingredient to the list
     *
     * @param ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        this.ingredientList.add(ingredient);
    }

    /**
     * Removes an ingredient to the list
     *
     * @param ingredient to remove
     */
    public void removeIngredient(Ingredient ingredient) {
        this.ingredientList.remove(ingredient);
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the Recipe
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }


    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        return name;
    }

    /**
     * Gets the hash version of the recipe based on the name
     *
     * @return int representation of recipe hash
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    /**
     * Checks if two recipes are equal to each other
     * 
     * @param obj to check with
     * @return truth statement of equality
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            return other.name == null;
        }
        else return name.equals(other.name);
    }

}
