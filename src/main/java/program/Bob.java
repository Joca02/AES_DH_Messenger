package program;

import javafx.stage.Stage;

public class Bob extends Client{
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Bob");
        super.start(primaryStage);


    }

    public static void main(String[] args) {
        launch(args);
    }
}
