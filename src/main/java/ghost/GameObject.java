package ghost;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;

/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the GameObject class, an abstract class which other class inherits from and uses to set and get information and methods.
*/

public class GameObject {
    //STATIC VARIABLES
    protected static boolean gameOver = false, win = false, loss = false;
    protected static PImage gameOverImg;

    protected static long activatedAt = Long.MAX_VALUE;
    protected static boolean timerSet = false;

    protected static PFont font;

    protected static int speed;
    protected static Tile[][] map;
    protected static PApplet app;

    protected static boolean invisibleMode = false;
    protected static Long invisibleLength = Long.MAX_VALUE;

    protected static PImage[] frightenedImg;
    protected static boolean frightened = false;
    protected static int frightenedLength;

    protected static boolean scatterMode = false;
    protected static long modeTimer = Long.MAX_VALUE;
    protected static List<Integer> modeLengths;
    protected static int modeIndex = 0;


    //INSTANCE VARIABLES
    protected PImage img;
    protected int width;
    protected int height;

    protected PVector pos;
    protected PVector vel;

    protected boolean dead = false;
    protected float frightenedImgIndex = 0;


    //BASIC SETTERS AND GETTERS USED TO OBTAIN AND SET VALUES FOR EACH OBJECT THAT IS A SUB-CLASS OF GAMEOBJECT ===========================

    /**
     * This method will return the image of an object.
     * @return img
     */
    public PImage getImg() {
        return img;
    }

    /**
     * This method will set the image of an object.
     * @param img the image of an object.
     */
    public void setImg(PImage img) {
        this.img = img;
    }

    /**
     * This method will return the position of an object.
     * @return pos.
     */
    public PVector getPos() {
        return pos;
    }

    /**
     * This method will set the position of an object.
     * @param pos the position of an object.
     */
    public void setPos(PVector pos) {
        this.pos = pos;
    }
    
    /**
     * This method will return the velocity of an object.
     * @return vel
     */
    public PVector getVel() {
        return vel;
    }

    /**
     * This method will set the velocity of an object.
     * @param vel the velocity of an object.
     */
    public void setVel(PVector vel) {
        this.vel = vel;
    }

    /**
     * This method will return the speed of an object.
     * @return speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * This method will return whether an object is dead or not.
     * @return dead
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * This method will kill the ghosts for the rest of the round if the ghosts collide with Pacman while in frightended mode; this method is used in the Pacman class, 
     * checkDirection() method.
    */ 
    public void kill() {
        dead = true;
    }

    /**
     * This method is used by most subclasses which inherits from the GameObject class; this method draws objects onto the canvas.
    */    
    public void draw() {
        app.image(this.getImg(), this.getPos().x * 16 - 5, this.getPos().y * 16 - 5);
    }

    //METHODS USED BY THE GAME ===========================

        /** 
         * This method checks whether the game is finished. The game can only be finished if the Pacman is able to obtain all fruits or is killed by colliding with any of the ghosts.
         * This method works in conjunction with App.reset().
         * @param app the App.
         * @param waka a Pacman object.
         * @return boolean
        */
    public static boolean finished(App app, Pacman waka) {
        if (gameOver) {
            return true;

        } else if (waka.isDead()) { //if Waka is dead, but still has left over lives, just reset the game back to the original state.
            app.reset();
        }
        return false;
    }

    /**
     * This method will draw either a win or loss message. Using System.currentTimeMillis(), we ensure that the message is shown for 10 seconds before resetting to the 
     * original state of the game.
     * @param app the App.
    */
    public static void drawEnding(App app) {
        if (loss) {
            app.background(0, 0, 0);
            app.image(gameOverImg, App.WIDTH/2 - 57, App.HEIGHT/2 - 30);

        } else {
            app.background(0, 0, 0);
            app.textFont(font);
            app.textAlign(App.CENTER);
            app.text("YOU WIN", App.WIDTH/2- 10, App.HEIGHT/2);
        }

        if (!timerSet) {
            activatedAt = System.currentTimeMillis();
            timerSet = true;
        }

        if (System.currentTimeMillis() - activatedAt >= 10*1000) { //checking if we have waited for 10 seconds.
                gameOver = win = loss = false;
                timerSet = false;
                activatedAt = Long.MAX_VALUE;
        }

        app.reset();
    }

