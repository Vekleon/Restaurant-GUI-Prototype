package employees;


import GUI.BillCreator;
import GUI.Controllers.LogWriter;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import dishes.*;
import javafx.stage.Stage;
import restaurant.Bill;
import restaurant.Menu;
import restaurant.Order;
import dishes.DishInterpreter;

import java.util.ArrayList;

/**
 * The server class. Can create orders and give them to the chef. Will acknowledge when the order/food is delivered.
 * Has the ability to create bills.
 *
 * @author Victor Huang
 */
public class Server extends Employee {

    private static int numOfServers = 0;
    private ArrayList<Order> orders;

    public Server() {
        super(false, "Server", numOfServers + 1);
        this.orders = new ArrayList<>();
        numOfServers++;
    }

    public Server(int oldId) {
        super(false, "Server", oldId);
        this.orders = new ArrayList<>();
    }

    /**
     * Initializes an order and adds the table to this server's responsibilities
     *
     * @param tableNumber The table number that corresponds to the specific order
     * @param menu        The restaurant's menu
     * @return The order so that it can be held in the restaurant's database
     */
    public Order createOrder(int tableNumber, Menu menu) {
        for (Order currOrder : orders) {
            if (currOrder.getStatusDishes("prepared").size() > 0) {
                return null;
            }
        }
        Order order = new Order(tableNumber, menu);
        orders.add(order);
        return order;
    }

    /**
     * Adds a specific dish to the specified order.
     *
     * @param order the order that is being updated
     * @param dish  the dish that is being added to the order
     * @return the dish that has been added to the order
     */
    public Recipe addToOrder(Order order, String dish) throws Exception {
        Recipe currentDish;
        if (dish.matches("[0-9]+")) {
            currentDish = order.addDish(Integer.parseInt(dish));
        } else {
            currentDish = order.addDish(dish);
        }
        if (currentDish == null) {
            return null;
        } else if (!currentDish.isPossible()) {
            order.removeDish(currentDish);
            throw new Exception("Not enough ingredients to make this dish. It will not be added to the order.");
        }
        LogWriter.writeIn("Server " + getJobId() + " has added " + dish + " to table "
                + order.getTableNumber() + "'s order.");
        return currentDish;
    }

    /**
     * Allows customers to make accommodations to their dishes by adding extra ingredients they want or taking away the
     * ingredients they do not want. You can only add or subtract existing ingredients.
     *
     * @param dish       the dish that accommodations are being made on
     * @param foodName   the specific dishes' name
     * @param ingredient the ingredient being added or subtracted
     * @param quantity   the amount that is being added or subtracted
     */
    public void makeAccommodation(Recipe dish, String foodName, String ingredient, int quantity) throws Exception {
        int accommodationAmount = quantity;
        if (accommodationAmount < 0) {
            if (dish.getIngredients().get(ingredient) < accommodationAmount * -1) {
                accommodationAmount = dish.getIngredients().get(ingredient) * -1;
            }
            dish.subtractIngredient(ingredient, accommodationAmount * -1);
        } else {
            dish.addIngredient(ingredient, accommodationAmount);
        }
        if (dish.isPossible()) {
            if (dish instanceof Combo) {
                String previousInstructions = ((Combo) dish).getInstructions().get(foodName);
                if (previousInstructions.equals("")) {
                    ((Combo) dish).setInstructions(foodName, accommodationAmount + ": " + ingredient);
                } else {
                    ((Combo) dish).setInstructions(foodName,
                            instructionUpdater(previousInstructions, ingredient, accommodationAmount));
                }
            } else {
                String previousInstructions = ((Food) dish).getInstructions();
                if (previousInstructions.equals("")) {
                    ((Food) dish).setInstructions(accommodationAmount + ": " + ingredient);
                } else {
                    ((Food) dish).setInstructions(instructionUpdater(previousInstructions, ingredient,
                            accommodationAmount));
                }
            }
        } else {
            dish.subtractIngredient(ingredient, accommodationAmount);
            throw new Exception();
        }
    }

    /**
     * Removes the specified dish from the order
     *
     * @param order the order that the dish is being removed from
     * @param dish  the dish that is being removed
     */
    public void removeFromOrder(Order order, Recipe dish) {
        dish.setStatus("cancelled");
        order.removeDish(dish);
        LogWriter.writeIn("Server " + getJobId() + " has removed " + dish.getName() + " from the order.");
    }

    /**
     * Allows the order to be confirmed. It will log the confirmation to the logger accordingly.
     *
     * @param order      The order that is ready to be sent of to the chefs
     * @param chefOrders The global list of orders that the chef look at
     */
    public void confirmOrder(Order order, ArrayList<Order> chefOrders) {
        if (order.getStatusDishes("waiting").size() > 0) {
            chefOrders.add(order);
            StringBuilder confirmLog = new StringBuilder(200);
            String beginLog = "Server " + getJobId() + " has just taken the following order for table " +
                    order.getTableNumber() + ":\n";
            confirmLog.append(beginLog);
            for (Recipe dish : order.getStatusDishes("waiting")) {
                confirmLog.append(DishInterpreter.dishToString(dish));
                confirmLog.append("\n");
            }
            LogWriter.writeIn(confirmLog.toString());
        }
    }

