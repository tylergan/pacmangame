/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the GhostController class, where it calls upon all of the methods used by the ghosts at a single time.
*/

package ghost;

import java.util.List;
import java.util.stream.Collectors;

public class GhostController extends GameObject {
    List<GameObject> ghosts;

    /**
     * Constructor of the GhostController class.
     */
    public GhostController() {
    }

    /**
     * This method is a setter for the ghosts variable in the GhostController class. It also sets Blinky-Inky partners.
     * @param ghosts a List of ghosts
     */
    public void setGhosts(List<GameObject> ghosts) {
        this.ghosts = ghosts;
        checkInkyBlinky();
    }

    /**
     * This method will obtain a bunch of Inkies and Blinkies from the List of ghosts and will pair the two together.
     */
    public void checkInkyBlinky() {
        List<GhostInky> inkies = ghosts.stream().filter(o -> (o instanceof GhostInky)).map(o -> (GhostInky) o).collect(Collectors.toList());
        List <GhostBlinky> blinkies = ghosts.stream().filter(o -> (o instanceof GhostBlinky)).map(o -> (GhostBlinky) o).collect(Collectors.toList());

        if (inkies.size() == 0 || blinkies.size() == 0) {return;}

        for (int i = 0, j = 0; i < inkies.size(); i++, j++) {
            if (j == blinkies.size()) {
                j = 0;
            }

            inkies.get(i).setBlinkyPartner(blinkies.get(j));
        }
    }

    /**
     * This method will move all Ghosts, switching it between scatter and chase mode.
     */
    public void move() {
        if (modeTimer == Long.MAX_VALUE) {
            modeTimer = System.currentTimeMillis();

            if (modeIndex % 2 == 0) {scatterMode = true;} 
            else {scatterMode = false;}

        } else if (System.currentTimeMillis() - modeTimer >= modeLengths.get(modeIndex) * 1000) {
            changeModeIndex();
            modeTimer = Long.MAX_VALUE;
        }

        for (GameObject ghost : ghosts) {
            ghost.move();
        }
    }

    /**
     * This method swtiches between different modes (scatter and chase). The duration of each mode is given within a List, which this method is used to index through that List.
     */
    public void changeModeIndex() {
        modeIndex++;

        if (modeIndex >= modeLengths.size()) {
            modeIndex = 0;
        }
    }

    /**
     * This method will draw out all ghosts in the List.
     */
    public void draw() {
        for (GameObject ghost : ghosts) {
            ghost.drawGhost();
        }
    }

    /**
     * This method will draw out the target of all Ghosts.
     */
    public void drawTarget() {
        for (GameObject ghost : ghosts) {

            if (!ghost.isDead() && !GameObject.frightened) {
                ghost.drawTarget();
            }
        }
    }
}