    /**
     * This method is used in the main App class to reset GameObject states (specifically concerned with the Ghosts) back to its original state.
    */
    public static void reset() {
        frightened = false;
        invisibleMode = false;
        modeIndex = 0;
        modeTimer = Long.MAX_VALUE;
    }
    
    //METHODS UNIQUE TO THE GHOSTS ONLY ===========================

    /**
     * This method is the controller of the movement for the ghost. It will help the ghost make decisions when approaching a wall as well as make decisions when targeting Pacman. 
     * It works with multiple other methods: setTargetNode() and chooseShortestDecision().
     * Additional getters and setters from other classes were used to obtain needed information, such as a Tile's neighbours, for example.
    */
    public void checkDirection() {
        setTargetNode();

        if (pos.x % 1 != 0 || pos.y % 1 != 0) { //only checking for whole indexes
            return;
        }

        //check if we are going into a wall or are at an intersection
        PVector futurePos = new PVector(pos.x + vel.x, pos.y + vel.y);
        if ((map[(int) futurePos.y][(int) futurePos.x + 1].isWall() && vel.x == 1/16f * speed) || //if we are moving right, check future (x+1,y) = wall
            (map[(int) futurePos.y + 1][(int) futurePos.x].isWall() && vel.y == 1/16f * speed) || //if we are moving down, check future (x,y+1) = wall
            (map[(int) futurePos.y][(int) futurePos.x].isWall())) 
            {   
                Tile current = map[(int) pos.y][(int) pos.x];

                if (current.isIntersection()) { //check if we are at an intersection, first
                    chooseShortestDecision();
                    return;
                }
                
                //otherwise, find possible nodes to avoid running into wall without going backwards
                List<Tile> neighbours = current.getPossibleTurns();
                
                for (Tile node: neighbours) {
                    if ((node.getPos().x - pos.x) + vel.x*16/speed == 0 && (node.getPos().y - pos.y) + vel.y*16/speed == 0) {
                        continue; //ignore any nodes that will cause us to go backwards
                    }

                    PVector turnTo = new PVector(node.getPos().x - pos.x, node.getPos().y - pos.y); //first available node found
                    vel = new PVector(turnTo.x/16f * speed, turnTo.y/16f * speed); //set new vel
                    break;
                }
                return;
            }

        chooseShortestDecision();
    }

    /**
     * This method is used by the ghosts ONLY. They run away from from Pacman, choosing random directions to go in (without going backwards (except for the initial turn backwards)) 
     * everytime they are located at an intersersection. This method works with chooseRandomDirection().
    */
    public void runAway() {
        if (System.currentTimeMillis() - activatedAt >= frightenedLength*1000) { //Checking for the duration of how long they have been in frightened mode
            frightened = false;
            return;
        }

        if (pos.x % 1 != 0 || pos.y % 1 != 0) { //only checking for whole indexes
            return;
        }

        //check if we are going into a wall or are at an intersection
        PVector futurePos = new PVector(pos.x + vel.x, pos.y + vel.y);
        if ((map[(int) futurePos.y][(int) futurePos.x + 1].isWall() && vel.x == 1/16f * speed) || //if we are moving right, check future (x+1,y) = wall
            (map[(int) futurePos.y + 1][(int) futurePos.x].isWall() && vel.y == 1/16f * speed) || //if we are moving down, check future (x,y+1) = wall
            (map[(int) futurePos.y][(int) futurePos.x].isWall())) 
            {   
                Tile current = map[(int) pos.y][(int) pos.x];

                if (current.isIntersection()) { //check if we are at an intersection, first
                    chooseRandomDirection();
                    return;
                }
                
                //otherwise, find possible nodes to avoid running into wall without going backwards
                List<Tile> neighbours = current.getPossibleTurns();
                
                for (Tile node: neighbours) {
                    if ((node.getPos().x - pos.x) + vel.x*16/speed == 0 && (node.getPos().y - pos.y) + vel.y*16/speed == 0) {
                        continue; //ignore any nodes that will cause us to go backwards
                    }
                    PVector turnTo = new PVector(node.getPos().x - pos.x, node.getPos().y - pos.y);
                    vel = new PVector(turnTo.x/16f * speed, turnTo.y/16f * speed);
                    break;
                }

                return;
            }
        
        if (map[(int) pos.y][(int) pos.x].isIntersection()) { //check if we are at an intersection, first
                chooseRandomDirection();
                return;
        }
    }

