/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the Tiles class, where objects found in the map file are created to interact with the other objects in the game.
*/

package ghost;

import java.util.LinkedList;
import java.util.List;

import processing.core.PVector;

public class Tile extends GameObject{
    private boolean wall = false;
    private int wallState;

    private boolean empty = false;

    private boolean fruit = false;
    private boolean superFruit = false;
    private int superFruitState = 0;

    private boolean antiPower = false;

    private float blinkIndex = 0;

    private boolean isIntersection = false;
    private LinkedList<Tile> possibleTurns = new LinkedList<>();
    private float hCost;

    /**
     * The constructor of the Tile class.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Tile(int x, int y) {
        pos = new PVector(x, y);
    }

    /**
     * This method determines whether the tile is a wall.
     * @return wall
     */
    public boolean isWall() {
        return wall;
    }

    /**
     * This method will set the Tile to be a wall, and if it is a wall, it wil also set this tile's type of wall.
     * @param wall boolean
     * @param wallState int
     */
    public void setWall(boolean wall, int wallState) {
        this.wall = wall; 
        this.wallState = wallState;
    }

    /**
     * This method will return the state of the wall.
     * @return wallState
     */
    public int getWallState() {
        return wallState;
    }

    /**
     * This method determines whether the tile is a fruit.
     * @return fruit
     */
    public boolean isFruit() {
        return fruit;
    }

    /**
     * This method will set the Tile to be a fruit.
     * @param fruit boolean
     */
    public void setFruit(boolean fruit) {
        this.fruit = fruit;
    }

    /**
     * This method conducts the fruit function.
     */
    public void fruitFunction() {
        fruit = false;
        empty = true;
    }

    /**
     * This method determines whether the tile is a super fruit.
     * @return superFruit
     */
    public boolean isSuperFruit() {
        return superFruit;
    }

    /**
     * This method will set the Tile to be a super fruit.
     * @param superFruit boolean
     */
    public void setSuperFruit(boolean superFruit) {
        this.superFruit = superFruit;
    }

    /**
     * This method will the state of the super fruit (to give it a flashing effect).
     * @return superFruitState
     */
    public int getSuperFruitState() {
        return superFruitState;
    }

    /**
     * This method will set the state of the superfruit by incrementing an index and by adding that index to an int.
     */
    public void setSuperFruitState() {
        blinkIndex += 0.2;
        this.superFruitState = (int) (this.superFruitState + blinkIndex) % 2;
    }

    /**
     * This method conducts the super fruit function, which causes ghosts to go into frightened mode.
     */
    public void superFruitFunction() {
        GameObject.frightened = true;
        GameObject.activatedAt = System.currentTimeMillis(); //set at what time we activated this mode
        superFruit = false;
    }

    /**
     * This method determines whether the tile is a anti-power up.
     * @return antiPower
     */
    public boolean isAntiPower() {
        return antiPower;
    }

    /**
     * This method will set the Tile to be an anti power up.
     * @param antiPower boolean
     */
    public void setAntiPower(boolean antiPower) {
        this.antiPower = antiPower;
    }

    /**
     * This method executes the anti-power up fucntion, which causes ghosts to go into invisible mode.
     */
    public void antiPowerFunction() {
        invisibleMode = true;
        GameObject.invisibleLength = System.currentTimeMillis();
        antiPower = false;
        empty = true;
    }

    /**
     * This method determines whether the tile is an empty tile.
     * @return empty
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * This method will set the Tile to be an empty tile.
     * @param empty boolean
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     * This method determines whether the tile is an intersection.
     * @return isIntersection
     */
    public boolean isIntersection() {
        return isIntersection;
    }

    /**
     * This method will calculate the euclidean distance of the particular Tile to a target position.
     * @param targetPos the target position.
     */
    public void setHCost(PVector targetPos) {
        hCost = (float) Math.sqrt(Math.pow(targetPos.x - pos.x, 2) + Math.pow(targetPos.y - pos.y, 2));
    }

    /**
     * This method will return the calculated heuristic (Euclidean distance).
     * @return hCost
     */
    public float getHCost() {
        return hCost;
    }

    /**
     * This method will return all the possible turns that can be made based off the neighbouring tiles which are not walls.
     * @return possibleTurns
     */
    public List<Tile> getPossibleTurns() {
        return possibleTurns;
    }

    /**
     * This method will take in a matrix and find all the intersections located in the matrix. Additionally, it will go through all possible Tiles and determine whether the 
     * neighbouring tiles of each Tile is a possible and valid move.
     * @param map a matrix
     */
    public void setIntersection(Tile[][] map) {
        //ignore if it the current tile is a wall or out of bounds
        if (map[(int) pos.y][(int) pos.x].isWall() || !(pos.y > 2 && pos.y < map.length - 2) || !(pos.x > 0 && pos.x < map[0].length - 1)) {return;}

        //check if intersection
        int canTurn = 0;

        //check horizontal
        if (!map[(int) pos.y + 1][(int) pos.x].isWall()) {
            possibleTurns.add(map[(int) pos.y + 1][(int) pos.x]);
            canTurn++; 
        
        } if (!map[(int) pos.y - 1][(int) pos.x].isWall()){
            possibleTurns.add(map[(int) pos.y - 1][(int) pos.x]); 
            canTurn++; 
        
        //check vertical
        } if (!map[(int) pos.y][(int) pos.x + 1].isWall()) {
            possibleTurns.add(map[(int) pos.y][(int) pos.x + 1]);
            canTurn++; 
        
        } if (!map[(int) pos.y][(int) pos.x - 1].isWall()) {
            possibleTurns.add(map[(int) pos.y][(int) pos.x - 1]);
            canTurn++; 
        }

        if (canTurn >= 3) {
            this.isIntersection = true;
        }
    }
}
