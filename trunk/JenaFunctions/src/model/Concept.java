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
	private String prefLabel;
	private String altLabel;
	private Map<String, List<String>> definitionPerLanguage = new HashMap<String, List<String>>();
	
	//metadata Properties
	private Metadata metadata;
	
	//geo properties
	private String xCoordinate;
	private String yCoordinate;
	
	//relations
	private List<Concept> children = new ArrayList<Concept>();
	private List<Concept> parents = new ArrayList<Concept>();
	private List<Concept> related = new ArrayList<Concept>();
	
	public Concept(){
		
	}
	
	public Concept(String name){
		this.uuid = UUID.randomUUID();
		this.name = name;
		this.prefLabel = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.prefLabel = name;
	}

	public Map<String, List<String>> getDefinitionPerLanguage() {
		return definitionPerLanguage;
	}

	public void setDefinitionPerLanguage(
			Map<String, List<String>> definitionPerLanguage) {
		this.definitionPerLanguage = definitionPerLanguage;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	public String getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(String xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public String getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(String yCoordinate) {
		this.yCoordinate = yCoordinate;
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

	public String getPrefLabel() {
		return prefLabel;
	}

	public void setPrefLabel(String prefLabel) {
		this.prefLabel = prefLabel;
	}

	public String getAltLabel() {
		return altLabel;
	}

	public void setAltLabel(String altLabel) {
		this.altLabel = altLabel;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}	
	
}