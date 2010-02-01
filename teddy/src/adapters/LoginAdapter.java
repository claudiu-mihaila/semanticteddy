package adapters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.collections.iterators.ArrayListIterator;

import utils.Globals;
import utils.Profile;
import utils.User;

public class LoginAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2012408009230209893L;
	
	private User user;
	private String defaultLanguage;
	private static ArrayList<String> alluserLines = null;

	public LoginAdapter() {
		// citirea tuturor userilor
		loadAllUsers();
		
		this.user = new User();
		user.setUsername("");
		user.setPassword("");
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
	
	public String login(String username, String password) {
		if (alluserLines==null){
			loadAllUsers();
		}
		
		for (String str : alluserLines){
			if (str.startsWith(username)){
				String[] components = str.split(" ");
				if ((components[0].equals(username)) && (components[1].equals(password)))
				{
//				    currentProfile = new Profile(new User(username, password), components[2]);
					defaultLanguage = components[2];
				    return "Login";
				}
			}
		}
		return null;
	}
	
	private void loadAllUsers(){
		try{
			BufferedReader myReader = new BufferedReader(new FileReader(new File(Globals.teddySecurityFilePath)));
			String temp = myReader.readLine();
			alluserLines = new ArrayList<String>();
			while(temp!=null){
				alluserLines.add(temp);
				temp = myReader.readLine();
			}
			myReader.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
