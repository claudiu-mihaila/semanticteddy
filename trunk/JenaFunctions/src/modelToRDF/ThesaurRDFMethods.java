package modelToRDF;

import java.util.UUID;

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
	
	public void addRootConceptToRDF(UUID uuid, String name){
		Resource newResource = rdfModel.createResource(projectUri + uuid.toString());
		newResource.addProperty(getPrefLabelProperty(), name);
	}

	public Resource createResourceWithName(UUID uuid, String name){
		Resource newResource = rdfModel.createResource(projectUri + uuid.toString());
		newResource.addProperty(getPrefLabelProperty(), name);
		return newResource;
	}
	
	public void addNarrowerConceptToRDF(UUID currentName, UUID newUuid, String newName){
		//var cu bag
//		Resource currentR2 = rdfModel.getResource(projectUri + currentName);
//		if (currentR2 != null){
//			Statement temp = currentR2.getProperty(getNarrowChildProperty());
//			 Bag childsBag;
//			 if (temp ==null || (temp !=null && temp.getBag()==null)){
//				 childsBag = rdfModel.createBag(projectUri + "narrowBag");
//				 currentR2.addProperty(getNarrowChildProperty(), childsBag);
//			 }
//			 else{
//				 childsBag = temp.getBag();
//			 }
//			 Resource narrowConcept = createResourceWithName(newName);
//			 childsBag.add(narrowConcept);
//		}
		//var fara bag
		Resource currentR2 = rdfModel.getResource(projectUri + currentName.toString());
		if (currentR2 != null){
			 Resource narrowConcept = createResourceWithName(newUuid, newName);
			 currentR2.addProperty(getNarrowChildProperty(), narrowConcept);
		}
		
	}
	
	public void addBroaderConceptToRDF(UUID currentUUID, UUID parentExistingUUID){
		//var cu bag
//		Resource currentR2 = rdfModel.getResource(projectUri + currentName);
//		if (currentR2 != null){
//			 Bag parentsBag = currentR2.getProperty(getBroadParentProperty()).getBag();
//			 if (parentsBag ==null){
//				 parentsBag = rdfModel.createBag(projectUri + "definitionBag");
//				 currentR2.addProperty(getBroadParentProperty(), parentsBag);
//			 }
//			 Resource broadConcept = rdfModel.getResource(projectUri + parentExistingName);
//			 parentsBag.add(broadConcept);
//			 
//			 Bag childsBag = broadConcept.getProperty(getNarrowChildProperty()).getBag();
//			 if (childsBag ==null){
//				 childsBag = rdfModel.createBag();
//				 broadConcept.addProperty(getNarrowChildProperty(), childsBag);
//			 }
//			 childsBag.add(currentR2);
//		}
		
		//var fara bag
		Resource currentR2 = rdfModel.getResource(projectUri + currentUUID);
		Resource broadConcept = rdfModel.getResource(projectUri + parentExistingUUID);
		
		if (currentR2 != null && broadConcept != null){
				 currentR2.addProperty(getBroadParentProperty(), broadConcept);
		}
	}

	//related
	public void addRelatedRDF(UUID currentName, UUID relatedName){
		//var cu bag
//		Resource current = rdfModel.getResource(projectUri + currentName);
//		Resource related = rdfModel.getResource(projectUri + relatedName);
//		
//		Bag relatedBag = current.getProperty(getRelatedPrperty()).getBag();
//		if (relatedBag == null){
//			relatedBag = rdfModel.createBag(projectUri + "relatedBag");
//		}
//		relatedBag.add(related);
		
		//var fara bag
		Resource current = rdfModel.getResource(projectUri + currentName.toString());
		Resource related = rdfModel.getResource(projectUri + relatedName.toString());
		
		if (current!= null && related != null)
			current.addProperty(getRelatedPrperty(), related);
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
		//var cu bag
//		Resource currentR2 = rdfModel.getResource(projectUri + currentName);
//		if (currentR2 != null){
//			Statement objR = currentR2.getProperty(getDefinitionProperty());
//			Bag defBag = null;
//			if (objR==null  || (objR!=null && objR.getBag()==null)){
//					defBag = rdfModel.createBag(projectUri+"definitionBag");
//					currentR2.addProperty(getDefinitionProperty(), defBag);
//			}
//			else
//				defBag = objR.getBag();
//			 
//			 Resource definitionResource = createResourceWithName(definition);
//			 definitionResource.addProperty(DC.language, language);
//			 
//			 defBag.add(definitionResource);	
//		}
		//var fara bag
		;
	}
	
	public void editPrefLabel(UUID currentName, String label){
		Resource currentR2 = rdfModel.getResource(projectUri + currentName.toString());
	    currentR2.removeAll(getPrefLabelProperty());
	    currentR2.addProperty(getPrefLabelProperty(), label);
	}
	
	public void addAltLabel(UUID currentName, String label){
		Resource currentR2 = rdfModel.getResource(projectUri + currentName.toString());
	    currentR2.addProperty(getAltLabelProperty(), label);
	}
	
	public void editAltLabel(UUID currentUUID, String label){
		Resource currentR2 = rdfModel.getResource(projectUri + currentUUID.toString());
	    currentR2.removeAll(getAltLabelProperty());
	    currentR2.addProperty(getAltLabelProperty(), label);
	}
	
	public void printRDfModel(){
		rdfModel.setNsPrefix( "Teddy", projectUri );
		rdfModel.setNsPrefix("SKOS", SKOSURI);
		rdfModel.write(System.out);
//		rdfModel.write(System.out, "N-TRIPLE");
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
