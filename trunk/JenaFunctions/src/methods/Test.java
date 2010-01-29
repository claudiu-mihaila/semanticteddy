package methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import model.Concept;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 ThesaurJavaMethods tools = new ThesaurJavaMethods();
		 		 
		 Concept rootConcept = tools.addRootConcept("MyRoot");
    	 tools.addDefinition(rootConcept, "def1", "RO");
    	 tools.addDefinition(rootConcept, "def2", "RO");
    	 tools.addDefinition(rootConcept, "def3", "EN");
		 
		 Concept child1 = tools.addChildConcept(rootConcept, "Child1");
		 tools.addAltLabel(child1, "Irina", "RO");
		 tools.addAltLabel(child1, "Maria", "RO");
		 tools.addAltLabel(child1, "Irene", "EN");
		 
		 tools.addLatitude(rootConcept, "-55.2355542");
		 tools.addLongitude(rootConcept, "44.2359957");
		 tools.editLatitude(rootConcept, "55.2355542");
		 tools.editLongitude(rootConcept, "-44.2359957");
		 
		 Concept child2 = tools.addChildConcept(rootConcept, "Child2");
		 tools.addDefinition(child2, "Boy", "EN");
		 
		 Concept child3 = tools.addChildConcept(rootConcept, "Child3");
		 
		 Concept subChild1 = tools.addChildConcept(child3, "sub3_1");
		 Concept subChild2 = tools.addChildConcept(child2, "sub3_2");
		 tools.addParentConcept(subChild1, rootConcept);
		 tools.addRelatedConcept(subChild2, child2);
		 tools.addRelatedConcept(subChild2, child3);
		 
		 
		 tools.printAsObject(rootConcept);
		 tools.rdfModel.printRDFModel();
		 
		 tools.removeChildConcept(child3, subChild1);
		 tools.removeDefinition(rootConcept, "def1", "RO");
		 tools.editDefinition(rootConcept, "def2", "definitie", "RO");
		 tools.editPrefLabel(child2, "Child2", "Ioan", "RO");
		 tools.addPrefLabel(child2, "John", "EN");
		 tools.removeRelatedConcept(child2, subChild2);
		 tools.editAltLabel(child1, "Irene", "Mary", "EN");
		 tools.removeAltLabel(child1, "Irina", "RO");
		 
		 tools.printAsObject(rootConcept);
		 tools.rdfModel.printRDFModel();
		
		 try
		 {
		InputStream in = new FileInputStream(new File("teddy.rdf"));

		Model model = ModelFactory.createMemModelMaker().createFreshModel();
		model.read(in,null); // null base URI, since model URIs are absolute
		in.close();

		/*String queryString =
			"select distinct ?name where {[] a ?name } LIMIT 10";*/
		
		String queryString =
			"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "+
			"SELECT distinct ?x ?label ?definition WHERE {" +
					  "?x skos:PREFLABEL [] ;" +
					  "skos:DEFINITION ?definition ;" +
					  "skos:PREFLABEL ?label." +	"}";
					  //"FILTER regex (?definition, \"EN$\", \"i\") }";

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		ResultSetFormatter.out(System.out, results, query);

		qe.close();
		 }
		 catch(Exception e)
		 {
		 e.printStackTrace();
		 
		 }
	}

}
