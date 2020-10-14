package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;


/**
 * This is the class that draws the map to the Pattern window and saves a particular
 * pattern to the XML document. This class uses DOM to read and save data to XML.
 */
public class mapNewPattern {
    private int rows, cols, height, width, cellSize, cellBorderSize;
    private final int CELLBORDERDENOMINATOR = 4;
    /**
     * The Map.
     */
    private int[][] map;

    /**
     * Sets map.
     *
     * @param map the map
     */
    public void setMap(int[][] map) {
        this.map = map;
    }

    private File file;
    private GraphicsContext graphics;
    /**
     * The Factory.
     */
    private DocumentBuilderFactory factory;
    /**
     * The Builder.
     */
    private DocumentBuilder builder;
    /**
     * The Document.
     */
    private Document document;
    /**
     * The Transformer factory.
     */
    private TransformerFactory transformerFactory;
    /**
     * The Transformer.
     */
    private Transformer transformer;
    /**
     * The Source.
     */
    private DOMSource source;
    /**
     * The Node.
     */
    private Node node;
    /**
     * The Node list.
     */
    private NodeList nodeList;


    /**
     * Constructor. Instantiates a new Map new pattern. Makes the 2D array ready for the
     * canvas. If an XML document does not exist a new one is created. If a XML document
     * exists then its read to a instance of a document object.
     *
     * @param width    the width
     * @param height   the height
     * @param cellsize the cellsize
     * @param graphics the graphics
     * @throws ParserConfigurationException the parser configuration exception
     * @throws TransformerException         the transformer exception
     */
    public mapNewPattern(int width, int height, int cellsize, GraphicsContext graphics) throws ParserConfigurationException, TransformerException {
        this.height = height;
        this.width = width;
        this.rows = width / cellsize;
        this.cols = height / cellsize;
        this.cellSize = cellsize;
        this.cellBorderSize = cellsize / CELLBORDERDENOMINATOR;
        this.graphics = graphics;
        map = new int[rows][cols];
        file = new File("pattern.xml");
        if (!file.exists()){
            createNewFile();
        }
        else{

            try {
                factory = DocumentBuilderFactory.newInstance();
                builder = factory.newDocumentBuilder();
                document = builder.parse(file);
                document.getDocumentElement().normalize();

            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
        }
        nodeList = document.getElementsByTagName("ListOfPatterns");
        node = document.getFirstChild();
    }

    /**
     * Creates a new XML document.
     *
     * @throws ParserConfigurationException the parser configuration exception
     * @throws TransformerException         the transformer exception
     */
    public void createNewFile() throws ParserConfigurationException, TransformerException {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        Element listPatterns = document.createElement("ListOfPatterns");
        document.appendChild(listPatterns);

        transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
        source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    /**
     * Removes a specific pattern from the list in the XML document.
     *
     * @param patternName the pattern name
     * @throws TransformerException the transformer exception
     */
    public void removePatternFromXML(String patternName) throws TransformerException {
        NodeList nodeListPatterns = nodeList.item(0).getChildNodes();

        for(int i = 0; i < nodeListPatterns.getLength(); i++){
            String[] string = nodeListPatterns.item(i).getTextContent().split("TheArray");
            if (string[0].equals(patternName)){
                Node node = nodeListPatterns.item(i);
                while (node.hasChildNodes()){
                    node.removeChild(node.getFirstChild());
                }
                nodeListPatterns.item(i).getParentNode().removeChild(nodeListPatterns.item(i));
            }
        }

        transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
        source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    /**
     * Gets a specific map from the XML document. Returns the 2D map.
     *
     * @param patternName the pattern name
     * @return the int [ ] [ ]
     */
    public int[][] getMapFromXML(String patternName){
        int[][] patternMap = new int[map.length][map[0].length];

        NodeList nodeListPattern = nodeList.item(0).getChildNodes();
        for(int k = 0; k < nodeListPattern.getLength(); k++){
            if(nodeListPattern.item(k).getTextContent().contains(patternName)){
                NodeList nodeListPatternName = nodeListPattern.item(k).getChildNodes();
                for(int c = 0; c < nodeListPatternName.getLength(); c++){
                    NodeList nodeListItems = nodeListPatternName.item(c).getChildNodes();
                    for(int i = 0; i < nodeListItems.getLength(); i++){
                        NodeList nodeItems = nodeListItems.item(i).getChildNodes();
                        for(int j = 0; j < nodeItems.getLength(); j++) {
                            patternMap[i-1][j] = Integer.parseInt(nodeItems.item(j).getTextContent());
                        }
                    }
                }
            }
        }
        return patternMap;
    }

    /**
     * Reads all the patterns that exist on the XML document and returns it as
     * a String array.
     *
     * @return the string [ ]
     */
    public String[] getListOfPatternNames(){
        NodeList nodeListPatterns = nodeList.item(0).getChildNodes();
        String[] listOfPatternNames = new String[nodeListPatterns.getLength()];

        for (int i = 0; i < nodeListPatterns.getLength(); i++){
            String[] tempString;
            tempString = nodeListPatterns.item(i).getTextContent().split("TheArray");
            listOfPatternNames[i] = tempString[0];
        }
        return listOfPatternNames;
    }

    /**
     * Checks if a specific pattern exists in the XML document.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean doesPatternExist(String name){
        NodeList listOfPatterns = nodeList.item(0).getChildNodes();

        for(int i = 0; i < listOfPatterns.getLength(); i++){
            var temp = listOfPatterns.item(i).getTextContent().split("TheArray");
            if(temp[0].equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Saves the instance of document object to XML file.
     *
     * @param name the name
     * @throws TransformerException the transformer exception
     */
    public void saveToFile(String name) throws TransformerException {

        Element patternName = document.createElement("PatternName");
        patternName.setTextContent(name);

        Element listItems = document.createElement("ListItems");
        listItems.setTextContent("TheArray");
        patternName.appendChild(listItems);

        for(int i = 0; i < map.length; i++) {
            Element Items = document.createElement("Items");
            Items.setAttribute("ID", String.valueOf(i));
            listItems.appendChild(Items);
            for(int j = 0; j < map[0].length; j++) {
                Element Item = document.createElement("Item");
                Item.setTextContent(String.valueOf(map[i][j]));
                Item.setAttribute("ID", String.valueOf(j));
                Items.appendChild(Item);
            }
        }
        node.appendChild(patternName);

        transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
        source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    /**
     * Draws the map.
     */
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
     * Initialize clear map.
     */
    public void initializeClearMap() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = 0;
            }
        }
        draw();
    }


    /**
     * Change cell state.
     *
     * @param x the x
     * @param y the y
     */
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

    /**
     * Set cell alive.
     *
     * @param x the x
     * @param y the y
     */
    public void setCellAlive(int x, int y){
        int row = x / cellSize;
        int col = y / cellSize;
        map[row][col] = 1;
        draw();
    }
}
