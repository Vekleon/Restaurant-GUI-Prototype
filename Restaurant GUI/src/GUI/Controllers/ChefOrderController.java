package GUI.Controllers;

import GUI.Screen.ChefOrderScreen;
import GUI.Screen.Screen;
import dishes.Combo;
import dishes.DishInterpreter;
import dishes.Food;
import dishes.Recipe;
import employees.Chef;
import employees.Employee;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import restaurant.Order;

import java.util.ArrayList;

/**
 * Used to manipulate background data for ChefOrderScreen.
 *
 * @author Thomas Leung
 */
public class ChefOrderController {
    private Order order;
    private ArrayList<Employee> employees;
    private Button orderButton;

    /**
     * initialize a controller.
     *
     * @param order       the order in which this controller is responsible for
     * @param employees   the list of employees working in the restaurant.
     * @param orderButton the button used to get to the ChefOrderScreen.
     */
    ChefOrderController(Order order, ArrayList<Employee> employees, Button orderButton) {
        this.order = order;
        this.employees = employees;
        this.orderButton = orderButton;
    }

    /**
     * returns a layout for the dishes in the order
     *
     * @return An array list of HBox's for each dish.
     */
    public ArrayList<HBox> getDishes(ChefOrderScreen chefOrderScreen) {
        ArrayList<HBox> layouts = new ArrayList<>();
        ArrayList<Recipe> dishes = order.getDishes();

        //Create a new button for each dish
        for (Recipe dish : dishes) {
            HBox layout = new HBox();
            layout.setSpacing(5);
            Label dishName = new Label();

            dishName.setText(dish.getName());

            //Set style for the label.
            dishName.setMinWidth(300);
            dishName.getStyleClass().add("acknowledgeLabel");

            //button setup
            Button status = new Button();
            status.getStyleClass().add("acknowledged");
            status.setText("Cooked");
            status.setOnAction(e -> chefOrderScreen.popUpChef(dish));
            dish.setDishButton(status);

            Button showInstructions = new Button();
            showInstructions.setMinWidth(Screen.WIDTH / 10);
            showInstructions.getStyleClass().add("instructions");
            showInstructions.setText("Instructions");
            showInstructions.setOnAction(e -> chefOrderScreen.popUpInstructions(createInstructions(dish)));

            //finalize layout
            layout.getChildren().addAll(dishName, status, showInstructions);
            layouts.add(layout);
        }
        return layouts;
    }

    /**
     * Used to create the VBox for the pop up screen displaying the extra instructions for the order.
     *
     * @param dish the dish whose instructions is being read
     * @return returns the VBox of the instructions for the pop up screen.
     */
    private VBox createInstructions(Recipe dish) {
        VBox instructionLayout = new VBox();
        Label instructions = new Label();
        instructions.setLayoutX(0);
        instructions.setLayoutY(0);

        //If the dish is a combo, then create a label specifically for it.
        if (dish instanceof Combo) {
            instructions.setText(dish.getName() + "\n" + DishInterpreter.chefDishInfo(dish, "    "));
        } else {
            if (!((Food) dish).getInstructions().equals("")) {
                instructions.setText(DishInterpreter.chefDishInfo(dish, ""));
            } else {
                instructions.setText("No instructions.");
            }
        }
        instructionLayout.getChildren().add(instructions);
        return instructionLayout;
    }

    /**
     * Used to find a valid chef to acknowledge the order.
     *
     * @param chefId the id the chef entered into the system.
     * @return a String which tells whether a chef can acknowledge the order.
     */
    public String findChef(String chefId) {
        if (chefId.length() == 0) {
            return "Please enter an ID.";
        }
        for (Employee chef : employees) {
            if (chef instanceof Chef && chef.getJobId() == Integer.valueOf(chefId)) {
                if (!((Chef) chef).acknowledgeOrder(order)) {
                    return "A previous order needs to be acknowledged before this one.";
                }
                return null;
            }
        }
        return "Chef not found.";
    }

    /**
     * Used to find a valid to cook the order.
     *
     * @param chefId the id of the chef entered into the system.
     * @param dish   the dish that is being prepared.
     * @return returns a String which tells whether a proper chef was found to prepare the dish.
     */
    public String findChef(String chefId, Recipe dish) {
        if (chefId.length() == 0) {
            return "Please enter an ID.";
        }
        for (Employee chef : employees) {
            if (chef instanceof Chef && chef.getJobId() == Integer.valueOf(chefId)) {
                return ((Chef) chef).foodCooked(order, dish);
            }
        }
        return "Chef not found.";
    }

    /**
     * Sets the status button of the dish to finish.
     *
     * @param dish the dish that was finished cooking.
     */
    public void cooked(Recipe dish) {
        Button status = dish.getDishButton();
        status.setText("Finished");
        status.getStyleClass().add("finished");
        status.setOnAction(e -> {
        });
        for (Recipe foodItem : order.getDishes()) {
            if (!foodItem.getStatus().equals("prepared")) {
                return;
            }
        }
        orderButton.getStyleClass().add("completed");
    }

    public Order getOrder() {
        return order;
    }

    /**
     * Used to update the order screen to update the food status live.
     *
     * @param chefOrderScreen The screen that is to be manipulated.
     */
    public void updateChefOrderScreen(ChefOrderScreen chefOrderScreen) {
        for (Recipe dish : order.getDishes()) {
            if (dish.getStatus().equals("waiting") && order.getAcknowledge()) {
                dish.setStatus("acknowledged");
                dish.getDishButton().getStyleClass().add("acknowledged");
                dish.getDishButton().setText("Cooked");
                dish.getDishButton().setOnAction(e -> chefOrderScreen.popUpChef(dish));
            }
            else if (dish.getStatus().equals("cancelled")){
                order.removeDish(dish);
            }
        }
    }

}
