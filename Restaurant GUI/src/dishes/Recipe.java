package dishes;

import employees.Chef;
import javafx.scene.control.Button;
import restaurant.Inventory;

import java.util.HashMap;

/**
 * A class used to manage a list of ingredients. This is inherited by anything which can be ordered by the
 * customer, such as food.
 *
 * @author Jian Xian Li (Jason Li)
 */
public abstract class Recipe {
    HashMap<String, Integer> ingredients;
    protected Inventory inventory;
    String status; //Can be "unconfirmed", "waiting", "acknowledged", "prepared", "delivered", "cancelled"
    double price;
    Button dishButton;

    /**
     * Create a recipe to make a certain menu item
     *
     * @param ingredients A hash map with ingredient names as keys, which map to the quantity of a certain
     *                    ingredient needed.
     * @param inventory   The inventory corresponding to where this recipe will be used
     */
    Recipe(HashMap<String, Integer> ingredients, Inventory inventory) {
        this.ingredients = ingredients;
        this.inventory = inventory;
    }

    /**
     * Returns whether it is possible to create the food corresponding to this recipe with the current inventory stock
     *
     * @return true if it is possible, false otherwise
     */
    public boolean isPossible() {
        for (String ing : ingredients.keySet()) {
            if (ingredients.get(ing) > inventory.getQuantity(ing)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Add an ingredient to this food's recipe
     *
     * @param ingredient     The ingredient to be added
     * @param numIngredients How much of the ingredient to be added
     */
    public void addIngredient(String ingredient, int numIngredients) {
        if (ingredients.containsKey(ingredient)) {
            int currentNumber = ingredients.get(ingredient);
            ingredients.put(ingredient, currentNumber + numIngredients);
        } else {
            ingredients.put(ingredient, numIngredients);
        }
    }

    /**
     * Remove an amount of an ingredient from this food's recipe. if there are none of such ingredient in this recipe,
     * nothing is done. Prints an error if there aren't enough ingredients to subtract.
     *
     * @param ingredient     The ingredient to be subtracted
     * @param numIngredients How much of the ingredient to be subtracted
     */
    public void subtractIngredient(String ingredient, int numIngredients) {
        if (ingredients.containsKey(ingredient) && ingredients.get(ingredient) >= numIngredients) {
            int currentNum = ingredients.get(ingredient);
            ingredients.put(ingredient, currentNum - numIngredients);
        } else {
            System.err.println("Attempted subtraction: " + numIngredients + ", but this dish only contains: "
                    + ingredients.get(ingredient));
        }
    }

    /**
     * Completely removes an ingredient from this food's recipe
     *
     * @param ingredient The ingredient to be removed
     */
    public void subtractIngredient(String ingredient) {
        if (ingredients.containsKey(ingredient)) {
            ingredients.remove(ingredient);
        }
    }

    /**
     * Create what this recipe corresponds to. A recipe can be successfully cooked if there are enough ingredients in
     * the inventory to fulfill this instance's recipe. If a recipe can be successfully created, the ingredients will
     * be subtracted from the inventory.
     *
     * @return Whether the creation of this recipe is successful
     */
    public boolean create() {
        if (isPossible()) {
            for (String ingredient : ingredients.keySet()) {
                inventory.subtractIngredient(ingredient, ingredients.get(ingredient));
            }
            return true;
        } else {
            System.err.println("There weren't enough ingredients to create this.");
            return false;
        }
    }

    /**
     * returns the HashMap representation of this recipe's ingredients
     *
     * @return HashMap of the ingredients
     */
    public HashMap<String, Integer> getIngredients() {
        return ingredients;
    }


    /**
     * Returns the price of the instance represented by this recipe
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the current status of this food, either "waiting", "acknowledged", "prepared", or "delivered"
     *
     * @return The current status of this food
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Set the current status of this food. If the newStatus parameter is not one of the required ones, nothing
     * will happen.
     *
     * @param newStatus The new status. Must be one of "waiting", "acknowledged", "prepared", or "delivered"
     */
    public void setStatus(String newStatus) {
        newStatus = newStatus.toLowerCase();
        if (newStatus.equals("waiting") || newStatus.equals("acknowledged") || newStatus.equals("prepared") ||
                newStatus.equals("delivered") || newStatus.equals("cancelled")) {
            status = newStatus;
        }
    }

    /**
     * Returns the name of the instance represented by this recipe
     *
     * @return the name
     */
    public abstract String getName();

    /**
     * Returns a copy of the instance represented by this recipe
     *
     * @return a copy
     */
    public abstract Recipe getCopy();

    /**
     * Deals with sending back the menu item associated with this recipe
     */
    public abstract void sendBack();

    public abstract int getItemNum();

    public void setDishButton(Button dishButton){
        this.dishButton = dishButton;
    }

    public Button getDishButton(){
        return dishButton;
    }
}
