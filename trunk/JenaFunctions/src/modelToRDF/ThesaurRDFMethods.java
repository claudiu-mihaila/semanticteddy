package modelToRDF;

import java.io.FileWriter;
import java.util.UUID;
import org.sealife.skos.editor.SKOSVocabulary;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

import utils.Globals;
import vocabulary.GeoVocabulary;

public class ThesaurRDFMethods {
	
	String SKOSURI = "http://www.w3.org/2004/02/skos/core#";
	
	Model rdfModel = ModelFactory.createDefaultModel();
	
	Resource conceptResource = rdfModel.createResource(SKOSURI + SKOSVocabulary.CONCEPT);
	Resource conceptSchemeResource = rdfModel.createResource(SKOSURI + SKOSVocabulary.CONCEPTSCHEME);

	Property narrowerProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.NARROWER.toString());
	Property broaderProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.BROADER.toString());
	Property relatedProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.RELATED.toString());
	
	Property prefLabelProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.PREFLABEL.toString());
	Property altLabelProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.ALTLABEL.toString());
	Property definitionProperty = rdfModel.createProperty(SKOSURI + SKOSVocabulary.DEFINITION.toString());

	Resource pointResource = rdfModel.createResource(GeoVocabulary.POINT);
	Property latitudeProperty = rdfModel.createProperty(GeoVocabulary.LATITUDE);
	Property longitudeProperty = rdfModel.createProperty(GeoVocabulary.LONGITUDE);
	
	public ThesaurRDFMethods() {}
	
	/**
	 * Creates a root resource.
	 * @param uuid the UUID of the resource
	 * @param name the name of the resource
	 * @param language
	 */
	public void addRootConcept(UUID uuid, String name, String language) {
		Resource resource = rdfModel.createResource(Globals.projectUri + uuid.toString());
		resource.addProperty(RDF.type, conceptSchemeResource);
		resource.addProperty(getPrefLabelProperty(), name, language);
	}

	public Resource createResourceWithName(UUID uuid, String name, String language){
		Resource resource = rdfModel.createResource(Globals.projectUri + uuid.toString());
		resource.addProperty(RDF.type, conceptResource);
		resource.addProperty(getPrefLabelProperty(), name, language);
		return resource;
	}
	
