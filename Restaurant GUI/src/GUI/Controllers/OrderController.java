package GUI.Controllers;

import GUI.LayoutFactory;
import GUI.Screen.ChefOrderScreen;
import employees.Employee;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import restaurant.Order;

import java.util.ArrayList;

public class OrderController {
    private ArrayList<Order> orders;
    private Stage stage;
    private ArrayList<Employee> employees;
    public OrderController(Stage stage, ArrayList<Order> orders, ArrayList<Employee> employees){
        this.orders = orders;
        this.stage = stage;
        this.employees = employees;
    }

    /**
     * Checks to see if the order already has a button assigned to it, if not make a new one and assign it as screen.
     */
    public void createOrderScreens(SwitchController switchController){
        for(Order order: orders){
            if(order.getOrderButton() == null){
                Button orderScreen = new Button();
                orderScreen.setMinWidth(235);
                orderScreen.setMinHeight(85);
                orderScreen.getStyleClass().add("chefOrderInitial");
                orderScreen.getStyleClass().add("order");
                orderScreen.setText("Order for Table: " + order.getTableNumber());
                ChefOrderScreen screen = new ChefOrderScreen(stage, switchController,
                        new ChefOrderController(order, employees, orderScreen));

                orderScreen.setOnAction(e-> screen.show());
                order.setOrderButton(orderScreen);
            }
        }
    }

    /**
     * Checks if there are orders that are finished, if so, it will remove those orders
     */
    public void updateOrders(){
        for (Order order : orders){
            if (order.getDishes().size() == order.getStatusDishes("delivered").size()
                    + order.getStatusDishes("cancelled").size()){
                orders.remove(order);
            }
        }
    }

    // get the layout of the screen.
    public GridPane getOrderLayout(){
        return LayoutFactory.getOrderLayout(orders);
    }
}
