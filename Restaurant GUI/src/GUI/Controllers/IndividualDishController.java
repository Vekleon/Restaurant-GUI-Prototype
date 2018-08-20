package GUI.Controllers;

import dishes.Combo;
import dishes.Food;
import dishes.Recipe;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import restaurant.Menu;
import restaurant.Table;

import java.util.HashMap;

/**
 * The controller for individual dishes in within the table order screen
 *
 * @author Jian Xian
 */
public class IndividualDishController {
    private HBox dishLine;
    private Recipe dish;
    private Table table;
    private VBox orderPanel;

    /**
     * Create an individual dish controller
     *
     * @param dishLine The line which this dish is in the GUI on the table order screen
     * @param dish     The dish which this controller controls
     * @param table    The table which this dish is at
     */
    IndividualDishController(VBox orderPanel, HBox dishLine, Recipe dish, Table table) {
        this.orderPanel = orderPanel;
        this.dishLine = dishLine;
        this.dish = dish;
        this.table = table;
    }

    /**
     * Removes a dish from an order
     *
     * @param orderPanel The order panel of the dish
     */
    public void cancelDish(VBox orderPanel) {
        table.getServer().removeFromOrder(table.getOrder(), dish);
        orderPanel.getChildren().remove(dishLine);
    }

    /**
     * Makes the Hbox a special state where the server must close out of the cancelled order
     */
    public void attemptUnexpectedCancellation() {
        if (orderPanel.getChildren().contains(dishLine)) {
            dishLine.getChildren().remove(1, dishLine.getChildren().size());
            Label cancel = new Label("Cancelled");
            Button removeButton = new Button("X");
            removeButton.setMinWidth(50);
            removeButton.getStyleClass().add("cancel");
            dishLine.getChildren().addAll(cancel, removeButton);
            removeButton.setOnAction(e -> orderPanel.getChildren().remove(dishLine));
        }
    }

