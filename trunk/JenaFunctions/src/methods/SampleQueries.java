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
					+ "skos:PREFLABEL ?label.}" + "ORDER BY ?label";

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

		Model model = init();
		SampleQuery1(model);
		SampleQuery2(model);
		SampleQuery3(model);
		SampleQuery4(model);
	}

}
