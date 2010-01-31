package modelToRDF;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;

import org.ontoware.rdf2go.RDF2Go;
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
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.DC_10;
import com.hp.hpl.jena.vocabulary.RDF;

public class ThesaurRDFMethods {
	
//	Model rdfModel = ModelFactory.createDefaultModel();
	public String modelPath = Globals.projectFolder;
	
	Model rdfModel = null;
	Resource conceptResource;
	Resource conceptSchemeResource;
	Resource userResource ;

	Property narrowerProperty;
	Property broaderProperty;
	Property relatedProperty;
	
	Property prefLabelProperty;
	Property altLabelProperty;
	Property definitionProperty;

	Resource pointResource ;
	Property latitudeProperty;
	Property longitudeProperty ;	
	
	public ThesaurRDFMethods(String modelPath)throws Exception {
		this.modelPath = modelPath;
		rdfModel = loadModelOrCreate(modelPath);
		defineSpecificProperties();
	}
	
	private void defineSpecificProperties(){
		conceptResource = rdfModel.createResource(Globals.SKOSURI + SKOSVocabulary.CONCEPT);
		conceptSchemeResource = rdfModel.createResource(Globals.SKOSURI + SKOSVocabulary.CONCEPTSCHEME);
		userResource = rdfModel.createResource(Globals.projectUri + "PROJECTCREATOR");

		narrowerProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.NARROWER.toString());
		broaderProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.BROADER.toString());
		relatedProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.RELATED.toString());
		
		prefLabelProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.PREFLABEL.toString());
		altLabelProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.ALTLABEL.toString());
		definitionProperty = rdfModel.createProperty(Globals.SKOSURI + SKOSVocabulary.DEFINITION.toString());

		pointResource = rdfModel.createResource(GeoVocabulary.POINT);
		latitudeProperty = rdfModel.createProperty(GeoVocabulary.LATITUDE);
		longitudeProperty = rdfModel.createProperty(GeoVocabulary.LONGITUDE);	
	}
	
	//In cazul in care directorul este gol, TDB va crea indecsii si tabela de noduri,
	//altfel, daca exista modelul, din rulari anterioare de programe,
	//atunci se va conecta la informatiile deja existente.
	public Model loadModelOrCreate(String modelPath)throws Exception{
		if (modelPath.equals("") || modelPath == null)
			modelPath = Globals.projectFolder;
		cleanAndCreateDirectors(modelPath, false);	
        if (modelPath.equals("")){
        	return TDBFactory.createModel(Globals.projectFolder);
        }
        else
        	return TDBFactory.createModel(modelPath); 
	}
	
	public void cleanAndCreateDirectors(String aPath, Boolean clean){
		
		File myFile = new File(aPath.substring(0,aPath.lastIndexOf("\\")));
		
		if (myFile.exists()==true && clean==true)
			myFile.delete();
		
		if (myFile.exists()==false)
			myFile.mkdirs();
		
		try{
			if (!aPath.substring(aPath.lastIndexOf("\\"),aPath.length()-1).equals(""))
				(new File(aPath)).createNewFile();
		}
		catch(Exception ex){}
	}
	
	public void exportXML(String xmlFilePath){
		try
		{
			if (xmlFilePath.equals("") || xmlFilePath==null)
				xmlFilePath = Globals.xmlDefaultExportPath;
			cleanAndCreateDirectors(xmlFilePath, true);
			PrintWriter outputXML = new PrintWriter(new File(xmlFilePath));
			
			rdfModel.setNsPrefix("teddy", Globals.projectUri);
			rdfModel.setNsPrefix("skos", Globals.SKOSURI);
			rdfModel.setNsPrefix(GeoVocabulary.getPrefix(), GeoVocabulary.getUri());
			rdfModel.setNsPrefix("dcTerms",DCTerms.getURI());
			rdfModel.setNsPrefix("dc_10", DC_10.getURI());
			
			rdfModel.write(System.out);
			rdfModel.write(outputXML);
			outputXML.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void exportTurtle(String turtleFilePath){
		org.ontoware.rdf2go.model.Model model;
		try{
			model = RDF2Go.getModelFactory().createModel();
			model.open();
			
			exportXML("D:\\Temp\\XmlForTurtle.xml");
			model.readFrom(new FileReader(new File("D:\\Temp\\XmlForTurtle.xml")));

			if (turtleFilePath==null || turtleFilePath.equals(""))
				turtleFilePath = Globals.turtleDefaultExportPath;
			cleanAndCreateDirectors(turtleFilePath, true);
			model.writeTo(new FileWriter(turtleFilePath), org.ontoware.rdf2go.model.Syntax.Turtle);
			model.close();
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
	}
	
	public void closeSession(){
		rdfModel.close();
		rdfModel.commit();
		System.out.println("stooop");
	}
	
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
		
		if (parentResource != null && childResource != null){
		      parentResource.addProperty(getNarrowerProperty(), childResource);
		      addMetadataToOldResource(parentResource, user);
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
		
		if (childResource != null && parentResource != null){
				 childResource.addProperty(getBroaderProperty(), parentResource);
				 addMetadataToOldResource(childResource, user);
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
