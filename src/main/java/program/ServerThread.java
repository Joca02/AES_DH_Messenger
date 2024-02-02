package program;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import java.io.*;
import java.net.Socket;

import java.util.ArrayList;

public class ServerThread extends Thread {
    TextArea textArea;


   static ArrayList<ObjectInputStream> ins;
    static ArrayList<ObjectOutputStream> outs;
    ArrayList<Socket>sockets;
    int clientID;

    public ServerThread(int clientID,ArrayList<Socket>sockets,TextArea textArea,ArrayList<ObjectInputStream> inputs,ArrayList<ObjectOutputStream> outputs) throws IOException {
        this.clientID=clientID;
        this.textArea=textArea;

        this.sockets=sockets;
        if(ins==null&&outs==null)
        {
            ServerThread.ins =inputs;
            ServerThread.outs=outputs;
        }

    }

    @Override
    public void run() {
        try {
            while (true)
            {
                Object receivedObject = ins.get(clientID).readObject();
                if(receivedObject instanceof Message)
                {
                    Message message = (Message) receivedObject;
                    message.message=message.encrypt();
                    Platform.runLater(() -> textArea.appendText(message.toString() + "\n"));
                    message.sendPublicKeys(Server.clientsKeys);
                    for(int i=0;i<sockets.size();i++)
                    {
                        if(i!=clientID)
                        {

                            outs.get(i).writeObject(message);
                            outs.get(i).flush();
                        }
                    }
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
    }
}
