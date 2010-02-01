package methods;

import model.Concept;
import utils.User;

public class Test {

	public static void main(String[] args) {
		try {

		 ThesaurJavaMethods tools = new ThesaurJavaMethods("simina", "simina", "E:\\My Folder\\Master\\WADe\\Teddy\\Tests");
		
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
		 
	//	 tools.printAsObject(rootConcept);
		 
//		 tools.removeChildConcept(child3, subChild1);
//		 tools.removeDefinition(rootConcept, "def1", "RO");
//		 tools.editDefinition(rootConcept, "def2", "definitie", "RO");
//		 tools.editPrefLabel(child2, "Child2", "Ioan", "RO");
//		 tools.addPrefLabel(child2, "John", "EN");
//		 tools.removePrefLabel(child2, "Ana", "DE");
//		 tools.removeRelatedConcept(child2, subChild2);
//		 tools.editAltLabel(child1, "Irene", "Mary", "EN");
//		 tools.removeAltLabel(child1, "Irina", "RO");
//		 tools.deleteConcept(child4);
		 
		// tools.printAsObject(rootConcept);
		
		 tools.getRdfModel().getRdfModel().write(System.out, "TURTLE");
//		 tools.getRdfModel().exportXML("E:/My Folder/Master/WADe/Teddy/Tests/XMLTry.xml");
		// tools.closeProject();

//		 tools.getRdfModel().exportXML("D:\\MyTestFolderr\\sdasdad\\XMLTry.xml");
//		 tools.getRdfModel().exportTurtle(null);
		 tools.closeProject();

		
	//	 SampleQueries.SampleQuery1(tools.getRdfModel().getRdfModel());

		 // Afisare prin iterarea solutiilor
//		 ResultSet results = SparqlEndpoint.executeQueryOnModel(tools.getRdfModel().getRdfModel(), Globals.queryString1); 
//		 for (Object var : results.getResultVars())
//			 System.out.print(var.toString() + "\t"); 
//		 System.out.println();
//		 while(results.hasNext() == true) {
//			 QuerySolution qs = (QuerySolution)results.next();
//			 System.out.print(results.getRowNumber() + "\t");
//			 for (Object var : results.getResultVars())
//				 System.out.print(qs.get(var.toString()).toString() + "\t");
//			 System.out.println();
//		 }
//		 
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
