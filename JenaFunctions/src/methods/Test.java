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
		 
		 Concept child1 = tools.addChildConcept(rootConcept, "Child1");
		 tools.addAltLabel(child1, "Irina");
		 Concept child2 = tools.addChildConcept(rootConcept, "Child2");
		 tools.editPrefLabel(child2, "Ioan");
		 tools.addDefinition(child2, "Boy", "EN");
		 
	//	 tools.printAsObject(rootConcept);
		 tools.rdfModel.printRDfModel();
	}

}
