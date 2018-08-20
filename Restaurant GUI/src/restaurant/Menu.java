package restaurant;

import dishes.Combo;
import dishes.Food;
import dishes.Recipe;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class representing the restaurant's menu. The items are read from the menu.txt text file, which can be edited
 *
 * @author Jian Xian Li (Jason Li)
 */
public class Menu {
    private String menuFile = "menu.txt";
    private ArrayList<Food> defaultFoods;
    private ArrayList<Combo> defaultCombos;
    private Inventory inventory;


    private Menu() {

    }

    /**
     * Constructs a menu object with the current information on the menu.txt file
     *
     * @param inventory The inventory of the restaurant which this menu refers to
     */
    public Menu(Inventory inventory) {
        this();
        this.inventory = inventory;
        defaultFoods = new ArrayList<>();
        retrieveFoods(menuFile);
        defaultCombos = new ArrayList<>();
        retrieveCombos(menuFile);
    }


    /**
     * Fill the default food array with data from a file. The file must be in the specified menu format.
     *
     * @param menuFile The name of the file that the menu data is on
     */
    private void retrieveFoods(String menuFile) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(menuFile))) {
            String line = fileReader.readLine();
            while (line != null) {
                String[] splitList = line.split("\\s-\\s");
                //Deals with non-combos first
                if (splitList.length == 4) {
                    defaultFoods.add(getFood(splitList));
                }
                line = fileReader.readLine();
            }
        } catch (IOException e) {
            System.err.println("Menu file does not exist");
        }
    }


    /**
     * Returns a food based on a specific array format
     *
     * @param splitList For index i: 0 - menu item id, 1 - food name, 2 - price, 3 - ingredients String format, see
     *                  createIngredients method
     * @return A food based on this array
     */
    private Food getFood(String[] splitList) {
        int menuID = Integer.parseInt(splitList[0]);
        String foodName = splitList[1];
        double foodPrice = Double.parseDouble(splitList[2]);
        HashMap<String, Integer> ingredients = createIngredients(splitList[3]);
        return new Food(menuID, foodName, foodPrice, ingredients, inventory);
    }

    /**
     * Creates an ingredients HashMap using a string with format ex. "[ing1:1, ing2:1, ing3:3]"
     *
     * @param ingredients The string of specified format
     * @return The HashMap equivalent to what is represented by ingredients
     */
    private HashMap<String, Integer> createIngredients(String ingredients) {
        ingredients = ingredients.substring(1, ingredients.length() - 1);
        String[] ingArray = ingredients.split(",\\s");

        HashMap<String, Integer> ingMap = new HashMap<>();
        for (String ingredient : ingArray) {
            String[] ingData = ingredient.split(":"); //ingData[0] is ingredient name, ingData[1] is quantity
            ingMap.put(ingData[0], Integer.parseInt(ingData[1]));
        }

        return ingMap;
    }


    private void retrieveCombos(String menuFile) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(menuFile))) {
            String line = fileReader.readLine();
            while (line != null) {
                String[] splitList = line.split("\\s-\\s");
                if (splitList.length == 3) {
                    defaultCombos.add(getCombo(splitList));
                }
                line = fileReader.readLine();
            }
        } catch (IOException e) {
            System.err.println("Menu file does not exist");
        }
    }

    /**
     * Creates a Combo with a String array of a specific format
     *
     * @param splitList For index i: 0 - "C" + menu item id, 1 - [food name 1, food name 2, ...], 2 - price
     * @return A combo corresponding to the array
     */
    private Combo getCombo(String[] splitList) {
        double price = Double.parseDouble(splitList[2]);
        String name = splitList[0];
        splitList[1] = splitList[1].substring(1, splitList[1].length() - 1);
        String[] foodNames = splitList[1].split(",\\s");
        Food[] foods = new Food[foodNames.length];
        for (int i = 0; i < foodNames.length; i++) {
            foods[i] = getDefaultFood(foodNames[i]);
        }
        Combo c = new Combo(price, name, foods, inventory);
        return new Combo(price, name, foods, inventory);
    }

    /**
     * Returns a new instance of a food initialized with default values given the name of the food. If this name
     * is not on the menu (that is, menu.txt), null will be returned
     *
     * @param foodName The name of the food to be returned (needs to be on menu)
     * @return A copy of a food corresponding to foodName, given default values
     */
    private Food getDefaultFood(String foodName) {
        for (Food food : defaultFoods) {
            if (food.getName().equals(foodName)) {
                return food.getCopy();
            }
        }
        System.err.println("There is no food by the name of " + foodName + " on the menu");
        return null;
    }


    /**
     * Returns a dish which corresponds to its dish name. A food's name is the second value in a food entry in
     * menu.txt while a combo's name is the first value (always in the format C + menu item number)
     * Returns null and outputs an error if no dish is found.
     *
     * @param dishName The name of the dish
     * @return A dish corresponding to dishName
     */
    public Recipe getDefaultDish(String dishName) {
        //Check foods first
        for (Food food : defaultFoods) {
            if (food.getName().equals(dishName)) {
                return food.getCopy();
            }
        }
        //Then check combos
        for (Combo combo : defaultCombos) {
            if (combo.getName().equals(dishName)) {
                return combo.getCopy();
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("No menu item corresponding to " + dishName);
        alert.showAndWait();
        return null;
    }

    /**
     * Returns a dish which corresponds to its menu item number. This method works when all dishes have different
     * menu numbers. Returns null if no dish is found.
     *
     * @param menuItemNumber The menu item number
     * @return A dish corresponding to menuItemNumber
     */
    public Recipe getDefaultDish(int menuItemNumber) {
        //Check foods first
        for (Food food : defaultFoods) {
            if (food.getItemNum() == menuItemNumber) {
                return food.getCopy();
            }
        }
        for (Combo combo : defaultCombos) {
            if (combo.getItemNum() == menuItemNumber) {
                return combo.getCopy();
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("No menu item number corresponding to " + menuItemNumber);
        alert.showAndWait();
        return null;
    }

}
