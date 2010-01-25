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
	
	Model rdfModel = ModelFactory.createDefaultModel();
	
	Property narrowChildProperty = rdfModel.createProperty(SKOSVocabulary.NARROWER.toString());
	Property broadParentProperty = rdfModel.createProperty(SKOSVocabulary.BROADER.toString());
	Property relatedPrperty = rdfModel.createProperty(SKOSVocabulary.RELATED.toString());
	
	Property preferredNameProperty = rdfModel.createProperty("PreferredName");
	Property definitionProperty = rdfModel.createProperty(SKOSVocabulary.DEFINITION.toString());
	
	public ThesaurRDFMethods(){
		
	}
	
	public void addRootConceptToRDF(String name){
		rdfModel.createResource(name);
	}

	public void addNarrowerConceptToRDF(String currentName, String newName){
		Resource currentR2 = rdfModel.getResource(currentName);
		if (currentR2 != null){
			Statement temp = currentR2.getProperty(getNarrowChildProperty());
			 Bag childsBag;
			 if (temp ==null || (temp !=null && temp.getBag()==null)){
				 childsBag = rdfModel.createBag();
				 currentR2.addProperty(getNarrowChildProperty(), childsBag);
			 }
			 else{
				 childsBag = temp.getBag();
			 }
			 Resource narrowConcept = rdfModel.createResource(newName);
			 childsBag.add(narrowConcept);
		}
	}
	
	public void addBroaderConceptToRDF(String currentName, String parentExistingName){
		Resource currentR2 = rdfModel.getResource(currentName);
		if (currentR2 != null){
			 Bag parentsBag = currentR2.getProperty(getBroadParentProperty()).getBag();
			 if (parentsBag ==null){
				 parentsBag = rdfModel.createBag();
				 currentR2.addProperty(getBroadParentProperty(), parentsBag);
			 }
			 Resource broadConcept = rdfModel.getResource(parentExistingName);
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
		Resource currentR = rdfModel.getResource(currentName);
		currentR.addProperty(DC.creator, author);
	}
	
	public void addMetadataDateCreatedRDF(String currentName, String date){
		Resource currentR = rdfModel.getResource(currentName);
		currentR.addProperty(DC.date, date);
	}
	
	//definition
	public void addDefinitionPerLanguageRDf(String currentName, String definition, String language )
	{
		Resource currentR2 = rdfModel.getResource(currentName);
		if (currentR2 != null){
			Statement objR = currentR2.getProperty(getDefinitionProperty());
			Bag defBag = null;
			if (objR==null  || (objR!=null && objR.getBag()==null)){
					defBag = rdfModel.createBag();
					currentR2.addProperty(getDefinitionProperty(), defBag);
			}
			else
				defBag = objR.getBag();
			 
			 Resource definitionResource = rdfModel.createResource(definition);
			 definitionResource.addProperty(DC.language, language);
			 
			 defBag.add(definitionResource);
		}
	}
	
	//related
	public void addRelatedRDF(String currentName, String relatedName){
		Resource current = rdfModel.getResource(currentName);
		Resource related = rdfModel.getResource(relatedName);
		
		Bag relatedBag = current.getProperty(getRelatedPrperty()).getBag();
		if (relatedBag == null){
			relatedBag = rdfModel.createBag();
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

	public Property getPreferredNameProperty() {
		return preferredNameProperty;
	}

	public Property getDefinitionProperty() {
		return definitionProperty;
	}
	
}
