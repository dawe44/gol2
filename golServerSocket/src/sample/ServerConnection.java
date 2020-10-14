package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;


/**
 * This is the serializable object that is sent forth and back.
 */
class GolContent implements Serializable{
    public int[][] map;
}

/**
 * This class runs a connection on a separate thread. This class first accepts
 * a serializable object, changes it, then sends it back to the client.
 * When client disconnects it is removed from the arraylist of connections
 * through the super reference.
 */
public class ServerConnection extends Thread {
    /**
     * The Socket.
     */
    private Socket socket;
    /**
     * The Server.
     */
    private Server server;
    /**
     * The Oin.
     */
    private ObjectInputStream oin;
    /**
     * The Oout.
     */
    private ObjectOutputStream oout;
    /**
     * The Running.
     */
    private boolean running = true;
    /**
     * The Content.
     */
    private GolContent content = new GolContent();


    /**
     * Receives the socket and super reference.
     *
     * @param socket the socket
     * @param server the server
     */
    public ServerConnection(Socket socket, Server server){
        super("ServerConnectionThread");
        this.socket = socket;
        this.server = server;
    }

    /**
     * This thread first creates I/O object streams. Then in a while loop
     * receives and sends object, which contains a 2D array. If the array[0][0]
     * contains -1 it means that the client wants to disconnect and the code
     * breaks out of the loop and closes the connection. calculateNewMap() is
     * called in the while loop.
     */
    public void run(){
        try {
            oin = new ObjectInputStream(socket.getInputStream());
            oout = new ObjectOutputStream(socket.getOutputStream());

            while (running) {
                content = (GolContent) oin.readObject();
                if(content.map[0][0] == -1) break;

                int[][] nextMap = new int[content.map.length][content.map[0].length];
                calculateNewMap(nextMap);

                oout.writeObject(content);
                oout.flush();
            }
            closeConnection();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls checkEachCell() with nextMap as reference then sets
     * the 2D array map equals to nextMap.
     * @param nextMap
     */
    private void calculateNewMap(int[][] nextMap) {
        checkEachCell(nextMap);
        content.map = nextMap;
    }

    /**
     * Goes through every single cell by summing upp the neighbours
     * with countNeighbours() and deciding the state of cell
     * with decideStateOfCell(). This happens with a for loop in a for loop.
     * @param nextMap
     */
    private void checkEachCell(int[][] nextMap) {
        int sumNeighbours;
        for (int i = 0; i < content.map.length; i++) { //Rows
            for (int j = 0; j < content.map[0].length; j++) { //Cols
                sumNeighbours = countNeighbours(i, j);
                decideStateOfCell(nextMap, sumNeighbours, i, j);
            }
        }
    }

    /**
     * Decides if a cell should stay alive or dead based on the sum of neighbours.
     * If the sum of neighbours differ from the if statements the cell keeps the old
     * state.
     * @param nextMap
     * @param sumNeighbours
     * @param i
     * @param j
     */
    private void decideStateOfCell(int[][] nextMap, int sumNeighbours, int i, int j) {
        if (sumNeighbours == 3) {
            nextMap[i][j] = 1;
        } else if ((sumNeighbours < 2) || (sumNeighbours > 3)) {
            nextMap[i][j] = 0;
        } else {
            nextMap[i][j] = content.map[i][j];
        }
    }

    /**
     * First checks if a cell is
     * @param i
     * @param j
     * @return
     */
    private int countNeighbours(int i, int j) {
        int sum = 0;
        int istart, iend, jstart, jend;

        if(i == 0){ // Uggly code that checks if either x or y in (rows, cols):(x,y) is in a corner
            istart = 0;
        }else{
            istart = -1;
        }

        if(i == content.map.length -1){
            iend = 0;
        }
        else {
            iend = 1;
        }

        if(j == 0){
            jstart = 0;
        }else{
            jstart = -1;
        }

        if(j == content.map[0].length -1){
            jend = 0;
        }
        else {
            jend = 1;
        }

        for(int ni = istart; ni <= iend; ni++){
            for(int mj = jstart; mj <= jend; mj++){
                sum += content.map[i + ni][j + mj];
            }
        }
        sum -= content.map[i][j];
        return sum;
    }


    /**
     * Closes connection and removes the connection from the arraylist through
     * super reference created in the constructor.
     *
     * @throws IOException the io exception
     */
    public void closeConnection() throws IOException {
        oin.close();
        oout.close();
        socket.close();
        server.connections.remove(this);
        System.out.println(socket + " has been disconnected and was removed from the connection list!");
    }
}
