package GUI;

import GUI.Screen.Screen;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import restaurant.Order;
import restaurant.Table;

import java.util.ArrayList;

/**
 * A factory which produces layouts which would be used often
 */
public class LayoutFactory {
    /**
     * Sizes, then returns a back button on an HBox which is on the right, which takes up one tenth of the width
     *
     * @param backButton The back button in the layout
     * @return An HBox with a back button on the far right
     */
    public HBox getBackButtonLayout(Button backButton) {
        backButton.setMinWidth(100);
        HBox finalLayout = new HBox();
        Pane space = new Pane();
        space.setMinSize(Screen.WIDTH - backButton.getMinWidth(), 1);
        finalLayout.getChildren().addAll(space, backButton);
        return finalLayout;
    }

    // the fix is to do an if statement using a string provided by the parameter to see which screen the person is on.
    // using this we can determine which button is not needed.
    public Button[] navigationBar(Button buttonOne, Button buttonTwo, Button buttonThree,
                                  String screen) {
        switch (screen) {
            case "tables":
                buttonSetUp(buttonOne, "Orders");
                buttonSetUp(buttonTwo, "Inventory");
                buttonSetUp(buttonThree, "Employees");
                break;
            case "orders":
                buttonSetUp(buttonOne, "Tables");
                buttonSetUp(buttonTwo, "Inventory");
                buttonSetUp(buttonThree, "Employees");
                break;
            case "inventory":
                buttonSetUp(buttonOne, "Tables");
                buttonSetUp(buttonTwo, "Orders");
                buttonSetUp(buttonThree, "Employees");
                break;
            case "employees":
                buttonSetUp(buttonOne, "Tables");
                buttonSetUp(buttonTwo, "Orders");
                buttonSetUp(buttonThree, "Inventory");
                break;
        }
        buttonOne.getStyleClass().add("navigationButton");
        buttonTwo.getStyleClass().add("navigationButton");
        buttonThree.getStyleClass().add("navigationButton");

        return new Button[]{buttonOne, buttonTwo, buttonThree};
    }

    public void buttonSetUp(Button button, String name){
        button.setText(name);
        button.setMinWidth(Screen.WIDTH / 10);
    }

    /**
     * Returns a grid layout of the tables and sets table buttons for each table
     *
     * @param tables The buttons of the tables to be laid out
     * @return The layout
     */
    public GridPane getTableLayout(Table[] tables) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(5);
        int currRow = 0;
        int currCol = 0;
        for (Table table : tables) {
            //Make the button associated with this table
            Button currentTableButton = new Button();
            currentTableButton.setMinWidth(235);
            currentTableButton.setMinHeight(85);
            //Set col/row of this button on grid
            GridPane.setConstraints(currentTableButton, currCol, currRow);
            currentTableButton.setText("Table " + Integer.toString(table.getTableNumber()));
            //Set table as unoccupied for CSS
            currentTableButton.getStyleClass().add("unoccupied");
            currentTableButton.getStyleClass().add("table");
            grid.getChildren().add(currentTableButton);
            table.setTableButton(currentTableButton);

            //Adjust next row/ col
            if (currCol < 3) {
                currCol++;
            } else {
                currCol = 0;
                currRow++;
            }
        }
        return grid;
    }

    /**
     * Creates a layout for the order for the OrderScreen
     * @param orders the orders of the restaurant.
     * @return the layout of the orders.
     */
    public static GridPane getOrderLayout(ArrayList<Order> orders){
        GridPane grid = new GridPane();
        grid.getStyleClass().add("orderGrid");
        int currRow = 0;
        int currCol = 0;
        for(Order order: orders){
            GridPane.setConstraints(order.getOrderButton(), currCol, currRow);
            grid.getChildren().add(order.getOrderButton());

            if(currCol < 3){
                currCol++;
            }
            else{
                currCol = 0;
                currRow++;
            }
        }

        return grid;
    }
}
