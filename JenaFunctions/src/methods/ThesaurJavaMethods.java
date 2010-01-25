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
		rdfModel.addRootConceptToRDF(name);
		return rootConcept;
	}
	
	public void addChildConcept(Concept currentConcept, String name){
		currentConcept.getChilds().add(new Concept(name));
		rdfModel.addNarrowerConceptToRDF(currentConcept.getName(), name);
	}
	
	public void addParentConcept(Concept currentConcept, Concept parent){
		currentConcept.getParents().add(parent);
		parent.getChilds().add(currentConcept);
		
		rdfModel.addBroaderConceptToRDF(currentConcept.getName(), parent.getName());
	}
	
	public void addDefinition(Concept currentConcept, String definition, String language){
		List<String> definitions = currentConcept.getDefinitionPerLanguage().get(language);
		if (definitions == null)
			definitions = new ArrayList<String>();
		definitions.add(definition);
		currentConcept.getDefinitionPerLanguage().put(language, definitions);
		rdfModel.addDefinitionPerLanguageRDf(currentConcept.getName(), definition, language);
	}
	
	//related
	public void addRelated(Concept currentConcept, Concept relatedConcept){
		currentConcept.getRelated().add(relatedConcept);
		relatedConcept.getRelated().add(currentConcept);
		rdfModel.addRelatedRDF(currentConcept.getName(), relatedConcept.getName());
	}
	
	public void printAsObject(Concept currentConcept)
	{
		if (currentConcept !=null)
		{
			System.out.println();
			System.out.println("<<<");
			System.out.println("Concept: " + currentConcept.getName());
			System.out.println("PreferredName:" + (currentConcept.getPreferedName()==null?"":currentConcept.getPreferedName()));
			
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
				if (values !=null)
					for (String val : values){
						System.out.println(" value: " + val);
					}
			}
			for (Concept tempConcept : currentConcept.getChilds()){
				System.out.print(" child: ");
				printAsObject(tempConcept);
			}
			System.out.println(">>>");
		}
	}
}
