package dishes;

import restaurant.Inventory;

import java.util.HashMap;

/**
 * A combo that represents a menu item which contains multiple foods. These foods must be on the menu
 */
public class Combo extends Recipe {
    private String name;
    private String[] foods;
    private HashMap<String, String> instructions; //Maps food name to extra instructions

    /**
     * Constructs a combo menu item
     *
     * @param price     The price of this combo
     * @param name      The name of this combo, in the format "C" + menu item name string concatenation
     * @param foods     An array of the name of the foods in this combo (must be on menu)
     * @param inventory The inventory corresponding to the restaurant serving this combo
     */
    public Combo(double price, String name, Food[] foods, Inventory inventory) {
        super(new HashMap<>(), inventory);
        this.foods = new String[foods.length];
        this.price = price;
        //Fill up recipe's ingredients and add the name to foods array
        int index = 0;
        for (Food food : foods) {
            HashMap<String, Integer> newIngredients = food.getIngredients();
            for (String newIngredient : newIngredients.keySet()) {
                if (super.ingredients.containsKey(newIngredient)) {
                    int currNum = super.ingredients.get(newIngredient);
                    super.ingredients.put(newIngredient, currNum + newIngredients.get(newIngredient));
                } else {
                    super.ingredients.put(newIngredient, newIngredients.get(newIngredient));
                }
            }
            this.foods[index] = food.getName();
            index++;
        }
        this.name = name;
        this.status = "unconfirmed";
        this.instructions = new HashMap<>();
        for (String foodName : this.foods) {
            this.instructions.put(foodName, "");
        }
    }


    /**
     * Used to create a copy of this Combo
     *
     * @param price        The price of this combo
     * @param status       The status of this combo
     * @param name         The name of this combo, in the format "C" + menu item number
     * @param foods        Foods within this combo
     * @param inventory    Inventory reference
     * @param ingredients  HashMap representation of ingredients
     * @param instructions Instructions assigned to this particular combo
     */
    private Combo(double price, String status, String name, String[] foods, Inventory inventory,
                  HashMap<String, Integer> ingredients, HashMap<String, String> instructions) {
        super(ingredients, inventory);
        this.name = name;
        this.price = price;
        this.status = status;
        this.foods = foods;
        this.instructions = instructions;
    }

    /**
     * Returns the name of this combo, which is "C" + menu item number
     *
     * @return name of this combo in the format "C" + menu item number
     */
    public String getName() {
        return name;
    }

    /**
     * Send back the entire combo to be remade
     */
    public void sendBack() {
        setStatus("waiting");
    }


    /**
     * Returns the combo name equivalent of this combo, ex "Combo 8"
     *
     * @return The combo name
     */
    public String getComboName() {
        return "Combo " + name.charAt(1);
    }

    /**
     * Sets an instruction for a certain food in this combo
     *
     * @param foodName    The food name to set the instruction to (must be in this combo)
     * @param instruction The instruction for the specified food in the combo
     */
    public void setInstructions(String foodName, String instruction) {
        if (instructions.containsKey(foodName) && (instruction.length() == 0 ||
                instruction.matches("(-?[0-9]+:\\s[a-zA-z\\s]+)(,\\s-?[0-9]+:\\s[a-zA-z\\s]+)*"))) {
            instructions.put(foodName, instruction);
        } else {
            System.err.println("Failed Regex Instruction");
            System.err.println(instruction);
        }
    }

    /**
     * Returns the instructions HashMap
     *
     * @return instructions HashMap
     */
    public HashMap<String, String> getInstructions() {
        return instructions;
    }

    /**
     * Returns a list of the foods which are in this combo
     *
     * @return a list of Strings representing this combo's foods
     */
    public String[] getFoods() {
        return foods;
    }

    /**
     * Returns the menu item number
     *
     * @return menu item number
     */
    public int getItemNum() {
        return Integer.parseInt(name.substring(5));
    }

    /**
     * Returns a copy of this combo
     *
     * @return a copy of this combo object
     */
    public Combo getCopy() {
        HashMap<String, String> mapCopy = new HashMap<>(instructions);
        HashMap<String, Integer> ingCopy = new HashMap<>(ingredients);
        return new Combo(price, status, name, foods, inventory, ingCopy, mapCopy);
    }
}
