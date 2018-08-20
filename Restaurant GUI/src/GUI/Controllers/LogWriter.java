package GUI.Controllers;

import java.io.*;

public class LogWriter {
    public LogWriter(){}

    /**
     * writes into the Log.txt
     * @param log the information that will be written into the log
     */
    static public void writeIn(String log){
        try{
            File logFile = new File("Log.txt");

            BufferedWriter write = new BufferedWriter(new FileWriter(logFile, true));
            write.write(log + "\n");

            write.close();
        }
        catch(Exception e){
            System.err.println("Trouble working with File");
        }
    }

    /**
     * Writes into the Today's Earning.txt
     * @param earningLog the information that will be written into the log.
     */
    public static void earningLogger(String earningLog) {
        try {
            File earnings = new File("Today's Earnings.txt");
            BufferedWriter logger = new BufferedWriter(new FileWriter(earnings, true));
            if (!earnings.exists()) {
                if (!earnings.createNewFile()) {
                    System.err.println("Failed to create new file.");
                }
            }
            logger.write(earningLog + "\n");
            logger.close();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist!");
        } catch (IOException c) {
            System.out.println("Unknown file error occurred.");
        }
    }
}
