package adapters;

import java.io.Serializable;

import utils.User;

public class LoginAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2012408009230209893L;
	
	private User user;

	public LoginAdapter() {
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
	
	public String login(){
		//if accepted return "Login" else return null;
		return "Login";
	}
}
