package GUI.Screen;

import GUI.Controllers.TableController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * The screen for showing the tables
 *
 * @author Jian Xian Li
 */
public class TableScreen extends Screen {
    private Button orderButton;
    private Button inventoryButton;
    private Button employeeButton;
    private GridPane tableLayout;
    private TableController tableController;

    /**
     * Construct a table screen
     */
    public TableScreen(Stage stage) {
        super(stage);
        orderButton = new Button();
        inventoryButton = new Button();
        employeeButton = new Button();
    }

    @Override
    public Scene getScene() {
        Button[] buttons = layoutFactory.navigationBar(orderButton, inventoryButton, employeeButton,
                "tables");
        BorderPane overallLayout = new BorderPane();

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(buttons);

        overallLayout.setBottom(buttonBox);
        overallLayout.setCenter(tableLayout);
        Scene scene = new Scene(overallLayout, WIDTH, HEIGHT);
        scene.getStylesheets().add("style.css");
        return scene;
    }

    @Override
    public void update() {
        tableController.updateOrderScreens();
        tableController.updateTableButtons();
    }

    @Override
    public void initialize() {
        tableLayout = tableController.getTableLayout();
        orderButton.setOnAction(e -> switchController.goToOrder());
        inventoryButton.setOnAction(e -> switchController.goToInventory());
        employeeButton.setOnAction(e -> switchController.goToEmployee());
    }

    /**
     * Initialize this table screen's table controller
     *
     * @param tableController A table controller representing this restaurant
     */
    public void initTableController(TableController tableController) {
        this.tableController = tableController;
    }
}
