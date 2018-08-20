package employees;

import restaurant.Inventory;

import java.io.*;

/**
 * The manager class. Will assign receiver duties to other employees and can receive themselves.
 * Also manages the inventory and can send requests to restock the inventory
 *
 * @author Victor Huang
 */
public class Manager extends Employee {

    private static int numOfManagers = 0;

    public Manager(Inventory inventory) {
        super(true, "Manager", numOfManagers + 1);
        this.inventory = inventory;
        setReceiver(true, inventory);
        numOfManagers++;
    }

    public Manager(Inventory inventory, int oldId) {
        super(false, "Manager", oldId);
        this.inventory = inventory;
        setReceiver(true, inventory);
    }

    /**
     * Allows the manager to give or take away the responsibilities of a receiver to any employee
     *
     * @param employee The employee who will be get/lose receiver responsibilities
     */
    public void changeReceiverStatus(Employee employee) {
        employee.setReceiver(!employee.getReceiver(), inventory);
    }

    /**
     * This method has the manager indicate that they have sent the emails for new ingredient requests and will replace
     * the old requests.txt with a new updated one
     */
    protected void sendRequests() {
        try {
            String request;
            File requests = new File("Requests.txt");
            FileReader requestReader = new FileReader(requests);
            BufferedReader requestLine = new BufferedReader(requestReader);
            File updatedRequests = new File("tempRequests.txt");
            BufferedWriter updatedRequest = new BufferedWriter(new FileWriter(updatedRequests, false));
            if (!updatedRequests.exists()) {
                if (!updatedRequests.createNewFile()) {
                    System.err.println("Failed to create new file.");
                }
            }
            while ((request = requestLine.readLine()) != null) {
                //loop through all the lines of text and add that the email is sent.
                if (!request.contains("Email")) {
                    updatedRequest.write(request + "(Email Sent)");
                } else {
                    updatedRequest.write(request);
                }
            }
            requestReader.close(); //close the file
            updatedRequest.close();
            System.out.println("Emails for new ingredients have been sent.");
            if (!(updatedRequests.renameTo(requests))) {
                System.err.println("Could not rename file into Employees.txt");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist!");
        } catch (IOException c) {
            System.out.println("Unknown file error occurred.");
        }
    }

    /**
     * Allows the manager to get the printout of the inventory
     */
    public void checkInventory() {
        System.out.println(inventory.toString());
    }

    public static int getNumOfManagers() {
        return numOfManagers;
    }

    // used to set the number of managers in case there are already employees in the database.
    public static void setNumOfManagers(int numManagers) {
        numOfManagers = numManagers;
    }
}