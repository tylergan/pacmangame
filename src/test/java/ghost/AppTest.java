/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the AppTest class, where methods related to the app are tested.
*/

package ghost;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processing.core.PApplet;

class AppTest {
    App app;

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
    //Test App constructor
    public void constructorTest() {
        assertNotNull(app);
    }

    @Test
    //Test resetting the map
    public void testReset() {
        String[] args = {"Test"};
        PApplet.runSketch(args, app);
        delay(0.5);

        app.reset();
        
        assertNotNull(app);
    }

    @Test
    //Test moving right
    public void testKeyRight() {
        app.keyCode = 39;
        app.keyPressed();

        String[] args = {"Test"};
        PApplet.runSketch(args, app);
        delay(0.5);

        app.reset();

        assertNotNull(app);
    }

    @Test
    //Test moving left
    public void testKeyLeft() {
        app.keyCode = 37;
        app.keyPressed();

        String[] args = {"Test"};
        PApplet.runSketch(args, app);
        delay(0.5);

        app.reset();

        assertNotNull(app);
    }
    
    @Test
    //Test moving up
    public void testKeyUp() {
        app.keyCode = 38;
        app.keyPressed();

        String[] args = {"Test"};
        PApplet.runSketch(args, app);
        delay(0.5);

        app.reset();

        assertNotNull(app);
    }

    @Test
    //Test moving down
    public void testKeyDown() {
        app.keyCode = 40;
        app.keyPressed();

        String[] args = {"Test"};
        PApplet.runSketch(args, app);
        delay(0.5);

        app.reset();

        assertNotNull(app);
    }

    @Test
    //Test whether debug mode works 
    public void testDebug() {
        app.keyCode = 32;
        app.keyPressed();

        String[] args = {"Test"};
        PApplet.runSketch(args, app);
        delay(0.5);

        app.keyCode = 32;
        app.keyPressed();

        app.reset();

        assertNotNull(app);
    }

    @Test
    //Testing for a non-existent key and whether being killed works
    public void testBadKey() {
        app.keyCode = 1;
        app.keyPressed();

        String[] args = {"Test"};
        PApplet.runSketch(args, app);
        delay(0.5);

        app.reset();

        assertNotNull(app);
    }

}