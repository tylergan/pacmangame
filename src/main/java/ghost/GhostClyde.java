/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the GhostClyde class, where methods related to ignorant are stored.
*/

package ghost;

import java.util.Comparator;
import java.util.LinkedList;

import processing.core.PVector;

public class GhostClyde extends GameObject{
    private Pacman waka;
    private PVector targetNode;
    private boolean ignorantMode = false;
    private boolean completeBackwards = false;
    
    /**
     * Constructor of the Clyde class.
     * @param waka, a Pacman object.
    */
    public GhostClyde(Pacman waka) {
        this.waka = waka;
    }

    /**
     * This method is an overrided method that set Pinky's target node depending on the mode.
     */
    @Override
    public void setTargetNode() {
        if (!ignorantMode && !scatterMode) { //clyde will engage in ignorant mode if the ghost is 8 or less units within Pacman
            setClydeTargetNode();
        } else {
            scatter();
        }
    }

    /**
     * This method will set Clyde's target destination. In this case, Blinky will always target Pacman's position.
    */
    public void setClydeTargetNode() {
        targetNode = new PVector(waka.getPos().x, waka.getPos().y); //need to set an initial target so that the show() method does not refer to an empty target value
    }

    /**
     * This method will influence the ghosts to move into scatter mode. This method will simply set the Ghost's new target node to their respective scatter corner. 
     * For Clyde specifically, he will target the bottom left corner.
    */
    public void scatter() {
        targetNode = map[map.length - 1][0].getPos();
    }

    /**
     * This method will update Clyde's current position using a "velocity", determined through a series of checks made by a bunch of other methods located in this method. 
     * he most important method used for this function to work is checkDirection().
    */
    @Override
    public void move() {
        if (!frightened) {
            checkDirection();
        } else {
            if (!completeBackwards) {
                vel.mult(-1);
                completeBackwards = true;
            }
            runAway();
        }
        
        pos.add(vel);
    }

    /**
     * This method helps Clyde makes decision relative to its target node. It uses Euclidean distance as a heuristic and sorts all possible decisions (neighbouring Tiles - 
     * possible moves) using the heuristic to obtain the shortest (yet valid (as Ghosts cannot move backwards)) decision; this method works with scatter().
     * 
     * Note that Clyde, when found to have a heuristic score of less than 8 units close to Pacman, will cause Clyde to transition into scatter() mode (a personality trait of 
     * the ghost).
    */
    @Override
    public void chooseShortestDecision() {
        Tile intersection = map[(int) pos.y][(int) pos.x];
        LinkedList<Tile> sortedList = new LinkedList<>();

        //set the hCost in relation to targetNode
        for (Tile node: intersection.getPossibleTurns()) {
            node.setHCost(targetNode);
            sortedList.add(node);
        }

        sortedList.sort(Comparator.comparing(Tile::getHCost)); //sort the list by hCost
        PVector turnTo = new PVector(vel.x, vel.y);

        for (Tile node : sortedList) {
            if ((node.getPos().x - pos.x) + vel.x*16/speed == 0 && (node.getPos().y - pos.y) + vel.y*16/speed == 0) {
                continue;
            }

            if (node.getHCost() < 8) { //for clyde, check is we are too close to Pacman (8 units)
                ignorantMode = true;
            } else {
                ignorantMode = false;
            }

            turnTo = new PVector(node.getPos().x - pos.x, node.getPos().y - pos.y); //use the first node which has low hCost and complies with backwards rule
            break;
        }

        vel = new PVector(turnTo.x/16f * speed, turnTo.y/16f * speed);
    }

    /**
     * A method to show Clyde's target destination; this is only used for debug mode.
    */
    @Override
    public void drawTarget() {
        app.strokeWeight(2);
        app.stroke(255,69,0);

        app.line(pos.x*16 + 8, pos.y*16 + 8, targetNode.x*16 + 8, targetNode.y*16 + 8);
    }
}
