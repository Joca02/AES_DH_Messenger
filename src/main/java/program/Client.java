package program;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;


public class Client extends Application {

    private final int SCENE_WIDTH=600;
    private final int SCENE_HEIGHT=500;

    @Override
    public void start(Stage primaryStage) throws Exception {


        Label label = new Label("Enter your message:");

        TextField textField = new TextField();

        textField.setPrefWidth(300);
        HBox paneSendMsg=new HBox(10,label,textField);
        paneSendMsg.setAlignment(Pos.TOP_CENTER);
        Button sendButton = new Button("Send");

        TextArea textArea = new TextArea();


        sendButton.setOnAction(e -> {
            String message = textField.getText();
            textArea.appendText("You: " + message + "\n");
            textField.clear();
        });


        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(paneSendMsg, new Insets(20, 0, 0, 0));

        layout.getChildren().addAll(paneSendMsg, sendButton, textArea);




        Scene scene = new Scene(layout, SCENE_WIDTH, SCENE_HEIGHT);

        //STYLES
        label.setStyle("-fx-font-size: 14;");
        textField.setStyle("-fx-font-size: 14;");
        sendButton.getStyleClass().addAll("btn", "btn-primary");
        sendButton.setStyle("-fx-pref-width:"+SCENE_WIDTH+";");
        textArea.setStyle("-fx-font-size: 15;");
        layout.getStyleClass().add("container");
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());



        primaryStage.setScene(scene);

        primaryStage.show();

    }



}
