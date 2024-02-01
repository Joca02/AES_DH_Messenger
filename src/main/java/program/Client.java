package program;

import javafx.application.Application;
import javafx.application.Platform;
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

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Client extends Application {

    public String clientName;
    TextArea textArea;
    private  InetAddress adress;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void start(Stage primaryStage) throws Exception {

        adress=InetAddress.getLocalHost();
        socket=new Socket(adress,9000);
        in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);

        Scene scene=initScene();
        primaryStage.setScene(scene);

        new Thread(()->{
            try {
                 String receivedMessage ;
                while ((receivedMessage  = in.readLine()) != null) {
                    String finalReceivedMessage = receivedMessage;
                    //javaFX metoda koja azurira UI elemente iz sporedne niti
                    Platform.runLater(() -> textArea.appendText(finalReceivedMessage + "\n"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        primaryStage.show();

    }


    private Scene initScene()
    {
         final int SCENE_WIDTH=600;
         final int SCENE_HEIGHT=500;
        Label label = new Label("Enter your message:");

        TextField textField = new TextField();

        textField.setPrefWidth(300);
        HBox paneSendMsg=new HBox(10,label,textField);
        paneSendMsg.setAlignment(Pos.TOP_CENTER);
        Button sendButton = new Button("Send");

        textArea = new TextArea();


        sendButton.setOnAction(e -> {
            String message = textField.getText();
            if(message!=null && !message.trim().isEmpty())
            {
                textArea.appendText("You: " + message + "\n");
                out.println("["+clientName+"]: "+message);
                textField.clear();
            }

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

        return scene;


    }
    @Override
    public void stop() throws Exception {
        in.close();
        out.close();
        socket.close();
        super.stop();
    }


}