//	public Resource createResourceWithValue(String value){
//		Resource resource = rdfModel.createResource(Globals.projectUri + value);
//		return resource;
//	}
	
	public void addNarrowerResource(UUID parentUuid, UUID childUuid, String childName, String language){
		Resource parentResource = rdfModel.getResource(Globals.projectUri + parentUuid.toString());
		if (parentResource != null){
			 Resource childResource = createResourceWithName(childUuid, childName, language);
			 parentResource.addProperty(getNarrowerProperty(), childResource);
		}
	}
	
	public void removeNarrowerResource(UUID parentUuid, UUID childUuid) {
		Resource parentResource = rdfModel.getResource(Globals.projectUri + parentUuid.toString());
		Resource childResource = rdfModel.getResource(Globals.projectUri + childUuid.toString());
		
		if (parentResource != null && childResource != null) {
			rdfModel.remove(parentResource, getNarrowerProperty(), childResource.as(RDFNode.class));
		}
	}

	public void addBroaderResource(UUID childUuid, UUID parentUuid){
		Resource childResource = rdfModel.getResource(Globals.projectUri + childUuid);
		Resource parentResource = rdfModel.getResource(Globals.projectUri + parentUuid);
		
		if (childResource != null && parentResource != null){
				 childResource.addProperty(getBroaderProperty(), parentResource);
		}
	}
	
	public void removeBroaderResource(UUID childUuid, UUID parentUuid) {
		Resource parentResource = rdfModel.getResource(Globals.projectUri + parentUuid.toString());
		Resource childResource = rdfModel.getResource(Globals.projectUri + childUuid.toString());
		
		if (parentResource != null && childResource != null) {
			rdfModel.remove(childResource, getBroaderProperty(), parentResource.as(RDFNode.class));
		}
	}
	//related
	public void addRelatedResource(UUID currentUuid, UUID relatedUuid){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		Resource relatedResource = rdfModel.getResource(Globals.projectUri + relatedUuid.toString());
		
		if (currentResource!= null && relatedResource != null)
			currentResource.addProperty(getRelatedProperty(), relatedResource);
	}
	
	public void removeRelatedResource(UUID currentUuid, UUID relatedUuid) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		Resource relatedResource = rdfModel.getResource(Globals.projectUri + relatedUuid.toString());
		
		if (currentResource != null && relatedResource != null) {
			rdfModel.remove(currentResource, getRelatedProperty(), relatedResource.as(RDFNode.class));
		}
	}

	public void addMetadataAuthor(String currentName, String author){
		Resource currentR = rdfModel.getResource(Globals.projectUri + currentName);
		currentR.addProperty(DC.creator, author);
	}
	
	public void addMetadataDateCreated(String currentName, String date){
		Resource currentR = rdfModel.getResource(Globals.projectUri + currentName);
		currentR.addProperty(DC.date, date);
	}
	
	//definition
	public void addDefinition(UUID currentUuid, String definition, String language)
	{
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		if (currentResource != null){
//			// check definitions of current resource if language already included
//			StmtIterator si = currentR.listProperties(getDefinitionProperty());
//			while(si.hasNext()) {
//				Statement s = si.nextStatement();
//				// if language already included, replace with new definition
//				if (language.equals(s.getLanguage())) {
//					s.changeObject(definition, language);
//					return;
//				}
//			}
//			// it not included, include it
			currentResource.addProperty(getDefinitionProperty(), definition, language);
		}
	}
	
	public void editDefinition(UUID currentUuid, String oldDefinition, String newDefinition, String language){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		
		if (currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getDefinitionProperty(), oldDefinition, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				s.changeObject(newDefinition, language);
			}			
		}
	}

	public void removeDefinition(UUID currentUuid, String definition, String language) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		
		if(currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getDefinitionProperty(), definition, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				rdfModel.remove(s);
			}
		}
	}
	
	public void addPrefLabel(UUID currentUuid, String label, String language){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		if (currentResource != null) {
			currentResource.addProperty(getPrefLabelProperty(), rdfModel.createLiteral(label, language));
		}
	}
	
	public void editPrefLabel(UUID currentUuid, String oldLabel, String newLabel, String language){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());

		if (currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getPrefLabelProperty(), oldLabel, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				s.changeObject(newLabel, language);
			}			
		}
	}
	
	public void removePrefLabel(UUID currentUuid, String label, String language) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		
		if(currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getPrefLabelProperty(), label, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				rdfModel.remove(s);
			}
		}
	}
	
	public void addAltLabel(UUID currentUuid, String label, String language){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		if (currentResource != null) {
			currentResource.addProperty(getAltLabelProperty(), rdfModel.createLiteral(label, language));
		}
	}
	
	public void editAltLabel(UUID currentUuid, String oldLabel, String newLabel, String language){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		
		if (currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getAltLabelProperty(), oldLabel, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				s.changeObject(newLabel, language);
			}			
		}
	}
	
	public void removeAltLabel(UUID currentUuid, String label, String language) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		
		if(currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getAltLabelProperty(), label, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				rdfModel.remove(s);
			}
		}
	}
	
	public void addLatitude(UUID currentUuid, String latitude) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.addProperty(getLatitudeProperty(), latitude);
	}
	
	public void editLatitude(UUID currentUuid, String latitude) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.removeAll(getLatitudeProperty());
		currentResource.addProperty(getLatitudeProperty(), latitude);
	}	
	
	public void removeLatitude(UUID currentUuid) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.removeAll(getLatitudeProperty());
	}
	
	public void addLongitude(UUID currentUuid, String longitude) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.addProperty(getLongitudeProperty(), longitude);
	}
	
	public void editLongitude(UUID currentUuid, String longitude) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.removeAll(getLongitudeProperty());
		currentResource.addProperty(getLongitudeProperty(), longitude);
	}
	
	public void removeLongitude(UUID currentUuid) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.removeAll(getLongitudeProperty());
	}
	
	public void printRDFModel(){
		try
		{
		FileWriter fstream = new FileWriter("teddy.rdf");
		rdfModel.setNsPrefix("teddy", Globals.projectUri);
		rdfModel.setNsPrefix("skos", SKOSURI);
		rdfModel.setNsPrefix(GeoVocabulary.getPrefix(), GeoVocabulary.getUri());
		rdfModel.write(System.out);
		rdfModel.write(fstream);
		//		rdfModel.write(System.out, "N-TRIPLE");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Model getRdfModel() {
		return rdfModel;
	}

	public void setRdfModel(Model rdfModel) {
		this.rdfModel = rdfModel;
	}

	public Property getNarrowerProperty() {
		return narrowerProperty;
	}

	public Property getBroaderProperty() {
		return broaderProperty;
	}

	public Property getRelatedProperty() {
		return relatedProperty;
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

	public Property getLatitudeProperty() {
		return latitudeProperty;
	}

	public Property getLongitudeProperty() {
		return longitudeProperty;
	}
}