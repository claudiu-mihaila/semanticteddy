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
	
	public Concept addRootConcept(String name){
		Concept rootConcept = new Concept(name, Globals.defaultLanguage);
		rdfModel.addRootConcept(rootConcept.getUUID(), name, Globals.defaultLanguage);
		return rootConcept;
	}
	
	public Concept addChildConcept(Concept parentConcept, String name){
		Concept childConcept = new Concept(name, Globals.defaultLanguage);
		parentConcept.getChildren().add(childConcept);
		rdfModel.addNarrowerResource(parentConcept.getUUID(), childConcept.getUUID(), name, Globals.defaultLanguage);
		return childConcept;
	}
	
	public Concept removeChildConcept(Concept parentConcept, Concept childConcept) {
		parentConcept.getChildren().remove(childConcept);
		childConcept.getParents().remove(parentConcept);
		rdfModel.removeNarrowerResource(parentConcept.getUUID(), childConcept.getUUID());
		rdfModel.removeBroaderResource(childConcept.getUUID(), parentConcept.getUUID());
		return parentConcept;
	}
	
	public void addParentConcept(Concept childConcept, Concept parentConcept){
		childConcept.getParents().add(parentConcept);
		parentConcept.getChildren().add(childConcept);
		
		rdfModel.addBroaderResource(childConcept.getUUID(), parentConcept.getUUID());
	}
	
	public Concept removeParentConcept(Concept childConcept, Concept parentConcept) {
		childConcept.getParents().remove(parentConcept);
		parentConcept.getChildren().remove(childConcept);
		rdfModel.removeNarrowerResource(parentConcept.getUUID(), childConcept.getUUID());
		rdfModel.removeBroaderResource(childConcept.getUUID(), parentConcept.getUUID());
		return childConcept;
	}
	
	public void addRelatedConcept(Concept currentConcept, Concept relatedConcept){
		currentConcept.getRelated().add(relatedConcept);
		relatedConcept.getRelated().add(currentConcept);
		rdfModel.addRelatedResource(currentConcept.getUUID(), relatedConcept.getUUID());
	}
	
	public void removeRelatedConcept(Concept currentConcept, Concept relatedConcept) {
		currentConcept.getRelated().remove(relatedConcept);
		relatedConcept.getRelated().remove(currentConcept);
		rdfModel.removeRelatedResource(currentConcept.getUUID(), relatedConcept.getUUID());
		rdfModel.removeRelatedResource(relatedConcept.getUUID(), currentConcept.getUUID());
	}
	
	public void addDefinition(Concept currentConcept, String definition, String language){
        List<String> definitions = currentConcept.getDefinitions().get(language);
        if (definitions == null)
                definitions = new ArrayList<String>();
        definitions.add(definition);
        currentConcept.getDefinitions().put(language, definitions);
        rdfModel.addDefinition(currentConcept.getUUID(), definition, language);
	}
	
	public void editDefinition(Concept currentConcept, String oldDefinition, String newDefinition, String language){
        List<String> definitions = currentConcept.getDefinitions().get(language);
        int index = definitions.indexOf(oldDefinition);
        if (index >= 0) {
        	definitions.set(index, newDefinition);
    		rdfModel.editDefinition(currentConcept.getUUID(), oldDefinition, newDefinition, language);
        }
	}

	public void removeDefinition(Concept currentConcept, String definition, String language){
        List<String> definitions = currentConcept.getDefinitions().get(language);
        if (definitions != null)
        {
            definitions.remove(definition);
            currentConcept.getDefinitions().put(language, definitions);
            rdfModel.removeDefinition(currentConcept.getUUID(), definition, language);
        }
	}
	
	public void addPrefLabel(Concept currentConcept, String label, String language) {
        String isLabel = currentConcept.getPrefLabels().get(language);
        if (isLabel == null) {
        	currentConcept.getPrefLabels().put(language, isLabel);
        	rdfModel.addPrefLabel(currentConcept.getUUID(), label, language);
        }
	}
	
	public void editPrefLabel(Concept currentConcept, String oldLabel, String newLabel, String language) {
        String isLabel = currentConcept.getPrefLabels().get(language);
        if (isLabel != null) {
        	currentConcept.getPrefLabels().remove(language);
        	currentConcept.getPrefLabels().put(language, newLabel);
        	rdfModel.editPrefLabel(currentConcept.getUUID(), oldLabel, newLabel, language);
        }
	}
	
	public void addAltLabel(Concept currentConcept, String label, String language){
        List<String> labels = currentConcept.getAltLabels().get(language);
        if (labels == null)
            labels = new ArrayList<String>();
        labels.add(label);
        currentConcept.getAltLabels().put(language, labels);
        rdfModel.addAltLabel(currentConcept.getUUID(), label, language);
	}
	
	public void editAltLabel(Concept currentConcept, String oldLabel, String newLabel, String language){
        List<String> labels = currentConcept.getAltLabels().get(language);
        int index = labels.indexOf(oldLabel);
        if (index >= 0) {
        	labels.set(index, newLabel);
    		rdfModel.editAltLabel(currentConcept.getUUID(), oldLabel, newLabel, language);
        }
	}
	
	public void removeAltLabel(Concept currentConcept, String label, String language){
        List<String> labels = currentConcept.getAltLabels().get(language);
        if (labels != null)
        {
        	labels.remove(label);
            currentConcept.getDefinitions().put(language, labels);
            rdfModel.removeAltLabel(currentConcept.getUUID(), label, language);
        }
	}
	
	public void addLatitude(Concept currentConcept, String latitude) {
		currentConcept.setLatitude(latitude);
		rdfModel.addLatitude(currentConcept.getUUID(), latitude);
	}
	
	public void editLatitude(Concept currentConcept, String latitude) {
		currentConcept.setLatitude(latitude);
		rdfModel.editLatitude(currentConcept.getUUID(), latitude);
	}
	
	public void removeLatitude(Concept currentConcept) {
		currentConcept.setLatitude(null);
		rdfModel.removeLatitude(currentConcept.getUUID());
	}
	
	public void addLongitude(Concept currentConcept, String longitude) {
		currentConcept.setLongitude(longitude);
		rdfModel.addLongitude(currentConcept.getUUID(), longitude);
	}	
	
	public void editLongitude(Concept currentConcept, String longitude) {
		currentConcept.setLongitude(longitude);
		rdfModel.editLongitude(currentConcept.getUUID(), longitude);
	}
	
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
