package methods;

import java.util.ArrayList;
import java.util.List;
import utils.Globals;
import model.Concept;
import modelToRDF.ThesaurRDFMethods;

public class ThesaurJavaMethods {
	
	public ThesaurRDFMethods rdfModel ;
	
	public ThesaurJavaMethods(){
		rdfModel = new ThesaurRDFMethods();
	}
	
	/**
	 * Creates a root concept in the model
	 * @param name the name of the root concept
	 * @return the newly created root concept
	 */
	public Concept addRootConcept(String name){
		Concept rootConcept = new Concept(name, Globals.defaultLanguage);
		Globals.rootConcepts.add(rootConcept);
		rdfModel.addRootConcept(rootConcept.getUUID(), name, Globals.defaultLanguage);
		return rootConcept;
	}

//	public void removeRootConcept(Concept rootConcept) {
//		Globals.rootConcepts.remove(rootConcept);
//	}
	
	/**
	 * Creates a new child concept
	 * @param parentConcept the current concept, which will be the parent
	 * @param name the name of the child concept
	 * @return the newly created child concept
	 */
	public Concept addChildConcept(Concept parentConcept, String name){
		Concept childConcept = new Concept(name, Globals.defaultLanguage);
		parentConcept.getChildren().add(childConcept);
		rdfModel.addNarrowerResource(parentConcept.getUUID(), childConcept.getUUID(), name, Globals.defaultLanguage);
		return childConcept;
	}
	
	/**
	 * Removes a concept from a parent's children
	 * @param parentConcept the current parent concept
	 * @param childConcept the child concept to be removed from the parent's children
	 * @return the parent concept without the child concept
	 */
	public Concept removeChildConcept(Concept parentConcept, Concept childConcept) {
		parentConcept.getChildren().remove(childConcept);
		childConcept.getParents().remove(parentConcept);
		rdfModel.removeNarrowerResource(parentConcept.getUUID(), childConcept.getUUID());
		rdfModel.removeBroaderResource(childConcept.getUUID(), parentConcept.getUUID());
		return parentConcept;
	}
	
	/**
	 * Adds an already existing concept to a concept's parents
	 * @param childConcept the current child concept
	 * @param parentConcept the already existing parent
	 */
	public void addParentConcept(Concept childConcept, Concept parentConcept){
		childConcept.getParents().add(parentConcept);
		parentConcept.getChildren().add(childConcept);
		rdfModel.addBroaderResource(childConcept.getUUID(), parentConcept.getUUID());
	}
	
	/**
	 * Removes a concept from a child's parents
	 * @param childConcept the current child concept
	 * @param parentConcept the parent concept to be removed from the child's parents
	 * @return the child concept without the parent concept
	 */
	public Concept removeParentConcept(Concept childConcept, Concept parentConcept) {
		childConcept.getParents().remove(parentConcept);
		parentConcept.getChildren().remove(childConcept);
		rdfModel.removeNarrowerResource(parentConcept.getUUID(), childConcept.getUUID());
		rdfModel.removeBroaderResource(childConcept.getUUID(), parentConcept.getUUID());
		return childConcept;
	}
	
	/**
	 * Adds an already existing concept to a concept's related concepts 
	 * @param currentConcept the current concept
	 * @param relatedConcept the already existing concept 
	 */
	public void addRelatedConcept(Concept currentConcept, Concept relatedConcept){
		currentConcept.getRelated().add(relatedConcept);
		relatedConcept.getRelated().add(currentConcept);
		rdfModel.addRelatedResource(currentConcept.getUUID(), relatedConcept.getUUID());
	}
	
