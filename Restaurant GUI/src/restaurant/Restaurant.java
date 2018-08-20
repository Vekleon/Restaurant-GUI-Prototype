package restaurant;

import employees.Chef;
import employees.Employee;
import employees.Manager;
import employees.Server;

import java.io.*;
import java.util.ArrayList;

/**
 * The base program, handles initialization and user interface.
 *
 * @author Thomas Leung
 */
public class Restaurant {
    private Inventory inventory;
    private ArrayList<Employee> employees;
    private ArrayList<Order> orders;
    private ArrayList<Order> chefOrders;
    private Table[] tables;
    private Menu menu;

    /**
     * The restaurant initializer.
     */
    public Restaurant() {
        final int SMALLTABLES = 10;
        final int MEDIUMTABLES = 6;
        final int LARGETABLES = 4;

        inventory = new Inventory();
        employees = new ArrayList<>();
        menu = new Menu(inventory);
        orders = new ArrayList<>();
        chefOrders = new ArrayList<>();

        tables = new Table[20];
        //initialize tables;
        for (int i = 0; i < SMALLTABLES; i++) {
            tables[i] = new Table(2);
        }
        for (int i = 0; i < MEDIUMTABLES; i++) {
            tables[SMALLTABLES + i] = new Table(4);
        }
        for (int i = 0; i < LARGETABLES; i++) {
            tables[SMALLTABLES + MEDIUMTABLES + i] = new Table(8);
        }

        hireEmployees();
    }

    /**
     * Used to get information from Employees.txt for existing employees in the system.
     */
    private void hireEmployees(){
        String employee;
        //these values will be used to set the static value of the number of each type of employee class to ensure
        //consistency with employee id numbers.
        int biggestServerId = 0;
        int biggestManagerId = 0;
        int biggestChefId = 0;
        try{
            FileReader readFile = new FileReader("Employees.txt");
            BufferedReader read = new BufferedReader(readFile);

            while((employee = read.readLine()) != null){
                String[] separate = employee.split("_");
                if(employee.contains("Chef")){
                    employees.add(new Chef(Integer.valueOf(separate[1]), chefOrders));
                    if(Integer.valueOf(separate[1]) > biggestChefId){ biggestChefId = Integer.valueOf(separate[1]);}
                }
                else if(employee.contains("Server")){
                    employees.add(new Server(Integer.valueOf(separate[1])));
                    if(Integer.valueOf(separate[1]) > biggestServerId){ biggestServerId = Integer.valueOf(separate[1]);}
                }
                else if(employee.contains("Manager")){
                    employees.add(new Manager(inventory, Integer.valueOf(separate[1])));
                    if(Integer.valueOf(separate[1]) > biggestManagerId){ biggestManagerId = Integer.valueOf(separate[1]);}
                }
            }
            read.close();
        }
        catch(Exception e){
            System.err.println("Problem working with file");
        }
        //Sets the number of each type of employee class.
        Chef.setNumOfChefs(biggestChefId);
        Server.setNumOfServers(biggestServerId);
        Manager.setNumOfManagers(biggestManagerId);

    }

    //getter functions used to provide variables to classes.
    public Inventory getInventory() {
        return this.inventory;
    }

    public ArrayList<Employee> getEmployees() {
        return this.employees;
    }

    public ArrayList<Order> getOrders() {
        return this.orders;
    }

    public ArrayList<Order> getChefOrders() {
        return this.chefOrders;
    }

    public Table[] getTables() {
        return tables;
    }

    /**
     * Returns a server associated with an id. If no server exists, return null.
     *
     * @param id The id to match with a server
     * @return The server which matches the id
     */
    public Server getServer(int id) {
        for (Employee employee : employees) {
            if (employee instanceof Server) {
                Server server = (Server) (employee);
                if (server.getJobId() == id) {
                    return server;
                }
            }
        }
        return null;
    }

    /**
     * Returns the menu associated with this restaurant
     *
     * @return The menu
     */
    public Menu getMenu() {
        return menu;
    }
}
