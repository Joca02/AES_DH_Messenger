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
import java.util.HashMap;
import java.util.Map;

public class Server extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static Map<String, Integer> clientsKeys = new HashMap<>();
    private ArrayList<Socket> sockets;
    private ArrayList<ObjectInputStream> ins;
    private ArrayList<ObjectOutputStream> outs;
    private ServerSocket serverSocket;
    private TextArea textArea;
    private int counter = 0;
    private volatile boolean firstThreadCompleted = false;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Server");
        Scene scene = initScene();
        stage.setScene(scene);
        stage.show();

        serverSocket = new ServerSocket(9000);
        sockets = new ArrayList<>();
        ins = new ArrayList<>();
        outs = new ArrayList<>();

        // Smestam kljuceve u ovoj niti i kreiram streamove
        new Thread(() -> {
            try {
                for (int i = 0; i < 2; i++) {
                    Socket clientSocket = serverSocket.accept();
                    sockets.add(clientSocket);
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    ClientKey client = (ClientKey) in.readObject();
                    String name = client.clientName;
                    int cypherKey = client.key;
                    clientsKeys.put(name, cypherKey);


                    ins.add(in);
                    outs.add(out);

                }
                firstThreadCompleted = true;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //nit za razmenu poruka omogucena tek nakon sto su prosledjeni javni kljucevi
        new Thread(() -> {
            while (!firstThreadCompleted) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

                try {
                    ServerThread serverThread = new ServerThread(counter++, sockets, textArea, ins, outs);
                    serverThread.start();
                    ServerThread serverThread2 = new ServerThread(counter++, sockets, textArea, ins, outs);
                    serverThread2.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }).start();
    }

    private Scene initScene() {
        textArea = new TextArea("Server is running...\n");
        textArea.setEditable(false);
        textArea.setStyle("-fx-font-size: 15");
        VBox layout = new VBox(textArea);
        layout.setFillWidth(true);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        layout.setStyle("-fx-padding: 0;");

        return new Scene(layout, 300, 300);
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
