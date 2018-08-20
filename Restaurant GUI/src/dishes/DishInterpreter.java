package dishes;


import restaurant.Order;

import java.util.ArrayList;
import java.util.HashMap;

public class DishInterpreter {
    /**
     * Helper method that returns a string representation of the dish that we want to find
     *
     * @param dish The dish that we want to find
     * @return the string representation of the dish
     */
    public static String dishToString(Recipe dish) {
        StringBuilder output = new StringBuilder(200);
        boolean wordWith = false; //Checks if the word "with" is in the string
        output.append(dish.getName());

        if (dish instanceof Combo) {
            HashMap<String, String> instructions = ((Combo) dish).getInstructions();

            ArrayList<String> foodNames = new ArrayList<>();
            for (String foodName : instructions.keySet()) {
                if (!instructions.get(foodName).equals("") && instructions.get(foodName) != null) {
                    foodNames.add(foodName);
                }
            }

            int j = 0;
            for (String foodName : foodNames) {
                if (!(wordWith)) {
                    output.append(" with ");
                    wordWith = true;
                }
                output.append(instructionInterpret(instructions.get(foodName)));
                output.append(" on ");
                output.append(foodName);
                if (j == foodNames.size() - 2 && !instructions.get(foodNames.get(foodNames.size() - 1)).equals("")) {
                    output.append(" and ");
                } else if (j < foodNames.size() - 1) {
                    output.append(", ");
                }
                j++;
            }
        } else {
            if (!((Food) dish).getInstructions().equals("")) {
                output.append(" with ");
                output.append(instructionInterpret(((Food) dish).getInstructions()));
            }
        }
        return output.toString();
    }

    /**
     * Helper method that takes the coded instructions and converts it into a English statement
     *
     * @param instructions the coded instructions
     * @return The instructions in a more grammatically correct English statement
     */
    public static String instructionInterpret(String instructions) {
        if (instructions.equals("")) {
            return instructions;
        }
        String[] accommodations = instructions.split(",\\s");
        StringBuilder additions = new StringBuilder(100);
        StringBuilder subtractions = new StringBuilder(100);
        for (String accommodation : accommodations) {
            int tempQuantity = Integer.parseInt(accommodation.split(":\\s")[0]);
            String revisedAccommodation = tempQuantity + " " + accommodation.split(":\\s")[1];
            if (accommodation.contains("-")) {
                subtractions.append(revisedAccommodation.substring(1));
                subtractions.append(",");
            } else {
                additions.append(revisedAccommodation);
                additions.append(",");
            }
        }
        if (!subtractions.toString().equals("")) {
            subtractions.deleteCharAt(subtractions.length() - 1);
        }
        if (!additions.toString().equals("")) {
            additions.deleteCharAt(additions.length() - 1);
        }
        return interpretInstructions(additions.toString(), subtractions.toString());
    }

    public static String chefDishInfo(Recipe dish, String indent) {
        StringBuilder chefFormattedDish = new StringBuilder(50);
        if (dish instanceof Combo) {
            for (String foodName : ((Combo) dish).getInstructions().keySet()) {
                String foodItem = indent + foodName;
                chefFormattedDish.append(foodItem);
                chefFormattedDish.append("\n");
                String currInstruction = ((Combo) dish).getInstructions().get(foodName);
                if (!currInstruction.equals("")) {
                    chefFormattedDish.append(DishInterpreter.foodInfo(currInstruction, indent + indent));
                    chefFormattedDish.append("\n");
                }
            }
        } else {
            if (!((Food) dish).getInstructions().equals("")) {
                String foodInstruction = ((Food) dish).getInstructions();
                chefFormattedDish.append(DishInterpreter.foodInfo(foodInstruction, indent + indent));
                chefFormattedDish.append("\n");
            }
        }
        return chefFormattedDish.toString();
    }

