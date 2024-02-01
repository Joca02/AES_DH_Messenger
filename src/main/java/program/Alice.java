package program;

import javafx.stage.Stage;

public class Alice extends Client{
    @Override
    public void start(Stage primaryStage) throws Exception {

        clientName="Alice";
        primaryStage.setTitle(clientName);
        super.start(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
