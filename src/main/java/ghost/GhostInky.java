/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the GhostInky class, where methods related to whim are stored.
*/

package ghost;

import java.util.Comparator;
import java.util.LinkedList;

import processing.core.PVector;

public class GhostInky extends GameObject{
    private Pacman waka;
    private GhostBlinky blinky;
    private PVector targetNode;
    private boolean completeBackwards = false;

    /**
     * Constructor of the Inky class.
     * @param waka, a Pacman object.
    */
    public GhostInky(Pacman waka) {
        this.waka = waka;
    }

    /**
     * Giving Inky a Blinky object so that it can use Blinky's relative position to set his own target node.
     * @param blinky a Blinky object.
     */
    public void setBlinkyPartner(GhostBlinky blinky) {
        this.blinky = blinky;
    }

    /**
     * This method will update Inky's current position using a "velocity", determined through a series of checks made by a bunch of other methods located in this method. 
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
     * This method is an overrided method that set Inky's target node depending on the mode.
     */
    @Override
    public void setTargetNode() {
        if (!scatterMode) {
            setInkyTargetNode();
        } else {
            scatter();
        }
    }

    /**
     * This method will set Inky's target destination. Inky uses a unique targeting system. 
     * Firstly, an initial tile (which you can also be considered as a "midpoint tile") two units in front 
     * of Pacman. Then, a vector from the midway point to Blinky's position is found, and then
     * inverted from the initial tile to give you a new starting position.
     * However, you can instead use the midway point, Blinky's position and the midway point equation to calculate the 
     * new target location: [Xm = (x1 + x2)/2, Ym = (y1 + y2)/2] to [x1 = 2*Xm - x2, y1 = 2*Ym - y2].
    */
    public void setInkyTargetNode() {
        /*

        */
        PVector wakaDirec = new PVector(waka.getVel().x*16*2, waka.getVel().y*16*2);
        PVector midPointTile = new PVector(waka.getPos().x + wakaDirec.x, waka.getPos().y + wakaDirec.y); 
        
        PVector newTarget;

        if (blinky != null) {
            newTarget = new PVector(2 * midPointTile.x - blinky.getPos().x, 2 * midPointTile.y - blinky.getPos().y);
        } else {
            newTarget = midPointTile;
        }

        //ensure Inky is not targeting a node outside of the map (important for debug mode showing of line))
        if (newTarget.x < 1) {
            newTarget = new PVector(1, newTarget.y);
        } else if (newTarget.x > 26) {
            newTarget = new PVector(26, newTarget.y);
        }

        if (newTarget.y < 3) {
            newTarget = new PVector(newTarget.x, 3);
        } else if (newTarget.y > 32) {
            newTarget = new PVector(newTarget.x, 32);
        }

        targetNode = newTarget;
    }

    /**
     * This method will influence the ghosts to move into scatter mode. This method will simply set the Ghost's new target node to their respective scatter corner. 
     * For Inky specifically, he will target the bottom right corner.
    */
    public void scatter() {
        targetNode = map[map.length - 1][map[map.length - 1].length - 1].getPos();
    }
    
    /**
     * This method helps Inky makes decision relative to its target node. It uses Euclidean distance as a heuristic and sorts all possible decisions (neighbouring Tiles - 
     * possible moves) using the heuristic to obtain the shortest (yet valid (as Ghosts cannot move backwards)) decision; this method works with scatter().
    */
    @Override
    public void chooseShortestDecision() {
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
        app.stroke(0,255,255);
        
        app.line(pos.x*16 + 8, pos.y*16 + 8, targetNode.x*16 + 8, targetNode.y*16 + 8);
    }
}
