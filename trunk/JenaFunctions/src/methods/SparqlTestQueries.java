package methods;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author magda
 */
public class SparqlTestQueries {

    /**
     * @param args the command line arguments
     */
	private static Model model;
	
	private static void init() {
		model = ModelFactory.createDefaultModel();
//		model.open();
	}
	
	public static void QueryEnclave()
	{
		System.out.println("Query 1:");
		String sparqlQueryString=
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " + 
			"PREFIX type: <http://dbpedia.org/class/yago/> "+
			"PREFIX prop: <http://dbpedia.org/property/> " + 
			"SELECT distinct ?numeStat ?pop WHERE { " +
			"?stat a type:LandlockedCountries ; "+
			"rdfs:label ?numeStat ; " +
			" prop:populationEstimate ?pop . "+
			"		FILTER (?pop < 15000000) . " +
			"FILTER ( lang(?numeStat) = \"en\" )} LIMIT 15";

			Query query = QueryFactory.create(sparqlQueryString);
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

			try {
			    ResultSet results = qexec.execSelect();
			    for (; results.hasNext();) {

			    	QuerySolution soln = results.nextSolution() ;
		            String x = soln.get("numeStat").toString();
		            System.out.print(x +"\n");
			    }
			}
			finally {
			   qexec.close();
			}

	}
	
	public static void QueryEscu()
	{
		System.out.println("Query 2:");
		String sparqlQueryString=
			"PREFIX foaf: <http://xmlns.com/foaf/0.1/>"+
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
			"PREFIX dbp: <http://dbpedia.org/property/>"+
			"SELECT ?nume ?ocupatie WHERE { "+
			"   ?persoana foaf:name ?nume ;"+
			"             dbp:occupation ?ocupatie .  "+
			"   FILTER regex (?nume, \"escu$\")"+
			"}ORDER BY ?nume LIMIT 7";
		Query query = QueryFactory.create(sparqlQueryString);

        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);


        try {
            ResultSet results = qexec.execSelect();
            for ( ; results.hasNext() ; )
        {
            QuerySolution soln = results.nextSolution() ;
            String x = soln.get("nume").toString();
            System.out.print(x +"\n");
        }

        }
        finally { qexec.close() ; }

        }
	public  static void QuerySimple()
    {
		System.out.println("Query 3:");
        String sparqlQueryString = "select distinct ?Concept where {[] a ?Concept } LIMIT 10";

        Query query = QueryFactory.create(sparqlQueryString);

        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);


        try {
            ResultSet results = qexec.execSelect();
            while( results.hasNext() )
        {
            QuerySolution soln = results.nextSolution() ;
            String x = soln.get("Concept").toString();
            System.out.print(x +"\n");
        }

        }
        finally { qexec.close() ; }

        }

	public static void QueryEminescu()
	{
		System.out.println("Query 4:");
		String sparqlQueryString=
			"PREFIX dc: <http://purl.org/dc/elements/1.1/>"+
			"PREFIX foaf: <http://xmlns.com/foaf/0.1/>"+
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
			"PREFIX dbp: <http://dbpedia.org/property/>"+
			"SELECT ?locNastere ?dataNastere ?foto ?desc"+
				"WHERE { "+
				   "?persoana rdfs:label \"Mihai Eminescu\"@en ;"+
				   "dbp:birthPlace ?locNastere ;"+
				   "dbp:dateOfBirth ?dataNastere ;"+
				   "foaf:img ?foto . "+
				   "OPTIONAL { ?persoana dc:description ?desc } ."+
				"}ORDER BY DESC (?locNastere)";
		Query query = QueryFactory.create(sparqlQueryString);

        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);


        try {
            ResultSet results = qexec.execSelect();
            for ( ; results.hasNext() ; )
        {
            QuerySolution soln = results.nextSolution() ;
            String x = soln.toString();
            System.out.print(x +"\n");
        }

        }
        finally { qexec.close() ; }

        }
	public  static void QueryRomania()
    {
		System.out.println("Query 5:");
        String sparqlQueryString = 
        	"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"  +
        	"SELECT distinct ?info WHERE { <http://dbpedia.org/resource/Romania> rdfs:comment ?info .	}"+
        	"LIMIT 10";

        Query query = QueryFactory.create(sparqlQueryString);

        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);


        try {
            ResultSet results = qexec.execSelect();
            for ( ; results.hasNext() ; )
        {
            QuerySolution soln = results.nextSolution() ;
            String x = soln.get("info").toString();
            System.out.print(x +"\n");
        }
        }
        finally { qexec.close() ; }

        }
	public  static void QueryQ()
    {
		System.out.println("Query 6:");
        String sparqlQueryString = 
        	"PREFIX prop: <http://dbpedia.org/property/> "+
       		"ASK { <http://dbpedia.org/resource/Amazon_River> prop:length ?lungAmazon . "+
        		  "<http://dbpedia.org/resource/Nile> prop:length ?lungNil ."+
        		  " FILTER (?lungAmazon < ?lungNil) .}";

        Query query = QueryFactory.create(sparqlQueryString);

        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);


        try {
        	boolean result = qexec.execAsk();
            System.out.println(result);
        }
        finally { qexec.close() ; }

        }

public static void PopulateQuery()
{
	init();
	// creating URIs
	// persons
	Resource max = model.createResource("http://xam.de/foaf.rdf.xml#i");
	Resource konrad = model.createResource("http://example.com/persons#konrad");
	Resource guido = model.createResource("http://example.com/persons#guido");
	Resource james = model.createResource("http://example.com/persons#james");
	// relations
	Property hasName = model.createProperty("http://xmlns.com/foaf/0.1/#term_name");
	Property hasAge = model.createProperty("http://example.com/relations#age");
	Property hasTag = model.createProperty("http://example.com/relations#hasTag");
	// tags
	Literal tagJava = model.createLiteral("Java");
	Literal tagPython = model.createLiteral("Python");
	// adding statements
	// naming
	model.add(max, hasName, "Max Völkel");
	model.add(konrad, hasName, "Konrad V");
	model.add(guido, hasName, "Guido van Rossum");
	model.add(james, hasName, "James Gosling");
	
	// a typed property, age
	model.add(konrad, hasAge, model.createTypedLiteral(19));
	model.add(max, hasAge, model.createTypedLiteral(29));
	
	// tagging
	model.add(max, hasTag, tagJava);
	model.add(james, hasTag, tagJava);
	model.add(konrad, hasTag, tagJava);
	model.add(konrad, hasTag, tagPython);
	model.add(guido, hasTag, tagPython);
//	model.dump();
	System.out.println("Query 2:");
	String queryString = "SELECT ?resource ?tag WHERE { ?resource <"+hasTag+"> ?tag }";
	Query query = QueryFactory.create(queryString);
	QueryExecution qe = QueryExecutionFactory.create(query, model);
	ResultSet results = qe.execSelect();
	for(Object var : results.getResultVars()) {
		System.out.println(var.toString());
	}
	while (results.hasNext())
	{
		QuerySolution row = results.nextSolution();
		System.out.println(row.get("resource") + " is tagged as " + row.get("tag"));
	}
}
    public static void main(String[] args) {

		/*System.out.println("Query 1:");
		String queryString = "SELECT ?person WHERE { ?person <"+hasTag+"> "+tagJava.toSPARQL()+" }";*/
		PopulateQuery();
		QueryEnclave();
		QueryEscu();
		QuerySimple();
		QueryQ();
		QueryRomania();

		

    }
}

