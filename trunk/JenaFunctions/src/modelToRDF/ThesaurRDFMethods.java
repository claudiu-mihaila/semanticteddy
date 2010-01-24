package modelToRDF;

import org.sealife.skos.editor.SKOSVocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.VCARD;

public class ThesaurRDFMethods {
	
	Model rdfModel = ModelFactory.createDefaultModel();
	
	Property narrowChildProperty = rdfModel.createProperty(SKOSVocabulary.NARROWER.toString());
	Property broadParentProperty = rdfModel.createProperty(SKOSVocabulary.BROADER.toString());
	Property relatedPrperty = rdfModel.createProperty(SKOSVocabulary.RELATED.toString());
	
	Property preferredNameProperty = rdfModel.createProperty("PreferredName");
	
	public ThesaurRDFMethods(){
		
	}
	
	public void addRootConceptToRDF(String name){
		rdfModel.createResource(name);
	}

	public void addNarrowerConceptToRDF(String currentName, String newName){
		Resource currentR = rdfModel.getResource(currentName);
		if (currentR !=null){
			Resource narrowConcept = rdfModel.createResource(newName);
			currentR.addProperty(getNarrowChildProperty(), narrowConcept);
		}
	}
	
	public void addBroaderConceptToRDF(String currentName, String newName){
		Resource currentR = rdfModel.getResource(currentName);
		if (currentR !=  null)
		{
			Resource broadConcept = rdfModel.createResource(newName);
			currentR.addProperty(getBroadParentProperty(), broadConcept);
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
}
