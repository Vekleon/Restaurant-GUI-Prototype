package restaurant;

import dishes.Recipe;
import employees.Chef;

import java.util.ArrayList;
import javafx.scene.control.Button;

/**
 * This class represents a customer's order
 *
 * @author Jian Xian Li (Jason Li)
 */
public class Order {
    private int tableNumber;
    private ArrayList<Recipe> dishOrders;
    private Menu menu;
    private Button orderButton;
    private boolean acknowledge;
    private Chef designatedChef;

    /**
     * Create an order for a table
     *
     * @param tableNumber The table number
     * @param menu        The current menu which this order references
     */
    public Order(int tableNumber, Menu menu) {
        this.tableNumber = tableNumber;
        dishOrders = new ArrayList<>();
        this.menu = menu;
        acknowledge = false;
    }

    /**
     * Adds an already created dish to this order
     *
     * @param dish The dish to be added
     */
    public void addDish(Recipe dish) {
        dishOrders.add(dish);
    }

    /**
     * Adds a default dish (default values) from the menu onto this order. If the dish is not on the menu, an error
     * will display.
     *
     * @param dishName The name of this food (must be on the menu)
     * @return the dish that was just added
     */
    public Recipe addDish(String dishName) {
        Recipe dish = menu.getDefaultDish(dishName);
        if (dish != null) {
            dishOrders.add(dish);
        }
        return dish;
    }

    /**
     * Adds a dish based on its menu item number. For food, it is the first value on the menu in its line.
     * For combos, it is the number after the "C" in its line.
     *
     * @param menuItemNumber The menu item number.
     * @return the dish that was just added
     */
    public Recipe addDish(int menuItemNumber) {
        Recipe dish = menu.getDefaultDish(menuItemNumber);
        if (dish != null) {
            dishOrders.add(dish);
        }
        return dish;
    }

    /**
     * Removes the specific dish that is already on this order.
     *
     * @param dish The dish to be removed
     */
    public void removeDish(Recipe dish) {
        dishOrders.remove(dish);
    }

    /**
     * Removes a dish that is already on this order. If more than one dish of this name exists, the first dish
     * is removed. An error is displayed if the dish specified is not ordered.
     *
     * @param dishName The dish to be removed
     */
    public void removeDish(String dishName) {
        removeDish(dishName, 1);
    }

    /**
     * Removes a food that is already on this order. The position in which this food was ordered must be specified.
     * If a food in the order position exists, this removes that food. If not, an error is displayed.
     *
     * @param dishName The dish to be removed
     * @param i        The position in which the instance to be deleted is ordered (first = 1, second = 2, ...)
     */
    public void removeDish(String dishName, int i) {
        int current = 1;
        for (Recipe dish : dishOrders) {
            if (dishName.equals(dish.getName())) {
                if (current == i) {
                    dishOrders.remove(dish);
                    return;
                } else {
                    current++;
                }
            }
        }
        if (current == 1) {
            System.err.println("Menu item " + dishName + " does not exist in this order");
        } else {
            System.err.println("Menu item " + dishName + " does not exist in position " + i + " of this order.");
        }
    }

    /**
     * Returns the first dish of this name that was added to the order. Returns null if no dish by the name exists.
     *
     * @param dishName The name of the dish
     * @return The dish of this name
     */
    public Recipe getDish(String dishName) {
        return getDish(dishName, 1);
    }

    /**
     * Returns the i'th dish of this name that was added to the order. This is meant to be used when a table has
     * ordered 2 or more of the same dish. Returns null if no dish exists that was ordered in the specified position.
     *
     * @param dishName The name of the dish
     * @param i        The number of the dish. ex. 1 if it was added first, 2 if it was added second
     * @return The dish of this name
     */
    public Recipe getDish(String dishName, int i) {
        int current = 1;
        for (Recipe dish : dishOrders) {
            if (dishName.equals(dish.getName())) {
                if (current == i) {
                    return dish;
                } else {
                    current++;
                }
            }
        }
        return null;
    }

    /**
     * Returns the first dish on this order based on an item number. A dish's default
     * item number is its menu item number. If no dish exists with specified item number, null is returned.
     *
     * @param itemNum The item number of the dish
     * @return The dish with itemNum as its identifying number
     */
    public Recipe getDish(int itemNum) {
        return getDish(itemNum, 1);
    }

    /**
     * Returns the i'th dish of this item number that was added to the order.
     * Returns null if no dish exists that was ordered in the specified position.
     *
     * @param itemNum The name of the food
     * @param i       The number of the dish. ex. 1 if it was added first, 2 if it was added second
     * @return The dish of this item number on i'th position ordered
     */
    public Recipe getDish(int itemNum, int i) {
        int current = 1;
        for (Recipe dish : dishOrders) {
            if (itemNum == dish.getItemNum()) {
                if (current == i) {
                    return dish;
                } else {
                    current++;
                }
            }
        }
        return null;
    }

    /**
     * Returns a the dishes on this order which are of a certain status
     *
     * @param status The status to return (waiting, acknowledged, prepared, delivered)
     * @return An array list of the dishes of that status of this order
     */
    public ArrayList<Recipe> getStatusDishes(String status) {
        ArrayList<Recipe> finalList = new ArrayList<>();
        for (Recipe dish : dishOrders) {
            if (dish.getStatus().equals(status)) {
                finalList.add(dish);
            }
        }
        return finalList;
    }

    public ArrayList<Recipe> getDishes(){
        return dishOrders;
    }

    /**
     * Returns the table number of this particular order
     *
     * @return The table number
     */
    public int getTableNumber() {
        return tableNumber;
    }

    public void setOrderButton(Button orderButton){
        this.orderButton = orderButton;
    }

    public Button getOrderButton(){
        return orderButton;
    }

    public void setAcknowledge(){
        acknowledge = true;
    }

    public boolean getAcknowledge(){
        return acknowledge;
    }

    public void setDesignatedChef(Chef chef){
        designatedChef = chef;
    }

    public Chef getDesignatedChef(){
        return designatedChef;
    }
}
