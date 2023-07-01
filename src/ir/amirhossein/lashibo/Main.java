package ir.amirhossein.lashibo;
import java.net.ServerSocket;
import java.io.IOException;
public class Main {
	public static void main(String[] args) throws IOException{

		final int port = 3773; 
		Server server = new Server(new ServerSocket(port));
		server.runServer();
	}
}
