package GUI.Screen;

import GUI.Controllers.InventoryController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InventoryScreen extends Screen {
    private Button orderButton;
    private Button tableButton;
    private Button employeeButton;
    private Button restock;
    private Button request;
    private Button submitRequest;
    private Button submitRestock;
    private TextField ingredientInput;
    private TextField quantityInput;
    private Text ingredients;
    private Stage popUp;
    private Label message;
    private InventoryController inventoryController;

    /**
     * Construct an inventory screen
     * @param stage The stage of the application
     */
    public InventoryScreen(Stage stage){
        super(stage);
        this.ingredients = new Text();
        this.ingredients.setX(0); this.ingredients.setY(0);
        this.ingredients.setFont(new Font("Monospaced Regular", 14));
        message = new Label("Enter Information.");

        //Text fields
        ingredientInput = new TextField();
        ingredientInput.setPromptText("Enter ingredient name");

        quantityInput = new TextField();
        quantityInput.setPromptText("Enter quantity");

        setTextRestrictions();

        //Buttons
        orderButton = new Button();
        tableButton  = new Button();
        employeeButton = new Button();

        request = new Button();
        layoutFactory.buttonSetUp(request, "Request");
        request.getStyleClass().add("commandButton");

        restock = new Button();
        layoutFactory.buttonSetUp(restock, "Restock");
        restock.getStyleClass().add("commandButton");

        submitRequest = new Button();
        layoutFactory.buttonSetUp(submitRequest, "Submit");

        submitRestock = new Button();
        layoutFactory.buttonSetUp(submitRestock, "Submit");

        //Popup stage
        popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
    }

    @Override
    public Scene getScene() {
        ingredients.setText(inventoryController.getCurrentIngredients());
        Button[] buttons = layoutFactory.navigationBar(tableButton, orderButton, employeeButton,
                "inventory");
        BorderPane overallLayout = new BorderPane();

        HBox textBox = new HBox();
        textBox.getChildren().add(ingredients);

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(buttons);
        buttonBox.getChildren().addAll(restock, request);

        overallLayout.setCenter(textBox);
        overallLayout.setBottom(buttonBox);
        return new Scene (overallLayout, WIDTH, HEIGHT);
    }

    @Override
    public void update() {

    }

    /**
     * Set the functionality of the buttons
     */
    @Override
    public void initialize() {
        orderButton.setOnAction(e-> switchController.goToOrder());
        tableButton.setOnAction(e-> switchController.goToTable());
        employeeButton.setOnAction(e-> switchController.goToEmployee());
        request.setOnAction(e-> popUpScreen(submitRequest));
        restock.setOnAction(e-> popUpEmployee());
        submitRequest.setOnAction(e-> inventoryController.request(ingredientInput, quantityInput, message));
        submitRestock.setOnAction(e-> inventoryController.restock(ingredientInput, quantityInput, message, ingredients));
    }

    /**
     * creates a popup window which will be used to take input to determine which ingredient and how much
     * of it to request for the inventory.
     */
    private void popUpScreen(Button submissionType){
        GridPane grid = new GridPane();
        Label ingredient = new Label("Ingredient: ");
        Label quantity = new Label("Quantity: ");

        GridPane.setConstraints(ingredient, 0, 0);
        GridPane.setConstraints(quantity, 0, 1);
        GridPane.setConstraints(ingredientInput, 1, 0);
        GridPane.setConstraints(quantityInput, 1, 1);
        GridPane.setConstraints(submissionType, 10, 6);
        GridPane.setConstraints(message, 0, 3, 10, 1);

        grid.getChildren().addAll(ingredient, quantity, submissionType, quantityInput, ingredientInput, message);
        Scene scene = new Scene(grid, 490, 200);
        scene.getStylesheets().add("style.css");
        grid.getStyleClass().add("grid");
        popUp.setTitle("Enter ingredient name and quantity.");
        popUp.setScene(scene);
        popUp.show();
    }

    /**
     * Used to get the user to input their id to determine whether they have permission to restock inventory
     */
    private void popUpEmployee() {
        GridPane grid = new GridPane();
        Label id = new Label("Employee Id");

        TextField employeeId = new TextField();
        employeeId.setPromptText("Please enter employee ID");
        employeeId.textProperty().addListener((observable, oldValue, newValue) ->{
            if(!newValue.matches("[CSM][a-z]*_\\d+")){
                employeeId.setText(newValue.replaceAll("[^CSMa-z_\\d]", ""));
            }
        });

        Button submit = new Button();
        submit.getStyleClass().add("plus");
        submit.setText("Submit");
        submit.setMinWidth(WIDTH/10);

        GridPane.setConstraints(id,0,0);
        GridPane.setConstraints(employeeId, 1, 0);
        GridPane.setConstraints(submit, 8, 0);
        grid.getChildren().addAll(id, employeeId, submit);

        Scene scene = new Scene(grid, 480, 50);
        scene.getStylesheets().add("style.css");
        grid.getStyleClass().add("grid");

        popUp.setTitle("Set Receiver");
        popUp.setScene(scene);
        popUp.show();

        submit.setOnAction(e-> {
            if(inventoryController.findEmployee(employeeId.getText())){
                popUpScreen(submitRestock);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR");
                alert.setContentText("EMPLOYEE IS NOT A RECEIVER OR DOES NOT EXIST");
                alert.showAndWait();
            }
        });
    }

    /**
     * sets the restrictions on the text fields.
     * Title: What is the recommended way to make a numeric TextField in JavaFX?
     * Author: Evan Knowles
     * Date: 12/06/15
     * Version: Unknown
     * Type: source code
     * Availability: https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
     */
    private void setTextRestrictions(){
        //restricts this text box to only alphabetical letters.
        ingredientInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[a-zA-Z\\s]*")){
                ingredientInput.setText(newValue.replaceAll("[^a-zA-Z\\s]", ""));
            }
        });

        quantityInput.textProperty().addListener((observable, oldValue, newValue) ->{
            if (!newValue.matches("\\d*")) {
                quantityInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    //sets the inventoryController for this screen.
    public void initInventoryController(InventoryController inventoryController){
        this.inventoryController = inventoryController;
    }
}
