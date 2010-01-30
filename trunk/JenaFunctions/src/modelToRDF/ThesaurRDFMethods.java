package modelToRDF;

import java.io.FileWriter;
import java.util.Date;
import java.util.UUID;

import org.sealife.skos.editor.SKOSVocabulary;

import utils.Globals;
import vocabulary.GeoVocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.DC_10;
import com.hp.hpl.jena.vocabulary.RDF;

public class ThesaurRDFMethods {
	
	Model rdfModel = ModelFactory.createDefaultModel();
	
	Resource conceptResource = rdfModel.createResource(Globals.SKOSURI + SKOSVocabulary.CONCEPT);
	Resource conceptSchemeResource = rdfModel.createResource(Globals.SKOSURI + SKOSVocabulary.CONCEPTSCHEME);
	Resource userResource = rdfModel.createResource(Globals.projectUri + "PROJECTCREATOR");

	Property narrowerProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.NARROWER.toString());
	Property broaderProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.BROADER.toString());
	Property relatedProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.RELATED.toString());
	
	Property prefLabelProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.PREFLABEL.toString());
	Property altLabelProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.ALTLABEL.toString());
	Property definitionProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.DEFINITION.toString());

	Resource pointResource = rdfModel.createResource(GeoVocabulary.POINT);
	Property latitudeProperty = rdfModel.createProperty(GeoVocabulary.LATITUDE);
	Property longitudeProperty = rdfModel.createProperty(GeoVocabulary.LONGITUDE);
	
	public ThesaurRDFMethods() {}
	
	public Resource addMetadataToNewResource(Resource res, String user){
		//metadata info
		Resource authorResource = rdfModel.createResource(Globals.projectUri + user);
		authorResource.addProperty(RDF.type, userResource);
		res.addProperty(DC_10.creator, authorResource);
		res.addProperty(DC_10.date, (new Date()).toString());
		return res;
	}
	
	public Resource addMetadataToOldResource(Resource res, String user){
		Resource authorResource = rdfModel.createResource(Globals.projectUri + user);
		authorResource.addProperty(RDF.type, userResource);
		res.addProperty(DC_10.contributor, authorResource);
		res.addProperty(DCTerms.modified, (new Date()).toString());
		return res;
	}
	/**
	 * Creates a root resource.
	 * @param uuid the UUID of the resource
	 * @param name the name of the resource
	 * @param language
	 */
	public void addRootConcept(UUID uuid, String name, String language, String user) {
		//sa ii punem alt uri?
		Resource resource = rdfModel.createResource(Globals.projectUri + uuid.toString());
		resource.addProperty(RDF.type, conceptSchemeResource);
		resource.addProperty(getPrefLabelProperty(), name, language);
		//ar trebuisa pun un uuid la profil/user?
		addMetadataToNewResource(resource,user);
		
	}

	public Resource createResourceWithName(UUID uuid, String name, String language, String user){
		Resource resource = rdfModel.createResource(Globals.projectUri + uuid.toString());
		resource.addProperty(RDF.type, conceptResource);
		resource.addProperty(getPrefLabelProperty(), name, language);
		resource = addMetadataToNewResource(resource, user);
		return resource;
	}
	
	public void deleteConcept(UUID uuid) {
		Resource resource = rdfModel.createResource(Globals.projectUri + uuid.toString());
		
		rdfModel.removeAll(resource, null, null);
		rdfModel.removeAll(null, null, resource);
	}
	
	public void addNarrowerResource(UUID parentUuid, UUID childUuid, String childName, String language, String user){
		Resource parentResource = rdfModel.getResource(Globals.projectUri + parentUuid.toString());
		if (parentResource != null){
			 Resource childResource = createResourceWithName(childUuid, childName, language, user);
			 parentResource.addProperty(getNarrowerProperty(), childResource);
			 addMetadataToOldResource(parentResource, user);
		}
	}

	public void linkNarrowerResource(UUID parentUuid, UUID childUuid, String user){
		Resource parentResource = rdfModel.getResource(Globals.projectUri + parentUuid);
		Resource childResource = rdfModel.getResource(Globals.projectUri + childUuid);
		
//		boolean found = false;
		if (parentResource != null && childResource != null){
//				StmtIterator si = childResource.listProperties(getBroaderProperty());
//				while(si.hasNext() && found==false) {
//					Statement s = si.nextStatement();
//					Resource broaderResource = (Resource)s.getObject();
//					if (parentResource.equals(broaderResource)) {
//						found = true;
//					}
//				}
//				if (found ==false){
				      parentResource.addProperty(getNarrowerProperty(), childResource);
				      addMetadataToOldResource(parentResource, user);
//				}
		}
	}
	
	public void removeNarrowerResource(UUID parentUuid, UUID childUuid, String user) {
		Resource parentResource = rdfModel.getResource(Globals.projectUri + parentUuid.toString());
		Resource childResource = rdfModel.getResource(Globals.projectUri + childUuid.toString());
		
		if (parentResource != null && childResource != null) {
			rdfModel.remove(parentResource, getNarrowerProperty(), childResource.as(RDFNode.class));
			addMetadataToOldResource(parentResource, user);
		}
	}

	public void linkBroaderResource(UUID childUuid, UUID parentUuid, String user){
		Resource childResource = rdfModel.getResource(Globals.projectUri + childUuid);
		Resource parentResource = rdfModel.getResource(Globals.projectUri + parentUuid);
		
//		boolean found = false;
		if (childResource != null && parentResource != null){
//			StmtIterator si = childResource.listProperties(getNarrowerProperty());
//			while(si.hasNext() && found==false) {
//				Statement s = si.nextStatement();
//				Resource narrResource = (Resource)s.getObject();
//				if (parentResource.equals(narrResource)) {
//					found = true;
//				}
//			}
//			if (found ==false){
				 childResource.addProperty(getBroaderProperty(), parentResource);
//				 addMetadataToOldResource(childResource, user);
//			}
		}
	}
	
	public void removeBroaderResource(UUID childUuid, UUID parentUuid, String user) {
		Resource parentResource = rdfModel.getResource(Globals.projectUri + parentUuid.toString());
		Resource childResource = rdfModel.getResource(Globals.projectUri + childUuid.toString());
		
		if (parentResource != null && childResource != null) {
			rdfModel.remove(childResource, getBroaderProperty(), parentResource.as(RDFNode.class));
			addMetadataToOldResource(childResource, user);
		}
	}
	//related
	public void linkRelatedResource(UUID currentUuid, UUID relatedUuid, String user){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		Resource relatedResource = rdfModel.getResource(Globals.projectUri + relatedUuid.toString());
		
		if (currentResource!= null && relatedResource != null){
			currentResource.addProperty(getRelatedProperty(), relatedResource);
			addMetadataToOldResource(currentResource, user);
		}
		
	}
	
	public void removeRelatedResource(UUID currentUuid, UUID relatedUuid, String user) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		Resource relatedResource = rdfModel.getResource(Globals.projectUri + relatedUuid.toString());
		
		if (currentResource != null && relatedResource != null) {
			rdfModel.remove(currentResource, getRelatedProperty(), relatedResource.as(RDFNode.class));
			addMetadataToOldResource(currentResource, user);
		}
	}

	//definition
	public void addDefinition(UUID currentUuid, String definition, String language, String user)
	{
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		if (currentResource != null){
			currentResource.addProperty(getDefinitionProperty(), definition, language);
			addMetadataToOldResource(currentResource, user);
		}
	}
	
	public void editDefinition(UUID currentUuid, String oldDefinition, String newDefinition, String language, String user){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		
		if (currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getDefinitionProperty(), oldDefinition, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				s.changeObject(newDefinition, language);
			}			
		}
		addMetadataToOldResource(currentResource, user);
	}

	public void removeDefinition(UUID currentUuid, String definition, String language, String user) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		
		if(currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getDefinitionProperty(), definition, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				rdfModel.remove(s);
			}
		}
		
		addMetadataToOldResource(currentResource, user);
	}
	
	public void addPrefLabel(UUID currentUuid, String label, String language, String user){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		if (currentResource != null) {
			currentResource.addProperty(getPrefLabelProperty(), rdfModel.createLiteral(label, language));
			addMetadataToOldResource(currentResource, user);
		}
	}
	
	public void editPrefLabel(UUID currentUuid, String oldLabel, String newLabel, String language, String user){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());

		if (currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getPrefLabelProperty(), oldLabel, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				s.changeObject(newLabel, language);
			}			
		}
		addMetadataToOldResource(currentResource, user);
	}
	
	public void removePrefLabel(UUID currentUuid, String label, String language, String user) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		
		if(currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getPrefLabelProperty(), label, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				rdfModel.remove(s);
			}
		}
		addMetadataToOldResource(currentResource, user);
	}
	
	public void addAltLabel(UUID currentUuid, String label, String language, String user){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		if (currentResource != null) {
			currentResource.addProperty(getAltLabelProperty(), rdfModel.createLiteral(label, language));
			addMetadataToOldResource(currentResource, user);
		}
	}
	
	public void editAltLabel(UUID currentUuid, String oldLabel, String newLabel, String language, String user){
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		
		if (currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getAltLabelProperty(), oldLabel, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				s.changeObject(newLabel, language);
			}			
		}
		addMetadataToOldResource(currentResource, user);
	}
	
	public void removeAltLabel(UUID currentUuid, String label, String language, String user) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		
		if(currentResource != null) {
			StmtIterator si = rdfModel.listStatements(currentResource, getAltLabelProperty(), label, language);
			if (si.hasNext()) {
				Statement s = si.nextStatement();
				rdfModel.remove(s);
			}
		}
		
		addMetadataToOldResource(currentResource, user);
	}
	
	public void addLatitude(UUID currentUuid, String latitude, String user) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.addProperty(getLatitudeProperty(), latitude);
		addMetadataToOldResource(currentResource, user);
	}
	
	public void editLatitude(UUID currentUuid, String latitude, String user) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.removeAll(getLatitudeProperty());
		currentResource.addProperty(getLatitudeProperty(), latitude);
		addMetadataToOldResource(currentResource, user);
	}	
	
	public void removeLatitude(UUID currentUuid, String user) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.removeAll(getLatitudeProperty());
		addMetadataToOldResource(currentResource, user);
	}
	
	public void addLongitude(UUID currentUuid, String longitude, String user) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.addProperty(getLongitudeProperty(), longitude);
		addMetadataToOldResource(currentResource, user);
	}
	
	public void editLongitude(UUID currentUuid, String longitude, String user) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.removeAll(getLongitudeProperty());
		currentResource.addProperty(getLongitudeProperty(), longitude);
		addMetadataToOldResource(currentResource, user);
	}
	
	public void removeLongitude(UUID currentUuid, String user) {
		Resource currentResource = rdfModel.getResource(Globals.projectUri + currentUuid.toString());
		currentResource.removeAll(getLongitudeProperty());
		addMetadataToOldResource(currentResource, user);
	}
	
	public void printRDFModel(){
		try
		{
		FileWriter fstream = new FileWriter("teddy.rdf");
		rdfModel.setNsPrefix("teddy", Globals.projectUri);
		rdfModel.setNsPrefix("skos", Globals.SKOSURI);
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
