/**
 * Client side main class
 * Must include arguments: <server-address> <server-port>
 *
 * It will check the arguments before start the GUI
 *
 * @author Josh Feng, 1266669, chenhaof@student.unimelb.edu.au
 * @date 4 April 2024
 */

public class Main {
    private static String ip;
    private static int port;

    public static void main(String[] args) {
        // check arguments
        if (args.length < 2) {
            System.out.println(Params.ARGUMENTS_ERROR);
            return;
        }

        // try bind server address and port number
        ip = args[0];
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println(Params.PORT_ERROR + args[1]);
            return;
        }

        // create a client server and start GUI
        ClientServer clientServer = new ClientServer(ip, port);
        new GUI(clientServer);
    }

}
