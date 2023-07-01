package ir.amirhossein.lashibo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class User implements Serializable{
	private static final long serialVersionUID = -7197064378835125502L;
	private String username;
	private String emailAddress;
	private String password;
	private int balance;
	private int premiumMonthsLeft;
	private ArrayList<String> booksBought;
	private ArrayList<String> favoriteBooks;
	private ArrayList<String> stillReadingBooks;
	private ArrayList<String> finishedReadingBooks;
	public static ArrayList<User> users = new ArrayList<>();
	static {
		try(ObjectInputStream reader = new ObjectInputStream(new FileInputStream("users.bin"));
				){
			try {
				users = (ArrayList<User>)reader.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}catch(FileNotFoundException e) {}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public String getBooksBought() {
		String result = booksBought.size() != 0 ? booksBought.get(0) : "";
		for(int i = 1 ; i < booksBought.size() ; i++)
			result = result + ",," + booksBought.get(i);
		return result.trim();
	}

	public String getFavoriteBooks() {
		String result = favoriteBooks.size() != 0 ? favoriteBooks.get(0) : "";
		for(int i = 1 ; i < favoriteBooks.size() ; i++)
			result = result + ",," + favoriteBooks.get(i);
		return result.trim();
	}

	public String getStillReadingBooks() {
		String result = stillReadingBooks.size() != 0 ? stillReadingBooks.get(0) : "";
		for(int i = 1 ; i < stillReadingBooks.size() ; i++)
			result = result + ",," + stillReadingBooks.get(i);
		return result.trim();	
		}

	public String getFinishedReadingBooks() {
		String result = finishedReadingBooks.size() != 0 ? finishedReadingBooks.get(0) : "";
		for(int i = 1 ; i < finishedReadingBooks.size() ; i++)
			result = result + ",," + finishedReadingBooks.get(i);
		return result.trim();	
		}

	public User(String username, String emailAddress, String password) {
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
		this.balance = 0;
		this.premiumMonthsLeft = 0;
		this.booksBought = new ArrayList<>();
		this.favoriteBooks = new ArrayList<>();
		this.stillReadingBooks = new ArrayList<>();
		this.finishedReadingBooks = new ArrayList<>();
		users.add(this);
		saveUsers();
		System.out.println("User added username: " + username + " password: " + password);
	}

	public boolean addBalance(int amount) {
		if(amount <= 0)
			if(-amount > balance) 
				return false;
		this.balance += amount;
		saveUsers();
		return true;
	}

	public boolean setPassword(String newPassword) {
		if(newPassword != null && !newPassword.isEmpty()) {
			this.password = newPassword;
			saveUsers();
			return true;
		}
		return false;
	}


	public boolean buyBook(String name, int price) {
		if(addBalance(-price)) {
			this.booksBought.add(name);
			saveUsers();
			return true;
		}
		return false;
	}

	public boolean toggleFavoriteBook(String name) {
		if(favoriteBooks.contains(name)) {
			favoriteBooks.remove(name);
			saveUsers();
			return true;
		}
		else {
			favoriteBooks.add(name);
			saveUsers();
			return true;
		}
	}

	public boolean addToStillReading(String name) {
		if(booksBought.contains(name)) {
			stillReadingBooks.add(name);
			saveUsers();
			return true;
		}
		return false;
	}

	public boolean addToFinishedReading(String name) {
		if(stillReadingBooks.contains(name)) {
			finishedReadingBooks.add(name);
			stillReadingBooks.remove(name);
			saveUsers();
			return true;
		}
		return false;
	}

	public String getUsername() {
		return username;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public int getBalance() {
		return this.balance;
	}

	public int getPremiumMonthsLeft() {
		return this.premiumMonthsLeft;
	}

	public boolean addPremiumMonths(int months) {
		this.premiumMonthsLeft += months;
		saveUsers();
		return true;

	}
	public boolean authenticate(String password) {
		if (this.password.equals(password))
			return true;
		return false;
	}

	public boolean changePassword(String newPassword) {
		this.password = newPassword;
		saveUsers();
		return true;
	}

	public String changeUsername(String newUsername) {
		User redundant = null;
		for(User u : User.users) {
			if(u.getUsername().equals(newUsername)) {
				redundant = u;
			}
		}
		if(redundant != null){
			return "duplicate_username";
		}
		username = newUsername;
		saveUsers();
		return "true";
	}

	public void saveUsers() {
		synchronized (User.users) {
			try(ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
				writer.writeObject(users);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
