package ir.amirhossein.lashibo;
import java.io.IOException;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Server {
	
	private ServerSocket serverSocket;
	
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void runServer() {
		System.out.println("Server Started");
		try {
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("New Connection from " + ((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress().getHostAddress());
				ClientHandler clientHandler = new ClientHandler(socket);
				Thread clientThread = new Thread(clientHandler);
				clientThread.start();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopServer() {
		try {
			if(serverSocket != null)
				serverSocket.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
