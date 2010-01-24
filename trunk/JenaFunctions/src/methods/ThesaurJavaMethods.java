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
	
	public void updateDefinition(Concept currentConcept, String definition, String language){
		List<String> definitions = currentConcept.getDefinitionPerLanguage().get(language);
		if (definitions == null)
			definitions = new ArrayList<String>();
		definitions.add(definition);
		
	}
	
	public void printAsObject(Concept currentConcept)
	{
		if (currentConcept !=null)
		{
			System.out.println();
			System.out.println("Concept: " + currentConcept.getName());
			for (Concept tempConcept : currentConcept.getChilds()){
				System.out.print(" child: ");
				printAsObject(tempConcept);
			}
		}
	}
}
