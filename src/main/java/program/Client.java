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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class Client extends Application implements Serializable{

    /*class ClientKey implements Serializable{
       public String clientName;
        public int key;
       public ClientKey()
       {
            key= new SecureRandom().nextInt(3,100);
            clientName=Client.this.clientName;
       }
    }*/
    public String clientName;
    TextArea textArea;
    private  InetAddress adress;
    private  Socket socket;
    private  ObjectOutputStream out;
    private  ObjectInputStream in;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene=initScene();
        primaryStage.setScene(scene);
        adress=InetAddress.getLocalHost();
        socket=new Socket(adress,9000);

        out = new ObjectOutputStream(socket.getOutputStream());
        //in = new ObjectInputStream(socket.getInputStream());

        final ClientKey clientKey = new ClientKey(clientName);
        //predaje svoje ime i kljuc
        new Thread(()->{
            try {

                System.out.println(clientKey.clientName+" "+clientKey.key);

                out.writeObject(clientKey);
                out.flush();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();



        new Thread(()->{
            try {
                 in=new ObjectInputStream(socket.getInputStream());

                while (true) {

                    Message receivedMessage = (Message) in.readObject();
                    receivedMessage.decrypt();
                    String finalReceivedMessage=receivedMessage.toString();
                    //javaFX metoda koja azurira UI elemente iz sporedne niti
                    Platform.runLater(() -> textArea.appendText(finalReceivedMessage + "\n"));
                    //in.close();
                }
            } catch (IOException | ClassNotFoundException e) {
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
        textArea.setPrefHeight(300);
        textArea.setEditable(false);

        sendButton.setOnAction(e -> {
            String message = textField.getText();
            if(message!=null && !message.trim().isEmpty())
            {
                textArea.appendText("You: " + message + "\n");
                try {
                    Message messageObj=new Message(message,clientName);
                    out.writeObject(messageObj);
                    out.flush();


                } catch (NoSuchPaddingException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalBlockSizeException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                } catch (BadPaddingException ex) {
                    throw new RuntimeException(ex);
                } catch (InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                textField.clear();
            }

        });


        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(paneSendMsg, new Insets(20, 0, 0, 0));
        Label lblName=new Label(clientName);

        layout.getChildren().addAll(lblName, paneSendMsg, sendButton, textArea);




        Scene scene = new Scene(layout, SCENE_WIDTH, SCENE_HEIGHT);

        //STYLES
        lblName.setStyle("-fx-font-size: 35; -fx-font-family: 'Arial'; -fx-text-fill: #b06060; -fx-font-weight: bold; -fx-underline: true;");
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
