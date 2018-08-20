package restaurant;

import dishes.DishInterpreter;
import dishes.Recipe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The bill class. It allows the server to print out the bill with a variety of options.
 *
 * @author Victor Huang
 */
public class Bill {

    private ArrayList<ArrayList<Recipe>>  dishes;
    private static final int billSize = 40; // The width of the bill
    private int numberOfCustomers;
    private HashMap<Recipe, Integer> splitAmounts;

    public Bill(ArrayList<ArrayList<Recipe>> dishes, int numberOfCustomers) {
        this.dishes = dishes;
        this.splitAmounts = findDishOccurrences(dishes);
        this.numberOfCustomers = numberOfCustomers;
    }

    /**
     * Creates the bill with the idea of splitting the order evenly between a certain number of customers at the table
     *
     * @return the bill
     */
    public String[] printBill() {
        return bills(dishes, dishes.size(), numberOfCustomers);
    }

    /**
     * Creates the bill allowing for customers to choose which items they are paying for, and if they are splitting the
     * bill with others or not
     *
     * @param dishes            the dishes that the customers are paying for in this bill
     * @param numberOfCustomers the number of customers that will split this bill
     * @return An array of all string representations of the bills
     */
    private String[] bills(ArrayList<ArrayList<Recipe>>  dishes, int numOfCustomBills, int numberOfCustomers) {
        String[] billRepresentations = new String[numOfCustomBills];
        //Checks if each person is getting a separate bill
        if (numOfCustomBills > 1) {
            int i = 0;
            for (ArrayList<Recipe>  oneBillDishes : dishes) {
                billRepresentations[i++] = singleBill(oneBillDishes, 1, numberOfCustomers);
            }
            return billRepresentations;
        }
        //Will split one bill accordingly
        billRepresentations[0] = singleBill(dishes.get(0), numberOfCustomers, numberOfCustomers);
        return billRepresentations;
    }

    /**
     * Creates a single bill, which will split the prices according to the number of customers.
     *
     * @param oneBillDishes     An array that holds all the dishes that will be on this bill
     * @param numberOfCustomers The number of customers at the table (used to check for gratuity)
     * @return The string representation of a bill
     */
    private String singleBill(ArrayList<Recipe>  oneBillDishes,int numberOfSplits, int numberOfCustomers) {
        StringBuilder singleBill = new StringBuilder(500);
        int indent = billSize / 20;
        double subtotal = 0.00;
        double gratuityAmount = 0.00;
        singleBill.append(nextLineAdder(DishInterpreter.charExtender("-", billSize)));
        singleBill.append(nextLineAdder("NAME" + DishInterpreter.charExtender(" ", billSize - 9) + "PRICE"));
        singleBill.append(nextLineAdder(DishInterpreter.charExtender("-", billSize)));
        //Formatted portion for each dish
        for (Recipe dish : oneBillDishes) {
            double price = roundToTwo(dish.getPrice() / splitAmounts.get(dish));
            subtotal += price;
            String dishLine = formattedBillLine(dish.getName(), price, billSize);
            singleBill.append(nextLineAdder(dishLine));
            //Checks for extra instructions, and also prints out the foods in a combo
            singleBill.append(DishInterpreter.chefDishInfo(dish, DishInterpreter.charExtender(" ", indent)));
        }
        singleBill.append(nextLineAdder(DishInterpreter.charExtender("-", billSize)));
        //If the bill is being split among people,
        if (numberOfSplits > 1){
            String indicator = "Bill split among " + numberOfSplits + " people";
            singleBill.append(indicator);
            singleBill.append((nextLineAdder(DishInterpreter.charExtender(" ", billSize - indicator.length()))));
        }
        //Formatted portion for subtotal, tax, gratuity, and total
        subtotal = roundToTwo(subtotal/numberOfSplits);
        singleBill.append(nextLineAdder(formattedBillLine("Sub Total:", subtotal, billSize)));
        double taxAmount = percentCharges(subtotal, 0.13);
        singleBill.append(nextLineAdder(formattedBillLine("Tax:", taxAmount, billSize)));
        //Gratuity is applied to tables of 8 or more customers
        if (numberOfCustomers >= 8) {
            gratuityAmount = percentCharges(subtotal, 0.15);
            singleBill.append(nextLineAdder(formattedBillLine("Gratuity:", gratuityAmount, billSize)));
        }
        singleBill.append(formattedBillLine("TOTAL:", roundToTwo(subtotal + taxAmount + gratuityAmount), billSize));
        return singleBill.toString();
    }

    /**
     * A method that returns a HashMap with each dish corresponding with the number of times it appears on the
     * different bills. This is feature that allows any dish to be split amount any bill.
     *
     * @param dishes the ArrayList that holds all the dishes that will be added to the bill
     * @return the key-value pairings between a dish and the number of times it appears
     */
    private HashMap<Recipe, Integer> findDishOccurrences(ArrayList<ArrayList<Recipe>>  dishes) {
        HashMap<Recipe, Integer> occurrences = new HashMap<>();
        for (ArrayList<Recipe>  oneBillDishes : dishes) {
            for (Recipe dish : oneBillDishes) {
                if (occurrences.containsKey(dish)) {
                    occurrences.put(dish, occurrences.get(dish) + 1);
                } else {
                    occurrences.put(dish, 1);
                }
            }
        }
        return occurrences;
    }

    /**
     * Returns the tax on a certain price, rounded to 2 decimal places (nearest cent)
     *
     * @param price The price to calculate tax on
     * @param rate  The rate that is being added, given as a decimal (ex: 0.13 = 13 percent)
     * @return The tax amount on price, rounded to the nearest cent
     */
    private double percentCharges(double price, double rate) {
        double addedCharges = price * rate;
        return roundToTwo(addedCharges);
    }

    /**
     * Returns a rounded double to 2 decimal places
     *
     * @return the rounded double
     */
    private double roundToTwo(double num) {
        double rounded = num * 100;
        rounded = (double) Math.round(rounded);
        rounded /= 100;
        return rounded;
    }

    /**
     * This is a helper method that formats the words and prices in a well formatted line
     *
     * @param words    The words either dealing with money or the dish names
     * @param price    the price for the dish, tax, etc.
     * @param billSize the width of the bill
     * @return A well formatted line for the bill
     */
    private String formattedBillLine(String words, double price, int billSize) {
        String tempString = words + price;
        StringBuilder formattedLine = new StringBuilder(60);
        formattedLine.append(words);
        if (tempString.charAt(tempString.length() - 2) == '.') {
            formattedLine.append(DishInterpreter.charExtender(" ", billSize - (tempString.length() + 1)));
            formattedLine.append(price);
            formattedLine.append("0");
        } else {
            formattedLine.append(DishInterpreter.charExtender(" ", billSize - tempString.length()));
            formattedLine.append(price);
        }
        return formattedLine.toString();
    }

    /**
     * Takes a string and adds a newline to it
     *
     * @param line the string you are adding a newline to
     * @return the new formatted string
     */
    private String nextLineAdder(String line) {
        return line + "\n";
    }

}
