/*
    Author: Tyler Gan
    Date: 21/11/20
    INFO1113 Assignment 2: Waka Waka

    This is the MapParseTest class, where methods related to the Map Parse are tested.
*/

package ghost;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapParseTest {
    App app;
    Pacman waka;
    MapParse parseMap;

    @BeforeEach
    public void setObjects() {
        app = new App();
        waka = new Pacman();
        parseMap = new MapParse(waka);
    }

    @Test
    //Testing for getting map
    public void testGetMap() {
        assertNotNull(parseMap.getMap());
        assertTrue(parseMap.getMap() instanceof Tile[][]);
    }

    @Test
    //Test getting fruiit count
    public void testGetFruitCount() {
        assertTrue(Integer.class.isInstance(parseMap.getFruitCount()));
        assertTrue(parseMap.getFruitCount() != 0);
    }

    @Test
    //Make sure we are getting a list of ghosts
    public void testGetGhosts() {
        assertNotNull(parseMap.getGhosts());
        assertFalse(parseMap.getGhosts().isEmpty());
    }
}
