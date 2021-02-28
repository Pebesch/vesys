package ch.fhnw.ds.networking.close;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// Client which opens a connection which is then closed using s.close
public class CloseClient1 {

	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 55555;

		Socket s = new Socket(host, port, null, 0);
		System.out.println("connected to " + s.getRemoteSocketAddress());

		confirm("confirm close");
		s.close();

		System.out.println("connection closed.");
		
		confirm("confirm program end");
	}
	
	private static void confirm(String prompt) throws IOException {
		System.out.println(prompt);
		new BufferedReader(new InputStreamReader(System.in)).readLine();
	}
}
