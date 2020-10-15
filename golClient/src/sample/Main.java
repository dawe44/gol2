package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

//All the javafx code occurs here, this is the only file with spaghetti code, please ignore this file
//I will clean this later

/**
 * Inside this main contains all code for JavaFX GUI.
 */
public class Main extends Application {
    /**
     * The constant CELLSIZE.
     */
    public static int CELLSIZE = 10, /**
     * The Width.
     */
    WIDTH = 800, /**
     * The Height.
     */
    HEIGHT = 600, /**
     * The Patternwidth.
     */
    PATTERNWIDTH = 400, /**
     * The Patternheight.
     */
    PATTERNHEIGHT = 400, /**
     * The Patterncellsize.
     */
    PATTERNCELLSIZE = 50;
    /**
     * The Count generation.
     */
    SimpleIntegerProperty countGeneration = new SimpleIntegerProperty(0);
    /**
     * The constant INIT_VALUE.
     */
//    public static final int ROWS = (int)Math.floor(WIDTH / CELLSIZE);
//    public static final int COLS = (int)Math.floor(HEIGHT / CELLSIZE);
    public static final double INIT_VALUE = 40;
    /**
     * The Graphics.
     */
    public GraphicsContext graphics;
    /**
     * The Graphics pattern.
     */
    public GraphicsContext graphicsPattern;
    /**
     * The Gol.
     */
    GameOfLife gol;
    /**
     * The New pattern class.
     */
    public mapNewPattern newPatternClass;
    public AnimationTimer runAnimation;
    /**
     * The Random.
     */
    public Random random = new Random();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();

        Canvas canvas = new Canvas(WIDTH , HEIGHT);
        Canvas canvasPattern = new Canvas(PATTERNWIDTH , PATTERNHEIGHT);

        graphics = canvas.getGraphicsContext2D();
        graphicsPattern = canvasPattern.getGraphicsContext2D();

        Menu file = new Menu("File"); // Create MenuBar
        MenuItem save = new MenuItem("Save");
        MenuItem close = new MenuItem("Close Window");
        close.setOnAction(e->primaryStage.close());

        Menu edit = new Menu("Edit");
        MenuItem configureMap = new MenuItem("Configure Map");

        newPatternClass = new mapNewPattern(PATTERNWIDTH, PATTERNHEIGHT, PATTERNCELLSIZE, graphicsPattern);
        ComboBox<String> comboBoxConfigurePatterns = new ComboBox<>();
        String[] listOfPatternNamesConfigure = newPatternClass.getListOfPatternNames();
        for(int i = 0; i < listOfPatternNamesConfigure.length; i++) {
            String tempString = listOfPatternNamesConfigure[i];
            comboBoxConfigurePatterns.getItems().add(tempString);
        }
        MenuItem newPattern = new MenuItem("Configure Patterns");
        Stage newFileStage = new Stage();
        VBox newPatternVBox = new VBox(10);
        newPatternVBox.setPadding(new Insets(10));
        newPatternVBox.setStyle("-fx-background-color: rgb(58, 80, 107);");
        newFileStage.setScene(new Scene(newPatternVBox, 410, 540));
        newFileStage.setTitle("Configure Patterns");
        Button resetPatternBtn = new Button("Reset map");
        Button savePatternBtn = new Button("Save Pattern");
        Button loadPatternBtn = new Button("Load Pattern");
        Button loadListOfPatternBtn = new Button("Load List");
        Button deletePatternBtn = new Button("Delete Selected Pattern");
        HBox newPatternBtnsHBox = new HBox(20);
        HBox newPatternBtnsHBox2 = new HBox(20);
        HBox newPatternBtnsHBox3 = new HBox(20);
        newPatternBtnsHBox.getChildren().addAll(resetPatternBtn);
        newPatternBtnsHBox2.getChildren().addAll(loadListOfPatternBtn, comboBoxConfigurePatterns, loadPatternBtn);
        newPatternBtnsHBox3.getChildren().addAll(savePatternBtn, deletePatternBtn);
        newPatternVBox.getChildren().addAll(canvasPattern, newPatternBtnsHBox ,newPatternBtnsHBox2, newPatternBtnsHBox3);
        newPattern.setOnAction(e -> newFileStage.show());
        newPatternClass.draw();
        Button saveToFile = new Button("Save to File");

