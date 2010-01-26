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
    	 tools.addDefinition(rootConcept, "def2", "RO");
    	 tools.addDefinition(rootConcept, "def3", "EN");
		 
		 Concept child1 = tools.addChildConcept(rootConcept, "Child1");
		 tools.addAltLabel(child1, "Irina");
		 tools.addLatitude(rootConcept, "-55.2355542");
		 tools.addLongitude(rootConcept, "44.2359957");
		 tools.editLatitude(rootConcept, "55.2355542");
		 tools.editLongitude(rootConcept, "-44.2359957");
		 
		 Concept child2 = tools.addChildConcept(rootConcept, "Child2");
		 tools.editPrefLabel(child2, "Ioan");
		 tools.addDefinition(child2, "Boy", "EN");
		 
		 Concept child3 = tools.addChildConcept(rootConcept, "Child3");
		 
		 Concept subChild1 = tools.addChildConcept(child3, "sub3_1");
		 Concept subChild2 = tools.addChildConcept(child2, "sub3_2");
		 tools.addParentConcept(subChild1, rootConcept);
		 tools.addRelated(subChild2, child2);
		 tools.addRelated(subChild2, child3);
		 
		 
	//	 tools.printAsObject(rootConcept);
		 tools.rdfModel.printRDFModel();
	}

}
