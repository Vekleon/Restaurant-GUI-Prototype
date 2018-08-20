package GUI;

import employees.Server;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The billCreator class. This creates the popup screen for the different bills.
 *
 * @author Victor Huang
 */
public class BillCreator {

    private Server server;

    public BillCreator(Server server) {
        this.server = server;
    }

    /**
     * Creates a popup screen that shows a single bill
     *
     * @param bill The string representation that will be converted into a popup Screen
     * @return The popup screen that displays a single bill
     */
    public Stage singleBillStage(String bill) {
        //Basic stage properties
        Stage singleBillPopUp = new Stage();
        singleBillPopUp.initModality(Modality.APPLICATION_MODAL);
        singleBillPopUp.setTitle("Bill");
        //Get the panel for the single bill
        VBox overallLayout = getBillPanel(bill);
        //Payment Button
        Button paymentButton = new Button("Confirm Payment");
        String[] bills = new String[1];
        bills[0] = bill;
        paymentButton.setOnAction(e -> server.billPayment(singleBillPopUp, bills));
        VBox.setMargin(paymentButton, new Insets(25, 0, 0, 150));
        overallLayout.getChildren().add(paymentButton);
        //Set up and show the stage
        Scene popUpScene = new Scene(overallLayout, 310, 500);
        popUpScene.getStylesheets().add("style.css");
        singleBillPopUp.setScene(popUpScene);
        singleBillPopUp.setResizable(false);
        return singleBillPopUp;
    }

    /**
     * Creates a popup screen that shows multiple bills on multiple tabs which you can click through
     *
     * @param bills A array of string representations of bills
     * @return The popup screen with multiple bills separated by tabs
     */
    public Stage multipleBillStage(String[] bills) {
        Stage multipleBillPopUp = new Stage();
        multipleBillPopUp.initModality(Modality.APPLICATION_MODAL);
        multipleBillPopUp.setTitle("Bills");
        //Layout on stage
        VBox overallLayout = new VBox();
        //Bill tabs
        TabPane billTabs = new TabPane();
        //Make a tab for each bill in the array of bills
        int billNumber = 0;
        for (String bill : bills) {
            Tab currentTab = new Tab("Bill " + ++billNumber);
            VBox billOverallLayout = getBillPanel(bill);
            currentTab.setContent(billOverallLayout);
            billTabs.getTabs().add(currentTab);
        }
        //Payment button
        Button paymentButton = new Button("Confirm Payment");
        paymentButton.setOnAction(e -> server.billPayment(multipleBillPopUp, bills));
        VBox.setMargin(paymentButton, new Insets(17, 0, 0, 150));
        // Set up stage and show it
        overallLayout.getChildren().addAll(billTabs, paymentButton);
        Scene popUpScene = new Scene(overallLayout, 310, 500);
        popUpScene.getStylesheets().add("style.css");
        multipleBillPopUp.setScene(popUpScene);
        multipleBillPopUp.setResizable(false);
        return multipleBillPopUp;
    }

    /**
     * Get a bill panel which displays most of the information of the bill
     *
     * @param bill The string representation of the bill
     * @return The bill panel
     */
    private VBox getBillPanel(String bill) {
        VBox overallLayout = new VBox();
        //For the scroll box
        ScrollPane ingredientScrollPanel = new ScrollPane();
        VBox billBox = new VBox();
        billBox.setSpacing(3);
        for (String line : bill.split("\n")) {
            //A single line of the bill
            Label lineLabel = new Label(line);
            lineLabel.getStyleClass().add("billLabel");
            lineLabel.setMinWidth(100);
            //Add everything to vertical box
            billBox.getChildren().add(lineLabel);
        }
        ingredientScrollPanel.setMinHeight(400);
        ingredientScrollPanel.setMaxHeight(400);
        ingredientScrollPanel.setContent(billBox);
        overallLayout.getChildren().addAll(ingredientScrollPanel);
        return overallLayout;
    }
}
