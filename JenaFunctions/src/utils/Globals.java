package utils;

import com.hp.hpl.jena.vocabulary.DCTerms;


public class Globals {
	public static String projectFolder="C:\\Users\\Pavlos\\workspace\\teddyModel";
	public static String projectUri= "http://Teddy#";
	public static String SKOSURI = "http://www.w3.org/2004/02/skos/core#";
	public static String defaultLanguage = "RO";
	public static String dotPath = "C:/Program Files/Graphviz2.26.2/bin/dot.exe";
	//public static ArrayList<Concept> rootConcepts = new ArrayList<Concept>();
	public static String queryString1 = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
										+ "SELECT distinct ?concept ?label ?definition WHERE {"
										+ "?concept skos:PREFLABEL [] ;"
										+ "skos:DEFINITION ?definition ;"
										+ "skos:PREFLABEL ?label."
										+ "FILTER langMatches(lang(?definition),\"EN\") }";
	public static String queryString2 = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
										+ "SELECT distinct ?concept ?label WHERE {"
										+ "?concept skos:PREFLABEL [] ;"
										+ "skos:PREFLABEL ?label.} ORDER BY ?label";
	public static String queryString3 = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
										+ "SELECT distinct ?concept ?label WHERE {"
										+ "?concept skos:PREFLABEL [] ;"
										+ "skos:PREFLABEL ?label."
										+ "FILTER (regex(str(?label), '^c', 'i')) }";
	public static String queryString4 = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "
										+ "SELECT distinct ?preflabel ?altlabel WHERE {"
										+ "?concept skos:PREFLABEL [] ;"
										+ "skos:ALTLABEL ?altlabel ; "
										+ "skos:PREFLABEL ?preflabel. } "
										+ "LIMIT 3";
}
