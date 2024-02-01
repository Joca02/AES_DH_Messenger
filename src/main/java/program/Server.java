package program;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    ArrayList<BufferedReader>ins;
    ArrayList<PrintWriter>outs;
    ArrayList<Socket>sockets;
    ServerSocket serverSocket;
    TextArea textArea;
    static int counter=0;
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Server");
        Scene scene=initScene();
        stage.setScene(scene);
        stage.show();

        serverSocket=new ServerSocket(9000);
        ins=new ArrayList<>();
        outs=new ArrayList<>();
        sockets=new ArrayList<>();
        new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    sockets.add(clientSocket);
                    ins.add(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                    outs.add(new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true));
                    ServerThread serverThread = new ServerThread(counter++,sockets, textArea, ins, outs);
                    serverThread.start();
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }).start();
    }

    private Scene initScene()
    {
        textArea=new TextArea("Server is running...\n");
        textArea.setStyle("-fx-font-size: 15");
        VBox layout=new VBox(textArea);
        layout.setFillWidth(true);// Allow VBox to grow and fill the available width
        VBox.setVgrow(textArea, Priority.ALWAYS);
        layout.setStyle("-fx-padding: 0;"); // Remove any padding


        return new Scene(layout,300,300);
    }

    @Override
    public void stop() throws Exception {
        for (int i = 0; i < sockets.size(); i++) {
            sockets.get(i).close();
            ins.get(i).close();
            outs.get(i).close();
        }
        super.stop();
    }
}
