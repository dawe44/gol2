package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Random;

/**
 * This class is used to draw the map on the canvas of the GUI.
 */
public class GameOfLife implements GameOfLivable {
    private int rows, cols, height, width, cellSize, cellBorderSize;
    private int cellBorderDenominator;
    private int probability;

    public void setProbability(int probability){
        this.probability = probability;
    }
    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    private int[][] map;
    private Random random = new Random();
    private GraphicsContext graphics;
    /**
     * The Client.
     */
    public Client client;

    /**
     * Constructor with width, height, cellsize and an instance of GraphicsContext inherited.
     * A private instance of the class Client is instantiated here.
     *
     * @param width    the width
     * @param height   the height
     * @param cellsize the cellsize
     * @param graphics the graphics
     */
    public GameOfLife(int width, int height, int cellsize, GraphicsContext graphics) {
        this.height = height;
        this.width = width;
        this.rows = width / cellsize;
        this.cols = height / cellsize;
        this.cellSize = cellsize;
        this.cellBorderDenominator = (int)(0.1 * this.cellSize) + 10;
        this.cellBorderSize = this.cellSize / this.cellBorderDenominator;
        this.graphics = graphics;
        map = new int[rows][cols];
        this.probability = 5;
        this.client = new Client();
    }

    /**
     * Sets the state on every cell on the 2D gridmap to 0 (dead).
     */
    @Override
    public void initializeClearMap() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = 0;
            }
        }
        draw();
    }

    /**
     * Each cell on the gridmap gets a state. Which state it is is chosen randomly 50/50.
     */
    @Override
    public void initializeRandomMap() {
        int value;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                value = random.nextInt(100);
                if(value < probability) {
                    map[i][j] = 1;
                }
            }
        }
        draw();
    }

    /**
     * The 2D gridmap is drawn on the canvas of the GUI with this method.
     */
    @Override
    public void draw() {
        graphics.setFill(Color.rgb(58, 80, 107));
        graphics.fillRect(0, 0, width, height);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                graphics.setFill(Color.rgb(58, 80, 107));
                graphics.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                if (map[i][j] == 1) {
                    graphics.setFill(Color.rgb(111, 255, 233));
                    graphics.fillRect((i * cellSize) + 1, (j * cellSize) + 1, cellSize - cellBorderSize, cellSize - cellBorderSize);
                } else if (map[i][j] == 0) {
                    graphics.setFill(Color.rgb(11, 18, 49));
                    graphics.fillRect((i * cellSize) + 1, (j * cellSize) + 1, cellSize - cellBorderSize, cellSize - cellBorderSize);
                }
            }
        }
    }

    /**
     * The gridmap on the canvas is updated by getting a new map from the server.
     * The communication occurs through a instance of the Client class
     * where a method is called, with the current map used as a parameter,
     * through the instance which returns a new map.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void update() throws IOException, ClassNotFoundException {

        long computationTime = System.currentTimeMillis();
        int[][] nextMap;
        long startTime = System.currentTimeMillis();

        nextMap = client.sendAndReceiveMap(map);

        long endTime = System.currentTimeMillis();
        long computation1 = endTime - startTime;
        startTime = System.currentTimeMillis();
        map = nextMap;
        draw();
        endTime = System.currentTimeMillis();
        long computation2 = endTime - startTime;
        long end = System.currentTimeMillis();
        long computation3 = end - computationTime;
        System.out.println("Server ping: " + computation1 + " | Drawing: " + computation2 + " | Total ping ms: " + computation3);
    }

    /**
     * The method from the client class is called the amount of times "int amount" is.
     * @param amount
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void update(int amount) throws IOException, ClassNotFoundException {
        int[][] nextMap;
        for(int k = 0; k <= amount; k++) {
            nextMap = client.sendAndReceiveMap(map);
            map = nextMap;
        }
        draw();
    }

    /**
     * This method is used to load a pattern to the current 2D gridmap.
     * @param patternMap
     */
    @Override
    public void setPatternToMap(int[][] patternMap){
        initializeClearMap();
        int centralRows = (rows / 2) - 4;
        int centralCols = (cols / 2) - 4;
        int x = 0;
        int y = 0;
        for (int i = centralRows; i < patternMap.length + centralRows; i++) {
            for (int j = centralCols; j < patternMap[0].length + centralCols; j++) {
                if (patternMap[x][y] == 1) {
                    map[i][j] = 1;
                } else {
                    map[i][j] = 0;
                }
                y++;
            }
            y = 0;
            x++;
        }
        draw();
    }

    /**
     * The canvas on the GUI gets a new width and height.
     * @param newWidth
     * @param newHeight
     */
    @Override
    public void changeMapSize(int newWidth, int newHeight){
        this.width = newWidth;
        this.height = newHeight;
        this.rows = newWidth / this.cellSize;
        this.cols = newHeight / this.cellSize;

        map = new int[this.rows][this.cols];
        initializeClearMap();
        //initializeRandomMap();
    }

    /**
     * The cellsize of the current gridmap is changed to whatever is passed
     * as a parameter. The amount of cells increases or decreases depending
     * on the parameter value.
     * @param cellSize
     */
    @Override
    public void changeCellSize(int cellSize){
        this.cellSize = cellSize;
        this.cellBorderDenominator = (int) (this.cellSize * 0.1) + 10;
        this.cellBorderSize = cellSize / this.cellBorderDenominator;
        this.rows =  this.width  / this.cellSize;
        this.cols =  this.height / this.cellSize;

        map = new int[this.rows][this.cols];
        initializeRandomMap();
    }

    /**
     * Sets a specific cell alive.
     * @param x
     * @param y
     */
    @Override
    public void setCellAlive(int x, int y){
        int row = x / cellSize;
        int col = y / cellSize;
        map[row][col] = 1;
        draw();
    }

    /**
     * Changes the state of a specific cell.
     * @param x
     * @param y
     */
    @Override
    public void changeCellState(int x, int y){
        int row = x / cellSize;
        int col = y / cellSize;
        if(map[row][col] == 1){
            map[row][col] = 0;
        }
        else if(map[row][col] == 0){
            map[row][col] = 1;
        }
        draw();
    }
}
