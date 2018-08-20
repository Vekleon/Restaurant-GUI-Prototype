package GUI.Screen;

import GUI.Controllers.TableOrderController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class TableOrderScreen extends Screen {
    private Button backButton; //Return to tables
    private TableOrderController tableOrderController;
    private VBox orderPanel, rightPanel;

    /**
     * Create a table order screen
     *
     * @param stage                The stage reference
     * @param tableOrderController A controller for this table
     */
    public TableOrderScreen(Stage stage, TableOrderController tableOrderController) {
        super(stage);
        backButton = new Button();
        backButton.setText("Back");
        this.tableOrderController = tableOrderController;
    }

    @Override
    public Scene getScene() {
        Scene finalScene;
        if (tableOrderController.getTable().getStatus().equals("unoccupied")) {
            finalScene = getUnoccupiedScene();
        } else {
            finalScene = getOccupiedScene();
        }
        finalScene.getStylesheets().add("style.css");
        return finalScene;
    }

    /**
     * The scene that would represent this table, if it were unoccupied. This scene consists of a button, which pops
     * up a screen which lets a server initialize a table
     *
     * @return The unoccupied scene
     */
    private Scene getUnoccupiedScene() {
        //Overall layout
        BorderPane overallLayout = new BorderPane();
        //Button on bottom
        HBox backLayout = layoutFactory.getBackButtonLayout(backButton);
        overallLayout.setBottom(backLayout);
        //Top (table information)
        overallLayout.setTop((new Text("-UNOCCUPIED- Table " +
                Integer.toString(tableOrderController.getTable().getTableNumber()) +
                "\nThis table is a seating of size " + Integer.toString(tableOrderController.getTable().getSize()))));
        GridPane middleLayout = initializeForm();
        BorderPane.setMargin(middleLayout, new Insets(HEIGHT / 3, 0, 0, WIDTH / 3));
        overallLayout.setCenter(middleLayout);
        return new Scene(overallLayout, WIDTH, HEIGHT);
    }

    /**
     * Helper method which returns the form to enter table information to initialize
     *
     * @return A form which lets you initialize a table (complete with button)
     */
    private GridPane initializeForm() {
        GridPane finalForm = new GridPane();
        //Create stuff needed for grid
        Label serverId = new Label("Server ID: ");
        TextField serverIdField = new TextField();
        Label customerNumber = new Label("Number of Customers: ");
        TextField customerNumberField = new TextField();
        Button initButton = new Button();
        initButton.setText("Initialize Table");
        //Add to grid and set spacing
        finalForm.add(serverId, 0, 0);
        finalForm.add(serverIdField, 1, 0);
        finalForm.add(customerNumber, 0, 1);
        finalForm.add(customerNumberField, 1, 1);
        finalForm.add(initButton, 1, 2);
        finalForm.setVgap(10);
        GridPane.setHalignment(initButton, HPos.RIGHT);
        //Make it so you can only enter numbers in text fields
        serverIdField.textProperty().addListener(new NumberListener(serverIdField, true));
        customerNumberField.textProperty().addListener(new NumberListener(customerNumberField, false));
        //Set button event
        initButton.setOnAction(e -> tryToInitialize(serverIdField.getText(), customerNumberField.getText()));
        return finalForm;
    }

    /**
     * A class to listen to the text field and only accept numbers.
     */
    class NumberListener implements ChangeListener<String> {
        TextField field;
        boolean acceptZeroFirst;

        /**
         * Create a new number listener
         *
         * @param fieldListening  The field which this is listening to
         * @param acceptZeroFirst Whether the first value can be zero
         */
        NumberListener(TextField fieldListening, boolean acceptZeroFirst) {
            this.field = fieldListening;
            this.acceptZeroFirst = acceptZeroFirst;
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (acceptZeroFirst && !newValue.matches("[0-9]*")) {
                field.setText(oldValue);
            } else if (!acceptZeroFirst && (newValue.length() != 0 && !newValue.matches("[1-9][0-9]*"))) {
                field.setText(oldValue);
            }
        }
    }

    /**
     * Try to initialize table with the current id entered in the text field
     *
     * @param serverID       Must match regex [0-9]*
     * @param customerNumber Must match [1-9][0-9]*
     */
    private void tryToInitialize(String serverID, String customerNumber) {
        int status;
        if (serverID.length() > 0 && customerNumber.length() > 0) {
            status = tableOrderController.attemptInitialize(Integer.parseInt(serverID),
                    Integer.parseInt(customerNumber));
        } else {
            return;
        }
        //Success
        if (status == 0) {
            rightPanel = getTableRightPanel();
            orderPanel = new VBox();
            orderPanel.setPadding(new Insets(10, 0, 0, 5));
            orderPanel.setSpacing(5);
            this.show();
        }
        //Server id failed
        else if (status == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Server ID not found in system");
            alert.showAndWait();
            // Table size failed
        } else if (status == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Customer number exceeds maximum table size");
            alert.showAndWait();
        } else if (status == 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("This server has a dish waiting to be delivered");
            alert.showAndWait();
        }
    }

    /**
     * The scene that would represent the table, if it were occupied and set up
     *
     * @return The occupied scene
     */
    private Scene getOccupiedScene() {
        BorderPane overallLayout = new BorderPane();
        //Button on bottom
        HBox backLayout = layoutFactory.getBackButtonLayout(backButton);
        overallLayout.setBottom(backLayout);
        //Panel on right
        overallLayout.setRight(rightPanel);
        ScrollPane orderScroller = new ScrollPane();
        //Create order scroller
        orderScroller.setContent(orderPanel);
        orderScroller.setMinWidth(WIDTH - 250);
        orderScroller.setMaxWidth(WIDTH - 250);
        overallLayout.setLeft(orderScroller);
        return new Scene(overallLayout, WIDTH, HEIGHT);
    }

    /**
     * Creates the right hand panel which displays some information and lets the server take some actions
     *
     * @return The panel
     */
    private VBox getTableRightPanel() {
        VBox finalPanel = new VBox();
        //Server ID information
        String serverString = "Server ID: " + tableOrderController.getTable().getServer().getJobId();
        Label serverText = new Label(serverString);
        serverText.setMinWidth(200);
        serverText.getStyleClass().add("serverLabel");
        serverText.setPadding(new Insets(0, 0, 0, 5));
        //Table occupancy
        String customerNumString = "Number of Customers: " + tableOrderController.getTable().getNumberOfOccupants();
        Label customerText = new Label(customerNumString);
        customerText.getStyleClass().add("serverLabel");
        customerText.setMinWidth(200);
        customerText.setPadding(new Insets(0, 0, 0, 5));
        //Functional buttons
        Button addDishButton = new Button("Add Dish");
        Button billButton = new Button("Print Bill");
        addDishButton.setMinSize(200, 100);
        billButton.setMinSize(200, 20);
        //Set button functionality
        addDishButton.setOnAction(e -> addDishPopup());
        billButton.setOnAction(e -> tableOrderController.getBillController().setUpBill(finalPanel, billButton,
                tableOrderController.getDishControllerMap()));
        //Confirm order button
        Button confirmButton = new Button("Confirm Order");
        confirmButton.setMinWidth(200);
        confirmButton.setOnAction(e -> tableOrderController.confirmOrder());
        // Terminate table button
        Button terminateButton = new Button("Terminate Table");
        terminateButton.setMinWidth(200);
        terminateButton.setOnAction(e -> {
            tableOrderController.terminateTableOrder();
            this.show();
        });
        terminateButton.getStyleClass().add("minus");
        //Add everything
        finalPanel.getChildren().addAll(serverText, customerText, addDishButton,
                billButton, confirmButton, terminateButton);
        finalPanel.setPadding(new Insets(20, 10, 0, 0));
        finalPanel.setSpacing(10);
        return finalPanel;
    }

    /**
     * This is what add dish will cause to pop up. Here, the server selects a food
     */
    private void addDishPopup() {
        Stage popup = new Stage();
        popup.setTitle("Add Dish");
        GridPane finalForm = new GridPane();
        //Create stuff needed for grid
        Label dishName = new Label("Dish Name:");
        TextField dishField = new TextField();
        Button confirmButton = new Button();
        confirmButton.setText("Confirm Dish");
        //Add to grid and set spacing
        finalForm.add(dishName, 0, 0);
        finalForm.add(dishField, 1, 0);
        finalForm.add(confirmButton, 1, 1);
        finalForm.setVgap(10);
        finalForm.setPadding(new Insets(5, 0, 0, 20));
        GridPane.setHalignment(confirmButton, HPos.RIGHT);
        //Set button event
        confirmButton.setOnAction(e -> {
            try {
                tableOrderController.addDish(orderPanel, dishField.getText());
                this.show();
            } catch (Exception e1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Not enough ingredients for this dish!");
                alert.show();
            }
            popup.close();
        });
        popup.setScene(new Scene(finalForm, 300, 80));
        popup.setResizable(false);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.show();
    }

    @Override
    public void update() {
        tableOrderController.updateTableStatus();
        tableOrderController.updateCancelled();
        tableOrderController.updateDishGUI();
    }

    @Override
    public void initialize() {
        setBackButtonEvent();
    }

    /**
     * Sets back button event
     */
    private void setBackButtonEvent() {
        backButton.setOnAction(e -> switchController.goToTable());
    }

    /**
     * Returns this screen's table number
     *
     * @return The table number
     */
    public int getTableNumber() {
        return tableOrderController.getTable().getTableNumber();
    }
}
