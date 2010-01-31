package adapters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import methods.ThesaurJavaMethods;
import model.Concept;
import model.LanguageEnum;
import utils.User;
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

	public ConceptAdapter(MainPageAdapter mainPageAdapter) {
		this.mainAdapter = mainPageAdapter;
		try{
			this.concept = this.sample();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Concept sample () throws Exception{
		ThesaurJavaMethods tools = new ThesaurJavaMethods(new User("SimSim", "Sim"), "G://eclipsework//teddyModel");
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
		 
		 return rootConcept;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public Concept getConcept() {
		return concept;
	}
	
	
	//SKOS TAB--------------------------------------------------------------------------------------------------
	//Preferred labels
	public DataModel getPreferredLabelsDM (){
		if (null==this.preferredLabelsDM){
			List<MapEntryView<String,String>> aux = new LinkedList<MapEntryView<String, String>>();
			for (Entry<String, String> x : this.concept.getPrefLabels().entrySet()){
				aux.add(new MapEntryView<String, String>(x));
			}
			if (1!=this.getAvailableLangsForPreferredLabels().size())
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
		items.add(new SelectItem("", "", "", true, true));
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
			if (null!= x.getKey() && !x.getKey().isEmpty())
				this.concept.getPrefLabels().put(x.getKey(), x.getValue());
		}
		this.resetPreferredLabelsDM();
		
		
	}
	
	
	//Alternative labels--------------------------------------------------------------------------------------------------
	public DataModel getAlternativeLabelsDM (){
		if (null==this.alternativeLabelsDM){
			List<MapEntryView<String,List<String>>> aux = new LinkedList<MapEntryView<String, List<String>>>();
			for (Entry<String, List<String>> x : this.concept.getAltLabels().entrySet()){
				MapEntryView<String, List<String>> y = new MapEntryView<String, List<String>>(x);
				if (null==y.getValue() || y.getValue().isEmpty())
					y.setValue(Arrays.asList("_"));
				else
					y.getValue().add("_");
				aux.add(y);
			}
			if (1!=this.getAvailableLangsForPreferredLabels().size())
				aux.add(new MapEntryView<String, List<String>>("", new LinkedList<String>()));
			this.alternativeLabelsDM = new ListDataModel(aux);
		}
		return this.alternativeLabelsDM;
	}
	
	public void resetAlternativeLabelsDM (){
		this.alternativeLabelsDM = null;
	}
	
	public List<SelectItem> getAvailableLangsForAlternativeLabels (){
		List<SelectItem> items = new LinkedList<SelectItem>();
		items.add(new SelectItem("", "", "", true, true));
		Set<String> used = this.concept.getAltLabels().keySet();
		for (LanguageEnum l : LanguageEnum.values()) {
			if (!used.contains(l.toString())){
				items.add(new SelectItem(l, l.toString()));
			}
		}
		return items;
	}
	
	public void addNewAlternativeLine (ActionEvent evt){
		MapEntryView<String, List<String>> x = null;
		if (this.alternativeLabelsDM.getRowData() instanceof MapEntryView<?, ?>){
			x = (MapEntryView<String, List<String>>) this.alternativeLabelsDM.getRowData();
			if (null!= x.getKey() && !x.getKey().isEmpty())
				this.concept.getAltLabels().put(x.getKey(), x.getValue());
		}
		this.resetAlternativeLabelsDM();
		
		
	}
	
	//VISUALIZATION TAB--------------------------------------------------------------------------------
	
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
		   result = result + "\""+ concept.getName() + "\" -> \"" + c.getName() + "\" [label = \"N\", color = \"#000000\"]" + "; ";
		for (Concept c : concept.getParents())
		   result = result + "\""+ concept.getName() + "\" -> \"" + c.getName() + "\" [label = \"B\", color = \"#000000\"]" + "; ";
		for (Concept c : concept.getRelated())
			result = result + "\""+ concept.getName() + "\" -> \"" + c.getName() + "\" [label = \"R\", color = \"#999999\"]" + "; ";
		result = result + "}";
		return result;
	}

	
}