    /**
     * Creates a popup to set ingredients on a food item, assuming this is controlling a food
     */
    public Stage getFoodIngredientsStage() {
        //Basic stage properties
        Stage foodIngredientPopup = new Stage();
        foodIngredientPopup.initModality(Modality.APPLICATION_MODAL);
        foodIngredientPopup.setTitle("Set Ingredients");
        //Get the panel for the food and its ingredients
        VBox overallLayout = getFoodIngredientPanel((Food) dish);
        //Button to exit
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> foodIngredientPopup.close());
        VBox.setMargin(exitButton, new Insets(25, 0, 0, 300));
        overallLayout.getChildren().add(exitButton);
        //Set up and show the stage
        Scene popUpScene = new Scene(overallLayout, 600, 300);
        popUpScene.getStylesheets().add("style.css");
        foodIngredientPopup.setScene(popUpScene);
        foodIngredientPopup.setResizable(false);
        return foodIngredientPopup;
    }

    /**
     * Creates a popup to set ingredients on a combo item, assuming this is controlling a combo
     *
     * @param menu The menu of the restaurant
     */
    public Stage getComboIngredientsStage(Menu menu) {
        Stage comboIngredientPopup = new Stage();
        comboIngredientPopup.initModality(Modality.APPLICATION_MODAL);
        comboIngredientPopup.setTitle("Set Ingredients");
        //Layout on stage
        VBox overallLayout = new VBox();
        //Food tabs
        TabPane foodTabs = new TabPane();
        //Make a tab for each food in the combo
        String[] comboFoods = ((Combo) dish).getFoods();
        for (String food : comboFoods) {
            Tab currentTab = new Tab(food);
            VBox foodOverallLayout = getFoodIngredientPanel((Food) menu.getDefaultDish(food));
            currentTab.setContent(foodOverallLayout);
            foodTabs.getTabs().add(currentTab);
        }
        //Exit button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> comboIngredientPopup.close());
        VBox.setMargin(exitButton, new Insets(17, 0, 0, 300));
        // Set up stage and show it
        overallLayout.getChildren().addAll(foodTabs, exitButton);
        Scene popUpScene = new Scene(overallLayout, 600, 300);
        popUpScene.getStylesheets().add("style.css");
        comboIngredientPopup.setScene(popUpScene);
        comboIngredientPopup.setResizable(false);
        return comboIngredientPopup;
    }

    /**
     * Get a food ingredient panel given a food
     *
     * @param food The relevant food
     * @return The food ingredient panel
     */
    private VBox getFoodIngredientPanel(Food food) {
        VBox overallLayout = new VBox();
        //Current ingredients
        HashMap<String, Integer> ingredientsMap = food.getIngredients();
        // Description for the scroll box
        HBox description = new HBox();
        Label currIngredients = new Label("Current Ingredients");
        currIngredients.setMinWidth(200);
        Label quantityLabel = new Label("Current Quantity");
        description.getChildren().addAll(currIngredients, quantityLabel);
        //For the scroll box
        ScrollPane ingredientScrollPanel = new ScrollPane();
        VBox currentIngredientBox = new VBox();
        currentIngredientBox.setSpacing(5);
        for (String ingredient : ingredientsMap.keySet()) {
            //Create a horizontal layout for each ingredient
            HBox singleIngredientLine = new HBox();
            singleIngredientLine.setSpacing(5);
            //Name of ingredient
            Label ingredientLabel = new Label(ingredient);
            ingredientLabel.getStyleClass().add("ingredientNameLabel");
            ingredientLabel.setMinWidth(200);
            //Quantity of ingredient
            Label ingredientQuantity = new Label(Integer.toString(ingredientsMap.get(ingredient)));
            ingredientQuantity.setMinWidth(200);
            //Buttons to add, subtract, and remove
            //Add Button
            Button addButton = new Button("+");
            addButton.setMinWidth(50);
            addButton.getStyleClass().add("plus");
            addButton.setOnAction(e -> changeCurrentIngredient(food.getName(), ingredient,
                    ingredientQuantity, 1));
            //Subtract Button
            Button subtractButton = new Button("-");
            subtractButton.setMinWidth(50);
            subtractButton.getStyleClass().add("minus");
            subtractButton.setOnAction(e -> changeCurrentIngredient(food.getName(), ingredient,
                    ingredientQuantity, -1));
            //Remove Button
            Button removeButton = new Button("X");
            removeButton.setMinWidth(50);
            removeButton.getStyleClass().add("cancel");
            removeButton.setOnAction(e -> removeCurrentIngredient(food.getName(), ingredient,
                    ingredientQuantity));
            //Add everything to horizontal line
            singleIngredientLine.getChildren().addAll(ingredientLabel, ingredientQuantity,
                    addButton, subtractButton, removeButton);
            currentIngredientBox.getChildren().add(singleIngredientLine);

        }
        ingredientScrollPanel.setMinHeight(200);
        ingredientScrollPanel.setMaxHeight(200);
        ingredientScrollPanel.setContent(currentIngredientBox);
        overallLayout.getChildren().addAll(description, ingredientScrollPanel);
        return overallLayout;
    }

    /**
     * Changes a certain ingredient
     *
     * @param foodName     The food name to set ingredients
     * @param ingredient   The ingredient to be changed
     * @param quantityLine The line which refers to the quantity
     * @param quantity     The quantity to change, positive for addition, negative for subtraction
     */
    private void changeCurrentIngredient(String foodName, String ingredient, Label quantityLine, int quantity) {
        int oldQuantity = Integer.parseInt(quantityLine.getText());
        if (oldQuantity + quantity >= 0) {
            try {
                table.getServer().makeAccommodation(dish, foodName, ingredient, quantity);
                quantityLine.setText(Integer.toString(oldQuantity + quantity));
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("NOT ENOUGH INGREDIENTS");
                alert.show();
            }
        }
    }

    /**
     * Removes a certain ingredient
     *
     * @param foodName     The food name to set ingredients
     * @param ingredient   The ingredient to be changed
     * @param quantityLine The line which refers to the quantity
     */
    private void removeCurrentIngredient(String foodName, String ingredient, Label quantityLine) {
        int oldQuantity = Integer.parseInt(quantityLine.getText());
        changeCurrentIngredient(foodName, ingredient, quantityLine, -1 * oldQuantity);
    }

    /**
     * Updates the GUI based on dish statuses
     */
    public void updateDishGUI() {
        if (dish.getStatus().equals("prepared")) {
            dishLine.getChildren().get(1).setDisable(false);
        }
    }

    /**
     * Update GUI to insert a deliver button
     */
    public void insertDeliverButtonAndDeleteButtonAfter() {
        dishLine.getChildren().remove(1);
        //Create a deliver button, currently greyed out
        Button deliverButton = new Button("Deliver");
        deliverButton.setMinWidth(200);
        deliverButton.getStyleClass().add("plus");
        deliverButton.setOnAction(e -> deliver());
        deliverButton.setDisable(true);
        dishLine.getChildren().add(1, deliverButton);
    }

    /**
     * Confirms delivery of the dish related to this controller
     */
    private void deliver() {
        table.getServer().foodDelivered(table.getOrder(), dish);
        dishLine.getChildren().remove(1, dishLine.getChildren().size());
        Button complaintButton = new Button("File Complaint");
        complaintButton.getStyleClass().add("minus");
        dishLine.getChildren().add(complaintButton);
        complaintButton.setOnAction(e -> fileComplaint());
    }

    /**
     * Shows a popup where you can file complaint
     */
    private void fileComplaint() {
        Stage complaintPopUp = new Stage();
        complaintPopUp.initModality(Modality.APPLICATION_MODAL);
        complaintPopUp.setResizable(false);
        GridPane complaintGrid = new GridPane();
        //Make the labels and buttons
        Label enterComplaint = new Label("Enter the complaint");
        enterComplaint.setPadding(new Insets(0, 0, 0, 250));
        TextArea complaintField = new TextArea();
        complaintField.setMinHeight(300);
        complaintField.setMaxHeight(300);
        complaintField.setMinWidth(600);
        complaintField.setMaxWidth(600);
        complaintField.setWrapText(true);
        //Horizontal layout for buttons
        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("plus");
        confirmButton.setMinWidth(300);
        confirmButton.setMinHeight(90);
        confirmButton.setOnAction(e -> confirmComplaint(complaintField.getText(), complaintPopUp));
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("minus");
        cancelButton.setMinWidth(300);
        cancelButton.setMinHeight(90);
        cancelButton.setOnAction(e -> complaintPopUp.close());
        //Add everything to grid
        complaintGrid.add(enterComplaint, 0, 0, 2, 1);
        complaintGrid.add(complaintField, 0, 1, 2, 1);
        complaintGrid.add(confirmButton, 0, 2);
        complaintGrid.add(cancelButton, 1, 2);
        Scene complaintScene = new Scene(complaintGrid, 590, 400);
        complaintScene.getStylesheets().add("style.css");
        complaintPopUp.setScene(complaintScene);
        complaintPopUp.show();
    }

    /**
     * Confirms a complaint
     *
     * @param complaint    The filed complaint text
     * @param stageToClose The stage to close at the end of this method
     */
    private void confirmComplaint(String complaint, Stage stageToClose) {
        table.getServer().complaint(table.getOrder(), complaint, dish);
        //Create a deliver button, currently greyed out
        Button deliverButton = new Button("Deliver");
        deliverButton.setMinWidth(200);
        deliverButton.getStyleClass().add("plus");
        deliverButton.setOnAction(e -> deliver());
        deliverButton.setDisable(true);
        dishLine.getChildren().remove(1);
        dishLine.getChildren().add(1, deliverButton);
        stageToClose.close();
    }

    /**
     * Reverts the HBox which represents this dish into just the label
     */
    public void revertToName() {
        dishLine.getChildren().remove(1, dishLine.getChildren().size());
    }
}
