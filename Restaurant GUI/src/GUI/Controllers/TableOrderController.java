package GUI.Controllers;

import dishes.Combo;
import dishes.Food;
import dishes.Recipe;
import employees.Server;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.Table;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used to add foods to orders, deal with servers
 *
 * @author Jian Xian Li
 */
public class TableOrderController {
    private Restaurant restaurant;
    private Table table;
    private HashMap<Recipe, IndividualDishController> dishControllerMap;
    private BillController billController;

    /**
     * Create a table order controller.
     *
     * @param restaurant The restaurant
     * @param table      The table which this controller represents
     */
    TableOrderController(Restaurant restaurant, Table table) {
        this.restaurant = restaurant;
        this.table = table;
        dishControllerMap = new HashMap<>();
        billController = new BillController(table);
    }

    /**
     * Attempt to initialize a table with a server id. Will initialize if server id exists, will not otherwise.
     * If successful, table status changes to occupied and the screen updates.
     *
     * @param serverId The server id of the server attempting to initialize the table
     * @return 0 if initialization successful, 1 if serverID failed, 2 if customerNumber failed
     */
    public int attemptInitialize(int serverId, int customerNumber) {
        Server server = restaurant.getServer(serverId);
        if (server != null && customerNumber <= table.getSize()) {
            Order newOrder = server.createOrder(table.getTableNumber(), restaurant.getMenu());
            if (newOrder == null) {
                return 3;
            }
            table.setStatus("occupied");
            table.setServer(server);
            table.setOrder(newOrder);
            table.getTableButton().setText(table.getTableButton().getText() + "\nServer " + serverId);
            table.setNumberOfOccupants(customerNumber);
            return 0;
        } else if (server == null) {
            return 1;
        } else {
            return 2;
        }
    }


    /**
     * Adds dish and updates order panel
     *
     * @param orderPanel The order panel to be updated
     * @param dishName   The dish to add
     */
    public void addDish(VBox orderPanel, String dishName) throws Exception{
        Recipe dish = table.getServer().addToOrder(table.getOrder(), dishName);
        if (dish != null) {
            HBox dishLine = new HBox();
            IndividualDishController dishController = new IndividualDishController(orderPanel, dishLine, dish, table);
            Label dishText = new Label(dish.getName());
            dishText.setMinWidth(200);
            //Create and assign events to buttons
            //Ingredients button
            Button ingButton = new Button("+/- Ingredients");
            ingButton.setMinWidth(200);
            ingButton.getStyleClass().add("foodOption");
            if (dish instanceof Food) {
                Stage foodIngredientPopUp = dishController.getFoodIngredientsStage();
                ingButton.setOnAction(e -> foodIngredientPopUp.show());
            } else if (dish instanceof Combo) {
                Stage comboIngredientPopUp = dishController.getComboIngredientsStage(restaurant.getMenu());
                ingButton.setOnAction(e -> comboIngredientPopUp.show());
            }
            //Cancel button
            Button cancelButton = new Button("X");
            cancelButton.getStyleClass().add("cancel");
            cancelButton.setOnAction(e -> dishController.cancelDish(orderPanel));
            //Finalize dish line and add to order panel
            dishLine.setSpacing(5);
            dishLine.getChildren().addAll(dishText, ingButton, cancelButton);
            orderPanel.getChildren().add(dishLine);
            //Map this dish to its individual dish controller
            dishControllerMap.put(dish, dishController);
        }
        updateTableStatus();
    }


    /**
     * Return the table associated with this controller
     */
    public Table getTable() {
        return table;
    }

    /**
     * Updates table status
     */
    public void updateTableStatus() {
        //Unoccupied and billed status always set to occupied by server
        if (!table.getStatus().equals("unoccupied")) {
            if (table.getOrder().getStatusDishes("prepared").size() > 0) {
                table.setStatus("ready");
            } else if (table.getOrder().getStatusDishes("unconfirmed").size() > 0 ||
                    table.getOrder().getStatusDishes("waiting").size() > 0 ||
                    table.getOrder().getStatusDishes("acknowledged").size() > 0) {
                table.setStatus("waiting");
                //Set to ready if there is an order prepared but not delivered yet
            }
            //Set to occupied if they have no orders yet (delivered dishes is also 0)
            else if (table.getOrder().getStatusDishes("delivered").size() == 0) {
                table.setStatus("occupied");
            } else {
                table.setStatus("completed");
            }
        }
    }

    /**
     * When the server confirms the order, it is consolidated and sent to the chef order screen. An order with
     * all of the unconfirmed foods is sent.
     */
    public void confirmOrder() {
        Order orderToChef = new Order(table.getTableNumber(), restaurant.getMenu());
        ArrayList<Recipe> unconfirmedDishes = table.getOrder().getStatusDishes("unconfirmed");
        for (Recipe dish : unconfirmedDishes) {
            dish.setStatus("waiting");
            orderToChef.addDish(dish);
            // Change View
            IndividualDishController dishController = dishControllerMap.get(dish);
            dishController.insertDeliverButtonAndDeleteButtonAfter();
        }
        table.setStatus("ordered");
        table.getServer().confirmOrder(orderToChef, restaurant.getChefOrders());
    }

    /**
     * Updates the GUI based on dish statuses
     */
    public void updateDishGUI() {
        for (IndividualDishController dishController : dishControllerMap.values()) {
            dishController.updateDishGUI();
        }
    }


    /**
     * Returns this table's bill controller
     *
     * @return The bill controller
     */
    public BillController getBillController() {
        return billController;
    }

    /**
     * Returns the dish controller hash map, which maps recipes to their controllers
     *
     * @return The dish controller hash map
     */
    public HashMap<Recipe, IndividualDishController> getDishControllerMap() {
        return dishControllerMap;
    }

    /**
     * Terminates this table and resets it to unoccupied
     */
    public void terminateTableOrder() {
        table.setStatus("unoccupied");
        table.getServer().removeOrder(table.getOrder());
        table.setOrder(null);
        table.setServer(null);
        table.resetTableButton();
    }

    /**
     * Updates the cancelled orders from outside sources
     */
    public void updateCancelled(){
        if(table.getOrder() != null) {
            ArrayList<Recipe> cancelledDishes = table.getOrder().getStatusDishes("cancelled");
            for (Recipe dish : cancelledDishes) {
                dishControllerMap.get(dish).attemptUnexpectedCancellation();
            }
        }
    }
}
