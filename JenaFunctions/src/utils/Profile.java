package utils;

import java.util.HashMap;
import java.util.Map;

import model.Concept;

public class Profile {
	private String profileName;
	private String profileDefaultLanguage;
	private Map<Concept,Boolean> profileProjects = new HashMap<Concept, Boolean>(); 
	
	public Profile(){
		
	}
	
	public Profile(String  usr, String language, Map<Concept, Boolean> projects){
		this.profileName = usr;
		this.profileDefaultLanguage = language;
		this.profileProjects = projects;
	}
	
	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
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
