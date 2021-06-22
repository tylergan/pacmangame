/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the GhostControllerTest class, where methods related to the ghost controller are tested.
*/

package ghost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PVector;

public class GhostControllerTest {
    App app;
    Pacman waka;
    GhostController ghostC;

    List<GameObject> ghosts;
    List<GhostBlinky> blinkies;
    List<GhostPinky> pinkies;
    List<GhostInky> inkies;
    List<GhostClyde> clydes;

    long testTime;

    @BeforeEach
    //A method that sets the following objects before each test
    public void setObjects() {
        app = new App();
        waka = new Pacman();

        MapParse parseMap = new MapParse(waka);
        waka.map = parseMap.getMap();

        ghostC = new GhostController();

        ghosts = parseMap.getGhosts();
        blinkies = ghosts.stream().filter(o -> o instanceof GhostBlinky).map(o -> (GhostBlinky) o).collect(Collectors.toList());
        pinkies = ghosts.stream().filter(o -> o instanceof GhostPinky).map(o -> (GhostPinky) o).collect(Collectors.toList());
        inkies = ghosts.stream().filter(o -> o instanceof GhostInky).map(o -> (GhostInky) o).collect(Collectors.toList());
        clydes = ghosts.stream().filter(o -> o instanceof GhostClyde).map(o -> (GhostClyde) o).collect(Collectors.toList());

        ghostC.setGhosts(ghosts);
    }

    @Test
    //Test basic constructor
    public void testConstructor() {
        assertNotNull(ghostC);
    }

    @Test
    //Testing the setting of the ghosts
    public void testSetGhosts() {
        assertNotNull(ghosts);
    }

    @Test
    //Test setting an Inky a Blinky partner 
    public void testInkyLessBlinky() {
        List<GameObject> tempGhosts = new LinkedList<>();

        GhostBlinky blinky = new GhostBlinky(waka);
        GhostInky inky1 = new GhostInky(waka), inky2 = new GhostInky(waka);

        tempGhosts.add(blinky);
        tempGhosts.add(inky1); tempGhosts.add(inky2);

        ghostC.setGhosts(tempGhosts);

        tempGhosts.clear();

        tempGhosts.add(pinkies.get(0));
        tempGhosts.add(clydes.get(0));

        ghostC.setGhosts(tempGhosts);

        assertNotNull(ghostC);
    }

    @Test
    //Test moving all ghosts
    public void testMove() {
        GhostBlinky blinky = blinkies.get(0);
        GhostPinky pinky = pinkies.get(0);
        GhostInky inky = inkies.get(0);
        GhostClyde clyde = clydes.get(0);

        //store starting positions
        PVector tempBlinky = new PVector(blinky.getPos().x, blinky.getPos().y);
        PVector tempPinky = new PVector(pinky.getPos().x, pinky.getPos().y);
        PVector tempInky = new PVector(inky.getPos().x, inky.getPos().y);
        PVector tempClyde = new PVector(clyde.getPos().x, clyde.getPos().y);

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 1 * 1000) {
            ghostC.move();
        }

        assertTrue(tempBlinky.x != blinky.getPos().x && tempBlinky.y != blinky.getPos().y);
        assertTrue(tempPinky.x != pinky.getPos().x && tempPinky.y != pinky.getPos().y);
        assertTrue(tempInky.x != inky.getPos().x && tempInky.y != inky.getPos().y);
        assertTrue(tempClyde.x != clyde.getPos().x && tempClyde.y != clyde.getPos().y);
    }

    @Test
    //Test the changing indexes
    public void changeModeIndex() {
        GhostBlinky blinky = blinkies.get(0);

        for (int i = 0; i < 8; i++) {
            ghostC.changeModeIndex();
        }

        assertEquals(0, blinky.modeIndex);
    }
}
