package dishes;

import restaurant.Inventory;

import java.util.HashMap;

/**
 * The class representing a food item (a dish to be delivered to the customer)
 *
 * @author Jian Xian Li (Jason Li)
 */
public class Food extends Recipe {
    private String name;
    private String instructions;
    private int itemNum;

    /**
     * Create a food with an ingredient manager and a name
     *
     * @param itemNum     The menu number of this food item
     * @param name        The name of this food item
     * @param ingredients A hash map with ingredient names as keys, which map to the quantity of a certain
     *                    ingredient needed.
     * @param inventory   The inventory corresponding to where this recipe will be used
     */
    public Food(int itemNum, String name, double price, HashMap<String, Integer> ingredients, Inventory inventory) {
        super(ingredients, inventory);
        this.itemNum = itemNum;
        this.name = name;
        this.price = price;
        this.status = "unconfirmed";
        this.instructions = "";
    }


    /**
     * Adds an additional cost onto the current price of this food.
     *
     * @param additionalCost The additional cost to be added on top of the price
     */
    public void addCosts(int additionalCost) {
        price += additionalCost;
    }

    /**
     * Send this food back into the system to be remade
     */
    public void sendBack() {
        setStatus("waiting");
    }


    /**
     * Returns the name of this food
     *
     * @return The name of this food
     */
    public String getName() {
        return name;
    }

    /**
     * Returns this food's item number, most likely its number on the menu
     *
     * @return The item number
     */
    public int getItemNum() {
        return itemNum;
    }


    /**
     * Set the instructions on this food (special instructions for the chef, other than default)
     *
     * @param instruction the instruction for the chef
     */
    public void setInstructions(String instruction) {
        instructions = instruction;
    }

    /**
     * Returns the instructions associated with this food;
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Returns a copy of the food represented by this instance, which has the same name, price, ingredients, and
     * inventory reference.
     *
     * @return a copy of this food
     */
    public Food getCopy() {
        HashMap<String, Integer> ingCopy = new HashMap<>(ingredients);
        Food foodCopy = new Food(itemNum, name, price, ingCopy, inventory);
        foodCopy.setInstructions(instructions);
        return foodCopy;
    }
}
