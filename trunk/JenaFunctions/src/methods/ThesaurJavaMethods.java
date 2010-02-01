package methods;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import model.Concept;
import model.Metadata;
import modelToRDF.ThesaurRDFMethods;
import utils.Globals;
import utils.Profile;
import utils.User;

public class ThesaurJavaMethods {
	
	private ThesaurRDFMethods rdfModel ;
	public Profile currentProfile;
	private ArrayList<String> alluserLines = null;
	
	public ThesaurJavaMethods(String username, String pass, String modelPath) throws Exception{
		Boolean userFound = this.logUser(username, pass);
		if (userFound){
			rdfModel = new ThesaurRDFMethods(modelPath);
			//currentProfile e deja completat
			loadProjects();
		}
	}
	
	public boolean logUser(String username, String password){
		if (alluserLines==null){
			loadAllUsers();
		}
		
		for (String str : alluserLines){
			if (str.startsWith(username)){
				String[] components = str.split(" ");
				if ((components[0].equals(username)) && (components[1].equals(password)))
				{
				    currentProfile = new Profile(new User(username, password), components[2]);
				    return true;
				}
			}
		}
		return false;
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
	
	public void closeProject(){
		rdfModel.closeSession();
	}
	
	public Profile createProfile(User usr, String defLanguage){
		Profile prof = new Profile(usr,defLanguage);
		this.currentProfile = prof;
		return prof;
	}

	public void loadProjects(){
		for (String project : allProjectsNames()){
			Concept root = loadProject(project, currentProfile.getProfileDefaultLanguage());
			currentProfile.getProfileProjects().put(root, Boolean.TRUE);
		}
	}
	List<Concept> toBeLoaded = new ArrayList<Concept>();
	
	public Concept loadProject(String project, String defL){
		Map<UUID, Concept> loadedConcepts = new HashMap<UUID, Concept>();
		toBeLoaded = new ArrayList<Concept>();
		
		Concept rootConcept = new Concept();
		rootConcept.setUUID(UUID.fromString(project));
		rootConcept = loadAConcept(loadedConcepts,rootConcept, defL);
		
		loadedConcepts.put(rootConcept.getUUID(), rootConcept);
		
		int i=0;
		while(i<toBeLoaded.size()){
			Concept c = toBeLoaded.get(i);
			c = loadAConcept(loadedConcepts, c, defL);
			loadedConcepts.put(c.getUUID(), c);
			i=i+1;
		}
		printAsObject(rootConcept);
		return rootConcept;
	}

	public Concept loadAConcept(Map<UUID, Concept> loadedConcepts, Concept con, String defL){
		con.setPrefLabels(rdfModel.getPrefLabelPropertyForUUIDConcept(con.getUUID()));
		con.setName(con.getPrefLabels().get(defL));
		con.setAltLabels(rdfModel.getAltLabelPropertyForUUIDConcept(con.getUUID()));
		con.setDefinitions(rdfModel.getDefinitionPropertyForUUIDConcept(con.getUUID()));
		
		Metadata rootMetadata = new Metadata();
		rootMetadata.setAuthor(rdfModel.getMetadataCreatorForUUIDConcept(con.getUUID()));
		rootMetadata.setDateCreated(rdfModel.getMetadataDateForUUIDConcept(con.getUUID()));
		rootMetadata.setLastChangeBy(rdfModel.getMetadataContributorForUUIDConcept(con.getUUID()));
		rootMetadata.setLastChangeDate(rdfModel.getMetadataModifiedForUUIDConcept(con.getUUID()));
		con.setMetadata(rootMetadata);
		
		//geo
		con.setLatitude(rdfModel.getLatitudePropertyForUUIDConcept(con.getUUID()));
		con.setLongitude(rdfModel.getLongitudePropertyForUUIDConcept(con.getUUID()));
		
		
		
		//children
		List<String> uuidChilds = rdfModel.getNarrowerPropertyFORUUIDConcept(con.getUUID());
		List<Concept> childs = new ArrayList<Concept>();
		for (String str : uuidChilds){
			if (loadedConcepts.containsKey(UUID.fromString(str))){
				childs.add(loadedConcepts.get(UUID.fromString(str)));
			}
			else
			{
				Concept newConcept = new Concept();
				newConcept.setUUID(UUID.fromString(str));
				toBeLoaded.add(newConcept);
				childs.add(newConcept);
			}
		}
		con.setChildren(childs);
		
		//parents
		List<String> uuidPArents = rdfModel.getBroaderPropertyFORUUIDConcept(con.getUUID());
		List<Concept> parents = new ArrayList<Concept>();
		for (String str : uuidPArents){
			if (loadedConcepts.containsKey(UUID.fromString(str))){
				parents.add(loadedConcepts.get(UUID.fromString(str)));
			}
			else
			{
				Concept newConcept = new Concept();
				newConcept.setUUID(UUID.fromString(str));
				toBeLoaded.add(newConcept);
				parents.add(newConcept);
			}
		}
		con.setParents(parents);
		
		//related
		List<String> uuidRelateds = rdfModel.getNarrowerPropertyFORUUIDConcept(con.getUUID());
		List<Concept> relateds = new ArrayList<Concept>();
		for (String str : uuidRelateds){
			if (loadedConcepts.containsKey(UUID.fromString(str))){
				relateds.add(loadedConcepts.get(UUID.fromString(str)));
			}else
			{
				Concept newConcept = new Concept();
				newConcept.setUUID(UUID.fromString(str));
				toBeLoaded.add(newConcept);
				relateds.add(newConcept);
			}
			
		}
		con.setRelated(relateds);
		
		return con;
	}
	
	public ArrayList<String> allProjectsNames(){
		ArrayList<String> projects = new ArrayList<String>();
		try{
			BufferedReader myReader = new BufferedReader(new FileReader(new File(Globals.teddyProjectsFilePath)));
			String temp = myReader.readLine();
			while(temp!=null){
				projects.add(temp);
				temp = myReader.readLine();
			}
			myReader.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return projects;
	}
	
	public void storeAllProjects(ArrayList<String> projects){
		try{
			BufferedWriter myWriter = new BufferedWriter(new FileWriter(new File(Globals.teddyProjectsFilePath)));
			for (String str : projects)
			{
				myWriter.write(str);
				myWriter.newLine();
			}
			myWriter.close();
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * Creates a root concept in the model
	 * @param name the name of the root concept
	 * @return the newly created root concept
	 */
	public Concept addRootConcept(String name){
		Concept rootConcept = new Concept(name,currentProfile.getProfileDefaultLanguage(),currentProfile.getProfileUser().getUsername());
		currentProfile.getProfileProjects().put(rootConcept, Boolean.TRUE);
		
		ArrayList<String> allRoots = this.allProjectsNames();
		allRoots.add(rootConcept.getUUID().toString());
		this.storeAllProjects(allRoots);
		
		rdfModel.addRootConcept(rootConcept.getUUID(), name, currentProfile.getProfileDefaultLanguage(),currentProfile.getProfileUser().getUsername());
		return rootConcept;
	}

	public void removeRootConcept(Concept rootConcept) {
		currentProfile.getProfileProjects().remove(rootConcept);
		//din rdf remove
	}
	
	/**
	 * Deletes a concept from the model
	 * @param currentConcept the current concept
	 * @throws Exception if the current concept still has narrower/related concepts
	 */
	public void deleteConcept(Concept currentConcept) throws Exception {
		if (currentConcept.getChildren().size() > 0)
			throw new Exception("The concept has narrower concepts!");
		if (currentConcept.getRelated().size() > 0)
			throw new Exception("The concept has related concepts!");
		for (Concept parent : currentConcept.getParents()) {
			parent.getChildren().remove(currentConcept);
		}
		rdfModel.deleteConcept(currentConcept.getUUID());
	}
	
	/**
	 * Creates a new child concept
	 * @param parentConcept the current concept, which will be the parent
	 * @param name the name of the child concept
	 * @return the newly created child concept
	 */
	public Concept addChildConcept(Concept parentConcept, String name){
		Concept childConcept = new Concept(name, currentProfile.getProfileDefaultLanguage(), currentProfile.getProfileUser().getUsername());
		parentConcept.addChild(childConcept, currentProfile.getProfileUser().getUsername());
		childConcept.addParent(parentConcept, currentProfile.getProfileUser().getUsername());
		rdfModel.addNarrowerResource(parentConcept.getUUID(), childConcept.getUUID(), name, currentProfile.getProfileDefaultLanguage(), currentProfile.getProfileUser().getUsername());
		return childConcept;
	}
	
	/**
	 * Links an already existing concept to a concept's parents
	 * @param childConcept the current child concept
	 * @param parentConcept the already existing parent
	 * @throws Exception if childConcept has a broader/related relation with parentConcept
	 */
	public void linkChildConcept(Concept parentConcept, Concept childConcept) throws Exception {
		if (parentConcept.equals(childConcept) == true)
			throw new Exception("The two concepts are identical!");
		if (parentConcept.getParents().contains(childConcept) == true)
			throw new Exception("The concepts already share a broader/narrower relation!");
		if (parentConcept.getRelated().contains(childConcept) == true)
			throw new Exception("The concepts already share a related relation!");
		if (parentConcept.getParents().contains(childConcept) == false) {
			parentConcept.addChild(childConcept, currentProfile.getProfileUser().getUsername());
			childConcept.addParent(parentConcept, currentProfile.getProfileUser().getUsername());
			rdfModel.linkNarrowerResource(parentConcept.getUUID(), childConcept.getUUID(), currentProfile.getProfileUser().getUsername());
		}
	}
	
	/**
	 * Removes a concept from a parent's children
	 * @param parentConcept the current parent concept
	 * @param childConcept the child concept to be removed from the parent's children
	 * @return the parent concept without the child concept
	 */
	public Concept removeChildConcept(Concept parentConcept, Concept childConcept) {
		parentConcept.removeChild(childConcept, currentProfile.getProfileUser().getUsername());
		childConcept.removeParent(parentConcept, currentProfile.getProfileUser().getUsername());
		rdfModel.removeNarrowerResource(parentConcept.getUUID(), childConcept.getUUID(), currentProfile.getProfileUser().getUsername());
		rdfModel.removeBroaderResource(childConcept.getUUID(), parentConcept.getUUID(),currentProfile.getProfileUser().getUsername());
		return parentConcept;
	}
	
	/**
	 * Links an already existing concept to a concept's parents
	 * @param childConcept the current child concept
	 * @param parentConcept the already existing parent
	 * @throws Exception if childConcept has a narrower/related relation with parentConcept
	 */
	public void linkParentConcept(Concept childConcept, Concept parentConcept) throws Exception{
		if (parentConcept.equals(childConcept) == true)
			throw new Exception("The two concepts are identical!");
		if (childConcept.getChildren().contains(parentConcept) == true)
			throw new Exception("The concepts already share a broader/narrower relation!");
		if (childConcept.getRelated().contains(parentConcept) == true)
			throw new Exception("The concepts already share a related relation!");
		if (childConcept.getParents().contains(parentConcept) == false) {
			childConcept.addParent(parentConcept, currentProfile.getProfileUser().getUsername());
			parentConcept.addChild(childConcept, currentProfile.getProfileUser().getUsername());
			rdfModel.linkBroaderResource(childConcept.getUUID(), parentConcept.getUUID(), currentProfile.getProfileUser().getUsername());
		}
	}
	
	/**
	 * Removes a concept from a child's parents
	 * @param childConcept the current child concept
	 * @param parentConcept the parent concept to be removed from the child's parents
	 * @return the child concept without the parent concept
	 */
	public Concept removeParentConcept(Concept childConcept, Concept parentConcept) {
		childConcept.removeParent(parentConcept, currentProfile.getProfileUser().getUsername());
		parentConcept.removeChild(childConcept, currentProfile.getProfileUser().getUsername());
		rdfModel.removeNarrowerResource(parentConcept.getUUID(), childConcept.getUUID(), currentProfile.getProfileUser().getUsername());
		rdfModel.removeBroaderResource(childConcept.getUUID(), parentConcept.getUUID(), currentProfile.getProfileUser().getUsername());
		return childConcept;
	}
	
	/**
	 * Adds an already existing concept to a concept's related concepts 
	 * @param currentConcept the current concept
	 * @param relatedConcept the already existing concept 
	 * @throws Exception if currentConcept has a broader/narrower relation to relatedConcept
	 */
	public void linkRelatedConcept(Concept currentConcept, Concept relatedConcept) throws Exception{
		if (currentConcept.equals(relatedConcept) == true)
			throw new Exception("The two concepts are identical!");
		if (currentConcept.getChildren().contains(relatedConcept) == true ||
				currentConcept.getParents().contains(relatedConcept) == true)
			throw new Exception("The concepts already share a broader/narrower relation!");
		if (currentConcept.getRelated().contains(relatedConcept) == false) {
			currentConcept.addRelated(relatedConcept, currentProfile.getProfileUser().getUsername());
			relatedConcept.addRelated(currentConcept, currentProfile.getProfileUser().getUsername());
			rdfModel.linkRelatedResource(currentConcept.getUUID(), relatedConcept.getUUID(), currentProfile.getProfileUser().getUsername());
		}
	}
	
	/**
	 * Removes a concept from a concept's related concepts
	 * @param currentConcept the current concept
	 * @param relatedConcept the related concept to be removed
	 */
	public void removeRelatedConcept(Concept currentConcept, Concept relatedConcept) {
		currentConcept.removeRelated(relatedConcept, currentProfile.getProfileUser().getUsername());
		relatedConcept.removeRelated(currentConcept, currentProfile.getProfileUser().getUsername());
		rdfModel.removeRelatedResource(currentConcept.getUUID(), relatedConcept.getUUID(), currentProfile.getProfileUser().getUsername());
		rdfModel.removeRelatedResource(relatedConcept.getUUID(), currentConcept.getUUID(), currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Adds a new definition to a concept
	 * @param currentConcept the concept to be defined
	 * @param definition the definition to be added
	 * @param language the language of the definition
	 */
	public void addDefinition(Concept currentConcept, String definition, String language){
        List<String> definitions = currentConcept.getDefinitions().get(language);
        if (definitions == null)
                definitions = new ArrayList<String>();
        definitions.add(definition);
        currentConcept.getDefinitions().put(language, definitions);
        currentConcept.setModified(currentProfile.getProfileUser().getUsername());
        rdfModel.addDefinition(currentConcept.getUUID(), definition, language, currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Replaces an existing definition with a new one. Both definitions are in the same language.
	 * @param currentConcept the concept to be redefined
	 * @param oldDefinition the current definition
	 * @param newDefinition the new definition
	 * @param language the language of the definitions
	 */
	public void editDefinition(Concept currentConcept, String oldDefinition, String newDefinition, String language){
        List<String> definitions = currentConcept.getDefinitions().get(language);
        int index = definitions.indexOf(oldDefinition);
        if (index >= 0) {
        	definitions.set(index, newDefinition);
            currentConcept.setModified(currentProfile.getProfileUser().getUsername());
    		rdfModel.editDefinition(currentConcept.getUUID(), oldDefinition, newDefinition, language, currentProfile.getProfileUser().getUsername());
        }
	}

	/**
	 * Removes a definition from a concept
	 * @param currentConcept the current concept
	 * @param definition the definition to be removed
	 * @param language the language of the definition
	 */
	public void removeDefinition(Concept currentConcept, String definition, String language){
        List<String> definitions = currentConcept.getDefinitions().get(language);
        if (definitions != null)
        {
            definitions.remove(definition);
            currentConcept.getDefinitions().put(language, definitions);
            currentConcept.setModified(currentProfile.getProfileUser().getUsername());
            rdfModel.removeDefinition(currentConcept.getUUID(), definition, language, currentProfile.getProfileUser().getUsername());
        }
	}
	
	/**
	 * Adds a preferred label to a concept
	 * @param currentConcept the current concept
	 * @param label the label to be assigned to the concept
	 * @param language the language of the label
	 * @throws Exception if the language already has a label defined or if the label is already an alternative label for the language
	 */
	public void addPrefLabel(Concept currentConcept, String label, String language) throws Exception {
        String isLabel = currentConcept.getPrefLabels().get(language);
        if (isLabel != null)
        	throw new Exception("The selected language already has a preferred label set!");
        List<String> altLabels = currentConcept.getAltLabels().get(language);
        if (altLabels != null)
        	if (altLabels.contains(label) == true)
        		throw new Exception("This label is already an alternative label for this language!");
        currentConcept.getPrefLabels().put(language, label);
        currentConcept.setModified(currentProfile.getProfileUser().getUsername());
        rdfModel.addPrefLabel(currentConcept.getUUID(), label, language, currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Replaces an existing preferred label with a new one. Both labels are in the same language.
	 * @param currentConcept the current concept to be re-labeled
	 * @param oldLabel the existing label
	 * @param newLabel the new label
	 * @param language the language of the labels
	 * @throws Exception if the language does not have a preferred label defined, if the old label does not match the existing label, or if the new label is already an alternative label for the language
	 */
	public void editPrefLabel(Concept currentConcept, String oldLabel, String newLabel, String language) throws Exception {
        String isLabel = currentConcept.getPrefLabels().get(language);
        if (isLabel == null)
        	throw new Exception("The selected language already does not have a preferred label set!");
        	// addPrefLabel(currentConcept, newLabel, language);
        if (isLabel.equals(oldLabel) == false)
        	throw new Exception("The old label does not match the existing label!");
        List<String> altLabels = currentConcept.getAltLabels().get(language);
        if (altLabels != null)
        	if (altLabels.contains(newLabel) == true)
        		throw new Exception("This label is already an alternative label for this language!");	
        currentConcept.getPrefLabels().remove(language);
        currentConcept.getPrefLabels().put(language, newLabel);
        currentConcept.setModified(currentProfile.getProfileUser().getUsername());
        rdfModel.editPrefLabel(currentConcept.getUUID(), oldLabel, newLabel, language, currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Removes a preferred label from a concept
	 * @param currentConcept the current concept 
	 * @param label the label to be removed
	 * @param language the language of the label
	 * @throws Exception if there is no preferred label set for the given language
	 */
	public void removePrefLabel(Concept currentConcept, String label, String language) throws Exception{
        String isLabel = currentConcept.getPrefLabels().get(language);
        if (isLabel == null)
        	throw new Exception("There is no preferred label for this language!");
        currentConcept.getPrefLabels().remove(language);
        currentConcept.setModified(currentProfile.getProfileUser().getUsername());
        rdfModel.removePrefLabel(currentConcept.getUUID(), label, language, currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Adds an alternative label to a concept.
	 * @param currentConcept the concept to be labeled
	 * @param label the label to be assigned to the concept
	 * @param language the language of the label
	 * @throws Exception if the label is already a preferred label for the given language
	 */
	public void addAltLabel(Concept currentConcept, String label, String language) throws Exception{
		String prefLabel = currentConcept.getPrefLabels().get(language);
		if (prefLabel != null)
			if (prefLabel.equals(label) == true)
				throw new Exception("This label is already a preferred label for this language!");
        List<String> altLabels = currentConcept.getAltLabels().get(language);
		if (altLabels == null)
		    altLabels = new ArrayList<String>();
		if (altLabels.contains(label) == false) {
			altLabels.add(label);
			currentConcept.getAltLabels().put(language, altLabels);
		    currentConcept.setModified(currentProfile.getProfileUser().getUsername());
			rdfModel.addAltLabel(currentConcept.getUUID(), label, language, currentProfile.getProfileUser().getUsername());
		}
	}
	
	/**
	 * Replaces an existing alternative label with a new one. Both labels are in the same language. 
	 * @param currentConcept the current concept
	 * @param oldLabel the existing label
	 * @param newLabel the new label
	 * @param language the language of the labels
	 * @throws Exception if there are no labels set, if there is no old label set, or if the new label is already a preferred label for the given language
	 */
	public void editAltLabel(Concept currentConcept, String oldLabel, String newLabel, String language) throws Exception{
		String prefLabel = currentConcept.getPrefLabels().get(language);
		if (prefLabel != null)
			if (prefLabel.equals(newLabel) == true)
				throw new Exception("This label is already a preferred label for this language!");
        List<String> labels = currentConcept.getAltLabels().get(language);
        if (labels == null)
        	throw new Exception("There are no labels defined for this language");
        int index = labels.indexOf(oldLabel);
        if (index < 0)
        	throw new Exception("This old label does not exist for this language!");
        labels.set(index, newLabel);
        currentConcept.setModified(currentProfile.getProfileUser().getUsername());
    	rdfModel.editAltLabel(currentConcept.getUUID(), oldLabel, newLabel, language, currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Removes an alternative label from a concept
	 * @param currentConcept the current concept
	 * @param label the label to be removed
	 * @param language the language of the label
	 * @throws Exception if there are no alternative labels or if the label does not exist for the given language
	 */
	public void removeAltLabel(Concept currentConcept, String label, String language) throws Exception{
        List<String> labels = currentConcept.getAltLabels().get(language);
        if (labels == null)
        	throw new Exception("There are no alternative labels for this language!");
        if (labels.contains(label) == false)
        	throw new Exception("The label does not exist as an alternative label for this language!");
        labels.remove(label);
        currentConcept.getDefinitions().put(language, labels);
        currentConcept.setModified(currentProfile.getProfileUser().getUsername());
        rdfModel.removeAltLabel(currentConcept.getUUID(), label, language, currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Adds the latitude to a concept. The latitude must be a valid {@code float} number.
	 * @param currentConcept the current concept
	 * @param latitude the latitude of the concept
	 */
	public void addLatitude(Concept currentConcept, String latitude) throws NumberFormatException {
		try {
			Float.parseFloat(latitude);
		} catch (NumberFormatException ex) {
			throw ex;
		}
		currentConcept.setLatitude(latitude);
	    currentConcept.setModified(currentProfile.getProfileUser().getUsername());
		rdfModel.addLatitude(currentConcept.getUUID(), latitude, currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Replaces the latitude for a concept. The latitude must be a valid {@code float} number.
	 * @param currentConcept the current concept
	 * @param latitude the latitude of the concept
	 */
	public void editLatitude(Concept currentConcept, String latitude) throws NumberFormatException {
		try {
			Float.parseFloat(latitude);
		} catch (NumberFormatException ex) {
			throw ex;
		}
		currentConcept.setLatitude(latitude);
	    currentConcept.setModified(currentProfile.getProfileUser().getUsername());
		rdfModel.editLatitude(currentConcept.getUUID(), latitude, currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Removes the latitude from a concept.
	 * @param currentConcept the current concept
	 */
	public void removeLatitude(Concept currentConcept) {
		currentConcept.setLatitude(null);
	    currentConcept.setModified(currentProfile.getProfileUser().getUsername());
		rdfModel.removeLatitude(currentConcept.getUUID(),currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Adds the longitude to a concept. The latitude must be a valid {@code float} number.
	 * @param currentConcept the current concept
	 * @param longitude the longitude of the concept
	 */
	public void addLongitude(Concept currentConcept, String longitude) throws NumberFormatException {
		try {
			Float.parseFloat(longitude);
		} catch (NumberFormatException ex) {
			throw ex;
		}
		currentConcept.setLongitude(longitude);
	    currentConcept.setModified(currentProfile.getProfileUser().getUsername());
		rdfModel.addLongitude(currentConcept.getUUID(), longitude, currentProfile.getProfileUser().getUsername());
	}	
	
	/**
	 * Replaces the longitude for a concept. The latitude must be a valid {@code float} number.
	 * @param currentConcept the current concept
	 * @param longitude the longitude of the concept
	 */
	public void editLongitude(Concept currentConcept, String longitude) throws NumberFormatException {
		try {
			Float.parseFloat(longitude);
		} catch (NumberFormatException ex) {
			throw ex;
		}
		currentConcept.setLongitude(longitude);
	    currentConcept.setModified(currentProfile.getProfileUser().getUsername());
		rdfModel.editLongitude(currentConcept.getUUID(), longitude, currentProfile.getProfileUser().getUsername());
	}
	
	/**
	 * Removes the longitude from a concept.
	 * @param currentConcept the current concept
	 */
	public void removeLongitude(Concept currentConcept) {
		currentConcept.setLongitude(null);
	    currentConcept.setModified(currentProfile.getProfileUser().getUsername());
		rdfModel.removeLongitude(currentConcept.getUUID(), currentProfile.getProfileUser().getUsername());
	}
	
	public void printAsObject(Concept currentConcept)
	{
		if (currentConcept !=null)
		{
			System.out.println();
			System.out.println("<<<");
			System.out.println("Concept: " + currentConcept.getName());
			System.out.println("PreferredLabel:" + (currentConcept.getPrefLabels()==null?"":currentConcept.getPrefLabels()));
			System.out.println("AlternativeLabel:" + (currentConcept.getAltLabels()==null?"":currentConcept.getAltLabels()));
			
			for (Concept parentC : currentConcept.getParents()){
				System.out.println("Parinte: " + parentC.getName());
			}
			
			for (Concept relatedC: currentConcept.getRelated()){
				System.out.println("Related: " + relatedC.getName());
			}
			
            for (String key : currentConcept.getDefinitions().keySet()){
                System.out.print("Definition: ");
                System.out.print("language: " + key );
                List<String> values = currentConcept.getDefinitions().get(key);
                if (values != null)
                        for (String val : values){
                                System.out.println(" value: " + val);
                        }
            }

			for (Concept tempConcept : currentConcept.getChildren()){
				System.out.print(" child: ");
				printAsObject(tempConcept);
			}
			System.out.println(">>>");
		}
	}

	public Profile getCurrentProfile() {
		return currentProfile;
	}

	public void setCurrentProfile(Profile currentProfile) {
		this.currentProfile = currentProfile;
	}

	public ThesaurRDFMethods getRdfModel() {
		return rdfModel;
	}

	public void setRdfModel(ThesaurRDFMethods rdfModel) {
		this.rdfModel = rdfModel;
	}
	
	
}