        TextField patternTextField = new TextField("Name of the pattern");
        VBox writeToFileVBox = new VBox(10);
        writeToFileVBox.setAlignment(Pos.CENTER);
        writeToFileVBox.getChildren().addAll(patternTextField, saveToFile);
        Stage writeToFileStage = new Stage();
        writeToFileStage.setScene(new Scene(writeToFileVBox, 300, 100));
        savePatternBtn.setOnAction(e -> writeToFileStage.show());
        newFileStage.resizableProperty().setValue(false);

        file.getItems().addAll(newPattern, save, close);

        Text txtWidth = new Text("Width:  ");
        TextField widthField = new TextField();
        Text txtHeight = new Text("Height: ");
        TextField heightField = new TextField();
        HBox hboxWidth = new HBox(10);
        HBox hboxHeight = new HBox(10);
        hboxHeight.setPadding(new Insets(10));
        hboxWidth.setPadding(new Insets(10));
        hboxWidth.getChildren().addAll(txtWidth, widthField);
        hboxHeight.getChildren().addAll(txtHeight, heightField);
        VBox vboxWidghHeight = new VBox(10);
        Button setWidthAndHeightBtn = new Button("Click to set the Width and Height");
        setWidthAndHeightBtn.setPadding(new Insets(10));
        vboxWidghHeight.getChildren().addAll(hboxWidth, hboxHeight, setWidthAndHeightBtn);
        vboxWidghHeight.setPadding(new Insets(10, 10, 10, 10));
        Stage mapConfigStage = new Stage();
        mapConfigStage.setTitle("Configurations for Map");
        mapConfigStage.setScene(new Scene(vboxWidghHeight, 250, 175));
        mapConfigStage.resizableProperty().setValue(false);

        MenuItem configureProbability = new MenuItem("Set probability");
        Text probabilityTxt = new Text("Choose probability for each cell (0-100): ");

        TextField probabilityField = new TextField();
        probabilityField.setPadding(new Insets(10));
        Button setProbabilityBtn = new Button("Set value");
        setProbabilityBtn.setPadding(new Insets(10));
        VBox probVbox = new VBox(10);
        probVbox.setPadding(new Insets(10));
        probVbox.getChildren().addAll(probabilityTxt, probabilityField, setProbabilityBtn);
        Stage probConfigStage = new Stage();
        probConfigStage.setTitle("Set probability");
        probConfigStage.setScene(new Scene(probVbox, 250, 175));
        probConfigStage.resizableProperty().setValue(false);
        configureProbability.setOnAction(e -> probConfigStage.show());

        MenuItem configureCellSize = new MenuItem("Change the Size of a Cell");
        Text txtChangeCellSize = new Text("Cellsize: ");
        TextField cellSizeTxtField = new TextField();
        HBox hboxCellSize = new HBox(10);
        Button setCellSizeBtn = new Button("Set CellSize");
        HBox hBoxCellSize2 = new HBox(10);
        hBoxCellSize2.getChildren().addAll(setCellSizeBtn);
        VBox vboxCellSize = new VBox(10);

        vboxCellSize.getChildren().addAll(hboxCellSize, hBoxCellSize2);
        Stage stageCellSize = new Stage();
        stageCellSize.setTitle("Set CellSize");
        stageCellSize.setScene(new Scene(vboxCellSize, 250, 140));
        stageCellSize.resizableProperty().setValue(false);
        hBoxCellSize2.setPadding(new Insets(10));
        hboxCellSize.setPadding(new Insets(10));
        hboxCellSize.getChildren().addAll(txtChangeCellSize, cellSizeTxtField);
        configureCellSize.setOnAction(e -> stageCellSize.show());

