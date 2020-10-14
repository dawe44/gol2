package sample;

import java.io.IOException;

/**
 * The interface Game of livable.
 */
public interface GameOfLivable {

    /**
     * Initialize clear map.
     */
    void initializeClearMap();

    /**
     * Initialize random map.
     */
    void initializeRandomMap();

    /**
     * Draw.
     */
    void draw();

    /**
     * Update.
     *
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    void update() throws IOException, ClassNotFoundException;

    /**
     * Update.
     *
     * @param amount the amount
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    void update(int amount) throws IOException, ClassNotFoundException;

    /**
     * Sets pattern to map.
     *
     * @param patternMap the pattern map
     */
    void setPatternToMap(int[][] patternMap);

    /**
     * Change map size.
     *
     * @param newWidth  the new width
     * @param newHeight the new height
     */
    void changeMapSize(int newWidth, int newHeight);

    /**
     * Change cell size.
     *
     * @param cellSize the cell size
     */
    void changeCellSize(int cellSize);

    /**
     * Sets cell alive.
     *
     * @param x the x
     * @param y the y
     */
    void setCellAlive(int x, int y);

    /**
     * Change cell state.
     *
     * @param x the x
     * @param y the y
     */
    void changeCellState(int x, int y);
}
