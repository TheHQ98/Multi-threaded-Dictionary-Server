/**
 * Client side server logic
 * functions include:
 *                  connect to server
 *                  disconnect to server
 *                  send message to server
 *                  receive message from server
 *
 *
 * @author Josh Feng, 1266669, chenhaof@student.unimelb.edu.au
 * @date 4 April 2024
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientServer {
    private String ip;
    private int port;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    public ClientServer (String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    // connect to the server, use default TCP
    public void connect() {
        try {
            socket = new Socket(ip, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // disconnect to the server
    public void disconnect() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // send message to the server
    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // receive message from server
    public String receiveMessage() {
        String message = null;
        try {
            message = inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }

    // check if is connected to the server
    public boolean checkServer() {
        return (socket != null) && socket.isConnected() && !socket.isClosed();
    }
}
