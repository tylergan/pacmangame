/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the Pacman class, where methods related to Waka are stored.
*/

package ghost;

import java.util.List;

import processing.core.PImage;
import processing.core.PVector;

public class Pacman extends GameObject{
    private int lives;
    private int fruitCount;

    private String currMove = "left";
    private boolean turning = false;

    private PImage[] images;
    private int[] state = new int[2];
    private double indexState;

    private PVector turnDirec;

    private List<GameObject> ghosts;

    /**
     * This method is the constructor of the Pacman class. Note that his first state (state[0]) will always be the closed-mouth state.
     */
    public Pacman() {
        this.state[0] = 0;
        this.indexState = 0;
    }

    /**
     * This method sets the ghosts to the ghosts variable of this class.
     * @param ghosts a List of ghosts.
     */
    public void setGhosts(List<GameObject> ghosts) {
        this.ghosts = ghosts;
    }

    /**
     * This method returns a PImage relative to Pacman's specific state.
     */
    @Override
    public PImage getImg() {
        return images[getState()];
    }
    
    /**
     * This method sets an array of images to Pacman's images variable.
     * @param images an array of Pacman states.
     */
    public void setImg(PImage[] images) {
        this.images = images;
    }

    /**
     * This method will increment the index by a small amount, allowing Pacman to experience an "animation-like" transition between states by delaying each transition.
     */
    public void animate() {
        this.indexState += 0.18;
    }

    /**
     * This method will return Pacman's current state (open-close mouth) as it constatly fluctuates between 0 and 1 (through the use of the modulo).
     * @return the state of Pacman.
     */
    public int getState() {
        int newIndex = (int) this.indexState % 2;
        return state[newIndex];
    }

    /**
     * This sets Pacman's second state depending on the direction he is facing.
     * @param x the state depending on which direction he is facing.
     */
    public void setState(int x) {
        state[1] = x;
    }

    /**
     * This method returns Pacman's number of lives.
     * @return lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * This method will set the number of lives Pacman has and is used in the MapParse() class.
     * @param lives the number of lives.
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * This method will set the number of fruits found in the map configuraiton.
     * @param fruitCount the number of fruits.
     */
    public void setFruitCount(int fruitCount) {
        this.fruitCount = fruitCount;
    }

    /**
     * This method will move Pacman in a particular direction; it works with the checkDirection() method to determine the validity of the move and whether it can move in that 
     * direction or not.
     */
    public void move() {
        int state = 0;
        if (currMove.equals("left")) {
            this.turnDirec = new PVector(-1, 0);
            state = 4;

        } else if (currMove.equals("up")) {
            this.turnDirec = new PVector(0, -1);
            state = 1;
    
        } else if (currMove.equals("right")) {
            this.turnDirec = new PVector(1, 0);
            state = 3;
    
        } else if (currMove.equals("down")) {
            this.turnDirec = new PVector(0, 1);
            state = 2;
        }

        if (validMove()) {
            this.pos.add(this.vel);
            
            if (turning) { //only turn if we are actually turning (not while going straight)
                this.setState(state);
                this.turning = false;
            }
        }
    }

    /**
     * This method does multiple checks. It checks for whethter we are interacting with a fruit, super fruit and ant-power up. Additionally, it checks whether we are able to move 
     * in the direction of our desired input. If not, the move is stored and conducted until the next possible opening is found, unless another input is given or we cannot move in
     * the specified direction because the straight-line direction and the turning direction is blocked.
     * @return true or false.
     */
    public boolean validMove() {
        PVector futureTurningPos = new PVector(pos.x + turnDirec.x, pos.y + turnDirec.y);
        PVector futureForwardPos = new PVector(pos.x + vel.x, pos.y + vel.y);

        //CELL CHECKS ==============================================
        if (pos.x % 1 == 0 && pos.y % 1 == 0) { //check for fruits in our current position.
            Tile tile = map[(int) pos.y][(int) pos.x];
            if (tile.isFruit() || tile.isSuperFruit()) {
                this.fruitCount--;
                tile.fruitFunction();

                if (fruitCount == 0) {
                    gameOver = win = true;
                }

                if (tile.isSuperFruit()) {
                    tile.superFruitFunction();
                }
            }

            if (tile.isAntiPower()) {
                tile.antiPowerFunction();
            }
        }

        //MOVEMENT CHECKS ==============================================
        if (vel.x * 16/speed + turnDirec.x == 0 && vel.y * 16/speed + turnDirec.y == 0) { //check if we are turning a full 180 degrees (gets rid of delays in case of 180 degree turns)
            vel = new PVector(turnDirec.x/16f * speed, turnDirec.y/16f * speed);
            this.turning = true;
            return true;
        }

        if (futureTurningPos.x % 1 != 0 || futureTurningPos.y % 1 != 0) { //only check for whole number indexes
            return true;
        }

        if (map[(int) futureTurningPos.y][(int) futureTurningPos.x].isWall()) { //check if the position we are turning to is a wall
            if ((map[(int) futureForwardPos.y][(int) futureForwardPos.x + 1].isWall() && vel.x == 1/16f * speed) || //if we are moving right, check future (x+1,y) = wall
                (map[(int) futureForwardPos.y + 1][(int) futureForwardPos.x].isWall() && vel.y == 1/16f * speed) || //if we are moving down, check future (x,y+1) = wall
                (map[(int) futureForwardPos.y][(int) futureForwardPos.x].isWall())) //otherwise, just check whether future (x,y) = wall
            {
                return false; //cannot move as we are trapped by a wall
            }
            return true; //we can still move forward, don't change current velocity.
        }
        this.vel = new PVector(turnDirec.x/16f * speed, turnDirec.y/16f * speed); //we will turn if we are able to, otherwise we just keep moving foward.
        this.turning = true;
        return true;
    }
    
    /**
     * This method will store the move given as input by the user.
     * @param newMove the new move defined by the input of the user.
     */
    public void setCurrMove(String newMove) {
        this.currMove = newMove;
    }
    
    /**
     * This method will check whether Pacman is dead or not by determining whether Pacman makes contact with the ghosts or not. This method also checks if the Ghosts are in 
     * frightened mode, which if they are, then Pacman will kill the ghosts instead.
     * @return true or false.
     */
    public boolean isDead() {
        for (GameObject ghost : ghosts) {
            float dist = (float) Math.sqrt(Math.pow(pos.x - ghost.getPos().x, 2) + Math.pow(pos.y - ghost.getPos().y, 2));

            if (dist < 1 && !ghost.isDead()) {

                if (GameObject.frightened) {
                    ghost.kill();
                } else {
                    if (lives == 1) {
                        gameOver = loss = true;
                        lives = 3;
                    } else {lives --;}
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This method will draw Pacman on the canvas in regards to his respective location. This method will also draw out his number of lives.
     */
    @Override
    public void draw() {
        app.image(this.getImg(), this.getPos().x * 16 - 5, this.getPos().y * 16 - 5);

        for (int i = 0; i < lives; i++) {
            app.image(images[3], i * 28 + 8, 544);
        }
    }
}
