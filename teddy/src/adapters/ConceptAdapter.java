package adapters;

import java.io.Serializable;

import model.Concept;

public class ConceptAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8000070981269152582L;

	private MainPageAdapter mainAdapter;
	
	private Concept concept;

	public ConceptAdapter(MainPageAdapter mainPageAdapter) {
		this.mainAdapter = mainPageAdapter;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public Concept getConcept() {
		return concept;
	}
	
	
}