	/**
	 * Removes a concept from a concept's related concepts
	 * @param currentConcept the current concept
	 * @param relatedConcept the related concept to be removed
	 */
	public void removeRelatedConcept(Concept currentConcept, Concept relatedConcept) {
		currentConcept.getRelated().remove(relatedConcept);
		relatedConcept.getRelated().remove(currentConcept);
		rdfModel.removeRelatedResource(currentConcept.getUUID(), relatedConcept.getUUID());
		rdfModel.removeRelatedResource(relatedConcept.getUUID(), currentConcept.getUUID());
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
        rdfModel.addDefinition(currentConcept.getUUID(), definition, language);
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
    		rdfModel.editDefinition(currentConcept.getUUID(), oldDefinition, newDefinition, language);
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
            rdfModel.removeDefinition(currentConcept.getUUID(), definition, language);
        }
	}
	
	/**
	 * Adds a preferred label to a concept
	 * @param currentConcept the current concept
	 * @param label the label to be assigned to the concept
	 * @param language the language of the label
	 */
	public void addPrefLabel(Concept currentConcept, String label, String language) {
        String isLabel = currentConcept.getPrefLabels().get(language);
        if (isLabel == null) {
        	currentConcept.getPrefLabels().put(language, isLabel);
        	rdfModel.addPrefLabel(currentConcept.getUUID(), label, language);
        }
	}
	
	/**
	 * Replaces an existing preferred label with a new one. Both labels are in the same language.
	 * @param currentConcept the current concept to be re-labeled
	 * @param oldLabel the existing label
	 * @param newLabel the new label
	 * @param language the language of the labels
	 */
	public void editPrefLabel(Concept currentConcept, String oldLabel, String newLabel, String language) {
        String isLabel = currentConcept.getPrefLabels().get(language);
        if (isLabel != null) {
        	currentConcept.getPrefLabels().remove(language);
        	currentConcept.getPrefLabels().put(language, newLabel);
        	rdfModel.editPrefLabel(currentConcept.getUUID(), oldLabel, newLabel, language);
        }
	}
	
	/**
	 * Removes a preferred label from a concept
	 * @param currentConcept the current concept 
	 * @param label the label to be removed
	 * @param language the language of the label
	 */
	public void removePrefLabel(Concept currentConcept, String label, String language){
        String isLabel = currentConcept.getPrefLabels().get(language);
        if (isLabel != null)
        {
        	currentConcept.getPrefLabels().remove(language);
            rdfModel.removePrefLabel(currentConcept.getUUID(), label, language);
        }
	}
	
	/**
	 * Adds an alternative label to a concept.
	 * @param currentConcept the concept to be labeled
	 * @param label the label to be assigned to the concept
	 * @param language the language of the label
	 */
	public void addAltLabel(Concept currentConcept, String label, String language){
        List<String> labels = currentConcept.getAltLabels().get(language);
        if (labels == null)
            labels = new ArrayList<String>();
        labels.add(label);
        currentConcept.getAltLabels().put(language, labels);
        rdfModel.addAltLabel(currentConcept.getUUID(), label, language);
	}
	
	/**
	 * Replaces an existing alternative label with a new one. Both labels are in the same language. 
	 * @param currentConcept the current concept
	 * @param oldLabel the existing label
	 * @param newLabel the new label
	 * @param language the language of the labels
	 */
	public void editAltLabel(Concept currentConcept, String oldLabel, String newLabel, String language){
        List<String> labels = currentConcept.getAltLabels().get(language);
        int index = labels.indexOf(oldLabel);
        if (index >= 0) {
        	labels.set(index, newLabel);
    		rdfModel.editAltLabel(currentConcept.getUUID(), oldLabel, newLabel, language);
        }
	}
	
	/**
	 * Removes an alternative label from a concept
	 * @param currentConcept the current concept
	 * @param label the label to be removed
	 * @param language the language of the label
	 */
	public void removeAltLabel(Concept currentConcept, String label, String language){
        List<String> labels = currentConcept.getAltLabels().get(language);
        if (labels != null)
        {
        	labels.remove(label);
            currentConcept.getDefinitions().put(language, labels);
            rdfModel.removeAltLabel(currentConcept.getUUID(), label, language);
        }
	}
	
	/**
	 * Adds the latitude to a concept. The latitude must be a valid {@code float} number.
	 * @param currentConcept the current concept
	 * @param latitude the latitude of the concept
	 */
	public void addLatitude(Concept currentConcept, String latitude) {
		currentConcept.setLatitude(latitude);
		rdfModel.addLatitude(currentConcept.getUUID(), latitude);
	}
	
	/**
	 * Replaces the latitude for a concept. The latitude must be a valid {@code float} number.
	 * @param currentConcept the current concept
	 * @param latitude the latitude of the concept
	 */
	public void editLatitude(Concept currentConcept, String latitude) {
		currentConcept.setLatitude(latitude);
		rdfModel.editLatitude(currentConcept.getUUID(), latitude);
	}
	
	/**
	 * Removes the latitude from a concept.
	 * @param currentConcept the current concept
	 */
	public void removeLatitude(Concept currentConcept) {
		currentConcept.setLatitude(null);
		rdfModel.removeLatitude(currentConcept.getUUID());
	}
	
	/**
	 * Adds the longitude to a concept. The latitude must be a valid {@code float} number.
	 * @param currentConcept the current concept
	 * @param longitude the longitude of the concept
	 */
	public void addLongitude(Concept currentConcept, String longitude) {
		currentConcept.setLongitude(longitude);
		rdfModel.addLongitude(currentConcept.getUUID(), longitude);
	}	
	
	/**
	 * Replaces the longitude for a concept. The latitude must be a valid {@code float} number.
	 * @param currentConcept the current concept
	 * @param longitude the longitude of the concept
	 */
	public void editLongitude(Concept currentConcept, String longitude) {
		currentConcept.setLongitude(longitude);
		rdfModel.editLongitude(currentConcept.getUUID(), longitude);
	}
	
	/**
	 * Removes the longitude from a concept.
	 * @param currentConcept the current concept
	 */
	public void removeLongitude(Concept currentConcept) {
		currentConcept.setLongitude(null);
		rdfModel.removeLongitude(currentConcept.getUUID());
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
	
}
