package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * The data, 2D array, is sent as an Object. Inside this class is a 2D array.
 */
class GolContent implements Serializable {
    /**
     * The Map.
     */
    public int[][] map;
}

/**
 * Client class is used to create a TCP connection to the server and
 * send and receive data (mainly a 2d array) from and to server.
 */
public class Client {
    /**
     * The constant PORT.
     */
    private static int PORT = 7090;
    /**
     * The constant host.
     */
    private static InetAddress host;
    /**
     * The Content.
     */
    private GolContent content;
    /**
     * The Socket.
     */
    public Socket socket;
    /**
     * The Oin.
     */
    private ObjectInputStream oin;
    /**
     * The Oout.
     */
    private ObjectOutputStream oout;

    /**
     * Instantiates a new Client. Calls method connect().
     */
    public Client(){
        connect();
    }

    /**
     * This method tries to connect to the server once.
     */
    public void connect(){
        try {
            System.out.println("Connecting to server");
            host = InetAddress.getLocalHost();
            this.socket = new Socket(host, PORT);
            this.oout = new ObjectOutputStream(socket.getOutputStream());
            this.oin = new ObjectInputStream(socket.getInputStream());
        } catch (ConnectException c){
            c.printStackTrace();
            System.out.println("Trying again");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection to server failed");
        }
    }

    /**
     * Closes the connection to the server by creating an instance of a 2D array with a single item '-1'.
     * The server gets this message and knows that the client wants to disconnect.
     */
    public void close(){
        try {
            content = new GolContent();
            content.map = new int[1][1];
            content.map[0][0] = -1;
            oout.writeObject(content);
            oout.flush();
            this.oout.close();
            this.oin.close();
            this.socket.close();
            System.out.println("Disconnected to the server");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends 2D array inside a serializable object to the server and receives the same type of object.
     * Returns the new 2D Array.
     *
     * @param map the map
     * @return the int [ ] [ ]
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public int[][] sendAndReceiveMap(int[][] map) throws IOException, ClassNotFoundException {
        content = new GolContent();
        this.content.map = map;

        oout.writeObject(content);
        oout.flush();

        content = (GolContent) oin.readObject();
        return content.map;
    }
}
