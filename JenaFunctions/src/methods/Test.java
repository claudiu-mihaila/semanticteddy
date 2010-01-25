package methods;

import model.Concept;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 ThesaurJavaMethods tools = new ThesaurJavaMethods();
		 
		 Concept rootConcept = tools.addRootConcept("MyRoot");
    	 tools.addDefinition(rootConcept, "def1", "RO");
		 
		 tools.addChildConcept(rootConcept, "Child1");
		 tools.addChildConcept(rootConcept, "Child2");
		 
	//	 tools.printAsObject(rootConcept);
		 tools.rdfModel.printRDfModel();
	}

}
