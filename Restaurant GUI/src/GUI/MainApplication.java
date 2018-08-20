package GUI;

import GUI.Controllers.EmployeeController;
import GUI.Controllers.InventoryController;
import GUI.Controllers.SwitchController;
import GUI.Controllers.TableController;
import GUI.Screen.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import restaurant.Restaurant;

import java.util.ArrayList;

public class MainApplication extends Application {
    private Restaurant restaurant;
    private TableScreen tableScreen;
    private OrderScreen orderScreen;
    private InventoryScreen inventoryScreen;
    private EmployeeScreen employeeScreen;
    private TableController tableController;
    private TableOrderScreen[] tableOrderScreens;
    private ArrayList<Screen> screens;
    private Stage mainStage;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        stage.setTitle("Restaurant");

        // create model, view, and controller
        // Acting model
        restaurant = new Restaurant();
        // views
        tableScreen = new TableScreen(stage);
        orderScreen = new OrderScreen(stage, restaurant.getChefOrders(), restaurant.getEmployees());
        inventoryScreen = new InventoryScreen(stage);
        employeeScreen = new EmployeeScreen(stage);

        //Set up table controller
        tableController = new TableController(restaurant.getTables());
        //Set up table order screens
        tableOrderScreens = tableController.getTableOrderScreens(stage, restaurant);

        //Set up array for easier implementing
        setArrayScreens();
        //Set all the necessary controllers
        setControllers();

        //Finally, initialize screens after all the controllers work
        for (Screen screen : screens) {
            screen.initialize();
        }

        // Show main screen
        tableScreen.show();
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Helper method to set controllers
     */
    private void setControllers() {
        // Create the switch controller and set up for every screen
        SwitchController switchController =
                new SwitchController(inventoryScreen, orderScreen, tableScreen, employeeScreen);
        for (Screen screen : screens) {
            screen.initSwitcher(switchController);
        }
        tableScreen.initTableController(tableController);

        //Set up inventory controller
        InventoryController inventoryController = new InventoryController(restaurant.getInventory(),
                restaurant.getEmployees());
        inventoryScreen.initInventoryController(inventoryController);

        //Set up employee controller
        EmployeeController employeeController = new EmployeeController(restaurant.getEmployees(),
                restaurant.getInventory(), restaurant.getChefOrders());
        employeeScreen.initEmployeeController(employeeController);
    }

    /**
     * Helper method to set up the array of all the screens
     */
    private void setArrayScreens() {
        screens = new ArrayList<>();
        screens.add(tableScreen);
        screens.add(inventoryScreen);
        screens.add(orderScreen);
        screens.add(employeeScreen);
        for (Screen screen : tableOrderScreens) {
            screens.add(screen);
        }
    }

    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    }
}
