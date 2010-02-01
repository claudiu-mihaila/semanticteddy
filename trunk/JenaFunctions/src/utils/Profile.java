package utils;

import java.util.HashMap;
import java.util.Map;

import model.Concept;

public class Profile {
	private User profileUser;
	private String profileDefaultLanguage;
	private Map<Concept,Boolean> profileProjects = new HashMap<Concept, Boolean>(); 
	
	public Profile(){
		
	}
	
	public Profile(User  usr, String language){
		this.profileUser = usr;
		this.profileDefaultLanguage = language;
	}
	
	public User getProfileUser() {
		return profileUser;
	}

	public void setProfileUser(User profileUser) {
		this.profileUser = profileUser;
	}

	public String getProfileDefaultLanguage() {
		return profileDefaultLanguage;
	}
	public void setProfileDefaultLanguage(String profileDefaultLanguage) {
		this.profileDefaultLanguage = profileDefaultLanguage;
	}

	public Map<Concept, Boolean> getProfileProjects() {
		return profileProjects;
	}

	public void setProfileProjects(Map<Concept, Boolean> profileProjects) {
		this.profileProjects = profileProjects;
	}
	
	
}
