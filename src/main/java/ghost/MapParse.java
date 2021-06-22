/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the MapParse class, where a map file is parsed and sets important information for each object to run the game.
*/


package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import processing.core.PVector;

public class MapParse {
    private Tile[][] map = new Tile[36][28];
    private int fruitCount = 0;
    private List<GameObject> ghosts = new LinkedList<>();

    /**
     * Constructor of the MapParse class. The actual parsing is done in the constructor.
     * @param waka a Pacman object.
     */
    public MapParse(Pacman waka) {
        JSONParse jsonParser = new JSONParse("config.json");

        GameObject.modeLengths = jsonParser.getModeLengths();
        GameObject.frightenedLength = jsonParser.getFrightenedLength();
        GameObject.speed = jsonParser.getSpeed();

        PVector initialVel = new PVector(-1/16f * jsonParser.getSpeed(), 0);

        waka.setLives(jsonParser.getLives());
        waka.setVel(initialVel);
    
        try {
            File f = new File(jsonParser.getMap());
            Scanner reader = new Scanner(f);

            int yCoor = 0;
            while (reader.hasNextLine()) {
                char[] row = reader.nextLine().trim().toCharArray();

                for (int xCoor = 0; xCoor < row.length; xCoor++) {
                    PVector pos = new PVector(xCoor, yCoor);
                    map[yCoor][xCoor] = new Tile(xCoor, yCoor);

                    switch(row[xCoor]) {
                        case '0':
                            map[yCoor][xCoor].setEmpty(true);
                            break;
                        
                        case '1': case '2': case '3': case '4': case '5': case '6':
                            map[yCoor][xCoor].setWall(true, Character.getNumericValue(row[xCoor]) - 1);
                            break;
                        
                        case '7':
                            map[yCoor][xCoor].setFruit(true);
                            fruitCount++;
                            break;
                        
                        case '8':
                            map[yCoor][xCoor].setSuperFruit(true);
                            fruitCount++;
                            break;
                        
                        case '9':
                            map[yCoor][xCoor].setAntiPower(true);
                            break;
                        
                        case 'p':
                            waka.setPos(pos);
                            map[yCoor][xCoor].setEmpty(true);
                            break;

                        case 'a':
                            GhostPinky pinky = new GhostPinky(waka);
                            pinky.setVel(initialVel);
                            pinky.setPos(pos);
                            ghosts.add(pinky);
                            map[yCoor][xCoor].setEmpty(true);
                            break;
                        
                        case 'c':
                            GhostBlinky blinky = new GhostBlinky(waka);
                            blinky.setVel(initialVel);
                            blinky.setPos(pos);
                            ghosts.add(blinky);
                            map[yCoor][xCoor].setEmpty(true);
                            break;
                        
                        case 'i':
                            GhostClyde clyde = new GhostClyde(waka);
                            clyde.setVel(initialVel);
                            clyde.setPos(pos);
                            ghosts.add(clyde);
                            map[yCoor][xCoor].setEmpty(true);
                            break;
                        
                        case 'w':
                            GhostInky inky = new GhostInky(waka);
                            inky.setVel(initialVel);
                            inky.setPos(pos);
                            ghosts.add(inky);
                            map[yCoor][xCoor].setEmpty(true);
                            break;
                        
                        default:
                        System.out.printf("\n%FAILURE: UNKNOWN ELEMENT FOUND IN MAP: %s\n", row[xCoor]);
                        System.exit(1);
                    }
                }
                yCoor++;
            }
            waka.setFruitCount(fruitCount);
            setIntersections();
            reader.close();

        } catch (FileNotFoundException e) {}
    }

    /**
     * This method will identify any tiles in the matrix which are intersections and then set those tiles as intersections.
     */
    public void setIntersections() {
        for (int yCoor = 0; yCoor < map.length; yCoor++) {
            for (int xCoor = 0; xCoor < map[yCoor].length; xCoor++) {
                map[yCoor][xCoor].setIntersection(map);
            }
        }
    }

    /**
     * This method will return the matrix.
     * @return map
     */
	public Tile[][] getMap() {
		return map;
    }
    
    /**
     * This method will return the number of fruits found in the matrix.
     * @return fruitCount
     */
    public int getFruitCount() {
        return fruitCount;
    }

    /**
     * This method will return all the Ghosts found in the map configuration.
     * @return ghosts
     */
    public List<GameObject> getGhosts() {
        return ghosts;
    }
    
}
