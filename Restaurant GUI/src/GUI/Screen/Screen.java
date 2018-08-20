package GUI.Screen;

import GUI.LayoutFactory;
import GUI.Controllers.SwitchController;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Some screen representation in the application
 *
 * @author Jian Xian Li
 */
public abstract class Screen {
    public final static int WIDTH = 1000;
    final static int HEIGHT = 500;
    LayoutFactory layoutFactory;
    private Stage mainStage;
    SwitchController switchController;

    /**
     * Create a new screen
     */
    Screen(Stage stage) {
        layoutFactory = new LayoutFactory();
        mainStage = stage;
    }

    /**
     * Return a scene which represents this class
     *
     * @return A scene for this class
     */
    public abstract Scene getScene();

    /**
     * Updates this screen with relevant information
     */
    public abstract void update();

    /**
     * Call after setting controllers
     */
    public abstract void initialize();

    /**
     * Shows this screen in the application with the default stylesheet
     */
    public void show() {
        update();
        Scene newScene = getScene();
        newScene.getStylesheets().add("style.css");
        mainStage.setScene(newScene);
    }

    /**
     * Initializes the controller, which allow any screen to implement screen switching
     *
     * @param controller The switch controller
     */
    public void initSwitcher(SwitchController controller) {
        this.switchController = controller;
    }
}
