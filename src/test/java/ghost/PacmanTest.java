/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the PacmanTest class, where methods related to Waka are tested.
*/

package ghost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PImage;
import processing.core.PVector;

public class PacmanTest extends Pacman{
    App app;
    Pacman waka;
    PVector pos;
    
    @BeforeEach
    //set the following conditions before each test
    public void setObjects() {
        app = new App();
        waka = new Pacman();

        MapParse parseMap = new MapParse(waka);
        waka.map = parseMap.getMap();

        List<GameObject> ghosts = parseMap.getGhosts();
        waka.setGhosts(ghosts);
    }

    @Test
    //Constructor test
    public void constructorTest() {
        assertNotNull(waka);
    }

    @Test
    //Test getting the speed
    public void getSpeedTest() {
        assertEquals(1, waka.getSpeed());
    }

    @Test
    //Testing getting vel for Pacman
    public void getVelTest() {
        assertNotNull(waka.getVel());
    }

    @Test
    //Test getting pos for Pacman 
    public void getPosTest() {
        assertNotNull(waka.getPos());
    }

    @Test
    //Test setting ghosts
    public void testSetGhosts() {
        List<GameObject> ghosts = new LinkedList<>();
        GhostBlinky blinky = new GhostBlinky(waka); ghosts.add(blinky);

        assertFalse(ghosts.isEmpty());
        waka.setGhosts(ghosts);
    }

    @Test
    //Test setting and getting images
    public void testGetImgs() {
        PImage[] img = {new PImage()};

        waka.setImg(img);
        assertNotNull(waka.getImg());
    }

    @Test
    //Test changing Pacman indexes from state to state
    public void testStateAnimate() {
        waka.animate(); waka.setState(4);
        assertEquals(0, waka.getState());

        for (int i = 0; i < 20; i++) { //animate 20 more times
            waka.animate();
        }

        assertEquals(4, waka.getState()); //next state should be '4' (looking to the left)
    }

    @Test
    //Make sure lives is equal to 3
    public void testGetLives() {
        assertEquals(3, waka.getLives());
    }

    @Test
    //Tests whether Pacman loses a life
    public void testLivesLost() {
        //check actual lives can change
        waka.setLives(waka.getLives() - 1); //simulate death

        assertEquals(2, waka.getLives());
    }

    @Test
    //Test that the movement of Pacman works
    public void testMove() {
        for (int i = 0; i < 20; i ++) {
            waka.move();
        }

        assertEquals(new PVector(11.75f, 20), waka.getPos());
    }

    @Test
    //Test that Pacman and Ghost collisions work
    public void simulateGhostCollision() {
        pos = new PVector(1, 1);
        waka.setPos(pos);

        List<GameObject> ghosts = new LinkedList<>();
        GhostBlinky blinky = new GhostBlinky(waka); ghosts.add(blinky);

        blinky.setPos(pos);
        waka.setGhosts(ghosts);

        assertTrue(waka.isDead());
    }

    @Test
    //Test that Pacman, when colliding with a Ghost at one health, dies
    public void testDeath() {
        pos = new PVector(1, 1);
        waka.setPos(pos);

        List<GameObject> ghosts = new LinkedList<>();
        GhostBlinky blinky = new GhostBlinky(waka); ghosts.add(blinky);
        blinky.setPos(pos);

        waka.setGhosts(ghosts);
        waka.setLives(1);
        waka.isDead();

        assertTrue(waka.gameOver);
        assertTrue(waka.loss);

        waka.gameOver = waka.loss = false;
    }

    @Test
    public void testKill() {
        pos = new PVector(1, 1);
        waka.setPos(pos);
        
        List<GameObject> ghosts = new LinkedList<>();
        GhostBlinky blinky = new GhostBlinky(waka); ghosts.add(blinky);
        blinky.setPos(pos);

        waka.setGhosts(ghosts);
        blinky.frightened = true;

        assertFalse(waka.isDead());
        assertTrue(blinky.isDead());
        blinky.frightened = false;
    }

    @Test
    //Test that Pacman interacts with the map correctly when on the last fruit
    public void testLastFruit() {
        waka.setFruitCount(1);

        for (int i = 0; i < 20; i++) {
            waka.move();
            System.out.println(waka.getPos());
        }

        assertTrue(waka.gameOver);
        assertTrue(waka.win);

        waka.gameOver = waka.win = false;
    }

    @Test
    //Test that Pacman interacts with the map correctly when he lands on a super fruit
    public void testSuperFruit() {
        waka.setPos(new PVector(1, 5));

        assertTrue(waka.map[(int) waka.getPos().y][(int) waka.getPos().x].isSuperFruit());

        waka.move();

        assertFalse(waka.map[(int) waka.getPos().y][(int) waka.getPos().x].isSuperFruit());
        assertTrue(waka.map[(int) waka.getPos().y][(int) waka.getPos().x].isEmpty());

        assertTrue(waka.frightened);
    }

    @Test
    //Test that Pacman interacts with the map correctly when he lands on a anti-power up
    public void testAntiFruit() {
        waka.setPos(new PVector(6, 20));

        assertTrue(waka.map[(int) waka.getPos().y][(int) waka.getPos().x].isAntiPower());

        waka.move();

        assertFalse(waka.map[(int) waka.getPos().y][(int) waka.getPos().x].isAntiPower());
        assertTrue(waka.map[(int) waka.getPos().y][(int) waka.getPos().x].isEmpty());

        assertTrue(waka.invisibleMode);
    }

    @Test
    //Test pacman moving left
    public void testLeftMove() {
        waka.setCurrMove("left");

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 0.5 * 1000) {
            waka.move();
        }

        assertTrue(waka.getPos().x == 9 && waka.getPos().y == 20);
    }

    @Test
    //Test pacman moving right
    public void testRightMove() {
        waka.setCurrMove("right");

        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 0.5 * 1000) {
            waka.move();
        }

        assertTrue(waka.getPos().x == 18 && waka.getPos().y == 20);
    }

    @Test
    //Test pacman moving up
    public void testUpMove() {
        waka.setCurrMove("up");
        
        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 0.5 * 1000) {
            waka.move();
        }

        assertTrue(waka.getPos().x == 9 && waka.getPos().y == 14);
    }

    @Test
    //Test pacman moving down
    public void testDownMove() {
        waka.setCurrMove("down");
        
        long testTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - testTime <= 0.5 * 1000) {
            waka.move();
        }

        assertTrue(waka.getPos().x == 9 && waka.getPos().y == 23);
    }
}
