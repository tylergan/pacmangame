/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the JSONParse class, a class used to parse the json config file.
*/

package ghost;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONParse {
    private JSONObject jsonConfig;

    /**
     * The constructor of the JSONParse class.
     * @param filename the filename of the JSON file.
     */
    public JSONParse(String filename) {
        try{
            jsonConfig = (JSONObject) new JSONParser().parse(new FileReader(filename));
        } catch (Exception e) {
            System.out.println("The JSONParser was not able to properly configure the file.");
            System.exit(1);
        }
    }

    /**
     * Will return the name of the filename the map.
     * @return map filename.
     */
    public String getMap() {
        return (String) jsonConfig.get("map");
    }

    /**
     * Will return the specified number of lives for Pacman.
     * @return lives
     */
    public int getLives() {
        return (int)(long) jsonConfig.get("lives");
    }

    /**
     * Will return the specified speed for Pacman.
     * @return speed 
     */
    public int getSpeed() {
        return (int)(long) jsonConfig.get("speed");
    }

    /**
     * Will return the specified modeLengths for the Ghosts.
     * @return modeLengths
     */
    public List<Integer> getModeLengths() {
        List<Integer> modeLengths = new ArrayList<>();

        JSONArray configModeLengths = (JSONArray) jsonConfig.get("modeLengths");
        Iterator<Long> iterator = configModeLengths.iterator();

        while (iterator.hasNext()) {
            modeLengths.add((int)(long) iterator.next());
        }

        return modeLengths;

    }

    /**
     * Will return the specified frightened length for the Ghosts.
     * @return frightenedLength 
     */
    public int getFrightenedLength() {
        return (int)(long) jsonConfig.get("frightenedLength");
    }
    
}
