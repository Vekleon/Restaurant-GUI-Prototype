package GUI.Controllers;

import GUI.LayoutFactory;
import GUI.Screen.TableOrderScreen;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import restaurant.Restaurant;
import restaurant.Table;

public class TableController {
    private Table[] tables;
    private TableOrderScreen[] tableOrderScreens;

    /**
     * Create a table controller to be used with table screen
     *
     * @param tables The table reference from the restaurant
     */
    public TableController(Table[] tables) {
        this.tables = tables;
    }

    /**
     * Initializes and returns the table order screens which are related to each table
     *
     * @param stage The stage to set for each order screen
     * @return The table order screens
     */
    public TableOrderScreen[] getTableOrderScreens(Stage stage, Restaurant restaurant) {
        //Create an order screen for every table
        tableOrderScreens = new TableOrderScreen[tables.length];
        for (int i = 0; i < tableOrderScreens.length; i++) {
            //Create a controller for the table orders
            TableOrderController tableOrderController = new TableOrderController(restaurant, tables[i]);
            tableOrderScreens[i] = new TableOrderScreen(stage, tableOrderController);
        }
        return tableOrderScreens;
    }

    /**
     * Updates the table buttons depending on their status
     */
    public void updateTableButtons() {
        for (Table table : tables) {
            //Assign the relevant style
            table.getTableButton().getStyleClass().clear();
            table.getTableButton().getStyleClass().add("button");
            table.getTableButton().getStyleClass().addAll(table.getStatus(), "table");
        }
    }

    private void setTableButtonEvents() {
        for (Table table : tables) {
            table.getTableButton().setOnAction(e -> goToTableOrder(table.getTableNumber()));
        }
    }

    /**
     * Returns the table layout for these tables
     */
    public GridPane getTableLayout() {
        GridPane tableLayout = new LayoutFactory().getTableLayout(tables);
        setTableButtonEvents();
        return tableLayout;
    }

    /**
     * Switch window to order screen with corresponding table number
     *
     * @param tableNum The table number to switch to
     */
    public void goToTableOrder(int tableNum) {
        TableOrderScreen correspondingOrder = getTableOrderScreen(tableNum);
        if (correspondingOrder != null) {
            correspondingOrder.show();
        }
    }


    /**
     * Returns the table order screen corresponding to a certain table number. Returns null if no such table exists.
     *
     * @param tableNum The table number to find
     * @return The corresponding table order screen
     */
    private TableOrderScreen getTableOrderScreen(int tableNum) {
        for (TableOrderScreen tableOrderScreen : tableOrderScreens) {
            if (tableOrderScreen.getTableNumber() == tableNum) {
                return tableOrderScreen;
            }
        }
        return null;
    }

    /**
     * Update table order screens
     */
    public void updateOrderScreens(){
        for (TableOrderScreen tableOrderScreen: tableOrderScreens){
            tableOrderScreen.update();
        }
    }
}
