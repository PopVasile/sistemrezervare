
package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    
    public static void main(String args[]) throws Exception
	{
            BufferedReader sin;
            PrintStream sout;
            BufferedReader stdin;
        try (Socket sk = new Socket("188.27.145.130",5000)) {
            sin = new BufferedReader(new InputStreamReader(sk.getInputStream()));
            sout = new PrintStream(sk.getOutputStream());
            stdin = new BufferedReader(new InputStreamReader(System.in));
            String s;
            while (  true )
            {
                s=sin.readLine();
                System.out.print("Server : "+s+"\n");
                s=stdin.readLine();
                sout.println(s);
                if ( s.equalsIgnoreCase("BYE") )
                {
                    System.out.println("Connection ended by client");
                    break;
                }
            }
        }
		 sin.close();
		 sout.close();
 		stdin.close();
	}
    
}