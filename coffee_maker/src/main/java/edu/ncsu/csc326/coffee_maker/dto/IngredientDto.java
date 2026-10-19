package edu.ncsu.csc326.coffee_maker.dto;


/**
 * Used to transfer Ingredient data between the client and server.
 * This class will serve as the response in the REST API.
 *
 * @author Lucas lpjensen
 */
public class IngredientDto {
    /** Ingredient id */
    private Long id;

    /** Ingredient name */
    private String name;

    /** Ingredient Amount */
    private Integer amount;

    public IngredientDto(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public IngredientDto() {
        this(null, 0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}