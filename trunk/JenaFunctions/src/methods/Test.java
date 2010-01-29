package methods;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import utils.Globals;
import model.Concept;

public class Test {

	public static void main(String[] args) {
		try {
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
		 tools.addPrefLabel(child2, "Ana", "DE");
		 
		 Concept child3 = tools.addChildConcept(rootConcept, "Child3");
		 
		 Concept subChild1 = tools.addChildConcept(child3, "sub3_1");
		 Concept subChild2 = tools.addChildConcept(child2, "sub3_2");
		 tools.linkParentConcept(subChild1, rootConcept);
		 tools.linkRelatedConcept(subChild2, child2);
		 tools.linkRelatedConcept(subChild2, child3);
		 
		 Concept child4 = tools.addChildConcept(subChild2, "Child 4");
		 tools.addPrefLabel(child4, "Copil 4", "EN");
		 tools.addAltLabel(child4, "copilas", "RO");
		 tools.addDefinition(child4, "definit", "FR");
		 tools.addLatitude(child4, "23.555");
		 tools.addLongitude(child4, "-55");		 
		 
		 tools.printAsObject(rootConcept);
		 tools.rdfModel.printRDFModel();
		 
		 tools.removeChildConcept(child3, subChild1);
		 tools.removeDefinition(rootConcept, "def1", "RO");
		 tools.editDefinition(rootConcept, "def2", "definitie", "RO");
		 tools.editPrefLabel(child2, "Child2", "Ioan", "RO");
		 tools.addPrefLabel(child2, "John", "EN");
		 tools.removePrefLabel(child2, "Ana", "DE");
		 tools.removeRelatedConcept(child2, subChild2);
		 tools.editAltLabel(child1, "Irene", "Mary", "EN");
		 tools.removeAltLabel(child1, "Irina", "RO");
		 tools.deleteConcept(child4);
		 
		 tools.printAsObject(rootConcept);
		 tools.rdfModel.printRDFModel();
		 
		 SampleQueries.SampleQuery1(tools.rdfModel.getRdfModel());

		 // Afisare prin iterarea solutiilor
		 ResultSet results = SparqlEndpoint.executeQueryOnModel(tools.rdfModel.getRdfModel(), Globals.queryString1); 
		 for (String var : results.getResultVars())
			 System.out.print(var + "\t"); 
		 System.out.println();
		 while(results.hasNext() == true) {
			 QuerySolution qs = results.next();
			 System.out.print(results.getRowNumber() + "\t");
			 for (String var : results.getResultVars())
				 System.out.print(qs.get(var).toString() + "\t");
			 System.out.println();
		 }
		 
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