    /**
     * This method is only used at intersections and will cause the ghost to pick a direction at random WITHOUT going backwards.
    */
    public void chooseRandomDirection() {
        Tile intersection = map[(int) pos.y][(int) pos.x];
        LinkedList<Tile> sortedList = new LinkedList<>();

        PVector turnTo = new PVector(vel.x, vel.y);

        for (Tile node : intersection.getPossibleTurns()) {
            if ((node.getPos().x - pos.x) + vel.x*16/speed == 0 && (node.getPos().y - pos.y) + vel.y*16/speed == 0) {
                continue; //ignore any backwards movement
            }
            sortedList.add(node);
        }

        Random rand = new Random();
        int randIndex = rand.nextInt(sortedList.size());
        Tile randTile = sortedList.get(randIndex);

        turnTo = new PVector(randTile.getPos().x - pos.x, randTile.getPos().y - pos.y); //use the first node which has low hCost and complies with backwards rule
        vel = new PVector(turnTo.x/16f * speed, turnTo.y/16f * speed);
    }

    /**
     * This method works similarly to how Pacman's open-close mouth animation works. We will only increase the index by a small amount (in floats) and add it to an int, which
     * will floor the number to give us an index.
    */ 
    public void setGhostFrightIndex() {
        frightenedImgIndex += 0.12;
    }

    /**
     * This method is used by the ghosts ONLY. Found in the GhostController class, draw() method, it will draw the Ghosts depending on one of its many states.
    */ 
    public void drawGhost() {
        if (dead) {return;} //don't draw ghosts if dead.

        if (!frightened && !invisibleMode) { //ghosts have no specific state, just draw normally.
            app.image(this.getImg(), this.getPos().x * 16 - 5, this.getPos().y * 16 - 5);
            
        }  else if (frightened) {
            int imgIndex = 0;
            if (System.currentTimeMillis() - activatedAt >= (frightenedLength - 2) * 1000) { //start flashing colours to show frightened mode is ending 2 seconds before hand.
                setGhostFrightIndex();
                imgIndex = (int) frightenedImgIndex % 2;
            }
            app.image(frightenedImg[imgIndex], this.getPos().x * 16 - 5, this.getPos().y * 16 - 5);

        } else {
            if (System.currentTimeMillis() - invisibleLength >= 8 * 1000) {
                invisibleMode = false;

            } else if ((System.currentTimeMillis() - invisibleLength) % (2 * 1000) < 20) { //ghosts are in invisible mode, only show flashes of them every 2 seconds-ish.
                app.image(frightenedImg[1], this.getPos().x * 16 - 5, this.getPos().y * 16 - 5);
            }
        }
    }

    //METHODS THAT WILL BE OVERRIDED BY EACH GHOST CLASS
    /**
     * Method that will be overrided by the Ghost classes.
     */
    public void chooseShortestDecision() {}

    /**
     * Method that will be overrided by the Ghost classes.
     */
    public void setTargetNode() {}

    /**
     * Method that will be overrided by the Ghost classes.
     */
    public void drawTarget() {}

    /**
     * Method that will be overrided by the Ghost classes.
     */
    public void move() {}

}
