/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the App class, the mainframe of the program which gradle uses to run the game.
*/

package ghost;

import processing.core.PApplet;

public class App extends PApplet {

    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;

    private boolean debugMode = false;

    private Pacman waka; 
    private GhostController ghostController;

    private MapParse mapParse;
    private DrawMap drawMap;

    public App() {
        setObjects();
    }

    public void setObjects() {
        waka = new Pacman();
        mapParse = new MapParse(waka);
        drawMap = new DrawMap(this, mapParse.getMap());
        ghostController = new GhostController();
    }

    public void setup() {
        frameRate(60);
        ghostController.setGhosts(mapParse.getGhosts());

        drawMap.loadTileImgs();
        drawMap.loadPacmanImgs(waka);
        drawMap.loadGhostImgs(mapParse.getGhosts());

        GameObject.map = mapParse.getMap();
        GameObject.app = this;
        
        waka.setGhosts(mapParse.getGhosts());
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void draw() { 
        if (!GameObject.finished(this, waka)) {
            drawMap.drawLayout();
            ghostController.draw();
            waka.move();
            waka.animate();
            waka.draw();

            ghostController.move();

            if (debugMode) {
                ghostController.drawTarget();
            }
        } else {
            GameObject.drawEnding(this);
        }
    }

    public void keyPressed() {
        if (keyCode == 37) {
            waka.setCurrMove("left");
        } else if (keyCode == 38) {
            waka.setCurrMove("up");
        } else if (keyCode == 39) {
            waka.setCurrMove("right");
        } else if (keyCode == 40) {
            waka.setCurrMove("down");
        } else if (keyCode == 32) {
            if (debugMode) {debugMode = false;} 
            else {debugMode = true;}
        }
    }

    /**
     * This method will reset the entire app by creating new objects, resetting GameObject static variables and calling the setup() method again.
     */
    public void reset() {
        int newLives = waka.getLives();
        this.setObjects();
        this.setup();
        GameObject.reset();
        waka.setLives(newLives); //as we are creating a new Pacman object, we need to set store his old lives and set it to the new object
    }

    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }

}
