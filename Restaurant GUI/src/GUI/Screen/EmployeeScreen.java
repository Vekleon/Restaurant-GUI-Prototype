package GUI.Screen;

import GUI.Controllers.EmployeeController;
import employees.Employee;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * Used to view possible commands involving employees registered in the restaurant.
 * @author Thomas Leung
 */
public class EmployeeScreen extends Screen{
    private final String PASSWORD = "$im:lI";
    private EmployeeController employeeController;
    private Button orderButton;
    private Button tableButton;
    private Button inventoryButton;
    private Button hireButton;
    private Button fireButton;
    private Button chef;
    private Button manager;
    private Button server;
    private Button confirm;
    private Button cancel;
    private Button setReceiver;
    private Stage popUp;
    private TextField employeeId;
    private TextField input;
    private Text employees;
    private Label message;

    /**
     * instantiates a screen that shows employee information and actions.
     * @param stage the display in which the screen will show.
     */
    public EmployeeScreen(Stage stage){
        super(stage);

        //Buttons
        orderButton = new Button();
        tableButton = new Button();
        inventoryButton = new Button();

        hireButton = new Button();
        layoutFactory.buttonSetUp(hireButton, "Hire");
        hireButton.getStyleClass().add("commandButton");
        hireButton.setMinHeight(HEIGHT/4);

        fireButton = new Button();
        layoutFactory.buttonSetUp(fireButton, "Fire");
        fireButton.getStyleClass().add("commandButton");
        fireButton.setMinHeight(HEIGHT/4);

        setReceiver = new Button();
        layoutFactory.buttonSetUp(setReceiver, "Set receiver");
        setReceiver.getStyleClass().add("commandButton");
        setReceiver.setMinHeight(HEIGHT/4);

        server = new Button();
        layoutFactory.buttonSetUp(server, "Server");
        server.getStyleClass().add("typeButton");

        manager = new Button();
        layoutFactory.buttonSetUp(manager, "Manager");
        manager.getStyleClass().add("typeButton");

        chef = new Button();
        layoutFactory.buttonSetUp(chef, "Chef");
        chef.getStyleClass().add("typeButton");

        confirm = new Button();
        layoutFactory.buttonSetUp(confirm, "Confirm");
        confirm.getStyleClass().add("plus");

        cancel = new Button();
        layoutFactory.buttonSetUp(cancel, "Cancel");
        cancel.getStyleClass().add("minus");

        //Text field
        employeeId = new TextField();
        employeeId.setPromptText("Please enter employee ID");
        employeeId.textProperty().addListener((observable, oldValue, newValue) ->{
           if(!newValue.matches("[CSM][a-z]*_\\d+")){
               employeeId.setText(newValue.replaceAll("[^CSMa-z_\\d]", ""));
           }
        });

        input = new TextField();

        //Text
        this.employees = new Text();
        employees.setX(0); employees.setY(0);

        message = new Label();

        //popup screen
        popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
    }

    @Override
    public Scene getScene(){
        Button[] buttons = layoutFactory.navigationBar(tableButton, orderButton, inventoryButton,
                "employees");
        BorderPane overallLayout = new BorderPane();
        employees.setText(employeeController.toString());

        HBox textBox = new HBox();
        textBox.getChildren().add(employees);
        HBox buttonBox = new HBox();
        VBox sideButtons = new VBox();
        buttonBox.getChildren().addAll(buttons);
        sideButtons.getChildren().addAll(hireButton, fireButton, setReceiver);

        overallLayout.setTop(textBox);
        overallLayout.setBottom(buttonBox);
        overallLayout.setRight(sideButtons);
        return new Scene (overallLayout, WIDTH, HEIGHT);
    }

    @Override
    public void update(){

    }

    @Override
    public void initialize(){
        orderButton.setOnAction(e-> switchController.goToOrder());
        tableButton.setOnAction(e-> switchController.goToTable());
        inventoryButton.setOnAction(e-> switchController.goToInventory());
        hireButton.setOnAction(e-> popUpPassword("hire"));
        fireButton.setOnAction(e-> popUpPassword("fire"));
        setReceiver.setOnAction(e-> popUpPassword("receiver"));
        server.setOnAction(e-> {
            setHireMessage("server");
            confirm.setOnAction(v-> resetButton("server"));
        });
        chef.setOnAction(e-> {
            setHireMessage("chef");
            confirm.setOnAction(v-> resetButton("chef"));
        });
        manager.setOnAction(e-> {
            setHireMessage("manager");
            confirm.setOnAction(v-> resetButton("manager"));
        });
    }

    /**
     * asks for a input to make sure the proper user is using this function
     * @param operation determine whether the user wants to hire, fire or set receiver.
     */
    private void popUpPassword(String operation){
        GridPane grid = new GridPane();
        Label pass = new Label("Password:");

        GridPane.setConstraints(pass, 0, 0);
        GridPane.setConstraints(input, 1, 0);
        GridPane.setConstraints(confirm, 8, 0);
        grid.getChildren().addAll(pass, input, confirm);
        Scene scene = new Scene(grid, 460, 50);
        scene.getStylesheets().add("style.css");
        grid.getStyleClass().add("grid");
        popUp.setTitle("Password");
        popUp.setScene(scene);
        popUp.show();

        confirm.setOnAction(e->{
           if(input.getText().equals(PASSWORD)){
               switch(operation){
                   case "hire":
                       popUpHire();
                       break;
                   case "fire":
                       popUpFire();
                       break;
                   case "receiver":
                       popUpReceiver();
               }

               input.clear();
           }
           else{
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setHeaderText("ERROR");
               alert.setContentText("Wrong Password!");
               alert.showAndWait();
           }
        });
    }