        edit.getItems().addAll(configureMap, configureCellSize, configureProbability);


        MenuItem configureConnection = new MenuItem("Check connection status");
        Menu connection = new Menu("Check connection");
        connection.getItems().addAll(configureConnection);
        Text txtCheckConnection = new Text("Connection status: ");
        Text txtConnectionStatus = new Text("Status unknown.");
        HBox hboxCheckConnection = new HBox(10);
        hboxCheckConnection.getChildren().addAll(txtCheckConnection, txtConnectionStatus);
        hboxCheckConnection.setPadding(new Insets(10));
        Button checkConnectionBtn = new Button("Check connection.");
        HBox hboxCheckConnectionStatus = new HBox(10);
        hboxCheckConnectionStatus.getChildren().addAll(checkConnectionBtn);
        hboxCheckConnectionStatus.setPadding(new Insets(10));
        Button connectBtn = new Button("Connect to server.");
        HBox hboxConnect = new HBox(10);
        hboxConnect.getChildren().addAll(connectBtn);
        hboxConnect.setPadding(new Insets(10));
        VBox vboxCheckConnection = new VBox(10);
        vboxCheckConnection.getChildren().addAll(hboxCheckConnection, hboxConnect, hboxCheckConnectionStatus);
        Stage stageCheckConnection = new Stage();
        stageCheckConnection.setTitle("Check connection");
        stageCheckConnection.setScene(new Scene(vboxCheckConnection, 250, 140));
        stageCheckConnection.resizableProperty().setValue(false);
        configureConnection.setOnAction(e -> stageCheckConnection.show());

        Menu help = new Menu("Help");

        MenuBar menubar = new MenuBar();
        menubar.getMenus().addAll(file, edit, help, connection);

        Button startBtn = new Button("Start"); //Create Bottom HBox
        Button pauseBtn = new Button("Pause");
        Button resetRandomBtn = new Button("Reset and Randomize Map");
        Button resetClearBtn = new Button("Reset and Clear Map");
        Button nextGenBtn = new Button("Next Gen");

