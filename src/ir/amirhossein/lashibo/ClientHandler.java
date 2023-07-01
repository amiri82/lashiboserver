package ir.amirhossein.lashibo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
public class ClientHandler implements Runnable {
	static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	protected Socket socket;
	private BufferedReader input;
	private BufferedWriter output;
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			clientHandlers.add(this);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void run(){
		String request;
		while(socket.isConnected() && !socket.isClosed()) {
			try {
				request = input.readLine();
				if(request == null) {
					socket.close();
					throw new IOException();
				}
				System.out.println(request);
				String result = processRequest(request);
				System.out.println(result);
				output.write(result + "\n");
				output.flush();
			}catch(IOException e) {
				clientHandlers.remove(this);
				System.out.println("Client was disconnected");
			}
		}
	}

	public String processRequest (String request) throws IOException{
		String[] requestParts = request.strip().split("::");
		switch(requestParts[0].toLowerCase()) {
			case "login":
				return login(requestParts);
			case "signup":
				return signUp(requestParts);
			case "addcredit":
				return Boolean.toString(addCredit(requestParts));
			case "changepassword":
				return changePassword(requestParts);
			case "changeusername":
				return changeUsername(requestParts);
			case "addpremiummonths":
				return addPremiumMonths(requestParts);
			case "addboughtbook":
				return addBoughtBook(requestParts);
			case "togglefavoritebook":
				return toggleFavoriteBook(requestParts);
			case "addtostillreading":
				return addToStillReading(requestParts);
			case "addtofinishedreading":
				return addToFinishedReading(requestParts);
		}
		return null;
		
	}
	
	public String login(String[] requestParts) {
		if(requestParts.length < 3)
			return "false";
		String username  = requestParts[1];
		String password = requestParts[2];
		User found = null;
		if(username == null || password == null)
			return "false";
		if(username.isEmpty() || password.isEmpty())
			return "false";
		for(User u : User.users) {
			if(u.getUsername().equals(username) || u.getEmailAddress().equals(username)) {
				found = u;
				break;
			}
		}
		if(found == null)
			return "false";
		if(found.authenticate(password))
			return found.getUsername() + "::" + found.getEmailAddress() + "::" + found.getBalance() + "::" + found.getPremiumMonthsLeft() + "::" + found.getBooksBought() + "::"  + found.getFavoriteBooks() + "::" + found.getStillReadingBooks() + "::" + found.getFinishedReadingBooks() + "::";
		return "false";
	}
	public String signUp(String[] requestParts) {
		if(requestParts.length < 4)
			return "error";
		String username = requestParts[1];
		String emailAddress = requestParts[2];
		String password = requestParts[3];
		if(username == null || emailAddress == null || password == null)
			return "error";
		if(username.isEmpty() || emailAddress.isEmpty() || password.isBlank())
			return "error";
		for(User u : User.users) {
			if(u.getUsername().equals(username)) {
				return "duplicate_username";
			}
		}
		return new User(username,emailAddress,password) == null ? "error" : "success";
	}
	
	public Boolean addCredit(String[] requestParts) {
		User found = null;
		String username = requestParts[1];
		int amount = Integer.parseInt(requestParts[2]);
		for(User u : User.users) {
			if(u.getUsername().equals(username)) {
				found = u;
				break;
			}
		}
		found.addBalance(amount);
		return true;
	}
	
	public String changePassword(String[] requestParts) {
		User found = null;
		String username = requestParts[1];
		String newPassword = requestParts[2];
		for (User u : User.users) {
			if(u.getUsername().equals(username)) {
				found = u;
				break;
			}
		}
		return found.changePassword(newPassword) ? "true" : "false";
	}
	
	public String changeUsername(String[] requesstParts) {
		User found = null;
		String oldUsername = requesstParts[1];
		String newUsername = requesstParts[2];
		for(User u : User.users) {
			if(u.getUsername().equals(oldUsername)) {
				found = u;
				break;
			}
		}
		return found.changeUsername(newUsername);
	}
	
	public String addPremiumMonths(String[] requestParts) {
		String username = requestParts[1];
		int months = Integer.parseInt(requestParts[2]);
		User found = null;
		for(User u : User.users) {
			if(u.getUsername().equals(username)) {
				found = u;
				break;
			}
		}
		return found.addPremiumMonths(months) ? "true" : "false";
	}
	
	public String addBoughtBook(String[] requestParts) {
		String username = requestParts[1];
		String boookName = requestParts[2];
		int price = Integer.parseInt(requestParts[3]);
		User found = null;
		for(User u : User.users) {
			if(u.getUsername().equals(username)) {
				found = u;
				break;
			}
		}
		return found.buyBook(boookName, price) ? "true" : "false";
	}
	
	public String toggleFavoriteBook(String[] requestParts) {
		String username = requestParts[1];
		String bookName = requestParts[2];
		User found = null;
		for(User u : User.users) {
			if(u.getUsername().equals(username)) {
				found = u;
				break;
			}
		}
		return found.toggleFavoriteBook(bookName) ? "true" : "false";
	}
	
	public String addToStillReading(String[] requestParts) {
		String username = requestParts[1];
		String bookName = requestParts[2];
		User found = null;
		for(User u : User.users) {
			if(u.getUsername().equals(username)) {
				found = u;
				break;
			}
		}
		return found.addToStillReading(bookName) ? "true" : "false";
	}
	
	public String addToFinishedReading(String[] requestParts) {
		String username = requestParts[1];
		String bookName = requestParts[2];
		User found = null;
		for(User u : User.users) {
			if(u.getUsername().equals(username)) {
				found = u;
				break;
			}
		}
		return found.addToFinishedReading(bookName) ? "true" : "false";
	}
	

}
