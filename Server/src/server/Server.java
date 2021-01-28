
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server {

    int port;
    ServerSocket server = null;
    Socket client = null;
    ExecutorService pool = null;
    int clientcount = 0;
    static int[] vector = new int[]{0, 0, 1, 0, 1, 1, 1, 0, 0, 0};

    public static void main(String[] args) throws IOException {
        Server serverobj = new Server(5000);
        serverobj.startServer();
    }

    Server(int port) {
        this.port = port;
        pool = Executors.newFixedThreadPool(5);
    }

    public void startServer() throws IOException {

        server = new ServerSocket(5000);
        System.out.println("Server Booted");
        System.out.println("Any client can stop the server by sending -1");
        while (true) {
            client = server.accept();
            clientcount++;
            ServerThread runnable = new ServerThread(client, clientcount, this);
            pool.execute(runnable);
        }

    }

    private static class ServerThread implements Runnable {

        Server server = null;
        Socket client = null;
        BufferedReader cin;
        PrintStream cout;
        Scanner sc = new Scanner(System.in);
        int id;
        String s;

        ServerThread(Socket client, int count, Server server) throws IOException {

            this.client = client;
            this.server = server;
            this.id = count;
            System.out.println("Connection " + id + "established with client " + client);

            cin = new BufferedReader(new InputStreamReader(client.getInputStream()));
            cout = new PrintStream(client.getOutputStream());

        }

        @Override
        public void run() {
            int x = 1;
            String menu = " formatul comenzii: [comanda][pozitie 0..9]\t"
                    + "comenzi accepptate: A - ask, R - reserve, Q - quit\t"
                    + "           exemplu: A2";
            cout.println(menu);
            try {
                while (true) {
                    String line;
                    while ((line = cin.readLine()) != null) {
                        if (line.length() == 2 || line.charAt(0) == 'q') {
                            switch (line.charAt(0)) {
                                case 'a':
                                case 'A':
                                    if (Character.isDigit(line.charAt(1))) {
                                        int index = Character.getNumericValue(line.charAt(1));
                                        if (vector[index] == 0) {
                                            cout.println("pozitia " + line.charAt(1) + " este libera");
                                        } else {
                                            cout.println("pozitia " + line.charAt(1) + " este ocupata");
                                        }
                                    } else {
                                        cout.println("** pozitie innexistenta **");
                                    }
                                    break;

                                case 'r':
                                case 'R':
                                    if (Character.isDigit(line.charAt(1))) {
                                        int index = Character.getNumericValue(line.charAt(1));
                                        if (vector[index] == 0) {
                                            cout.println("ai rezervat pozitia " + line.charAt(1));
                                            vector[index] = 1;
                                        } else {
                                            cout.println("pozitia " + line.charAt(1) + " este ocupata");
                                        }
                                    } else {
                                        cout.println("** pozitie innexistenta **");
                                    }
                                    break;
                                case 'q':
                                case 'Q':
                                    cout.println("bye");
                                    System.out.println("\nquit.");
                                    return;
                                //System.exit(0);
                            }
                        } else {
                            cout.println("** comanda incorecta **");
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error : " + ex);
            }

        }
    }

}
