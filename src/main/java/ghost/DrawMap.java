/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the DrawMap class, which is what draws all of the objects onto the canvas.
*/

package ghost;

import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class DrawMap {
    private PApplet app;
    private Tile[][] map;

    private PImage fruitImg;
    private PImage superFruitImg;
    private PImage anitiPowerImg;

    private PImage blinkyImg, pinkyImg, inkyImg, clydeImg;
    PImage[] wallImgs = new PImage[6], wakaImgs = new PImage[5], frightenedImg = new PImage[2];
    
    /**
     * Contructor for the DrawMap class.
     * @param app the app of the object.
     * @param map a matrix.
     */
    public DrawMap(PApplet app, Tile[][] map) {
        this.app = app;
        this.map = map;
    }

    /**
     * This method will load images to specific variables and is used in the setup of the app.
     */
    public void loadTileImgs() {
        fruitImg = app.loadImage("src/main/resources/fruit.png");
        superFruitImg = app.loadImage("src/main/resources/superFruit.png");
        anitiPowerImg = app.loadImage("src/main/resources/antiPower.png");

        wallImgs[0] = app.loadImage("src/main/resources/horizontal.png"); wallImgs[1] = app.loadImage("src/main/resources/vertical.png");
        wallImgs[2] = app.loadImage("src/main/resources/upLeft.png"); wallImgs[3] = app.loadImage("src/main/resources/upRight.png");
        wallImgs[4] = app.loadImage("src/main/resources/downLeft.png"); wallImgs[5] = app.loadImage("src/main/resources/downRight.png");

        GameObject.gameOverImg = app.loadImage("src/main/resources/gameover - Copy.png");
        GameObject.font = app.createFont("src/main/resources/PressStart2P-Regular.ttf", 10);
    }

    /**
     * This method will load Pacman images to specific variables and is used in the setup of the app.
     * @param waka a Pacman object.
     */
    public void loadPacmanImgs(Pacman waka) {
        wakaImgs[0] = app.loadImage("src/main/resources/playerClosed.png");
        wakaImgs[1] = app.loadImage("src/main/resources/playerUp.png");
        wakaImgs[2] = app.loadImage("src/main/resources/playerDown.png");
        wakaImgs[3] = app.loadImage("src/main/resources/playerRight.png");
        wakaImgs[4] = app.loadImage("src/main/resources/playerLeft.png");

        waka.setImg(wakaImgs);
    }

    /**
     * This method will load Ghost images to specific variables and is used in the setup of the app.
     * @param ghosts a List of Ghost objects.
     */
    public void loadGhostImgs(List<GameObject> ghosts) {
        blinkyImg = app.loadImage("src/main/resources/chaser.png");
        pinkyImg = app.loadImage("src/main/resources/ambusher.png");
        inkyImg = app.loadImage("src/main/resources/whim.png");
        clydeImg = app.loadImage("src/main/resources/ignorant.png");
        frightenedImg[0] = app.loadImage("src/main/resources/frightened.png");
        frightenedImg[1] = app.loadImage("src/main/resources/frightened2.png");

        for (GameObject ghost : ghosts) {
            if (ghost instanceof GhostBlinky) {
                ghost.setImg(blinkyImg);
            } else if (ghost instanceof GhostPinky) {
                ghost.setImg(pinkyImg);
            } else if (ghost instanceof GhostInky) {
                ghost.setImg(inkyImg);
            } else if (ghost instanceof GhostClyde) {
                ghost.setImg(clydeImg);
            }
        }

        GameObject.frightenedImg = frightenedImg;
    }

    /**
     * This method will draw out the Tiles defined by the given map configuration which uses the drawTiles(tile) method.
     */
    public void drawLayout() {
        app.background(0, 0, 0);

        for (int yCoor = 0; yCoor < map.length; yCoor++) {
            for (int xCoor = 0; xCoor < map[yCoor].length; xCoor++) {
                Tile tile = map[yCoor][xCoor];

                if (tile.isWall()) {
                    tile.setImg(wallImgs[tile.getWallState()]);
                } else if (tile.isFruit()) {
                    tile.setImg(fruitImg);
                } else if (tile.isSuperFruit()) {
                    tile.setImg(superFruitImg);
                } else if (tile.isAntiPower()){
                    tile.setImg(anitiPowerImg);
                }

                tile.setSuperFruitState();

                if (!tile.isEmpty()) {
                    if ((tile.isSuperFruit() || tile.isAntiPower()) && tile.getSuperFruitState() == 1) {
                        continue;
                    }
                    drawTiles(tile);
                }
            }
        }
    }

    /**
     * This method draws the tiles.
     * @param tile the Tile object.
     */
    public void drawTiles(Tile tile) {
        app.image(tile.getImg(), tile.getPos().x * 16, tile.getPos().y * 16);
    }
}
