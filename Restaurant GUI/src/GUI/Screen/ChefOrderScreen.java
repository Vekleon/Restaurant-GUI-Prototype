package GUI.Screen;

import GUI.Controllers.ChefOrderController;
import GUI.Controllers.SwitchController;
import dishes.Recipe;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChefOrderScreen extends Screen {
    private Button backButton;
    private ChefOrderController chefOrderController;
    private VBox displayDishes;
    private Stage popUp;
    private TextField chefId;
    private Button confirm;
    private Button close;

    /**
     * initialize a new chefOrderScreen which displays each individual dish and their ingredients.
     *
     * @param stage               the stage in which all the actions take place
     * @param switchController    the controller responsible for aiding in switching scenes
     * @param chefOrderController the controller used to manipulate the background data.
     */
    public ChefOrderScreen(Stage stage, SwitchController switchController, ChefOrderController chefOrderController) {
        super(stage);
        this.chefOrderController = chefOrderController;

        initSwitcher(switchController);
        backButton = new Button();
        backButton.setText("Back");
        initialize();

        confirm = new Button();
        confirm.setText("Confirm");
        confirm.getStyleClass().add("plus");

        close = new Button();
        close.setText("Close");
        close.getStyleClass().add("minus");
        close.setOnAction(e -> popUp.close());

        chefId = new TextField();
        chefId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[CSM][a-z]*_\\d*")) {
                chefId.setText(newValue.replaceAll("[^CSMa-z_\\d]", ""));
            }
        });

        displayDishes = new VBox();
        displayDishes.setSpacing(5);
        displayDishes.setPadding(new Insets(10, 0, 0, 5));
        displayDishes.getChildren().addAll(chefOrderController.getDishes(this));

        popUp = new Stage();
        popUp.setResizable(false);
        popUp.initModality(Modality.APPLICATION_MODAL);
    }

    @Override
    public Scene getScene() {
        if (chefOrderController.getOrder().getAcknowledge()) {
            return manageOrders();
        } else {
            return assignChef();
        }
    }

    /**
     * Used to find a chef to acknowledge the order.
     *
     * @return the scene in that is to be displayed to the screen.
     */
    private Scene assignChef() {
        //Overall Layout
        BorderPane overallLayout = new BorderPane();
        HBox backLayout = layoutFactory.getBackButtonLayout(backButton);
        overallLayout.setBottom(backLayout);

        StringBuilder dishNames = new StringBuilder(200);
        for (Recipe dish : chefOrderController.getOrder().getDishes()) {
            dishNames.append(dish.getName());
            dishNames.append(", ");
        }
        overallLayout.setTop((new Text("Order for table: " + Integer.toString(chefOrderController.getOrder().getTableNumber()) +
                "\nThis order contains: " + dishNames.toString() + ".")));

        GridPane inputLayout = chefInput();
        overallLayout.setCenter(inputLayout);
        BorderPane.setMargin(inputLayout, new Insets(HEIGHT / 3, 0, 0, WIDTH / 3));

        return new Scene(overallLayout, WIDTH, HEIGHT);
    }

    /**
     * Used to find the chef that is going to be responsible for cooking a dish.
     *
     * @return returns a grid that holds the input panel.
     */
    private GridPane chefInput() {
        GridPane grid = new GridPane();

        Label chefId = new Label("Chef ID#:");
        TextField chefIdInput = new TextField();
        Button submit = new Button();
        submit.setText("Acknowledge");

        grid.add(chefId, 0, 0);
        grid.add(chefIdInput, 1, 0);
        grid.add(submit, 1, 1);
        grid.setVgap(10);
        GridPane.setHalignment(submit, HPos.RIGHT);

        chefIdInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                chefIdInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        submit.setOnAction(e -> {
            if (chefOrderController.findChef(chefIdInput.getText()) == null) {
                show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR");
                alert.setContentText(chefOrderController.findChef(chefIdInput.getText()));
                alert.showAndWait();
            }
        });
        return grid;
    }

    /**
     * Used to display all the dishes on the screen.
     *
     * @return returns the scene to display to the screen.
     */
    private Scene manageOrders() {
        BorderPane overallLayout = new BorderPane();

        HBox buttonBox = layoutFactory.getBackButtonLayout(backButton);
        overallLayout.setBottom(buttonBox);

        ScrollPane orderScroller = new ScrollPane();
        orderScroller.setContent(displayDishes);
        orderScroller.setMinWidth(WIDTH - 250);
        overallLayout.setLeft(orderScroller);

        Scene scene = new Scene(overallLayout, WIDTH, HEIGHT);
        scene.getStylesheets().add("style.css");

        return scene;
    }

    /**
     * Used to determine which chef is responsible for cooking a dish.
     */
    public void popUpChef(Recipe dish) {
        GridPane grid = new GridPane();
        Label pass = new Label("Chef ID#:");

        GridPane.setConstraints(pass, 0, 0);
        GridPane.setConstraints(chefId, 1, 0);
        GridPane.setConstraints(confirm, 8, 0);
        grid.getChildren().addAll(pass, chefId, confirm);
        Scene scene = new Scene(grid, 420, 50);
        scene.getStylesheets().add("style.css");
        grid.getStyleClass().add("grid");
        popUp.setTitle("Chef Id");
        popUp.setScene(scene);
        popUp.show();

        confirm.setOnAction(e -> {
            if (chefOrderController.findChef(chefId.getText(), dish) != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR");
                alert.setContentText(chefOrderController.findChef(chefId.getText(), dish));
                alert.showAndWait();
            } else {
                chefOrderController.cooked(dish);
                chefId.clear();
                popUp.close();
            }
        });
    }

    /**
     * That pop up screen that displays the instructions for the dish.
     *
     * @param instructions The instructions specified for the dish.
     */
    public void popUpInstructions(VBox instructions) {
        instructions.getChildren().add(close);
        Scene scene = new Scene(instructions, 400, 300);
        popUp.setTitle("Instructions.");
        popUp.setScene(scene);
        popUp.show();
    }

    @Override
    public void update() {
        chefOrderController.updateChefOrderScreen(this);
    }

    @Override
    public void initialize() {
        backButton.setOnAction(e -> switchController.goToOrder());
    }
}
