package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Concept {

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
	
	public Concept(String name, String language){
		this.uuid = UUID.randomUUID();
		this.name = name;
		this.prefLabels.put(language, name);
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

	public List<Concept> getParents() {
		return parents;
	}

	public void setParents(List<Concept> parents) {
		this.parents = parents;
	}

	public List<Concept> getRelated() {
		return related;
	}

	public void setRelated(List<Concept> related) {
		this.related = related;
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
	
}