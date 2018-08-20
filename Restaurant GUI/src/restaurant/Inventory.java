package restaurant;

import dishes.DishInterpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for storing and keeping track of ingredients.
 *
 * @author Thomas Leung
 */
public class Inventory {
    private HashMap<String, int[]> ingredients;

    /**
     * instantiate a new inventory object and adds all the ingredients into the hash map appropriately.
     */
    public Inventory() {
        ingredients = new HashMap<>();
        String ingredient;
        String[] input;

        try{
            File requests = new File("Requests.txt");
            if(!requests.exists()){
                if (requests.createNewFile()){System.out.println("New Requests.txt has been created.");}
            }

            FileReader ingredientGetter = new FileReader("Ingredients.txt");
            BufferedReader ingredientLine = new BufferedReader(ingredientGetter);

            //loop through all the lines of text to pull out the ingredient name, starting quantity and threshold.
            while((ingredient = ingredientLine.readLine()) != null){
                input = ingredient.split("\\|");
                int[] tempArray = {Integer.parseInt(input[1].trim()),
                Integer.parseInt(input[2].trim())};
                ingredients.put(input[0].trim(), tempArray);
            }
            ingredientGetter.close(); //close the file
        }
        catch(FileNotFoundException e){System.out.println("File does not exist!");}
        catch(IOException c){System.out.println("Unknown file error occurred.");}
    }

    /**
     * The automated function that will run when an ingredient reaches a specified thresh hold
     *
     * @param ingredient the specified ingredient that will be requested for restock.
     */
    private void automatedRequest(String ingredient) {
        try {
            File requests = new File("Requests.txt");

            BufferedWriter request = new BufferedWriter(new FileWriter(requests, true));
            request.write("Please order 20 of " + ingredient + ".\n");

            request.close();
        } catch (Exception e) {
            System.out.println("Error reading/opening file");
        }
    }

    /**
     * A manual write in request for ingredients.
     *
     * @param ingredient the specified ingredient that will be requested for restock.
     */
     public boolean manualRequest(String ingredient, int requestQuantity) {
         for(String key: ingredients.keySet())
         {
             if(key.equals(ingredient)){
                 try {
                     File requests = new File("Requests.txt");

                     BufferedWriter request = new BufferedWriter(new FileWriter(requests, true));
                     request.write("Please order " + requestQuantity + " of " + ingredient + ".\n");

                     request.close();
                     System.out.println("finished");
                 } catch (Exception e) {
                     System.out.println("Error reading/opening file");
                 }
                 return true;
             }
         }
        return false;
    }

    //returns the quantity of the specified ingredient.
    public int getQuantity(String ingredient) {
        //Check to see if the ingredient is part of the menu first.
        try {
            return ingredients.get(ingredient)[0];
        }
        //This only runs if the ingredient does not exist.
        catch(NullPointerException notExisting){
            return 0;
        }
    }

    /**
     * Adds the new shipment of ingredients into the inventory.
     *
     * @param ingredient the specified ingredient that will be receiving an increase in quantity.
     * @param quantity   the amount that will be added into the inventory.
     */
    public boolean addIngredient(String ingredient, int quantity) {
        for(String key: ingredients.keySet()) {
            if(key.equals(ingredient)) {
                ingredients.get(ingredient)[0] += quantity;
                writeInChange();
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the quantity of said ingredient after use.
     *
     * @param ingredient the specified ingredient that will be receiving a decrease in quantity.
     * @param quantity   the amount that will  be used in the inventory.
     */
    public void subtractIngredient(String ingredient, int quantity) {
        ingredients.get(ingredient)[0] -= quantity;
        writeInChange();
        if (ingredients.get(ingredient)[0] <= ingredients.get(ingredient)[1]) {
            automatedRequest(ingredient);
        }
    }

    //returns a string representation of all ingredients in the inventory and their quantity.
    @Override
    public String toString() {
        //print out the ingredient name and the quantity.
        StringBuilder messageBuild = new StringBuilder(200);
        for (String ingredient : ingredients.keySet()) {
            String ingredientName = ingredient + DishInterpreter.charExtender(" ", 25 - ingredient.length());
            String quantity = "Quantity: " + ingredients.get(ingredient)[0];
            String threshold = "Threshold: " + ingredients.get(ingredient)[1];
            messageBuild.append(ingredientName);
            messageBuild.append(quantity);
            messageBuild.append(DishInterpreter.charExtender(" ", 25 - quantity.length()));
            messageBuild.append(threshold);
            messageBuild.append("\n");
        }
        return messageBuild.toString();
    }

    /**
     * Writes changes in the Ingredients.txt to retain changes for when the program closes.
     */
    private void writeInChange(){
        try{
            File ingredients = new File("Ingredients.txt");

            BufferedWriter writer = new BufferedWriter(new FileWriter(ingredients));
            for(Map.Entry<String, int[]> entry: this.ingredients.entrySet()){
                writer.write(entry.getKey() + " | " + entry.getValue()[0] + " | " + entry.getValue()[1] + "\n");
            }
            writer.close();
        }
        catch(Exception e){
            System.err.println("Error working with file!");
        }
    }
}
