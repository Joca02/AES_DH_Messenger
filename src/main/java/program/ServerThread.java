package program;

import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    TextArea textArea;


   static ArrayList<BufferedReader> ins;
    static ArrayList<PrintWriter> outs;
    ArrayList<Socket>sockets;
    int clientID;
    static ArrayList<String>messages;
    static {
        messages=new ArrayList<>();
    }
    public ServerThread(int clientID,ArrayList<Socket>sockets,TextArea textArea,ArrayList<BufferedReader> inputs,ArrayList<PrintWriter> outputs) throws IOException {
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
                String message=ins.get(clientID).readLine();
                textArea.appendText(message+"\n");
                for(int i=0;i<sockets.size();i++)
                {
                    if(i!=clientID)
                    {
                        outs.get(i).println(message+"\n");
                    }
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
