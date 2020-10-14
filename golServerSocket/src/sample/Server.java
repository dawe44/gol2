package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * This is the multi-threaded server class. The server listens for new connections all the time.
 * When a connection has been accepted it is added to a arraylist and the connection is ran on
 * a separate thread. After adding the connection the server keeps listening to new connections.
 */
public class Server {
    /**
     * The Serversocket.
     */
    private ServerSocket serversocket;
    /**
     * The Port.
     */
    private final int PORT = 7090;
    /**
     * The Connections.
     */
    public ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();
    /**
     * The Running.
     */
    private boolean running = true;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new Server();
    }

    /**
     * The server starts in the constructor. No other method was needed.
     *
     */
    public Server() {
        try {
            System.out.println("Server Listening.");
            serversocket = new ServerSocket(PORT);
            while(running) {
                Socket socket = serversocket.accept();
                System.out.println("Connected to " + socket + "!");
                ServerConnection serverconnection = new ServerConnection(socket, this);
                serverconnection.start();
                connections.add(serverconnection);
            }
            serversocket.close();

        } catch (IOException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }
}
