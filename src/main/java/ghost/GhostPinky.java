/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the GhostPinky class, where methods related to ambusher are stored.
*/


package ghost;

import java.util.Comparator;
import java.util.LinkedList;

import processing.core.PVector;

public class GhostPinky extends GameObject{
    private Pacman waka;
    private PVector targetNode;
    private boolean completeBackwards = false;
    
    /**
     * Constructor of the Pinky class.
     * @param waka a Pacman object.
    */
    public GhostPinky(Pacman waka) {
        this.waka = waka;
    }

    /**
     * This method is an overrided method that set Pinky's target node depending on the mode.
     */
    @Override
    public void setTargetNode() {
        if (!scatterMode) {
            setPinkyTargetNode();
        } else {
            scatter();
        }
    }

    /**
     * This method will set Pinky's target destination. Pinky looks two tiles ahead of Pacman.
    */
    public void setPinkyTargetNode() {
        PVector wakaDirec = new PVector(waka.getVel().x*16*4, waka.getVel().y*16*4);
        targetNode = new PVector(waka.getPos().x + wakaDirec.x, waka.getPos().y + wakaDirec.y); //set initial targetNode so that the show() method is not referring to an empty targetNode (null)

        //ensure that the ghost does not target an out of bounds location (important for debug mode lines)
        if (targetNode.x < 1) {
            targetNode = new PVector(1, targetNode.y);
        } else if (targetNode.x > 26) {
                targetNode = new PVector(26, targetNode.y);
        }

        if (targetNode.y < 3) {
            targetNode = new PVector(targetNode.x, 3);
        } else if (targetNode.y > 32) {
            targetNode = new PVector(targetNode.x, 32);
        }
    }

    /**
     * This method will influence the ghosts to move into scatter mode. This method will simply set the Ghost's new target node to their respective scatter corner. 
     * For Pinky specifically, he will target the top right corner.
    */
    public void scatter() {
        targetNode = map[0][map[0].length - 1].getPos();
    }



    /**
     * This method will update Pinky's current position using a "velocity", determined through a series of checks made by a bunch of other methods located in this method. 
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
     * This method is the controller of the movement for Pinky. It will help Pinky make decisions when approaching a wall as well as make decisions when targeting Pacman. 
     * It works with multiple other methods: setPinkyTargetMode(), scatter() and chooseShortestDecision().
     * Additional getters and setters from other classes were used to obtain needed information, such as a Tile's neighbours, for example.
    */
    @Override
    public void chooseShortestDecision() {
        /*
            This method helps Inky makes decision relative to its target node. It uses Euclidean distance as a heuristic 
            and sorts all possible decisions (neighbouring Tiles - possible moves) using the heuristic to obtain the 
            shortest (yet valid (as Ghosts cannot move backwards)) decision.
        */
        Tile intersection = map[(int) pos.y][(int) pos.x];
        LinkedList<Tile> sortedList = new LinkedList<>();

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
            turnTo = new PVector(node.getPos().x - pos.x, node.getPos().y - pos.y); //use the first node which has low hCost and complies with backwards rule
            break;
        }

        vel = new PVector(turnTo.x/16f * speed, turnTo.y/16f * speed);
    }

    /**
     * A method to show Inky's target destination; this is only used for debug mode.
    */
    @Override
    public void drawTarget() {
        app.strokeWeight(2);
        app.stroke(255,192,203);

        app.line(pos.x*16 + 8, pos.y*16 + 8, targetNode.x*16 + 8, targetNode.y*16 + 8);

    }
}
