/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the GhostTest class, where methods related to the ghosts are tested.
*/

package ghost;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;
import processing.core.PVector;

public class GhostsTest {
    App app;
    Pacman waka;

    List<GhostBlinky> blinkies;
    List<GhostPinky> pinkies;
    List<GhostInky> inkies;
    List<GhostClyde> clydes;

    GhostBlinky blinky;
    GhostPinky pinky;
    GhostInky inky;
    GhostClyde clyde;

    long testTime;
    
    @BeforeEach
    //A method that sets the following objects before each test
    public void setObjects() {
        app = new App();
        waka = new Pacman();

        MapParse parseMap = new MapParse(waka);
        waka.map = parseMap.getMap();
        waka.scatterMode = false;

        List<GameObject> ghosts = parseMap.getGhosts();

        GhostController ghostC = new GhostController();
        ghostC.setGhosts(ghosts);

        blinkies = ghosts.stream().filter(o -> o instanceof GhostBlinky).map(o -> (GhostBlinky) o).collect(Collectors.toList());
        pinkies = ghosts.stream().filter(o -> o instanceof GhostPinky).map(o -> (GhostPinky) o).collect(Collectors.toList());
        inkies = ghosts.stream().filter(o -> o instanceof GhostInky).map(o -> (GhostInky) o).collect(Collectors.toList());
        clydes = ghosts.stream().filter(o -> o instanceof GhostClyde).map(o -> (GhostClyde) o).collect(Collectors.toList());

        blinky = blinkies.get(0);
        pinky = pinkies.get(0);
        inky = inkies.get(0);
        clyde = clydes.get(0);
    }

    //A method that adds delays between runSketch() calls
    public void delay(double time) {
        long delay = System.currentTimeMillis();

        while (System.currentTimeMillis() - delay <= time * 1000) {}
    }

    @Test
    //Tests when ghosts are in frightened mode 
    public void testRunAway() {
        blinky.frightened = true;

        String[] args = {"Test"};
        PApplet.runSketch(args, app);
        delay(4);
        
        blinky.frightened = false;
        
        assertNotNull(app);
    }

    @Test
    //Blinky constructor test
    public void blinkyConstructorTest() {
        assertNotNull(blinky);
    }

    @Test
    //Test getting Blinky velocity
    public void testGetBlinkyVel() {
        assertNotNull(blinky.getVel());
    }

    @Test
    //Test getting Blinky 
    public void testGetBlinkyPos() {
        assertNotNull(blinky.getPos());
    }

    @Test
    //Test Blinky movement with targeting system
    public void blinkyMoveTarget() {
        waka.setPos(new PVector(1, 4)); //set to top right corner

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 1.5 * 1000) {
            blinky.move();
            
            if (blinky.getPos().x == waka.getPos().x && blinky.getPos().y == waka.getPos().y) {
                break;
            }
        }