        Text countTxt = new Text();
        countTxt.textProperty().bind(countGeneration.asString());
        countTxt.setFill(Color.rgb(111, 255, 233));
        Font textFont = new Font(18).font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18);
        countTxt.setFont(textFont);
        Text genTxt = new Text("Generation:");
        genTxt.setFill(Color.rgb(111, 255, 233));
        genTxt.setFont(textFont);

        TextField chooseGen = new TextField();
        chooseGen.setPrefWidth(60);
        Button chooseGenBtn = new Button("Skip Generation");

        HBox hbox1 = new HBox(10);
        hbox1.setPadding(new Insets(10, 0, 10, 10));
        hbox1.getChildren().addAll(startBtn, pauseBtn, nextGenBtn, genTxt, countTxt, chooseGen, chooseGenBtn);

        Slider slider = new Slider();
        slider.setValue(INIT_VALUE);
        slider.setMin(15);
        slider.setMax(500);
        TextField speedTxt = new TextField();
        speedTxt.setPrefWidth(60);
        speedTxt.textProperty().bindBidirectional(slider.valueProperty(), NumberFormat.getNumberInstance());

        HBox hbox2 = new HBox(10);
        hbox2.getChildren().addAll(slider, speedTxt, resetRandomBtn, resetClearBtn);
        hbox2.setPadding(new Insets(10));
        HBox hbox3 = new HBox(10);
        ComboBox<String> comboBoxPatterns = new ComboBox<>();
        String[] listOfPatternNames = newPatternClass.getListOfPatternNames();
        for(int i = 0; i < listOfPatternNames.length; i++) {
            String tempString = listOfPatternNames[i];
            comboBoxPatterns.getItems().add(tempString);
        }

        Button buttonLoadPattern = new Button("Load Pattern");
        Button buttonReloadListOfPatterns = new Button("Reload list");
        hbox3.getChildren().addAll(buttonReloadListOfPatterns, comboBoxPatterns, buttonLoadPattern);
        hbox3.setPadding(new Insets(10));

        VBox vbox = new VBox();
        VBox vboxAllHbox = new VBox();
        vboxAllHbox.getChildren().addAll(hbox3, hbox2, hbox1);
        vbox.getChildren().addAll(canvas, vboxAllHbox);
        //vbox.setStyle("-fx-background-color: rgb(58, 80, 107);");
        vbox.setPadding(new Insets(10, 10, 10, 10));

        VBox vboxAll = new VBox();
        vboxAll.getChildren().addAll(menubar, vbox);
        vboxAll.setStyle("-fx-background-color: rgb(58, 80, 107);");
        root.getChildren().addAll(vboxAll);
        System.out.println(random.nextInt(100));

        gol = new GameOfLife(WIDTH, HEIGHT, CELLSIZE, graphics);
        gol.initializeRandomMap();

        primaryStage.setTitle("My Conway's Game of Life");
        primaryStage.setScene(new Scene(root));
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();

        runAnimation = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if ((now - lastUpdate) >= TimeUnit.MILLISECONDS.toNanos((long) slider.getValue())) {
                    try {
                        gol.update();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    countGeneration.setValue(countGeneration.getValue() + 1);
                    lastUpdate = now;
                }
            }
        };
        configureMap.setOnAction(e -> {
            mapConfigStage.show();
            runAnimation.stop();
        });

        startBtn.setOnAction(e-> runAnimation.start());
        pauseBtn.setOnAction(e-> runAnimation.stop());
        resetRandomBtn.setOnAction(e-> {
            countGeneration.setValue(0);
            gol.initializeClearMap();
            gol.initializeRandomMap();
            runAnimation.stop();
        } );
        resetClearBtn.setOnAction(e->{
            countGeneration.setValue(0);
            gol.initializeClearMap();
            runAnimation.stop();
        });
        nextGenBtn.setOnAction(e -> {
            countGeneration.setValue(countGeneration.getValue() + 1);
            try {
                gol.update();
            } catch (ClassNotFoundException | IOException ex) {
                ex.printStackTrace();
            }
        });
        chooseGenBtn.setOnAction(e-> {
            System.out.println(Integer.parseInt(chooseGen.getText()));
            try {
                gol.update(Integer.parseInt(chooseGen.getText()));
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            countGeneration.setValue(countGeneration.getValue() + Integer.parseInt(chooseGen.getText()));
        });

        setWidthAndHeightBtn.setOnAction(e -> {
            if (Integer.parseInt(widthField.getText()) < 100 || Integer.parseInt(heightField.getText()) < 100){
                AlertBox.display("Values too low", "The value for Width and Height must be over 100.");
            }else if(Integer.parseInt(widthField.getText()) > 1400 || Integer.parseInt(heightField.getText()) > 800){
                AlertBox.display("Values too high", "The value must be lower than 1400 for width and 800 for height.");
            }
            else {
                gol.changeMapSize(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
                canvas.setWidth(Double.parseDouble(widthField.getText()));
                canvas.setHeight(Double.parseDouble(heightField.getText()));
                if(Integer.parseInt(widthField.getText()) > 534){
                    primaryStage.setWidth(canvas.getWidth() + 36);
                }else {
                    primaryStage.setWidth(534 + 36);
                }
                primaryStage.setHeight(canvas.getHeight() + vboxAllHbox.getHeight() + 84);
                countGeneration.setValue(0);
                gol.draw();
            }
        });

        setCellSizeBtn.setOnAction(e -> {
            gol.changeCellSize(Integer.parseInt(cellSizeTxtField.getText()));
        });

        canvas.setOnMousePressed(e -> {
            int x = (int)e.getX();
            int y = (int)e.getY();
            gol.changeCellState(x, y);
        });

        canvas.setOnMouseDragged(e -> {
            int x = (int)e.getX();
            int y = (int)e.getY();
            gol.setCellAlive(x, y);
        });

        canvasPattern.setOnMousePressed(e -> {
            int x = (int)e.getX();
            int y = (int)e.getY();
            newPatternClass.changeCellState(x, y);
        });

        canvasPattern.setOnMouseDragged(e -> {
            int x = (int)e.getX();
            int y = (int)e.getY();
            newPatternClass.setCellAlive(x, y);
        });

        resetPatternBtn.setOnAction(e -> {
            newPatternClass.initializeClearMap();
        });

        checkConnectionBtn.setOnAction(e -> {
            if(!gol.client.socket.isConnected()){
                txtConnectionStatus.setText("Client is NOT connected.");
            }
            else {
                txtConnectionStatus.setText("Client is connected.");
            }
        });

        connectBtn.setOnAction(e -> {
            if(!gol.client.socket.isConnected()) {
                gol.client.connect();
            }
        });

        buttonReloadListOfPatterns.setOnAction(e ->{
            reloadList(comboBoxPatterns);
        });

        loadListOfPatternBtn.setOnAction(e -> {
            reloadList(comboBoxConfigurePatterns);
        });

        saveToFile.setOnAction(e -> {
            try {
                if(newPatternClass.doesPatternExist(patternTextField.getText())){
                    AlertBox.display("Warning, can't save", "WARNING! The pattern already exists in the file!");
                }else {
                    newPatternClass.saveToFile(patternTextField.getText());
                    reloadList(comboBoxConfigurePatterns);
                    AlertBox.display("Successful save", "The pattern has been saved to the file.");
                }
            } catch (TransformerException ex) {
                ex.printStackTrace();
            }
        });


        deletePatternBtn.setOnAction(e -> {
            try{
                if(!newPatternClass.doesPatternExist(comboBoxConfigurePatterns.getValue())){
                    AlertBox.display("Message", "Message: The pattern does not exists in the file.");
                }
                else {
                    newPatternClass.removePatternFromXML(comboBoxConfigurePatterns.getValue());
                    reloadList(comboBoxConfigurePatterns);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        loadPatternBtn.setOnAction(e -> {
            newPatternClass.setMap(newPatternClass.getMapFromXML(comboBoxConfigurePatterns.getValue()));
            newPatternClass.draw();
        });

        buttonLoadPattern.setOnAction(e -> {
            runAnimation.stop();
            countGeneration.setValue(0);
            int map[][] = newPatternClass.getMapFromXML(comboBoxPatterns.getValue());
            gol.setPatternToMap(map);
        });

        newFileStage.setOnCloseRequest(e -> {
            reloadList(comboBoxPatterns);
        });

        setProbabilityBtn.setOnAction(e -> {
            if(Integer.parseInt(probabilityField.getText()) <= 100 && Integer.parseInt(probabilityField.getText()) >= 0) {
                gol.setProbability(Integer.parseInt(probabilityField.getText()));
                AlertBox.display("Success", "Probability has changed");
            }else{
                AlertBox.display("Fail", "Write value between 0-100");
            }
        });

        System.out.println(random.nextInt(100));
    }

    /**
     * Used to reload list of patterns from the XML document.
     * @param combobox
     */
    private void reloadList(ComboBox<String> combobox) {
        combobox.getItems().clear();
        var stringList = newPatternClass.getListOfPatternNames();
        for (String tempString : stringList) {
            combobox.getItems().add(tempString);
        }
    }

    /**
     * Disconnects from the server as soon as client is closed.
     */
    @Override
    public void stop(){
        runAnimation.stop();
        gol.client.close();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
