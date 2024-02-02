package program;

import javafx.stage.Stage;

import java.io.Serializable;
import java.security.SecureRandom;

public class Bob extends Client  {
    @Override
    public void start(Stage primaryStage) throws Exception {

        clientName="Bob";

       // Message.clientsKeys.put(clientName,new SecureRandom().nextInt());
        primaryStage.setTitle(clientName);
        super.start(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
