package ch.fhnw.ds.networking.close;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CloseServer {

	public static void main(String[] args) throws IOException {
		int port = 55555;
		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("Startet Echo Server on port " + port);
			while (true) {
				Socket s = server.accept();
				s.setSoTimeout(1000);
				
				Thread writer = new Thread(() -> {
					try {
						int n = 0;
						while(!s.isClosed()) {
							s.getOutputStream().write(n++);
							Thread.sleep(1000);
						}
						System.out.println("Writer: socket is closed");
					} catch (Exception e) {
						System.out.println("Exception in writer");
						System.out.println(e);
					}
				});
				writer.start();
				
				Thread reader = new Thread(() -> {
					while(!s.isClosed()) {
						try {
							System.out.printf("%s %d isClosed: %b%n", 
									LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
									s.getPort(),
									s.isClosed());
							
							int b = s.getInputStream().read();
							System.out.println("read():     " + b);
							
							if(b == -1) {
								confirm("confirm closing the socket");
								s.close();
							}
						} catch(SocketTimeoutException e) {
						} catch (Exception e) {
							System.out.println("Exception in reader");
							System.out.println(e);
							break;
						}
					}
					System.out.println("done serving " + s);
				});
				reader.start();
			}
		}
	}

	private static void confirm(String prompt) throws IOException {
		System.out.println(prompt);
		new BufferedReader(new InputStreamReader(System.in)).readLine();
	}

}

