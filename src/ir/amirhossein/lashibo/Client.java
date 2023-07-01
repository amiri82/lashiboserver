package ir.amirhossein.lashibo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws IOException{
		try(Socket socket = new Socket("127.0.0.1",3773);
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				Scanner scanner = new Scanner(System.in);)
		{
			while(socket.isConnected() && !socket.isClosed()) {
				String line = scanner.nextLine();
				output.write(line + "\n");
				output.flush();	
				String response = input.readLine();
				System.out.println(response);
				System.out.flush();
			}
		}
	}
}
