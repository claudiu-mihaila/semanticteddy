package adapters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
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

	public ConceptAdapter(MainPageAdapter mainPageAdapter) {
		this.mainAdapter = mainPageAdapter;
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
		if (null!=this.concept)
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
		if (null!=this.concept){
			items.add(new SelectItem("", "", "", true, true));
			Set<String> used = this.concept.getPrefLabels().keySet();
			for (LanguageEnum l : LanguageEnum.values()) {
				if (!used.contains(l.toString())){
					items.add(new SelectItem(l, l.toString()));
				}
			}
		}
		return items;
	}
	
	public void addNewPrefLine (ActionEvent evt){
		MapEntryView<String, String> x = null;
		if (null!=this.concept && this.preferredLabelsDM.getRowData() instanceof MapEntryView<?, ?>){
			x = (MapEntryView<String, String>) this.preferredLabelsDM.getRowData();
			if (null!= x.getKey() && !x.getKey().isEmpty())
				this.concept.getPrefLabels().put(x.getKey(), x.getValue());
		}
		this.resetPreferredLabelsDM();
		
		
	}
	
	
	//Alternative labels--------------------------------------------------------------------------------------------------
	public DataModel getAlternativeLabelsDM (){
		if (null!=this.concept)
			if (null==this.alternativeLabelsDM){
				List<MapEntryView<String,List<String>>> aux = new LinkedList<MapEntryView<String, List<String>>>();
				
				for (Entry<String, List<String>> x : this.concept.getAltLabels().entrySet()){
					
					MapEntryView<String, List<String>> y = new MapEntryView<String, List<String>>(x);
					
					if (null==y.getValue() || y.getValue().isEmpty()){
						List<String> vList = new LinkedList<String>();
						vList.add("aaa");
						vList.add("bbb");
						y.setValue(vList);
					}
					else
						if (!y.getValue().contains("_"))
							y.getValue().add("_");
					aux.add(y);
				}
				if (1!=this.getAvailableLangsForPreferredLabels().size()){
					MapEntryView<String, List<String>> y = new MapEntryView<String, List<String>>();
					y.setKey("");
					List<String> vList = new LinkedList<String>();
					vList.add("11_");
					vList.add("22_");
	
					y.setValue(vList);
					aux.add(y);
				}
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
			for (LanguageEnum l : LanguageEnum.values()) {
				if (!used.contains(l.toString())){
					items.add(new SelectItem(l, l.toString()));
				}
			}
		}
		return items;
	}
	
	public void addNewAlternativeLineLang (ActionEvent evt){
		MapEntryView<String, List<String>> x = null;
		if (null!=this.concept && this.alternativeLabelsDM.getRowData() instanceof MapEntryView<?, ?>){
			x = (MapEntryView<String, List<String>>) this.alternativeLabelsDM.getRowData();
			if (null!= x.getKey() && !x.getKey().isEmpty())
				this.concept.getAltLabels().put(x.getKey(), x.getValue());
		}
		this.resetAlternativeLabelsDM();		
	}
	
	public void addNewAlternativeLineLabel(ActionEvent evt){
		MapEntryView<String, List<String>> x = null;
		if (null!=this.concept && this.alternativeLabelsDM.getRowData() instanceof MapEntryView<?, ?>){
			x = (MapEntryView<String, List<String>>) this.alternativeLabelsDM.getRowData();
//			if (null!= x.getKey() && !x.getKey().isEmpty())
//				this.concept.getAltLabels().put(x.getKey(), x.getValue());
		}
//		this.resetAlternativeLabelsDM();
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
