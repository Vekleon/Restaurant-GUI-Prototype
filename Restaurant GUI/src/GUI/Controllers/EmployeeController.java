package GUI.Controllers;

import employees.Chef;
import employees.Employee;
import employees.Manager;
import employees.Server;
import javafx.scene.control.Label;
import restaurant.Inventory;
import restaurant.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Takes care of methods calls made from employeeScreen.
 * @author Thomas Leung
 */
public class EmployeeController {
    private ArrayList<Employee> employees;
    private ArrayList<Integer> oldServerId;
    private ArrayList<Integer> oldManagerId;
    private ArrayList<Integer> oldChefId;
    private ArrayList<Order> restaurantOrders;

    private Inventory inventory;
    private EmployeeWriter writer;

    /**
     * Used to manipulate data related to employees.
     * @param employees a list of the current hired employees
     * @param inventory the inventory that is within the restaurant.
     */
    public EmployeeController(ArrayList<Employee> employees, Inventory inventory, ArrayList<Order> restaurantOrders){
        this.employees = employees;
        oldServerId = new ArrayList<>();
        oldManagerId = new ArrayList<>();
        oldChefId = new ArrayList<>();

        writer = new EmployeeWriter();

        this.inventory = inventory;
        this.restaurantOrders = restaurantOrders;
        getFiredIds();
    }

    /**
     * hire an employee and add them into the restaurant data base.
     *
     * @param message the message displayed on screen.
     * @param employeeType the type of employee that is being hired.
     */
    public void hire(Label message, String employeeType){
        switch (employeeType) {
            case "manager":
                if (oldManagerId.size() != 0) {
                    employees.add(new Manager(inventory, oldManagerId.get(0)));
                    oldManagerId.remove(0);
                    //change the FireIds.txt to remove the old employee from the list.
                    writer.reWriteFile((employees.get(employees.size() - 1)).toString(), "FiredIds.txt");
                } else {
                    employees.add(new Manager(inventory));
                }
                break;
            case "chef":
                if (oldServerId.size() != 0) {
                    employees.add(new Chef(oldChefId.get(0), restaurantOrders));
                    oldChefId.remove(0);
                    //change the FireIds.txt to remove the old employee from the list.
                    writer.reWriteFile((employees.get(employees.size() - 1)).toString(), "FiredIds.txt");
                } else {
                    employees.add(new Chef(restaurantOrders));
                }
                break;
            case "server":
                if (oldServerId.size() != 0) {
                    employees.add(new Server(oldServerId.get(0)));
                    oldServerId.remove(0);
                    //change the FireIds.txt to remove the old employee from the list.
                    writer.reWriteFile((employees.get(employees.size() - 1)).toString(), "FiredIds.txt");
                } else {
                    employees.add(new Server());
                }
                break;
            default:
                return;
        }
        // set the message on the screen to confirm successful hiring and write into the Employees.txt
        message.setText("Finished hiring " + employees.get(employees.size() - 1).toString());
        writer.writeToEmployees((employees.get(employees.size() - 1)).toString());
    }

    /**
     * fire an employee and remove them from the restaurant data base.
     */
    public void fire(Employee employee){
        employees.remove(employee);
        if (employee instanceof Chef) {
            oldChefId.add(employee.getJobId());
            Collections.sort(oldChefId);
        } else if (employee instanceof Server) {
            oldServerId.add(employee.getJobId());
            Collections.sort(oldServerId);
        } else if (employee instanceof Manager) {
            oldManagerId.add(employee.getJobId());
            Collections.sort(oldManagerId);
        }
        else{
            return;
        }
        writer.writeToFired(employee.toString());
        writer.reWriteFile(employee.toString(), "Employees.txt");
    }

    /**
     * Find the employee inputted.
     * @param employeeId the employee inputted into the text field.
     * @return return the employee if found.
     */
    public Employee findEmployee(String employeeId){
        for(Employee employee: employees){
            if(employee.toString().equals(employeeId)){
                return employee;
            }
        }
        return null;
    }

    //return a string representation of all employees in the database.
    @Override
    public String toString(){
        //print out the employee's id.
        StringBuilder messageBuild = new StringBuilder(200);
        for(Employee employee: employees){
            messageBuild.append(employee.toString());
            messageBuild.append("\n");
        }
        return messageBuild.toString();
    }

    /**
     * Get the id values of the previously fired employees.
     */
    private void getFiredIds(){
        String id;
        // these value are used to set the static values of number of employees in each type of employee class in case
        // a fired employee Id is greater than the current number of employees to prevent inconsistencies in employee ids
        int biggestServerId = 0;
        int biggestManagerId = 0;
        int biggestChefId = 0;
        try{
            FileReader readFile = new FileReader("FiredIds.txt");
            BufferedReader read = new BufferedReader(readFile);

            while((id = read.readLine()) != null){
                String[] separate = id.split("_");
                if(id.contains("Chef")){
                    oldChefId.add(Integer.valueOf(separate[1]));
                    if(Integer.valueOf(separate[1]) > biggestChefId){ biggestChefId = Integer.valueOf(separate[1]);}
                }
                else if(id.contains("Server")){
                    oldServerId.add(Integer.valueOf(separate[1]));
                    if(Integer.valueOf(separate[1]) > biggestServerId) {biggestServerId = Integer.valueOf(separate[1]);}
                }
                else if(id.contains("Manager")){
                    oldManagerId.add(Integer.valueOf(separate[1]));
                    if(Integer.valueOf(separate[1]) > biggestManagerId) {biggestManagerId = Integer.valueOf(separate[1]);}
                }
            }
            read.close();
        }
        catch(Exception e){
            System.err.println("Problem working with file.");
        }
        // Sort the list of employee ids from least to greatest
        Collections.sort(oldChefId);
        Collections.sort(oldServerId);
        Collections.sort(oldManagerId);

        //checks to see if a fired employee id is greater than the current number of employees. If so, set a new
        //number of employees value.
        if(biggestManagerId > Manager.getNumOfManagers()){Manager.setNumOfManagers(biggestManagerId);}
        if(biggestServerId > Server.getNumOfServers()){Server.setNumOfServers(biggestServerId);}
        if (biggestChefId > Chef.getNumOfChefs()) {Chef.setNumOfChefs(biggestChefId);}
    }

    /**
     * Changes the receiver status of an employee to true
     * @param employee the employee whose receiver status will be changed.
     * @return returns whether the employee was already a receiver or not.
     */
    public boolean changeReceiverStatus(Employee employee){
        if(employee.getReceiver()){
            return false;
        }
        else{
            employee.setReceiver(true, inventory);
            return true;
        }

    }

}
