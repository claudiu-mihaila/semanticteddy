package methods;

import java.util.ArrayList;
import java.util.List;

import model.Concept;
import modelToRDF.ThesaurRDFMethods;

public class ThesaurJavaMethods {
	
	public ThesaurRDFMethods rdfModel ;
	
	public ThesaurJavaMethods(){
		rdfModel = new ThesaurRDFMethods();
	}
	
	public Concept addRootConcept(String name){
		Concept rootConcept =  new Concept(name);
		rdfModel.addRootConceptToRDF(rootConcept.getUuid(), name);
		return rootConcept;
	}
	
	public Concept addChildConcept(Concept currentConcept, String name){
		Concept childConcept = new Concept(name);
		currentConcept.getChildren().add(childConcept);
		rdfModel.addNarrowerConceptToRDF(currentConcept.getUuid(), childConcept.getUuid(), name);
		return childConcept;
	}
	
	public void addParentConcept(Concept currentConcept, Concept parent){
		currentConcept.getParents().add(parent);
		parent.getChildren().add(currentConcept);
		
		rdfModel.addBroaderConceptToRDF(currentConcept.getUuid(), parent.getUuid());
	}
	
	public void addDefinition(Concept currentConcept, String definition, String language){
		List<String> definitions = currentConcept.getDefinitionPerLanguage().get(language);
		if (definitions == null)
			definitions = new ArrayList<String>();
		definitions.add(definition);
		currentConcept.getDefinitionPerLanguage().put(language, definitions);
		rdfModel.addDefinitionPerLanguageRDf(currentConcept.getUuid(), definition, language);
	}
	
	//related
	public void addRelated(Concept currentConcept, Concept relatedConcept){
		currentConcept.getRelated().add(relatedConcept);
		relatedConcept.getRelated().add(currentConcept);
		rdfModel.addRelatedRDF(currentConcept.getUuid(), relatedConcept.getUuid());
	}
	
	public void editPrefLabel(Concept currentConcept, String label){
		currentConcept.setPrefLabel(label);
		rdfModel.editPrefLabel(currentConcept.getUuid(), label);
	}
	
	public void addAltLabel(Concept currentConcept, String label){
		currentConcept.setAltLabel(label);
		rdfModel.addAltLabel(currentConcept.getUuid(), label);
	}
	
	public void editAltLabel(Concept currentConcept, String label){
		currentConcept.setAltLabel(label);
		rdfModel.editAltLabel(currentConcept.getUuid(), label);
	}
	
	public void printAsObject(Concept currentConcept)
	{
		if (currentConcept !=null)
		{
			System.out.println();
			System.out.println("<<<");
			System.out.println("Concept: " + currentConcept.getName());
			System.out.println("PreferredLabel:" + (currentConcept.getPrefLabel()==null?"":currentConcept.getPrefLabel()));
			System.out.println("AlternativeLabel:" + (currentConcept.getAltLabel()==null?"":currentConcept.getAltLabel()));
			
			for (Concept parentC : currentConcept.getParents()){
				System.out.println("Parinte: " + parentC.getName());
			}
			
			for (Concept relatedC: currentConcept.getRelated()){
				System.out.println("Related: " + relatedC.getName());
			}
			
			for (String key : currentConcept.getDefinitionPerLanguage().keySet()){
				System.out.print("Definition: ");
				System.out.print("language: " + key );
				List<String> values = currentConcept.getDefinitionPerLanguage().get(key);
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