package modelToRDF;

import model.Concept;

import org.sealife.skos.editor.SKOSVocabulary;

import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DC;

public class ThesaurRDFMethods {
	
	String projectUri= "http://Teddy#";
	String SKOSURI = "http://www.w3.org/2004/02/skos/core#";
	
	Model rdfModel = ModelFactory.createDefaultModel();
	
	Property narrowChildProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.NARROWER.toString());
	Property broadParentProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.BROADER.toString());
	Property relatedPrperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.RELATED.toString());
	
	Property prefLabelProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.PREFLABEL.toString());
	Property altLabelProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.ALTLABEL.toString());
	Property definitionProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.DEFINITION.toString());
	
	public ThesaurRDFMethods(){
		
	}
	
	public void addRootConceptToRDF(String name){
		Resource newResource = rdfModel.createResource(projectUri + name);
		newResource.addProperty(getPrefLabelProperty(), name);
	}

	public Resource createResourceWithName(String name){
		Resource newResource = rdfModel.createResource(projectUri + name);
		newResource.addProperty(getPrefLabelProperty(), name);
		return newResource;
	}
	
	public void addNarrowerConceptToRDF(String currentName, String newName){
		Resource currentR2 = rdfModel.getResource(projectUri + currentName);
		if (currentR2 != null){
			Statement temp = currentR2.getProperty(getNarrowChildProperty());
			 Bag childsBag;
			 if (temp ==null || (temp !=null && temp.getBag()==null)){
				 childsBag = rdfModel.createBag(projectUri + "narrowBag");
				 currentR2.addProperty(getNarrowChildProperty(), childsBag);
			 }
			 else{
				 childsBag = temp.getBag();
			 }
			 Resource narrowConcept = createResourceWithName(newName);
			 childsBag.add(narrowConcept);
		}
	}
	
	public void addBroaderConceptToRDF(String currentName, String parentExistingName){
		Resource currentR2 = rdfModel.getResource(projectUri + currentName);
		if (currentR2 != null){
			 Bag parentsBag = currentR2.getProperty(getBroadParentProperty()).getBag();
			 if (parentsBag ==null){
				 parentsBag = rdfModel.createBag(projectUri + "definitionBag");
				 currentR2.addProperty(getBroadParentProperty(), parentsBag);
			 }
			 Resource broadConcept = rdfModel.getResource(projectUri + parentExistingName);
			 parentsBag.add(broadConcept);
			 
			 Bag childsBag = broadConcept.getProperty(getNarrowChildProperty()).getBag();
			 if (childsBag ==null){
				 childsBag = rdfModel.createBag();
				 broadConcept.addProperty(getNarrowChildProperty(), childsBag);
			 }
			 childsBag.add(currentR2);
		}
	}

	public void addMetadataAuthorRDF(String currentName, String author){
		Resource currentR = rdfModel.getResource(projectUri + currentName);
		currentR.addProperty(DC.creator, author);
	}
	
	public void addMetadataDateCreatedRDF(String currentName, String date){
		Resource currentR = rdfModel.getResource(projectUri + currentName);
		currentR.addProperty(DC.date, date);
	}
	
	//definition
	public void addDefinitionPerLanguageRDf(String currentName, String definition, String language )
	{
		Resource currentR2 = rdfModel.getResource(projectUri + currentName);
		if (currentR2 != null){
			Statement objR = currentR2.getProperty(getDefinitionProperty());
			Bag defBag = null;
			if (objR==null  || (objR!=null && objR.getBag()==null)){
					defBag = rdfModel.createBag(projectUri+"definitionBag");
					currentR2.addProperty(getDefinitionProperty(), defBag);
			}
			else
				defBag = objR.getBag();
			 
			 Resource definitionResource = createResourceWithName(definition);
			 definitionResource.addProperty(DC.language, language);
			 
			 defBag.add(definitionResource);
		}
	}
	
	public void editPrefLabel(String currentName, String label){
		Resource currentR2 = rdfModel.getResource(projectUri + currentName);
	    currentR2.removeAll(getPrefLabelProperty());
	    currentR2.addProperty(getPrefLabelProperty(), label);
	}
	
	public void addAltLabel(String currentName, String label){
		Resource currentR2 = rdfModel.getResource(projectUri + currentName);
	    currentR2.addProperty(getAltLabelProperty(), label);
	}
	
	public void editAltLabel(String currentName, String label){
		Resource currentR2 = rdfModel.getResource(projectUri + currentName);
	    currentR2.removeAll(getAltLabelProperty());
	    currentR2.addProperty(getAltLabelProperty(), label);
	}
	
	public void printRDfModel(){
		rdfModel.setNsPrefix( "Teddy", projectUri );
		rdfModel.setNsPrefix("SKOS", SKOSURI);

		rdfModel.write(System.out);
	//	rdfModel.write(System.out, "N-TRIPLE");

	}
	
	//related
	public void addRelatedRDF(String currentName, String relatedName){
		Resource current = rdfModel.getResource(projectUri + currentName);
		Resource related = rdfModel.getResource(projectUri + relatedName);
		
		Bag relatedBag = current.getProperty(getRelatedPrperty()).getBag();
		if (relatedBag == null){
			relatedBag = rdfModel.createBag(projectUri + "relatedBag");
		}
		relatedBag.add(related);
		
	}
	public Model getRdfModel() {
		return rdfModel;
	}

	public void setRdfModel(Model rdfModel) {
		this.rdfModel = rdfModel;
	}

	public Property getNarrowChildProperty() {
		return narrowChildProperty;
	}

	public Property getBroadParentProperty() {
		return broadParentProperty;
	}

	public Property getRelatedPrperty() {
		return relatedPrperty;
	}

	public Property getDefinitionProperty() {
		return definitionProperty;
	}

	public Property getPrefLabelProperty() {
		return prefLabelProperty;
	}

	public Property getAltLabelProperty() {
		return altLabelProperty;
	}
	
}
