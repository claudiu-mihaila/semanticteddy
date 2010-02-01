package methods;

import model.Concept;
import utils.User;

public class Test {

	public static void main(String[] args) {
		try {

		 ThesaurJavaMethods tools = new ThesaurJavaMethods("simina", "simina", "E:\\My Folder\\Master\\WADe\\Teddy\\Tests\\");
		
	/*	 Concept rootConcept = tools.addRootConcept("Cocktails");
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
	*/	 
		// tools.printAsObject(rootConcept);
		
		 tools.exportXML("C:\\t1\\txml.xml");
		 //tools.getRdfModel().getRdfModel().write(System.out, "TURTLE");
		 tools.exportTurtle("C:\\t1\\tutle.xml");
		 tools.closeProject();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
