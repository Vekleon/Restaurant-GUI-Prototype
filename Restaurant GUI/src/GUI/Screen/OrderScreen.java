package GUI.Screen;

import GUI.Controllers.OrderController;
import employees.Employee;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import restaurant.Order;

import java.util.ArrayList;

public class OrderScreen extends Screen {
    private OrderController orderController;
    private Button backButton;
    private Button tableButton;
    private Button inventoryButton;
    private Button employeeButton;

    /**
     * Construct a new order screen
     */
    public OrderScreen(Stage stage, ArrayList<Order> orders, ArrayList<Employee> employees) {
        super(stage);
        backButton = new Button();
        tableButton = new Button();
        inventoryButton = new Button();
        employeeButton = new Button();
        setBackButtonEvent();

        orderController = new OrderController(stage, orders, employees);
    }

    @Override
    public Scene getScene() {
        Button[] buttons = layoutFactory.navigationBar(tableButton, inventoryButton, employeeButton,
                "orders");
        orderController.createOrderScreens(this.switchController);

        BorderPane overallLayout = new BorderPane();
        GridPane orderLayout = orderController.getOrderLayout();
        ScrollPane orderScroller = new ScrollPane();
        orderScroller.setContent(orderLayout);
        orderScroller.setMinWidth(WIDTH);

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(buttons);

        overallLayout.setBottom(buttonBox);
        overallLayout.setCenter(orderScroller);

        Scene scene = new Scene(overallLayout, WIDTH, HEIGHT);
        scene.getStylesheets().add("style.css");

        return scene;
    }

    @Override
    public void update() {
        orderController.updateOrders();
    }

    @Override
    public void initialize() {
        setBackButtonEvent();
        inventoryButton.setOnAction(e-> switchController.goToInventory());
        tableButton.setOnAction(e-> switchController.goToTable());
        employeeButton.setOnAction(e-> switchController.goToEmployee());
    }

    /**
     * Sets up the back button
     */
    private void setBackButtonEvent(){
        backButton.setOnAction(e -> switchController.goToTable());
    }
}
