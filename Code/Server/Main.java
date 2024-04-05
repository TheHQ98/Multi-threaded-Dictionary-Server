/**
 * Server side main class
 * Must include arguments: <port> <dictionary-file>
 *
 * It will check the arguments, files, and start gui, then start server
 *
 * @author Josh Feng, 1266669, chenhaof@student.unimelb.edu.au
 * @date 4 April 2024
 */

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

public class Main {
    private static int port;
    private static int numConnect = 0;
    private static Dictionary dictionary;
    private static GUI gui;

    public static void main(String[] args) {
        if (!checkArgs(args)) {return;}

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println(Params.PORT_ERROR + args[0]);
            return;
        }

        // load file
        dictionary = new Dictionary(args[1]);

        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        // start gui
        gui = new GUI(port, args[1]);
        gui.refreshContext(dictionary.printAll());

        // server try start, and listen
        try(ServerSocket server = factory.createServerSocket(port))
        {
            System.out.println(Params.SERVER_START);

            // wait for connections.
            while(true)
            {
                Socket client = server.accept();
                numConnect++;
                gui.addDialog("----- Request " + numConnect + " -----\n");

                // start a new thread for a connection
                RequestHandler requestHandler = new RequestHandler(client, dictionary, numConnect, args[1], gui);
                requestHandler.start();
            }
        }
        catch (BindException e) {
            System.out.println("Port " + port + " is already bind, server start fail.");
        }
        catch (IllegalArgumentException e) {
            System.out.println("Port number is outside the specified range of valid port values (0-65535): " + port);
        }
        catch (IOException e)
        {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    // check the arguments if it is correct
    public static boolean checkArgs(String[] args) {
        // check if arguments is provided
        if (args.length < 2) {
            System.out.println(Params.ARGS_ERROR);
            return false;
        }

        File file = new File(args[1]);
        // check file is exist
        if (!file.exists()) {
            System.out.println("File not exist: " + args[1]);
            return false;
        }
        // check file is JSON file format
        if (!args[1].endsWith(Params.FILE_FORMAT)) {
            System.out.println(Params.FILE_FORMAT_ERROR);
            return false;
        }
        return true;
    }
}