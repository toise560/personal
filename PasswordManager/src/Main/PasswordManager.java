package Main;

import java.util.Scanner;

/**
 * Password manager back-end program to handle the storage and manipulation of user account data
 * @author Joseph Canale
 */
public class PasswordManager {

	/**
	 * Main method of the password manager back end to handle storage and manipulation of user account data
	 * @author Joseph Canale
	 * @param args (not used)
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Boolean run = true;
		while (run) {
		//Opening menu w/ instructions
			System.out.println("\nSelect Option: \n0.) Quit\n1.) Create New Account\n2.) Launch Account\n3.) Delete Account");
			try {
				while (true) {
					int launchInput = Integer.valueOf(scanner.nextLine());
					if (!(launchInput == 0 || launchInput == 1 || launchInput == 2 || launchInput == 3)) {
						System.out.println("\nInvalid Input");
					} else if (launchInput == 0) {
						run = false;
					} else if (launchInput == 1) {
						try {
							Account.create();
						} catch (Exception goBack) {
							System.out.println("Returning to main menu");
						}
						break;
					} else if (launchInput == 2){
						Account account = Account.launch();
						try {
							Login.execute(account);
						} catch (Exception goBack) { 
							System.out.println("Returning to main menu");
						}
					} else {
						System.out.println("Enter the account to delete: ");
						Account account = Account.launch();
						try {
							Account.delete(account);
						} catch (Exception goBack) {
							System.out.println("Returning to main menu");						
						}
					}
					break;
				}
			} catch (Exception e) {
				System.out.println("\nInvalid Input");
			}
		}
	}
}