    /**
     * A pop up window used to hire new employees
     */
    private void popUpHire(){
        GridPane grid = new GridPane();
        message.setText("What kind of employee are you hiring?");

        setConstraints();
        GridPane.setConstraints(message, 0, 0, 10, 1);
        grid.getChildren().addAll(message, chef, server, manager, confirm, cancel);
        Scene scene = new Scene(grid, 400, 250);
        scene.getStylesheets().add("style.css");
        grid.getStyleClass().add("grid");
        popUp.setTitle("Hire new employee");
        popUp.setScene(scene);
        popUp.show();

        //Change button functionality based off certain criteria.
        confirm.setOnAction(e-> message.setText("Please choose an employee type first."));

        cancel.setOnAction(e-> popUp.close());
    }

    // Sets the hire message to the screen.
    private void setHireMessage(String employeeType){
        message.setText("Hire a new " + employeeType + "?");
    }

    //Reset the functionality of the confirm button.
    private void resetButton(String employeeType){
        employeeController.hire(message, employeeType);
        employees.setText(employeeController.toString());
        confirm.setOnAction(k-> message.setText("Please choose an employee type first."));
    }

    /**
     * A pop up screen used to determine which employee should be removed from the restaurant and database.
     */
    private void popUpFire(){
        GridPane grid = new GridPane();
        Label id = new Label("Employee Id#");
        Label message = new Label("Enter employee ID and type to remove.");

        setConstraints();
        inputConstraints(id, grid, message);

        Scene scene = new Scene(grid, 330, 250);
        scene.getStylesheets().add("style.css");
        grid.getStyleClass().add("grid");

        popUp.setTitle("Fire employee");
        popUp.setScene(scene);
        popUp.show();

        //Button functionality
        confirm.setOnAction(e-> {
            Employee employee;
            if(!employeeId.getText().isEmpty() && employeeId.getText().matches("[CMS][a-z]+_\\d+")){
                employee = employeeController.findEmployee(employeeId.getText());
                if(employee == null){
                    message.setText("Employee not found.");
                }
                else{
                    employeeController.fire(employee);
                    message.setText("Successfully removed " + employee.toString() + ".");
                    employees.setText(employeeController.toString());
                }
            }
            else{
                message.setText("Invalid input!");
            }
        });

        cancel.setOnAction(e-> popUp.close());
    }

    /**
     * A pop up screen used to set the inputted employee into a receiver
     */
    private void popUpReceiver(){
        GridPane grid = new GridPane();
        Label id = new Label("Employee Id#");
        Label message = new Label("Enter employee ID.");

        setConstraints();
        inputConstraints(id, grid, message);

        Scene scene = new Scene(grid, 330, 250);
        scene.getStylesheets().add("style.css");
        grid.getStyleClass().add("grid");

        popUp.setTitle("Set Receiver");
        popUp.setScene(scene);
        popUp.show();

        //button functionality
        confirm.setOnAction(e-> {
            Employee employee;
            if(!employeeId.getText().isEmpty() && employeeId.getText().matches("[CMS][a-z]+_\\d+")){
                employee = employeeController.findEmployee(employeeId.getText());
                if(employee == null){
                    message.setText("Employee not found.");
                }
                else{
                    if(employeeController.changeReceiverStatus(employee)) {
                        message.setText("Successfully set receiver " + employee.toString() + ".");
                        employees.setText(employeeController.toString());
                    }
                    else{
                        message.setText("Employee is already a receiver \nor does not exist!");
                    }
                }
            }
            else{
                message.setText("Invalid input!");
            }
        });

        cancel.setOnAction(e-> popUp.close());
    }

    //Sets the constraints for the grid.
    private void setConstraints(){
        GridPane.setConstraints(chef, 0, 6);
        GridPane.setConstraints(server,  3, 6);
        GridPane.setConstraints(manager,5, 6);
        GridPane.setConstraints(confirm, 0, 10);
        GridPane.setConstraints(cancel, 5, 10);
    }

    /**
     * set grid constraints on employee entry screens
     * @param id the label that asks for an employee id
     * @param grid the grid that would be used to set screen layout.
     */
    private void inputConstraints(Label id, GridPane grid, Label message){
        GridPane.setConstraints(id, 0, 0);
        GridPane.setConstraints(employeeId, 1, 0, 6, 1);
        GridPane.setConstraints(message, 0, 1, 8, 1);
        grid.getChildren().addAll(message, confirm, cancel, id, employeeId);
    }

    //initialize the EmployeeController class for this classes buttons.
    public void initEmployeeController(EmployeeController employeeController){
        this.employeeController = employeeController;
    }
}
