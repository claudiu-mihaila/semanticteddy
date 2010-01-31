package adapters;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import model.Concept;
import model.LanguageEnum;

import org.openrdf.query.algebra.Str;
import org.richfaces.component.html.HtmlDataTable;

import views.MapEntryView;

public class ConceptAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8000070981269152582L;

	private MainPageAdapter mainAdapter;
	
	private Concept concept;
	
	private DataModel preferredLabelsDM;

	public ConceptAdapter(MainPageAdapter mainPageAdapter) {
		this.mainAdapter = mainPageAdapter;
		this.concept = new Concept("my concept", LanguageEnum.EL.name());
		this.concept.setPrefLabels(new HashMap<String, String>());
		this.concept.getPrefLabels().put(LanguageEnum.RO.toString(), "preferat");
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public Concept getConcept() {
		return concept;
	}
	
	public DataModel getPreferredLabelsDM (){
		if (null==this.preferredLabelsDM){
			List<MapEntryView<String,String>> aux = new LinkedList<MapEntryView<String, String>>();
			for (Entry<String, String> x : this.concept.getPrefLabels().entrySet()){
				aux.add(new MapEntryView<String, String>(x));
			}
			if (0!=this.getAvailableLangsForPreferredLabels().size())
				aux.add(new MapEntryView<String, String>("", ""));
			this.preferredLabelsDM = new ListDataModel(aux);
		}
		return this.preferredLabelsDM;
	}
	
	public void resetPreferredLabelsDM (){
		this.preferredLabelsDM = null;
	}
	
	public List<SelectItem> getAvailableLangsForPreferredLabels (){
		List<SelectItem> items = new LinkedList<SelectItem>();
		Set<String> used = this.concept.getPrefLabels().keySet();
		for (LanguageEnum l : LanguageEnum.values()) {
			if (!used.contains(l.toString())){
				items.add(new SelectItem(l, l.toString()));
			}
		}
		return items;
	}
	
	public void addNewPrefLine (ActionEvent evt){
		MapEntryView<String, String> x = null;
		if (this.preferredLabelsDM.getRowData() instanceof MapEntryView<?, ?>){
			x = (MapEntryView<String, String>) this.preferredLabelsDM.getRowData();
			this.concept.getPrefLabels().put(x.getKey(), x.getValue());
		}
		this.resetPreferredLabelsDM();
		
		
	}
	
	public String getDOTCode() {
		String result = "digraph G { ";
		result = result + "node [shape = doublecircle]; \"" + concept.getName() + "\";";
		
		result = result + "node [shape = ellipse];";
		for (Concept c : concept.getChildren())
			result = result + "\""+ c.getName() + "\"; ";
		for (Concept c : concept.getParents())
			result = result + "\""+ c.getName() + "\"; ";
			
		result = result + "node [color = \"#000000\", fillcolor = \"#EEEEEE\", style = filled];";
		for (Concept c : concept.getRelated())
			result = result + "\""+ c.getName() + "\"; ";
		
		for (Concept c : concept.getChildren())
			result = result + "\""+ concept.getName() + " -> " + c.getName() + "[label = \"N\", color = \"#000000\"]" + "\"; ";
		for (Concept c : concept.getParents())
			result = result + "\""+ concept.getName() + " -> " + c.getName() + "[label = \"B\", color = \"#000000\"]" + "\"; ";
		for (Concept c : concept.getRelated())
			result = result + "\""+ concept.getName() + " -> " + c.getName() + "[label = \"R\", color = \"#999999\"]" + "\"; ";	
	
		result = result + "}";
		return result;
	}
}