    /**
     * This method has the server confirm that he food has been delivered to the table
     *
     * @param dish The string representation of the food that has been delivered
     */
    public void foodDelivered(Order order, Recipe dish) {
        dish.setStatus("delivered");
        LogWriter.writeIn("Server " + getJobId() + " has successfully delivered " +
                DishInterpreter.dishToString(dish) + " to table " + order.getTableNumber() + ".");
    }

    /**
     * Removes the table from this server's responsibilities and prints the bill
     *
     * @param order The order that needs to be finished
     */
    public void removeOrder(Order order) {
        for (Recipe dish : order.getDishes()) {
            if (!dish.getStatus().equals("cancelled")) {
                dish.setStatus("cancelled");
            }
        }
        orders.remove(order);
    }

    /**
     * Will take a complaint of a customer and will make accommodations to the dish accordingly
     *
     * @param complaint The complaint from the customer
     * @param dish      The dish that needs to be dealt with
     */
    public void complaint(Order order, String complaint, Recipe dish) {
        dish.sendBack();
        LogWriter.writeIn("Server " + getJobId() + " has received a complaint for the dish" +
                DishInterpreter.dishToString(dish) + " from table " + order.getTableNumber()
                + " because " + complaint + ".");
    }

    /**
     * Helper method that will return a string used to update the instructions of a dish.
     *
     * @param oldInstructions the current instructions of the dish that will be updated
     * @param ingredient      the ingredient that needs to be updated in the instructions
     * @param quantity        the amount that of the ingredient that needs to be updated
     * @return the updated instructions with the accommodations made
     */
    private String instructionUpdater(String oldInstructions, String ingredient, int quantity) {
        String[] instructions = oldInstructions.split(",\\s");
        String currentInstruction = "";
        for (String instruction : instructions) {
            if (instruction.contains(ingredient)) {
                currentInstruction = instruction;
                break;
            }
        }
        if (currentInstruction.equals("")) {
            return oldInstructions + ", " + quantity + ": " + ingredient;
        } else {
            int instructionIndex = oldInstructions.indexOf(currentInstruction);
            String instructionPiece1 = oldInstructions.substring(0, instructionIndex);
            String instructionPiece2 = oldInstructions.substring(instructionIndex + currentInstruction.length());
            int newQuantity = Integer.parseInt(currentInstruction.split(":\\s")[0]) + quantity;
            if (newQuantity == 0) {
                if (instructionPiece1.equals("")) {
                    return "";
                } else if (instructionPiece2.equals("")) {
                    return instructionPiece1.substring(0, instructionPiece1.length() - 2);
                }
                return instructionPiece1.substring(0, instructionPiece1.length() - 2) + instructionPiece2;
            }
            return instructionPiece1 + newQuantity + ": " + ingredient + instructionPiece2;
        }
    }

    /**
     * This method creates the bill and the bill popup screen. If there is only one ArrayList in dishes, there
     * is only one customer, otherwise, there are as many customers as there are ArrayLists in dishes.
     *
     * @param dishes Holds ArrayLists of all the dishes
     * @return the billPopUp screen
     */
    public Stage getBillPopUp(ArrayList<ArrayList<Recipe>> dishes) {
        return getBillPopUp(dishes, dishes.size());
    }

    /**
     * This method creates the bill and the bill popup screen. Here, you can indicate the number of customers,
     * which is usually used to see if there is guaranteed gratuity on the bill.
     *
     * @param dishes            Holds ArrayLists of all the dishes
     * @param numberOfCustomers the number of customers at the table
     * @return the billPopUp screen
     */
    public Stage getBillPopUp(ArrayList<ArrayList<Recipe>> dishes, int numberOfCustomers) {
        Stage billPopUp;
        Bill bill = new Bill(dishes, numberOfCustomers);
        String[] bills = bill.printBill();
        BillCreator billCreator = new BillCreator(this);
        if (bills.length == 1) {
            billPopUp = billCreator.singleBillStage(bills[0]);
        } else {
            billPopUp = billCreator.multipleBillStage(bills);
        }
        return billPopUp;
    }

    /**
     * This is an method that is called when a bill payment has been confirmed. It will add up the total of all the
     * bills and log it appropriately in a text file.
     *
     * @param billPopUp The bill popup screen
     * @param bills     The array of bills
     */
    public void billPayment(Stage billPopUp, String[] bills) {
        double totalPayment = 0.0;
        for (String bill : bills) {
            String finalLine = bill.split("\n")[bill.split("\n").length - 1];
            totalPayment += Double.parseDouble(finalLine.split("\\s+")[1]);
        }
        LogWriter.earningLogger("Received $" + totalPayment + ".");
        billPopUp.close();
    }

    public static int getNumOfServers() {
        return numOfServers;
    }

    public static void setNumOfServers(int numServers) {
        numOfServers = numServers;
    }
}