    /**
     * A helper method that separates the instructions of a dish and gives each instruction its own line so that
     * it is easier to read. You can enter in a trailing indent line as well.
     *
     * @param instructions The instructions of the particular food item
     * @param indent       A string in the form of trailing spaces
     * @return The more easily readable instructions
     */
    private static String foodInfo(String instructions, String indent) {
        StringBuilder output = new StringBuilder(200);
        if (instructions.contains(",")) {
            String[] accommodations = instructions.split(", ");
            for (String accommodation : accommodations) {
                output.append(indent);
                int tempQuantity = Integer.parseInt(accommodation.split(":\\s")[0]);
                String revisedAccommodation = tempQuantity + " " + accommodation.split(":\\s")[1];
                if (accommodation.contains("-")) {
                    output.append(specificInterpret(revisedAccommodation.substring(1), "subtraction"));
                } else {
                    output.append(specificInterpret(revisedAccommodation, "addition"));
                }
                if (!accommodations[accommodations.length - 1].equals(accommodation)){
                    output.append("\n");
                }
            }
            return output.toString();
        } else if (!instructions.equals("")) {
            int tempQuantity = Integer.parseInt(instructions.split(":\\s")[0]);
            String revisedAccommodation = tempQuantity + " " + instructions.split(":\\s")[1];
            if (instructions.contains("-")) {
                return (indent + specificInterpret(revisedAccommodation.substring(1), "subtraction"));
            }
            return (indent + specificInterpret(revisedAccommodation, "addition"));
        }
        return "";
    }

    /**
     * Helper method that deals with the additions and subtractions of ingredients and returns the special
     * instructions that the chef needs to know to make the dish
     *
     * @param additions    The additions needed to be made
     * @param subtractions The subtractions needed to be made
     * @return A string representation of the accommodations that the chef needs to make for the dish
     */
    private static String interpretInstructions(String additions, String subtractions) {
        //extraInstructions will only contain the additions and subtractions
        StringBuilder extraInstructions = new StringBuilder(200);
        //Additions to the dish
        if (!(additions.equals(""))) {
            String temp = specificInterpret(additions, "addition");
            extraInstructions.append(temp);
        }
        //Checks if there are both additions and subtractions to the food
        if (!(additions.equals("") || subtractions.equals(""))) {
            extraInstructions.append(" and ");
        }
        //Subtractions to the dish
        if (!(subtractions.equals(""))) {
            String temp = specificInterpret(subtractions, "subtraction");
            extraInstructions.append(temp);
        }
        return (extraInstructions.toString());
    }

    /**
     * Helper method that will give instructions of either additions or subtractions need to be made
     *
     * @param accommodation the string representation of what ingredients to add or subtract
     * @param type          indicates whether this accommodation is an addition or a subtraction
     * @return A string representation of what has been added or subtracted from the food
     */
    private static String specificInterpret(String accommodation, String type) {
        if (accommodation.contains(",")) {
            StringBuilder output = new StringBuilder(100);
            String[] accommodations = accommodation.split(",");
            for (int i = 0; i < accommodations.length; i++) {
                //ingredientInfo[1] holds ingredient and ingredientInfo[0] holds the amount of the ingredient
                String[] ingredientInfo = accommodations[i].split("\\s");
                output.append(ingredientInfo[0].trim()); //Amount added/subtracted
                if (type.equals("addition")) {
                    output.append(" extra ");
                } else {
                    output.append(" less ");
                }
                output.append(accommodations[i].substring(ingredientInfo[0].length()).trim()); //the ingredient
                if (i < accommodations.length - 1) {
                    output.append(", ");
                }
            }
            return output.toString();
        } else {
            //ingredientInfo[1] holds ingredient and ingredientInfo[0] holds the amount of the ingredient
            String accommodations = accommodation.trim();
            String[] ingredientInfo = accommodations.split("\\s");
            if (type.equals("addition")) {
                return ingredientInfo[0].trim() + " extra " +
                        accommodations.substring(ingredientInfo[0].length()).trim();
            } else {
                return ingredientInfo[0].trim() + " less " +
                        accommodations.substring(ingredientInfo[0].length()).trim();
            }
        }
    }

    /**
     * This a helper method that creates a string of a specified size of a character
     *
     * @param size the size of the string
     * @return the extended character string
     */
    public static String charExtender(String character, int size) {
        StringBuilder line = new StringBuilder(60);
        for (int i = 0; i < size; i++) {
            line.append(character);
        }
        return line.toString();
    }
}
