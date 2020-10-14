package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This is a class with a static method. The method creates an AlertBox on the GUI when needed.
 */
public class AlertBox {

    /**
     * Pops a new window with title and message through the parameter.
     *
     * @param title   the title
     * @param message the message
     */
    public static void display(String title, String message){
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(350);

        Label label = new Label(message);
        Button button = new Button("Ok");
        button.setOnAction(e -> stage.close());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label, button);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }
}
