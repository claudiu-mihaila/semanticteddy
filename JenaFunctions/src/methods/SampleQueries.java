package methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import model.Concept;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class SampleQueries {

	public static Model init() {
		try {
			Model model;
			InputStream in = new FileInputStream(new File("teddy.rdf"));
			model = ModelFactory.createMemModelMaker().createFreshModel();
			model.read(in, null); // null base URI, since model URIs are
									// absolute
			in.close();
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void SampleQuery1(Model model) {
		System.out.println("Query 1: Conceptele care au o eticheta preferata si o definitie atasata"+
				", iar aceasta din urma este in engleza");
		try {
			String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
					+ "SELECT distinct ?concept ?label ?definition WHERE {"
					+ "?concept skos:PREFLABEL [] ;"
					+ "skos:DEFINITION ?definition ;"
					+ "skos:PREFLABEL ?label." +
					"FILTER langMatches(lang(?definition),\"EN\") }";

			Query query = QueryFactory.create(queryString);

			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet results = qe.execSelect();

			ResultSetFormatter.out(System.out, results, query);

			qe.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void SampleQuery2(Model model) {
		System.out.println("\nQuery 2: Toate conceptele cu etichetele lor preferate, ordonate dupa eticheta");
		try {

			String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
					+ "SELECT distinct ?concept ?label WHERE {"
					+ "?concept skos:PREFLABEL [] ;"
					+ "skos:PREFLABEL ?label.} ORDER BY ?label";

			Query query = QueryFactory.create(queryString);

			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet results = qe.execSelect();

			ResultSetFormatter.out(System.out, results, query);

			qe.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void SampleQuery3(Model model) {
		System.out.println("\nQuery 3: Toate conceptele a caror eticheta preferata incepe cu litera c");
		try {

			String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
					+ "SELECT distinct ?concept ?label WHERE {"
					+ "?concept skos:PREFLABEL [] ;"
					+ "skos:PREFLABEL ?label."
					+ "FILTER (regex(str(?label), '^c', 'i')) }";
					// "FILTER (regex(str(?label), '1$', 'i')) }";

			Query query = QueryFactory.create(queryString);

			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet results = qe.execSelect();

			ResultSetFormatter.out(System.out, results, query);

			qe.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public static void SampleQuery4(Model model) {
		System.out.println("\nQuery 4: Afiseaza primele 3 seturi de etichete preferate + alternative ale conceptelor care le au definite pe amandoua");
		try {

			String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
					+ "SELECT distinct ?preflabel ?altlabel WHERE {"
					+ "?concept skos:PREFLABEL [] ;"
					+ "skos:ALTLABEL ?altlabel ; "
					+ "skos:PREFLABEL ?preflabel. } " +
					"LIMIT 3";
					
			Query query = QueryFactory.create(queryString);

			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet results = qe.execSelect();

			ResultSetFormatter.out(System.out, results, query);

			qe.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void main(String[] args) {

		try {
			ThesaurJavaMethods tools =  new ThesaurJavaMethods("laura", "laura", null);
			
			 Concept rootConcept = tools.addRootConcept("Cocktails");
			 tools.addDefinition(rootConcept, "un stil de bãuturã amestecatã", "RO");
			 tools.addDefinition(rootConcept, "a style of mixed drink", "EN");
			 
			 Concept child1 = tools.addChildConcept(rootConcept, "Pre-dinner");
			 tools.addAltLabel(child1, "Inainte de masa", "RO");
			 tools.addAltLabel(child1, "Before dinner", "EN");
			 
			 Concept child2 = tools.addChildConcept(rootConcept, "Long drink");
			 tools.addDefinition(child2, "Alcohol with a large quantity of juice", "EN");
			 
			 Concept child3 = tools.addChildConcept(rootConcept, "After-dinner");
			 
			 Concept subChild1 = tools.addChildConcept(child3, "White Russian");
			 Concept subChild2 = tools.addChildConcept(child2, "Tequila Sunsrise");
			 Concept subChild3 = tools.addChildConcept(child2, "Tequila Sunset");

			 tools.linkRelatedConcept(subChild2, subChild3);

			 tools.addLatitude(subChild2, "33.448333");
			 tools.addLongitude(subChild2, "-112.073889");	
			 
			SampleQuery1(tools.getRdfModel().getRdfModel());
			SampleQuery2(tools.getRdfModel().getRdfModel());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		SampleQuery3(model);
//		SampleQuery4(model);
	}

}
