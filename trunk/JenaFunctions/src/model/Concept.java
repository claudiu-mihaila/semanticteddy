package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Concept implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1492471827021736032L;
	//basic properties
	private UUID uuid;
	private String name;
	private Map<String, String> prefLabels = new HashMap<String, String>();
	private Map<String, List<String>> altLabels = new HashMap<String, List<String>>();
	private Map<String, List<String>> definitions = new HashMap<String, List<String>>();
	
	//metadata Properties
	private Metadata metadata;
	
	//geo properties
	private String latitude;
	private String longitude;
	
	//relations
	private List<Concept> children = new ArrayList<Concept>();
	private List<Concept> parents = new ArrayList<Concept>();
	private List<Concept> related = new ArrayList<Concept>();
	
	public Concept() {}
	
	public Concept(String name, String language, String user){
		this.uuid = UUID.randomUUID();
		this.name = name;
		this.prefLabels.put(language, name);
		metadata = new Metadata();
		metadata.setDateCreated(new Date());
		metadata.setAuthor(user);
		metadata.setLastChangeDate(new Date());
		metadata.setLastChangeBy(user);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, List<String>> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(
			Map<String, List<String>> definitions) {
		this.definitions = definitions;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public List<Concept> getChildren() {
		return children;
	}

	public void setChildren(List<Concept> childs) {
		this.children = childs;
	}
	
	public void addChild(Concept child, String user) {
		this.children.add(child);
		this.metadata.setLastChangeBy(user);
		this.metadata.setLastChangeDate(new Date());
	}
	
	public void removeChild(Concept child, String user) {
		this.children.remove(child);
		this.metadata.setLastChangeBy(user);
		this.metadata.setLastChangeDate(new Date());
	}

	public List<Concept> getParents() {
		return parents;
	}

	public void setParents(List<Concept> parents) {
		this.parents = parents;
	}
	
	public void addParent(Concept parent, String user) {
		this.parents.add(parent);
		this.metadata.setLastChangeBy(user);
		this.metadata.setLastChangeDate(new Date());
	}
	
	public void removeParent(Concept parent, String user) {
		this.parents.remove(parent);
		this.metadata.setLastChangeBy(user);
		this.metadata.setLastChangeDate(new Date());
	}

	public List<Concept> getRelated() {
		return related;
	}

	public void setRelated(List<Concept> related) {
		this.related = related;
	}

	public void addRelated(Concept related, String user) {
		this.related.add(related);
		this.metadata.setLastChangeBy(user);
		this.metadata.setLastChangeDate(new Date());
	}
	
	public void removeRelated(Concept related, String user) {
		this.related.remove(related);
		this.metadata.setLastChangeBy(user);
		this.metadata.setLastChangeDate(new Date());
	}

	public Map<String, String> getPrefLabels() {
		return prefLabels;
	}

	public void setPrefLabels(Map<String, String> prefLabels) {
		this.prefLabels = prefLabels;
	}

	public Map<String, List<String>> getAltLabels() {
		return altLabels;
	}

	public void setAltLabels(Map<String, List<String>> altLabel) {
		this.altLabels = altLabel;
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}	
	
	public void setModified(String user) {
		this.metadata.setLastChangeBy(user);
		this.metadata.setLastChangeDate(new Date());
	}
	
	public String toString(){
		return this.name;
	}
}