package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.text.WordUtils;

/**
 * Class to process a user's account information
 * @author Joseph Canale
 */
public class Account {
	private String name;
	private String url;
	private String pass;
	private String user;
	private Boolean print;
	
	/**
	 * Constructor for new Account instances (Accounts represent user accounts)
	 * @param name - 'name' of the account
	 * @param location - url of the site associated with the account
	 * @param password - password associated with the account
	 * @param username - username associated with the account
	 * @param print - determines whether the Account instance is written to the storage file or not (true = write, false = not)
	 * @throws IOException if the BufferedWriter fails to write to a storage file
	 */
	public Account(String name, String location, String password, String username, Boolean print) {
		this.name = name;
		this.url = location;
		this.pass = password;
		this.user = username;
		this.print = print;
		
		if (print) {
			File accountStorage = new File("Accounts/Accounts.txt");
			try {
				if(!(accountStorage.exists())) {
					accountStorage.createNewFile();
					System.out.println("Created New Account Storage File");
				}
				try (BufferedWriter writer = new BufferedWriter(new FileWriter("Accounts/Accounts.txt", true))) {
					writer.write(this.toString());		//add arg to write()
					writer.newLine();
				}
			} catch (IOException e) {
				System.out.println("IOException in Storage.java");
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * Method to collect user input parameters to create a new account. Inputs are then passed to the Account constructor.
	 * @return true if the user enters all four account parameters without entering the .back escape command, false otherwise
	 * @see Account
	 */
	public static boolean create() {
		Scanner scanner = new Scanner(System.in);
		String name = "";
		String url = "";
		String username = "";
		String password = "";
		
		System.out.println("Enter Account Name: ");
		
		while (true) {
			name = scanner.nextLine();
			if (name.equals(".back")) {
				break;
			}
			System.out.println("Enter Account Url: ");			
			url = scanner.nextLine();
			if (url.equals(".back")) {
				break;
			}
			System.out.println("Enter Account Username: ");
			username = scanner.nextLine();
			if (username.equals(".back")) {
				break;
			}
			System.out.println("Enter Account Password: ");
			password = scanner.nextLine();
			break;
		} 
		if (name.equals(".back") || url.equals(".back") || username.equals(".back") || password.equals(".back")) {
			System.out.println("Returning to main menu");
			return false;
		}
		Account account = new Account(name, url, username, password, true);
		System.out.println("Succesfully created account: " + name);
		return true;
	}
	
	/**
	 * Method for reading the storage file and converting the contents into the account parameters for use in the program. 
	 * <p>
	 * This method works by treating commas as a delimiter and assigning each string between each delimiter to a string array.
	 * The string array is then reassigned to the account parameter it represents. The parameters are used to reconstruct the 
	 * user account by passing those variables to the Account constructor. This Account object is added to an ArrayList to keep 
	 * track of all accounts in the storage file. This process repeats until a blank line is reached, at which point the while
	 * loop terminates and the list of accounts is finalized.
	 * 
	 * Note: the reconstructed account is passed a "false" print boolean so that it will not be rewritten to the storage file
	 * </p>
	 * @return ArrayList<Account> containing all of the reconstructed account objects from the storage file
	 * @throws IOException if the BufferedWriter fails to read the storage file
	 */
	public static ArrayList<Account> read() {
		ArrayList<Account> accountList = new ArrayList<>();
		try {
			BufferedReader accountReader = new BufferedReader(new FileReader("Accounts/Accounts.txt"));
			String line;
			//reads each line and compiles data into an account object
			while ((line = accountReader.readLine()) != null) {
				String[] splitLine = line.split(",");
				String name = splitLine[0];
				String url = splitLine[1];
				String username = splitLine[2];
				String password = splitLine[3];
				Account account = new Account(name, url, username, password, false);
				accountList.add(account);
		      }
			}
		catch (IOException e){
			e.printStackTrace();
		}
		return accountList;
	}
	
	//crashes when selecting out of bound index
	/**
	 * Method to select an account to 'launch', or pass to the front-end program.
	 * <p>
	 * It should be noted that the front-end has not been developed yet. The Account is passed to a method in a placeholder class called 'Login'
	 * which prints the toString() of the account.
	 * </p>
	 * @return Account from the accountList ArrayList, depending on the user's selection. When an exception is thrown, the method returns null
	 * @throws Exception e if the user enters something other than an integer corresponding to an account. The method returns null when this happens.
	 */
	public static Account launch() {
		Scanner scanner = new Scanner(System.in);
		ArrayList<Account> accountList = new ArrayList<>(); //ArrayList to handle accounts
		
		accountList = Account.read(); //returns each account in storage to the program. Account.read() returns an arrayList which is passed to accountList for use in the program
		System.out.println("\nSelect an account to launch: \n[0] Back to Main Menu"); //instruction to user
		int i = 1; //
		for (Account account : accountList) {
			String accountName = "["+i+"] " + WordUtils.capitalize(account.returnName());
			System.out.println(accountName); //prints index+1 and account name 
			i++;
		}
		//user chooses account to launch
		int accSelect = scanner.nextInt(); //user enters integer 
		if (accSelect == 0) {
			return null;
		}
		try {
			int index = accSelect-1; //integer converted to index number by subtracting 1
			System.out.println(accountList.get(index));	//account returned in console
			return accountList.get(index); //selected account returned to main program
		} catch (Exception e) {
			System.out.println("Invalid Input");
			return null;
		}
	}
	
	/**
	 * This is a method to remove accounts from the storage file
	 * @param account for the method to remove from the storage file
	 * @throws IOException when the storage file path is not valid
	 */
	public static void delete(Account account) {
		File oldFile = new File("Accounts/Accounts.txt");
		File dupFile = new File("Accounts/tempAccounts.txt");
		ArrayList<Account> accountList = Account.read(); //ArrayList to handle accounts. Automatically pulls data from the file
		ArrayList<String> lineList = new ArrayList<>(); //ArrayList to store the contents of the text file for editing and reconstruction (line by line)
		//checks each account in the text file for a matching entry passed by the user:
		for (Account accountItem : accountList) {
			if (accountItem.toString().equals(account.toString())) { //Code triggered when a matching account string is found
				//instructions for managing a matching entry
				System.out.println("Deleting " + accountItem.returnName()); //"deletes" the account by simply not adding the account's toString() to the toString list (lineList<>)
			} else {
				lineList.add(accountItem.toString());
			}
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("Accounts/Accounts.txt"));
			for (String accountToString : lineList) {
				writer.write(accountToString);
				writer.newLine();
			}
			writer.close();
			oldFile.delete(); 
			dupFile.renameTo(oldFile); 
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public String returnName() {
		return name;
	}
	
	public String returnUrl() {
		return url;
	}
	
	public String returnPass() {
		return pass;
	}
	
	public String returnUser() {
		return user;
	}
	
	@Override
	public String toString() {
		return name+","+url+","+user+","+pass;
	}
}
