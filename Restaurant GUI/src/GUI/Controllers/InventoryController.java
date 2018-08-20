package GUI.Controllers;

import employees.Employee;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import restaurant.Inventory;

import java.util.ArrayList;

public class InventoryController {
    private Inventory inventory;
    private ArrayList<Employee> employees;

    /**
     * initializes a new instance of InventoryController
     * @param inventory the inventory within the restaurant
     * @param employees the employees working in the restaurant
     */
    public InventoryController(Inventory inventory, ArrayList<Employee> employees){
        this.inventory = inventory;
        this.employees = employees;
    }

    // gets the current name, quantity and threshold of the inventory
    public String getCurrentIngredients(){
        return inventory.toString();
    }

    /**
     * Used to write into the Request.txt
     * @param ingredient the text field that holds the user input for ingredient name
     * @param quantity the text field that holds the user input for quantity.
     * @param message the message that is being displayed to the user.
     */
    public void request(TextField ingredient, TextField quantity, Label message){
        //Checks to see if text boxes are filled
        if(textFilled(ingredient, quantity)) {
            //Checks to see if the input was accepted and was added to the Requests.txt
            if (inventory.manualRequest(ingredient.getText(), Integer.valueOf(quantity.getText()))) {
                message.setText("Successfully added " + quantity.getText() + " " +
                        ingredient.getText() + " to the requests.");
                ingredient.clear();
                quantity.clear();
            }
            else{
                message.setText("Invalid input!");
            }
        }
        else{
            message.setText("Please fill in all fields.");
        }
    }

    /**
     * Used to restock ingredients into the inventory based off user input.
     * @param ingredient the text field that contains the user input for ingredient
     * @param quantity the text field that contains the user input for quantity.
     * @param message the message that will be displayed to the user.
     * @param ingredients the display text that shows the inventory item names, quantity and threshold.
     */
    public void restock(TextField ingredient, TextField quantity, Label message, Text ingredients){
        if(textFilled(ingredient, quantity)){
            // Checks to see if the input was accepted and ingredients were properly added to the inventory.
            if (inventory.addIngredient(ingredient.getText(), Integer.valueOf(quantity.getText()))){
                message.setText("Successfully added " + quantity.getText() + " " +
                        ingredient.getText() + " to the inventory.");
                ingredients.setText(inventory.toString());
                ingredient.clear();
                quantity.clear();
            }
            else
            {
                message.setText("Invalid input!");
            }
        }
        else{
            message.setText("Please fill in all fields.");
        }
    }

    //Checks to see if the text fields were filled and returns a boolean to determine if so.
    /**
     * Title: Using JavaFX UI Controls
     * Author: Alla Redko
     * Date: ?/09/2013
     * Code Version: JavaFx 2.2
     * Type: source code
     *Availability: https://docs.oracle.com/javafx/2/ui_controls/text-field.htm
     */
    private boolean textFilled(TextField ingredientInput, TextField quantityInput){
        return ((ingredientInput.getText() != null && quantityInput != null ) &&
                (!ingredientInput.getText().isEmpty() && !quantityInput.getText().isEmpty()));
    }

    public boolean findEmployee(String employeeId){
        for(Employee employee: employees){
            if(employee.toString().equals(employeeId)){
                return employee.getReceiver();
            }
        }
        return false;
    }
}
