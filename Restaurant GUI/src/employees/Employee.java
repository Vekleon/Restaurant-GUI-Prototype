package employees;

import dishes.Combo;
import dishes.Food;
import dishes.Recipe;
import restaurant.Inventory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A abstract class for all employees. This class will declare all common attributes between any kind of employee,
 * mainly receiver methods.
 *
 * @author Victor Huang
 */
public abstract class Employee {

    private boolean receiver; // Checks if the employee can be a receiver or not
    private int jobId; // jobIds correspond to each specific job's id (Ex. 3rd chef will have id 3)
    private String jobType; // Specific job type
    protected Inventory inventory;

    protected Employee(boolean receiver, String type, int jobId) {
        this.receiver = receiver;
        this.jobType = type;
        this.jobId = jobId;
        this.inventory = null;
    }

    /**
     * Getter method for receiver
     *
     * @return if the employee currently has receiver duties or not
     */
    public boolean getReceiver() {
        return this.receiver;
    }

    /**
     * Setter method for receiver
     *
     * @param receiver  the value receiver will be set to
     * @param inventory the restaurant's inventory
     */
    public void setReceiver(boolean receiver, Inventory inventory) {
        this.receiver = receiver;
        if (this.inventory == null) {
            this.inventory = inventory;
        } else {
            this.inventory = null;
        }
    }

    /**
     * Getter method for the employee's specific job id
     *
     * @return the job id of the employee
     */
    public int getJobId() {
        return this.jobId;
    }

    /**
     * Reads through requests.txt and will receive the new shipments of ingredients.
     */
    public void receive() {
        if (receiver) {
            try {
                String request;
                File requests = new File("Requests.txt");
                FileReader requestReader = new FileReader(requests);
                BufferedReader requestLine = new BufferedReader(requestReader);
                File unreceivedRequests = new File("tempRequests.txt");
                BufferedWriter unreceivedRequest = new BufferedWriter(new FileWriter(unreceivedRequests, false));
                if (!unreceivedRequests.exists()) {
                    if (unreceivedRequests.createNewFile()) {
                        System.out.println("Successfully created new file.");
                    } else {
                        System.err.println("Failed to create new file.");
                    }
                }
                while ((request = requestLine.readLine()) != null) {
                    if (request.contains("(Email Sent)")) {
                        String[] array = request.split(" ");
                        String ingredient = request.substring(request.indexOf(array[4]), request.indexOf("."));
                        int quantity = Integer.parseInt(array[2]);
                        this.inventory.addIngredient(ingredient, quantity);
                        System.out.println("Successfully received " + quantity + " units of " + ingredient + ".");
                    } else {
                        unreceivedRequest.write(request);
                    }
                }
                requestReader.close(); //close the file
                unreceivedRequest.close();
                if (!(unreceivedRequests.renameTo(requests))) {
                    System.err.println("Could not rename file into Employees.txt");
                }
            } catch (FileNotFoundException e) {
                System.out.println("File does not exist!");
            } catch (IOException c) {
                System.out.println("Unknown file error occurred.");
            }
            if (this instanceof Manager) {
                ((Manager) this).sendRequests();
            }
        } else {
            System.err.println(jobType + " " + jobId + "does not have receiver responsibilities.");
        }
    }

    @Override
    public String toString() {
        return jobType + "_" + jobId;
    }
}