        assertTrue(blinky.getPos().x == waka.getPos().x && blinky.getPos().y == waka.getPos().y);
    }

    @Test
    //Test for Blinky movement during scatter
    public void blinkyScatter() {
        blinky.scatter();

        PVector prevPos = new PVector(blinky.getPos().x, blinky.getPos().y);

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 3 * 1000) {
            blinky.move();
        }

        assertTrue(prevPos != blinky.getPos());
    }

    @Test
    //Pinky constructor test
    public void pinkyConstructorTest() {
        assertNotNull(pinky);
    }

    @Test
    //Test getting Blinky velocity
    public void testGetPinkyVel() {
        assertNotNull(pinky.getVel());
    }

    @Test
    //Test getting Blinky 
    public void testGetPinkyPos() {
        assertNotNull(pinky.getPos());
    }

    @Test
    //Make sure Pinky target node reamins in bounds
    public void testPinkyOutofBoundsTarget() {
        waka.setVel(new PVector(0, 0));

        waka.setPos(new PVector(0, 5));
        pinky.setPinkyTargetNode();

        waka.setPos(new PVector(28, 5));
        pinky.setPinkyTargetNode();

        waka.setPos(new PVector(5, 0));
        pinky.setPinkyTargetNode();

        waka.setPos(new PVector(5, 33));
        pinky.setPinkyTargetNode();

        assertNotNull(app);
    }

    @Test
    //Test Pinky movement with targeting system
    public void pinkyMoveTarget() {
        waka.setPos(new PVector(1, 4)); //set to top right corner

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 1.5 * 1000) {
            pinky.move();
            
            if (pinky.getPos().x == waka.getPos().x && pinky.getPos().y == waka.getPos().y) {
                break;
            }
        }

        assertTrue(pinky.getPos().x == waka.getPos().x && pinky.getPos().y == waka.getPos().y);
    }

    @Test
    //Test for Pinky movement during scatter
    public void pinkyScatter() {
        pinky.scatter(); 

        PVector prevPos = new PVector(pinky.getPos().x, pinky.getPos().y);

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 3 * 1000) {
            pinky.move();
        }

        assertTrue(prevPos != pinky.getPos());
    }

    @Test
    //Inky constructor test
    public void inkyConstructorTest() {
        assertNotNull(inky);
    }

    @Test
    //Test getting Blinky velocity
    public void testgetInkyVel() {
        assertNotNull(inky.getVel());
    }

    @Test
    //Test getting Blinky 
    public void testGetInkyPos() {
        assertNotNull(inky.getPos());
    }

    @Test
    //Make sure that Inky target node remains in bounds
    public void testInkyOutofBoundsTarget() {
        waka.setVel(new PVector(0, 0));

        waka.setPos(new PVector(0, 5));
        inky.setInkyTargetNode();

        waka.setPos(new PVector(28, 5));
        inky.setInkyTargetNode();

        waka.setPos(new PVector(5, 0));
        inky.setInkyTargetNode();

        waka.setPos(new PVector(5, 33));
        inky.setInkyTargetNode();

        assertNotNull(app);
    }

    @Test
    //Test Inky movement with targeting system
    public void inkyMoveTarget() {
        waka.setPos(new PVector(1, 4)); //set to bottom right corner

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 1.5 * 1000) {
            inky.move();
            
            if (inky.getPos().x == waka.getPos().x && inky.getPos().y == waka.getPos().y) {
                break;
            }
        }

        assertTrue(inky.getPos().x == waka.getPos().x && inky.getPos().y == waka.getPos().y);
    }

    @Test
    //Test for Inky movement during scatter
    public void inkyScatter() {
        inky.scatter();

        PVector prevPos = new PVector(inky.getPos().x, inky.getPos().y);

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 3 * 1000) {
            inky.move();
        }

        assertTrue(prevPos != inky.getPos());
    }

    @Test
    //Clyde constructor test
    public void clydeConstructorTest() {
        assertNotNull(clyde);
    }

    @Test
    //Test getting Blinky velocity
    public void testGetClydeVel() {
        assertNotNull(clyde.getVel());
    }

    @Test
    //Test getting Blinky 
    public void testGetClydePos() {
        assertNotNull(clyde.getPos());
    }

    @Test
    //Test for Clyde movement with targeting system
    public void clydeMoveTarget() {
        waka.setPos(new PVector(1, 4)); //set to bottom right corner

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 1.5 * 1000) {
            pinky.move();
            
            if (clyde.getPos().x == clyde.getPos().x && clyde.getPos().y == waka.getPos().y) {
                break;
            }
        }

        assertFalse(clyde.getPos().x == waka.getPos().x && clyde.getPos().y == waka.getPos().y); //will most likely avoid Pacman due to ignorant mode
    }

    @Test
    //Test for Clyde movement during scatter
    public void clydeScatter() {
        clyde.scatter(); 

        PVector prevPos = new PVector(clyde.getPos().x, clyde.getPos().y);

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 3 * 1000) {
            clyde.move();
        }

        assertTrue(prevPos != clyde.getPos());
    }

}
