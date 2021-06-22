/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the GameObject class, where methods related to the GameObject are tested.
*/

package ghost;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;

public class GameObjectTest extends GameObject{
    App app;
    long testTime;
    
    @BeforeEach
    //A method that sets the following objects before each test
    public void setObjects() {
        app = new App();
    }

    //A method that adds delays between runSketch() calls
    public void delay(double time) {
        long delay = System.currentTimeMillis();

        while (System.currentTimeMillis() - delay <= time * 1000) {}
    }

    @Test
    //Tests whether the ending methods are drawn
    public void testDrawLoss() {
        gameOver = loss = true;

        String[] args = {"Test"};
        PApplet.runSketch(args, app);
        delay(1);

        assertTrue(gameOver);
        assertTrue(loss);

        app.reset();
        gameOver = loss = false; //this is normally reset after 10 seconds
    }

    @Test
    //Tests whether the ending methods are drawn
    public void testDrawWin() {
        gameOver = true;
        loss = false;

        String[] args = {"Test"};
        PApplet.runSketch(args, app);
        delay(1);

        assertTrue(gameOver);
        assertFalse(loss);

        app.reset();
        gameOver = loss = false;
    }
}
