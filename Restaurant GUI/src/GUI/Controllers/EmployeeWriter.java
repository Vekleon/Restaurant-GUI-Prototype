package GUI.Controllers;

import java.io.*;

class EmployeeWriter {

    EmployeeWriter(){}

    /**
     * Used to register that a new employee has been hired in the Employees.txt
     * @param employeeId The id of the employee that was hired.
     */
    void writeToEmployees(String employeeId){
        try{
            File employee = new File("Employees.txt");

            BufferedWriter emp = new BufferedWriter(new FileWriter(employee, true));
            emp.write("\n" + employeeId);

            emp.close();
        }
        catch(Exception e){
            System.err.println("Error working with file.");
        }
    }

    /**
     * Writes any additions made to the FiredIds.txt.
     * @param employeeId The employee ID that will be added to the FiredIds.txt.
     */
    void writeToFired(String employeeId){
        try{
            File fired = new File("FiredIds.txt");

            BufferedWriter fire = new BufferedWriter(new FileWriter(fired, true));
            fire.write(employeeId + "\n");

            fire.close();
        }
        catch(Exception e){
            System.err.println("Error working with file.");
        }
    }

    /**
     * Title: Find a line and remove it
     * Author: SingleShot
     * Date: 04/09/09
     * Code Version: Unknown
     * Type: source code
     * Availability: https://stackoverflow.com/questions/1377279/find-a-line-in-a-file-and-remove-it
     * @param employeeId The id of the employee that is going to be removed from the Employees.txt
     */
    void reWriteFile(String employeeId, String fileName){
        try {
            File input = new File(fileName);
            File replacement = new File("replacement.txt");

            BufferedReader reader = new BufferedReader(new FileReader(input));
            BufferedWriter writer = new BufferedWriter(new FileWriter(replacement));

            String currentLine;

            while((currentLine = reader.readLine()) != null){
                if(currentLine.equals(employeeId)) continue;
                writer.write(currentLine + "\n");
            }
            writer.close();
            reader.close();

            if(!(input.delete())){System.err.println("Could not delete Employees.txt.");}
            if(!(replacement.renameTo(input))){ System.err.println("Could not rename file into Employees.txt");}
        }
        catch(Exception e){
            System.err.println("Problem working with file.");
        }
    }
}
