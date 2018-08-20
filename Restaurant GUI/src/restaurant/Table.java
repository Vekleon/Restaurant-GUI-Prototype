package restaurant;

import employees.Server;
import javafx.scene.control.Button;

/**
 * this will be the class representation of a table within the restaurant.
 *
 * @author Thomas Leung
 */
public class Table {
    private static int tables;
    private int size;
    private int numberOfOccupants;
    private int tableNumber;
    private Server server;
    private Order order;
    private String status; //unoccupied, occupied, ordered, ready, completed
    private Button tableButton;

    /**
     * Initialize a new table instance that will represent a table within the restaurant.
     *
     * @param size the number of seats available for customers to sit in.
     */
    public Table(int size) {
        status = "unoccupied";
        this.size = size;
        this.tableNumber = ++tables;
    }

    /**
     * Sets the server to this table when a customer comes in.
     *
     * @param server the server that will be responsible for serving this table.
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * Sets the status of the table which will determines the colour of the table on the gui.
     *
     * @param status the status the current table is in. [unoccupied, ordering, waiting for order, eating, occupied]
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Assigns the order that customer made to this table.
     *
     * @param order the order in which the customer made.
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Sets the number of people seating at the table.
     *
     * @param occupants the number of people that are seated at the table.
     */
    public void setNumberOfOccupants(int occupants) {
        this.numberOfOccupants = occupants;
    }

    //returns the number of seats available for this table.
    public int getSize() {
        return size;
    }

    //returns the table number assigned to this table.
    public int getTableNumber() {
        return tableNumber;
    }

    //returns the server who was assigned this table.
    public Server getServer() {
        return server;
    }

    //returns the order the customer had ordered.
    public Order getOrder() {
        return order;
    }

    //returns the current status of the table.
    public String getStatus() {
        return status;
    }

    //returns the number of people sitting at the table.
    public int getNumberOfOccupants() {
        return numberOfOccupants;
    }

    //sets this table's button (for the GUI)
    public void setTableButton(Button tableButton) {
        this.tableButton = tableButton;
    }

    //returns the button associated with this table
    public Button getTableButton() {
        return tableButton;
    }

    /**
     * Resets the table button text to just Table + the table number
     */
    public void resetTableButton() {
        tableButton.setText("Table " + tableNumber);
    }

}

