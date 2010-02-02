package methods;

import java.io.OutputStream;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

public class SparqlEndpoint {

	public static ResultSet executeQueryOnModel(Model model, String queryString) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		qe.close();
		return results;
	}
	public static String executeQueryOnModelAsText(Model model, String queryString) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		qe.close();
		return ResultSetFormatter.asText(results);
	}
	
	public static void executeQueryOnModelToOut(Model model, String queryString, OutputStream out) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
	
		ResultSetFormatter.out(out, results, query);
		qe.close();
	}
	
}
