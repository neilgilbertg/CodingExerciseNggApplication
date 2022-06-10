package OpenHouse.AI.CodingExerciseNGG;

import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * The FileHandler Class is a class created to handle file interaction of the API.
 * Primary usage is to create/write json files into/from memory depending on the
 * method called.
 *
 * @author  Neil Gilbert Gallardo
 * @version 1.0
 * @since   2022-06-10
 */
public class FileHandler {
    private static final String FILEPATH = "logs/";
    private static final String EXTENSION = ".json";

    /**
     * This method is used write a json file into memory.
     * @param filename Name of the json file to be written into memory.
     * @param data  Contents of the json file to be written into memory.
     * @return boolean This returns true to signify that write operation is done.
     */
    public static boolean createLog(String filename, String data){
        try {
            File file = new File(FILEPATH + filename + EXTENSION);
            FileWriter fWr = new FileWriter(file);
            if (file.createNewFile()) {
                System.out.println("Log created: " + file.getName());
                fWr.write(data);
                fWr.close();
            } else {
                System.out.println("Overwriting on existing log: " + file.getName());
                fWr.write(data);
                fWr.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * This method is used to read all of the json file in memory for searching
     * @return ArrayList-JSONObject This returns all of the JSON data read inside the json files.
     */
    public static ArrayList<JSONObject> getLogFiles(){
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();

        File dir = new File(FILEPATH);
        File[] dirList = dir.listFiles();
        if (dirList != null) {
            for (File child : dirList) {
                jsonObjects.add(new JSONObject(parseFile(child)));
            }
        }
        return jsonObjects;
    }

    /**
     * This is a helper method that reads a whole file and condenses it to a single string variable.
     * @param f The file object to be condensed into a single string
     * @return String the condensed data
     */
    private static String parseFile(File f){
        String s = "";

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(f));
            String line = reader.readLine();
            while (line != null) {
                s+=line;
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
