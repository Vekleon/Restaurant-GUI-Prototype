package GUI.Controllers;

import GUI.Screen.*;

/**
 * A Controller meant to switch screens
 */
public class SwitchController {
    private InventoryScreen inventoryScreen;
    private OrderScreen orderScreen;
    private TableScreen tableScreen;
    private EmployeeScreen employeeScreen;

    /**
     * Creates the main controller (for main screen, basically switches screens)
     * @param inventoryScreen   The inventory screen
     * @param orderScreen   The order screen
     * @param tableScreen   The table screen
     */
    public SwitchController(InventoryScreen inventoryScreen, OrderScreen orderScreen,
                          TableScreen tableScreen, EmployeeScreen employeeScreen){
        this.inventoryScreen = inventoryScreen;
        this.orderScreen = orderScreen;
        this.tableScreen = tableScreen;
        this.employeeScreen = employeeScreen;
    }

    /**
     * Switch to inventory screen
     */
    public void goToInventory(){
        inventoryScreen.show();
    }

    /**
     * Switch to order screen
     */
    public void goToOrder(){
        orderScreen.show();
    }

    /**
     * Go to table screen
     */
    public void goToTable(){
        tableScreen.show();
    }

    /**
     * Go to employee screen
     */
    public void goToEmployee(){
        employeeScreen.show();
    }
}
