/**
 * allocate a handler for each user
 *
 * @author Josh Feng, 1266669, chenhaof@student.unimelb.edu.au
 * @date 4 April 2024
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class RequestHandler extends Thread {
    private Socket clientSocket;
    private Dictionary dictionary;
    private int id;
    private String file;
    private GUI gui;

    // initial setup
    public RequestHandler(Socket clientSocket, Dictionary dictionary, int id, String file, GUI gui) {
        this.clientSocket = clientSocket;
        this.dictionary = dictionary;
        this.id = id;
        this.file = file;
        this.gui = gui;
    }

    @Override
    public void run() {
        try
        {
            // Input stream
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            // Output Stream
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            String quest = input.readUTF();

            // Check the request type
            if (quest.equals(Params.QUERY)) {  // ask for query a word
                String word = input.readUTF();
                gui.addDialog("Request " + id + ", query: " + word + "\n");
                String result = dictionary.queryWord(word);
                if (result == null) {
                    result = "Not found word: " + word;
                    gui.addDialog(result + "\n");
                } else {
                    gui.addDialog("Found word: " + word + "\n");
                }
                output.writeUTF(result);
            } else if (quest.equals(Params.ADD)) { // ask for add a word and meanings
                String word = input.readUTF();
                String meanings = input.readUTF();
                gui.addDialog("Request " + id + ", add word: " + word + "\n");
                String result = dictionary.addWord(word, meanings);
                gui.addDialog(result + "\n");
                dictionary.saveFile(file);
                gui.refreshContext(dictionary.printAll());
                output.writeUTF(result);
            } else if (quest.equals(Params.REMOVE)) { // ask for remove a word and meanings
                String word = input.readUTF();
                gui.addDialog("Request " + id + ", remove word: " + word + "\n");
                String result = dictionary.removeWord(word);
                gui.addDialog(result + "\n");
                dictionary.saveFile(file);
                gui.refreshContext(dictionary.printAll());
                output.writeUTF(result);
            } else if (quest.equals(Params.UPDATE)) { // ask for update an exist word and meanings
                String word = input.readUTF();
                String meanings = input.readUTF();
                gui.addDialog("Request " + id + ", update: " + word + "\n");
                String result = dictionary.updateWord(word, meanings);
                gui.addDialog(result + "\n");
                dictionary.saveFile(file);
                gui.refreshContext(dictionary.printAll());
                output.writeUTF(result);
            } else {   // other request will be shows error, normally never go to this condition
                String message = input.readUTF();
                System.out.println("ERROR: request id: " + id + ", sent unknown message: " + message);
            }

            input.close();
            output.close();
            clientSocket.close();
        }
        catch (EOFException e)
        {
            System.out.println("Request " + id + " has shut down accidentally");
        } catch (IOException e) {
            System.out.println("Other error: " + e);
        }
    }
}
