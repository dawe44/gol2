package sample;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameOfLifeTest {
    GameOfLife golTest;
    Canvas canvas;
    GraphicsContext graphics;

    @BeforeEach
    void init(){
        canvas = new Canvas(60, 60);
        graphics = canvas.getGraphicsContext2D();
        golTest = new GameOfLife(60, 60, 10, graphics);
    }

    @Test
    void testBlinker() {
        int[][] map = {
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,1,1,1,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0}
        };

        int[][] expectedMap = {
                {0,0,0,0,0,0},
                {0,0,1,0,0,0},
                {0,0,1,0,0,0},
                {0,0,1,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0}
        };


        golTest.setMap(map);
        try {
            golTest.update();
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        int[][] current = golTest.getMap();
        assertEquals(Arrays.deepToString(expectedMap), Arrays.deepToString(current));
    }

    @Test
    void testLoaf() {
        int[][] map = {
                {0,0,0,0,0,0},
                {0,0,1,1,0,0},
                {0,1,0,0,1,0},
                {0,0,1,0,1,0},
                {0,0,0,1,0,0},
                {0,0,0,0,0,0}
        };

        int[][] expectedMap = {
                {0,0,0,0,0,0},
                {0,0,1,1,0,0},
                {0,1,0,0,1,0},
                {0,0,1,0,1,0},
                {0,0,0,1,0,0},
                {0,0,0,0,0,0}
        };

        golTest.setMap(map);
        try {
            golTest.update();
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        int[][] current = golTest.getMap();
        assertEquals(Arrays.deepToString(expectedMap), Arrays.deepToString(current));
    }

    @Test
    void testGlider() {
        int[][] map = {
                {0,0,0,0,0,0},
                {0,0,1,0,0,0},
                {0,0,0,1,0,0},
                {0,1,1,1,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0}
        };

        int[][] expectedMap = {
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,1,0,1,0,0},
                {0,0,1,1,0,0},
                {0,0,1,0,0,0},
                {0,0,0,0,0,0}
        };

        golTest.setMap(map);
        try {
            golTest.update();
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        int[][] current = golTest.getMap();
        assertEquals(Arrays.deepToString(expectedMap), Arrays.deepToString(current));
    }

    @Test
    void okay(){
        assertEquals(2, 2);
    }
}