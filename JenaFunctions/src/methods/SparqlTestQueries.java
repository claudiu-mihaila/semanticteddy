package methods;

import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.QueryResultTable;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.node.Node;
import org.ontoware.rdf2go.model.node.PlainLiteral;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.vocabulary.XSD;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 *
 * @author magda
 */
public class SparqlTestQueries {

    /**
     * @param args the command line arguments
     */
	private static Model model;
	private static URI hasTag;
	
	private static void init() throws ModelRuntimeException {
		model = RDF2Go.getModelFactory().createModel();
		model.open();
	}
	
	public static void tag(Resource resource, Node tag) throws ModelRuntimeException {
		model.addStatement(resource, hasTag, tag);
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
            for ( ; results.hasNext() ; )
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
        		  " FILTER (?lungAmazon > ?lungNil) .}";

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
	URI max = model.createURI("http://xam.de/foaf.rdf.xml#i");
	URI konrad = model.createURI("http://example.com/persons#konrad");
	URI guido = model.createURI("http://example.com/persons#guido");
	URI james = model.createURI("http://example.com/persons#james");
	// relations
	URI hasName = model.createURI("http://xmlns.com/foaf/0.1/#term_name");
	URI hasAge = model.createURI("http://example.com/relations#age");
	hasTag = model.createURI("http://example.com/relations#hasTag");
	// tags
	PlainLiteral tagJava = model.createPlainLiteral("Java");
	PlainLiteral tagPython = model.createPlainLiteral("Python");
	// adding statements
	// naming
	model.addStatement(max, hasName, "Max Völkel");
	model.addStatement(konrad, hasName, "Konrad V");
	model.addStatement(guido, hasName, "Guido van Rossum");
	model.addStatement(james, hasName, "James Gosling");
	
	// a typed property, age
	model.addStatement(konrad, hasAge, model.createDatatypeLiteral("19", XSD._integer));
	model.addStatement(max, hasAge, model.createDatatypeLiteral("29", XSD._integer));
	
	// tagging
	tag(max, tagJava);
	tag(james, tagJava);
	tag(konrad, tagJava);
	tag(konrad, tagPython);
	tag(guido, tagPython);
	model.dump();
	System.out.println("Query 2:");
	String queryString = "SELECT ?resource ?tag WHERE { ?resource <"+hasTag+"> ?tag }";
	QueryResultTable results = model.sparqlSelect(queryString);
	for(String var : results.getVariables()) {
		System.out.println(var);
	}
	for(QueryRow row : results) {
		System.out.println(row.getValue("resource") + " is tagged as " + row.getValue("tag"));
	}
}
    public static void main(String[] args) throws ModelRuntimeException {

		/*System.out.println("Query 1:");
		String queryString = "SELECT ?person WHERE { ?person <"+hasTag+"> "+tagJava.toSPARQL()+" }";*/
		PopulateQuery();
		/*QueryEnclave();
		QueryEscu();
		QuerySimple();*/
		QueryQ();
		QueryRomania();

		

    }
}

