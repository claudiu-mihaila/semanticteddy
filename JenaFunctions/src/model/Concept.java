package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Concept {

	//basic properties
	private String name;
	private String preferedName;
	private Map<String, List<String>> definitionPerLanguage = new HashMap<String, List<String>>();
	
	//metadata Properties
	private Metadata metadata;
	
	//geo properties
	private String xCoordinate;
	private String yCoordinate;
	
	//relations
	private List<Concept> childs = new ArrayList<Concept>();
	private List<Concept> parents = new ArrayList<Concept>();
	private List<Concept> related = new ArrayList<Concept>();
	
	public Concept(){
		
	}
	
	public Concept(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPreferedName() {
		return preferedName;
	}

	public void setPreferedName(String preferedName) {
		this.preferedName = preferedName;
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

	public List<Concept> getChilds() {
		return childs;
	}

	public void setChilds(List<Concept> childs) {
		this.childs = childs;
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
	
	
}