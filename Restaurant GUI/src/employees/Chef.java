package employees;


import GUI.Controllers.LogWriter;
import dishes.Recipe;
import dishes.DishInterpreter;
import restaurant.Order;

import java.util.ArrayList;

/**
 * The chef class. Can acknowledge and cook orders. Will indicate when the order is finished or if
 * they have cancelled the order. They can also cancel certain dishes if they are not going to cook them.
 *
 * @author Victor Huang
 */
public class Chef extends Employee {

    private static int numOfChefs = 0;
    private ArrayList<Order> chefOrders;

    public Chef(ArrayList<Order> chefOrders) {
        super(false, "Chef", numOfChefs + 1);
        this.chefOrders = chefOrders;
        numOfChefs++;
    }

    public Chef(int oldId, ArrayList<Order> chefOrders) {
        super(false, "Chef", oldId);
        this.chefOrders = chefOrders;
    }

    /**
     * The chef checks if they have the time to cook the order and will acknowledge that they have received the order
     * if they do have the time
     *
     * @param order The order that the chef will be cooking
     * @return if the order can be cooked by this chef or not
     */
    public boolean acknowledgeOrder(Order order) {
        if (chefOrders.size() > 0) {
            for (Order tempOrder : chefOrders) {
                if (tempOrder == order) {
                    break;
                } else if (!tempOrder.getAcknowledge()) {
                    return false;
                }
            }
        }
        order.setAcknowledge();
        for (Recipe dish : order.getStatusDishes("waiting")) {
            dish.setStatus("acknowledged");
        }
        LogWriter.writeIn("Chef " + getJobId() + " has acknowledged table "
                + order.getTableNumber() + "'s order.");
        return true;
    }

    /**
     * This method has the chef indicate that they have finished cooking the food
     *
     * @param order The order that the dish is from
     * @param dish  The dish that the chef has cooked
     * @return If the food was cooked or not
     */
    public String foodCooked(Order order, Recipe dish) {
        if (dish.getStatus().equals("cancelled")) {
            order.removeDish(dish);
            return "A server has cancelled " + DishInterpreter.dishToString(dish)
                    + " and it has been removed from the order.";
        } else if (!dish.isPossible()) {
            order.removeDish(dish);
            dish.setStatus("cancelled");
            return "Not enough ingredients to make" + DishInterpreter.dishToString(dish)
                    + ". The dish has been removed from the order.";
        }
        for (Order currOrder : chefOrders) {
            if (currOrder.getStatusDishes("waiting").size() > 0
                    && !currOrder.getStatusDishes("waiting").contains(dish)) {
                return "A dish from a previous order has not yet been acknowledged.";
            } else if (currOrder.getStatusDishes("acknowledged").contains(dish)) {
                break;
            }
        }
        dish.create();
        dish.setStatus("prepared");
        LogWriter.writeIn("Chef " + getJobId() + " has finished cooking " + DishInterpreter.dishToString(dish)
                + " from table " + order.getTableNumber() + "'s order.");
        return null;
    }

    /**
     * Removes the order from the chef's responsibilities. This can either be a cancellation of some sort or the chef
     * has completed the order.
     *
     * @param order   The order that the dish is from
     * @param routine Checks if it is a routine completed order, or a cancelled order instead
     */
    public void removeOrder(Order order, boolean routine) {
        if (!routine) {
            String orderLog = "Chef " + getJobId() + " has cancelled the following order:";
            for (Recipe dish : order.getStatusDishes("waiting")) {
                dish.setStatus("cancelled");
                String cancelledLog = DishInterpreter.dishToString(dish);
            }
            for (Recipe dish : order.getStatusDishes("cooking")) {
                dish.setStatus("cancelled");
                String cancelledLog = DishInterpreter.dishToString(dish);
            }
        } else {
            LogWriter.writeIn("Chef " + getJobId() + " has just completed an order for table "
                    + order.getTableNumber() + ".");
        }
        chefOrders.remove(order);
    }

    /**
     * Allows for the chef to cancel a dish
     *
     * @param order The order the dish is from
     * @param dish  The dish that is being cancelled
     */
    public void cancelDish(Order order, Recipe dish) {
        order.removeDish(dish);
        dish.setStatus("cancelled");
        LogWriter.writeIn("Chef" + getJobId() + " has cancelled " + DishInterpreter.dishToString(dish)
                + " from table " + order.getTableNumber() + "'s order.");
    }

    public static int getNumOfChefs() {
        return numOfChefs;
    }

    public static void setNumOfChefs(int numChefs) {
        numOfChefs = numChefs;
    }
}