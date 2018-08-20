package GUI.Controllers;

import dishes.Recipe;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import restaurant.Table;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A controller to deal with bill creating pop ups in a table order screen
 *
 * @author Jian Xian Li
 */
public class BillController {
    private Table table;
    private VBox optionPanel;
    private Button initialBillButton;
    private HashMap<Recipe, IndividualDishController> dishControllerMap;
    private Stage existingBill;

    /**
     * Creates a bill controller on a specific table
     *
     * @param table The table which this bill controller references
     */
    BillController(Table table) {
        this.table = table;
    }

    /**
     * Creates a popup which allows the server to set up the bill
     *
     * @param optionPanel       The panel which the bill button is on
     * @param billButton        The bill button
     * @param dishControllerMap Hashmap that maps recipes to their controllers
     */
    public void setUpBill(VBox optionPanel, Button billButton,
                          HashMap<Recipe, IndividualDishController> dishControllerMap) {
        this.dishControllerMap = dishControllerMap;
        this.optionPanel = optionPanel;
        this.initialBillButton = billButton;
        Stage billOptionPopUp = new Stage();
        billOptionPopUp.initModality(Modality.APPLICATION_MODAL);
        billOptionPopUp.setResizable(false);
        billOptionPopUp.setTitle("Bill Options");
        VBox billOptionPanel = new VBox();
        billOptionPanel.setSpacing(10);
        Button oneBill = new Button("One Bill");
        oneBill.setMinWidth(200);
        oneBill.setOnAction(e -> oneBill(billOptionPopUp));
        Button evenSplitBill = new Button("Even Split Bill");
        evenSplitBill.setMinWidth(200);
        evenSplitBill.setOnAction(e -> evenSplitBill(billOptionPopUp));
        Button customSplitBill = new Button("Custom Split Bill");
        customSplitBill.setMinWidth(200);
        customSplitBill.setOnAction(e -> customSplitBill(billOptionPopUp));
        billOptionPanel.setPadding(new Insets(10, 0, 0, 10));
        Scene billOptionScene = new Scene(billOptionPanel, 215, 160);
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> billOptionPopUp.close());
        cancelButton.setMinWidth(200);
        billOptionPanel.getChildren().addAll(oneBill, evenSplitBill, customSplitBill, cancelButton);
        cancelButton.getStyleClass().add("minus");
        billOptionScene.getStylesheets().add("style.css");
        billOptionPopUp.setScene(billOptionScene);
        billOptionPopUp.show();
    }

    /**
     * Pops up the one bill and saves it to the existing bill
     *
     * @param stageToClose The staget oclose after opening this popup
     */
    private void oneBill(Stage stageToClose) {
        existingBill = table.getServer().getBillPopUp(getFullDishArray());
        stageToClose.close();
        adjustPanel();
        existingBill.show();
    }

    /**
     * Pops up the split bill and saves it to the existing bill
     *
     * @param stageToClose The stage to close upon opening this new popup
     */
    private void evenSplitBill(Stage stageToClose) {
        existingBill = table.getServer().getBillPopUp(getFullDishArray(), table.getNumberOfOccupants());
        stageToClose.close();
        adjustPanel();
        existingBill.show();
    }

    /**
     * Returns a dish array of all dishes in the table's order which are confirmed
     *
     * @return The dish array
     */
    private ArrayList<ArrayList<Recipe>> getFullDishArray() {
        ArrayList<ArrayList<Recipe>> finalArrayList = new ArrayList<>();
        ArrayList<Recipe> dishes = table.getOrder().getDishes();
        ArrayList<Recipe> dishesCopy = new ArrayList<>(dishes);
        for (Recipe dish : dishesCopy) {
            if (dish.getStatus().equals("cancelled") || dish.getStatus().equals("unconfirmed")) {
                if (dish.getStatus().equals("unconfirmed")) {
                    IndividualDishController dishController = dishControllerMap.get(dish);
                    dishController.revertToName();
                }
                dishes.remove(dish);
            }
        }
        finalArrayList.add(dishes);
        return finalArrayList;
    }

    /**
     * Changes the original bill button to now open the existing bill pop up. Also removes add dish item button and
     * confirm order button.
     */
    private void adjustPanel() {
        initialBillButton.setOnAction(e -> existingBillPopUp());
        optionPanel.getChildren().remove(2);
        optionPanel.getChildren().remove(3);
    }

    /**
     * Pops up the custom bill creator and saves it to the existing bill
     *
     * @param stageToClose The stage to close when opening this new stage
     */
    private void customSplitBill(Stage stageToClose) {
        stageToClose.close();
        Stage customBill = new Stage();
        customBill.initModality(Modality.APPLICATION_MODAL);
        customBill.setResizable(false);
        customBill.setTitle("Custom Bill");
        HBox overallLayout = new HBox();
        VBox rightLayout = new VBox();
        ScrollPane scrollLeft = new ScrollPane();
        TabPane billPane = new TabPane();
        billPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        billPane.setPrefSize(350, 380);
        HBox bottomRight = new HBox();
        Button addBillButton = new Button("Add Bill");
        addBillButton.getStyleClass().add("addBill");
        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("plus");
        Button cancelButton = new Button("Cancel");
        //Set button properties
        addBillButton.setMinWidth(100);
        confirmButton.setMinWidth(100);
        cancelButton.setMinWidth(100);
        cancelButton.getStyleClass().add("minus");
        ScrollPane scrollRight = new ScrollPane();
        //Get confirmed dishes
        ArrayList<Recipe> dishes = table.getOrder().getDishes();
        //Make a sub-array of all dishes with only confirmed orders
        ArrayList<Recipe> dishArray = new ArrayList<>();
        for (Recipe dish : dishes) {
            if (!(dish.getStatus().equals("cancelled") || dish.getStatus().equals("unconfirmed"))) {
                dishArray.add(dish);
            }
        }
        //Records the amount of times a dish has been added in total
        int[] dishAdded = new int[dishArray.size()];
        //First tab
        Tab zeroTab = new Tab();
        zeroTab.setText("Bill 1");
        billPane.getTabs().add(zeroTab);
        //Some layout tools...
        VBox zeroList = new VBox();
        ScrollPane zeroScroll = new ScrollPane();
        zeroScroll.setContent(zeroList);
        zeroTab.setContent(zeroScroll);
        ArrayList<ArrayList<Recipe>> finalBillArray = new ArrayList<>();
        finalBillArray.add(new ArrayList<>());
        HashMap<Tab, ArrayList<Recipe>> tabBillArrayMap = new HashMap<>(); //Maps tabs to the array which represents it
        HashMap<Tab, VBox> tabListMap = new HashMap<>(); //Maps tabs to the VBox in the tabs
        tabBillArrayMap.put(zeroTab, finalBillArray.get(0));
        tabListMap.put(zeroTab, zeroList);
        int dishReference = 0; // A certain dish's reference to the above array
        VBox dishPane = new VBox();
        dishPane.setSpacing(10);
        // Getting the initial buttons be looping through the dish array
        for (Recipe dish : dishArray) {
            dishAdded[dishReference] = 0;
            int currDishReference = dishReference;
            Button currDishButton = new Button(dish.getName());
            currDishButton.setPrefSize(300, 40);
            currDishButton.getStyleClass().add("unfinished");
            currDishButton.setOnAction(e -> {
                Tab currentTab = billPane.getSelectionModel().getSelectedItem();
                addToBill(dish, tabListMap.get(currentTab), currDishReference, dishAdded, tabBillArrayMap.get(currentTab),
                        currDishButton);
            });
            dishPane.getChildren().add(currDishButton);
            dishReference++;
        }
        //Initialize buttons
        addBillButton.setOnAction(e -> addNewBill(billPane, tabBillArrayMap, tabListMap, finalBillArray));
        cancelButton.setOnAction(e -> customBill.close());
        confirmButton.setOnAction(e -> confirm(customBill, dishAdded, finalBillArray));

        //Make tab pane look better
        billPane.setTabMinWidth(80);

        //Put everything together
        HBox.setMargin(confirmButton, new Insets(0, 0, 0, 90));
        bottomRight.getChildren().addAll(addBillButton, confirmButton, cancelButton);
        scrollLeft.setContent(dishPane);
        scrollRight.setContent(billPane);
        rightLayout.getChildren().addAll(scrollRight, bottomRight);
        //Set remaining sizes to fit in
        billPane.setMinSize(350, 360);
        billPane.setMaxHeight(375);
        scrollRight.setMinSize(390, 350);
        scrollLeft.setMinSize(360, 400);
        rightLayout.setMinSize(390, 400);
        dishPane.setMinSize(350, 390);
        overallLayout.getChildren().addAll(scrollLeft, rightLayout);

        Scene newScene = new Scene(overallLayout, 750, 400);
        newScene.getStylesheets().add("style.css");
        customBill.setScene(newScene);
        customBill.show();
    }

    /**
     * Adds a dish to the current bill
     *
     * @param dish             The dish to be added
     * @param tabList          The current bill tab list sto add to
     * @param reference        The reference to the dish added array of the dish
     * @param dishAdded        The dish added array to keep track of total additions
     * @param currentBillArray The current bill array list which keeps track of the current bill
     * @param dishButton       The button related to the dish
     */
    private void addToBill(Recipe dish, VBox tabList, int reference, int[] dishAdded, ArrayList<Recipe> currentBillArray,
                           Button dishButton) {
        if (!(currentBillArray.contains(dish))) {
            currentBillArray.add(dish);
            HBox dishLine = new HBox();
            Label dishLabel = new Label(dish.getName());
            dishLabel.getStyleClass().add("foodOnBill");
            Button removeButton = new Button("X");
            removeButton.getStyleClass().add("minus");
            removeButton.setMinSize(50, 50);
            dishLabel.setMinSize(200, 50);
            dishLine.setPadding(new Insets(0, 0, 0, 10));
            dishLine.getChildren().addAll(dishLabel, removeButton);
            tabList.getChildren().add(dishLine);
            dishAdded[reference]++;
            dishButton.getStyleClass().clear();
            dishButton.getStyleClass().addAll("button", "finished");
            //Set up remove
            removeButton.setOnAction(e -> {
                currentBillArray.remove(dish);
                tabList.getChildren().remove(dishLine);
                dishAdded[reference]--;
                if (dishAdded[reference] == 0) {
                    dishButton.getStyleClass().clear();
                    dishButton.getStyleClass().addAll("button", "unfinished");
                }
            });
        }
    }

    /**
     * Adds a new bill tab
     *
     * @param billLayout The bill layout in form of a tab pane
     * @param tabBillMap Maps tabs to the bill arrays
     * @param tabListMap Maps tabs to the their inner layout lists
     */
    private void addNewBill(TabPane billLayout, HashMap<Tab, ArrayList<Recipe>> tabBillMap,
                            HashMap<Tab, VBox> tabListMap, ArrayList<ArrayList<Recipe>> overallArray) {
        Tab newTab = new Tab();
        newTab.setText("Bill " + (billLayout.getTabs().size() + 1));
        ScrollPane newScroller = new ScrollPane();
        VBox newList = new VBox();
        ArrayList<Recipe> newArray = new ArrayList<>();
        overallArray.add(newArray);
        //Update hash maps
        tabBillMap.put(newTab, newArray);
        tabListMap.put(newTab, newList);
        //Update content
        newScroller.setContent(newList);
        newTab.setContent(newScroller);
        billLayout.getTabs().add(newTab);

    }

    /**
     * Confirms this bill
     */
    private void confirm(Stage stageToClose, int[] dishAdded, ArrayList<ArrayList<Recipe>> allDishes) {
        for (int dishNum : dishAdded) {
            if (dishNum == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("SOME DISHES ARE STILL NOT ADDED");
                alert.show();
                return;
            }
        }
        stageToClose.close();
        getFullDishArray();
        adjustPanel();
        existingBill = table.getServer().getBillPopUp(allDishes);
        existingBill.show();
    }


    /**
     * Pops up the bill if it is already created
     */
    private void existingBillPopUp() {
        if (existingBill != null) {
            existingBill.show();
        }
    }
}