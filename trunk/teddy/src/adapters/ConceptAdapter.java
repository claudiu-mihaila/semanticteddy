package adapters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.ajax4jsf.context.AjaxContext;

import model.Concept;
import model.LanguageEnum;
import views.MapEntryView;


public class ConceptAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8000070981269152582L;

	private MainPageAdapter mainAdapter;
	
	private Concept concept;
	
	private DataModel preferredLabelsDM;
	
	private DataModel alternativeLabelsDM;
	
	private DataModel definitionsDM;
	
	private DataModel broaderDM;
	
	private DataModel narrowerDM;
	
	private DataModel relatedDM;

	public ConceptAdapter(MainPageAdapter mainPageAdapter) {
		this.mainAdapter = mainPageAdapter;
	}
	
	public void setConcept(Concept concept) {
		this.concept = concept;
//		this.resetConceptRelated ();
	}

	public void resetConceptRelated() {
		this.alternativeLabelsDM = null;
		this.preferredLabelsDM = null;
		this.definitionsDM = null;
		this.broaderDM = null;
		this.narrowerDM = null;
		this.relatedDM = null;
	}

	public Concept getConcept() {
		return concept;
	}
	
	
	//SKOS TAB------------------------------------------------------------------------------------------------------------------------
	public void changeName (){
		this.mainAdapter.setTreeData(null);
	}
	
	//Preferred labels
	public DataModel getPreferredLabelsDM (){
		if (null!=this.concept)
			if (null==this.preferredLabelsDM){
				List<MapEntryView<String,String>> aux = new LinkedList<MapEntryView<String, String>>();
				for (Entry<String, String> x : this.concept.getPrefLabels().entrySet()){
					aux.add(new MapEntryView<String, String>(x));
				}
				if (1!=this.getAvailableLangsForPreferredLabels().size())
					aux.add(new MapEntryView<String, String>("", ""));
				Collections.sort(aux);
				this.preferredLabelsDM = new ListDataModel(aux);
			}
		return this.preferredLabelsDM;
	}
	
	public void resetPreferredLabelsDM (){
		this.preferredLabelsDM = null;
	}
	
	public List<SelectItem> getAvailableLangsForPreferredLabels (){
		List<SelectItem> items = new LinkedList<SelectItem>();
		if (null!=this.concept){
			items.add(new SelectItem("", "", "", true, true));
			Set<String> used = this.concept.getPrefLabels().keySet();
			
			List<LanguageEnum> listL = Arrays.asList(LanguageEnum.values());
			Collections.sort(listL);
			
			for (LanguageEnum l : listL) {
				if (!used.contains(l.toString())){
					items.add(new SelectItem(l, l.toString()));
				}
			}
		}
		return items;
	}
	
	public void addNewPrefLineLang (){
		MapEntryView<String, String> x = null;
		if (null!=this.concept && null!=this.preferredLabelsDM && this.preferredLabelsDM.getRowData() instanceof MapEntryView<?, ?>){
			x = (MapEntryView<String, String>) this.preferredLabelsDM.getRowData();
			if (null!= x.getKey() && !x.getKey().isEmpty())
				this.concept.getPrefLabels().put(x.getKey(), x.getValue());
		}
		this.resetPreferredLabelsDM();
		
		
	}
	public void addNewPrefLineLabel (){
		MapEntryView<String, String> x = null;
		if (null!=this.concept && null!=this.preferredLabelsDM &&  this.preferredLabelsDM.getRowData() instanceof MapEntryView<?, ?>){
			x = (MapEntryView<String, String>) this.preferredLabelsDM.getRowData();
			if (null!= x.getKey() && !x.getKey().isEmpty())
				if (null!=x.getValue() && !x.getValue().isEmpty())
					this.concept.getPrefLabels().put(x.getKey(), x.getValue());
				else
					this.concept.getPrefLabels().remove(x.getKey());
				
		}
		this.resetPreferredLabelsDM();
		
		
	}
	
	
	//Alternative labels---------------------------------------------------------------------------------------------------------
	public DataModel getAlternativeLabelsDM (){
		if (null!=this.concept)
			if (null==this.alternativeLabelsDM){
				List<MapEntryView<String,String>> aux = new LinkedList<MapEntryView<String, String>>();	
				for (Entry<String, List<String>> x : this.concept.getAltLabels().entrySet()){					
					MapEntryView<String,String> y = new MapEntryView<String, String>();
					y.setKey(x.getKey());
					if (x.getValue().size()>0)
						y.setValue(x.getValue().get(0));
					else
						y.setValue("");
					aux.add(y);
				}
				if (1!=this.getAvailableLangsForPreferredLabels().size()){
					MapEntryView<String, String> y = new MapEntryView<String, String>("","");
					aux.add(y);
				}
				Collections.sort(aux);
				this.alternativeLabelsDM = new ListDataModel(aux);
			}
		return this.alternativeLabelsDM;
	}
	
	public void resetAlternativeLabelsDM (){
		this.alternativeLabelsDM = null;
	}
	
	public List<SelectItem> getAvailableLangsForAlternativeLabels (){
		List<SelectItem> items = new LinkedList<SelectItem>();
		if (null!=this.concept){
			items.add(new SelectItem("", "", "", true, true));
			Set<String> used = this.concept.getAltLabels().keySet();
			
			List<LanguageEnum> listL = Arrays.asList(LanguageEnum.values());
			Collections.sort(listL);
			
			for (LanguageEnum l : listL) {
				if (!used.contains(l.toString())){
					items.add(new SelectItem(l, l.toString()));
				}
			}
		}
		return items;
	}
	
	public void addNewAlternativeLineLang (ActionEvent evt){
		MapEntryView<String, String> x = null;
		if (null!=this.concept && null!=this.alternativeLabelsDM && this.alternativeLabelsDM.getRowData() instanceof MapEntryView<?, ?>){
			x = (MapEntryView<String, String>) this.alternativeLabelsDM.getRowData();
			if (null!= x.getKey() && !x.getKey().isEmpty())
				this.concept.getAltLabels().put(x.getKey(), Arrays.asList(x.getValue()));
		}
		this.resetAlternativeLabelsDM();		
	}
	
	public void addNewAlternativeLineLabel(ActionEvent evt){
		MapEntryView<String, String> x = null;
		if (null!=this.concept && null!=this.alternativeLabelsDM && this.alternativeLabelsDM.getRowData() instanceof MapEntryView<?, ?>){
			x = (MapEntryView<String,String>) this.alternativeLabelsDM.getRowData();
			if (null!= x.getKey() && !x.getKey().isEmpty())
				if (null!=x.getValue() && !x.getValue().isEmpty())
					this.concept.getAltLabels().put(x.getKey(), Arrays.asList(x.getValue()));
				else
					this.concept.getAltLabels().remove(x.getKey());
		}
		this.resetAlternativeLabelsDM();
	}
	
	//DEFINITIONS LIST-------------------------------------------------------------------------------------------------------
	
	//RELATIONS--------------------------------------------------------------------------------------------------------------
	
	public DataModel getBroaderDM() {
		if (null!=concept && null==this.broaderDM)
			this.broaderDM = new ListDataModel(this.concept.getParents());
		return broaderDM;
	}
	
	public void loadConceptBFromLink (){
		if (this.getBroaderDM().getRowData() instanceof Concept)
			this.setConcept((Concept) this.getBroaderDM().getRowData()); 
		
		UIComponent leftPanel = FacesContext.getCurrentInstance().getViewRoot().
			findComponent("applicationForm:leftPanel");
		UIComponent rightPanel = FacesContext.getCurrentInstance().getViewRoot().
			findComponent("applicationForm:rightPanel");
		AjaxContext ac = AjaxContext.getCurrentInstance();
		try {
			ac.addComponentToAjaxRender(leftPanel);
			ac.addComponentToAjaxRender(rightPanel);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}

	public DataModel getNarrowerDM() {
		if (null!=concept && null==this.narrowerDM)
			this.narrowerDM = new ListDataModel(this.concept.getChildren());
		return narrowerDM;
	}

	public void loadConceptNFromLink (){
		if (this.getNarrowerDM().getRowData() instanceof Concept)
			this.setConcept((Concept) this.getNarrowerDM().getRowData()); 

		UIComponent leftPanel = FacesContext.getCurrentInstance().getViewRoot().
			findComponent("applicationForm:leftPanel");
		UIComponent rightPanel = FacesContext.getCurrentInstance().getViewRoot().
			findComponent("applicationForm:rightPanel");
		AjaxContext ac = AjaxContext.getCurrentInstance();
		try {
			ac.addComponentToAjaxRender(leftPanel);
			ac.addComponentToAjaxRender(rightPanel);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
	
	public DataModel getRelatedDM() {
		if (null!=concept && null==this.relatedDM)
			this.relatedDM = new ListDataModel(this.concept.getRelated());		
		return relatedDM;
	}
	
	public void loadConceptRFromLink (){
		if (this.getRelatedDM().getRowData() instanceof Concept)
			this.setConcept((Concept) this.getRelatedDM().getRowData()); 

		UIComponent leftPanel = FacesContext.getCurrentInstance().getViewRoot().
			findComponent("applicationForm:leftPanel");
		UIComponent rightPanel = FacesContext.getCurrentInstance().getViewRoot().
			findComponent("applicationForm:rightPanel");
		AjaxContext ac = AjaxContext.getCurrentInstance();
		try {
			ac.addComponentToAjaxRender(leftPanel);
			ac.addComponentToAjaxRender(rightPanel);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
	
	
	//VISUALIZATION TAB------------------------------------------------------------------------------------------------------

	public long getTimeStamp() {
		return new Date().getTime();
	}
	
	public void paint(OutputStream out, Object data) {
		if (data instanceof Long) {
			BufferedImage img = null;
			try {
				img = this.createBufferedImageFormDOT();
			}
			catch (IOException e) {
				if (img == null) {
					img = this.createBufferedImageFormOtherFile("jpg");
				}
			}
			try {
				ImageIO.write(img, "bmp", out);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (Exception e) {}
				}
			}
		}
	}

	private BufferedImage createBufferedImageFormOtherFile(String extension) {		
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = img.createGraphics();
		graphics2D.setBackground(Color.WHITE);
		graphics2D.setColor(Color.BLACK);
		graphics2D.clearRect(0, 0, 100, 100);
		graphics2D.drawString(extension, 40, 40);
		return img;
	}

	private BufferedImage createBufferedImageFormDOT() throws IOException {
		
		String input = this.getDOTCode();
		String[] comms = {"\"C:/Program Files (x86)/Graphviz2.26/bin/dot.exe\"","-Tjpg"};

		Runtime rt = Runtime.getRuntime() ;
        Process p = rt.exec(comms);
         
        InputStream in = p.getInputStream();
        OutputStream out = p.getOutputStream();
        InputStream err = p.getErrorStream() ;

        out.write(input.getBytes());
        out.flush();
        out.close();

        BufferedImage img = null;
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(in);

			Iterator irIt = ImageIO.getImageReaders(iis);
			ImageReader imgReader = null;
			while (irIt.hasNext()) {
				imgReader = (ImageReader) irIt.next();
				imgReader.setInput(iis);

				try {
					img = imgReader.read(0);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (iis != null) {
				try {
					iis.close();
				} catch (Exception e) {
				}
				;
			}
		}

        in.close();
        err.close();
        
        p.destroy();		
		
		
		return img;
	}
	
	private String getDOTCode() {
		String result = "";
		if (null!=this.concept){
			result = "digraph G { ";
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
			   result = result + "\""+ concept.getName() + "\" -> \"" + c.getName() + "\" [label = \"N\", color = \"#000000\"]" + "; ";
			for (Concept c : concept.getParents())
			   result = result + "\""+ concept.getName() + "\" -> \"" + c.getName() + "\" [label = \"B\", color = \"#000000\"]" + "; ";
			for (Concept c : concept.getRelated())
				result = result + "\""+ concept.getName() + "\" -> \"" + c.getName() + "\" [label = \"R\", color = \"#999999\"]" + "; ";
			result = result + "}";
		}
		return result;
	}

	
}
