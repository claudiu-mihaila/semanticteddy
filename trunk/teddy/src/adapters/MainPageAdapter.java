package adapters;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import methods.SparqlEndpoint;
import methods.ThesaurJavaMethods;
import model.Concept;

import org.ajax4jsf.context.AjaxContext;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import utils.User;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class MainPageAdapter implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5284478227777129030L;

	private User user;
	
	private ConceptAdapter conceptAdapter;
		
	private TreeNode<Concept> treeData;
	
	private List<Concept> roots;
	
	public static ThesaurJavaMethods tools;
	
	private String query;

	private String executeQueryResult;
	
	public MainPageAdapter() {
		if (null==this.conceptAdapter)
			this.conceptAdapter = new ConceptAdapter(this);
		roots = new LinkedList<Concept>();
		
//		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//		User user = (User) request.getAttribute("Usr");
//		String lang = (String)request.getAttribute("DLang");
		doLogin(new User("laura", "laura"), "RO");
		
	}
	
	public void doLogin (User usr, String lang){
		if (usr!=null && lang!=null && !lang.isEmpty()){
			try {
				tools = new ThesaurJavaMethods(usr.getUsername(), usr.getPassword(), null);
				roots.add(this.sample());
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			
		}
	}
	private Concept sample () throws Exception{
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
		 
		 return rootConcept;
	}
	
	public ConceptAdapter getConceptAdapter() {
		return conceptAdapter;
	}

	public void setTreeData(TreeNode<Concept> treeData) {
		this.treeData = treeData;
	}

	public TreeNode<Concept> getTreeData() {
		if (this.treeData == null)
			this.buildTree();
		return this.treeData;	
	}

	private void buildTree() {
		
		treeData = new TreeNodeImpl<Concept>();
		for (Concept root: this.roots){
			TreeNode<Concept> uiNode = new TreeNodeImpl<Concept>();
			uiNode.setData(root);
			treeData.addChild(root, uiNode);
			this.createChildren(root, uiNode);
		}
	}
	
	public void createChildren(Concept node, TreeNode<Concept> uiNode) {
		TreeNode<Concept> uiChildNode;
		
		for (Concept c : node.getChildren()) {
			uiChildNode = new TreeNodeImpl<Concept>();
			uiChildNode.setData(c);
			uiNode.addChild(c.getUUID(), uiChildNode);
			if (null!=c.getChildren() || 0 != c.getChildren().size()){//is leaf
				this.createChildren(c, uiChildNode);
			}
		}
	}

	public void selectNode(NodeSelectedEvent event) {
		UITree tree = (UITree) event.getComponent();
		
		if (tree.getTreeNode().getData() instanceof Concept){
			Concept c = (Concept)tree.getTreeNode().getData();
			if (!c.equals(this.conceptAdapter.getConcept())){
				this.getConceptAdapter().setConcept(c);
				this.getConceptAdapter().resetConceptRelated();
				UIComponent richPanel = FacesContext.getCurrentInstance().getViewRoot().
					findComponent("applicationForm:rightPanel");
				AjaxContext ac = AjaxContext.getCurrentInstance();
				try {
					ac.addComponentToAjaxRender(richPanel);
				} catch (Exception e) {
					System.err.print(e.getMessage());
				}
			}
		}
	}
	
	public Boolean adviseNodeOpened(UITree tree) {
//		TreeNode<Concept> node = tree.getTreeNode();
//		if (null!=this.getConceptAdapter().getConcept())
//			if (null!=node.getChild(this.conceptAdapter.getConcept().getUUID()))
//					return Boolean.TRUE;
			
		return null;
	}
	
	public String close (){
		return "Close";
	}
	
	public void newRoot (){
		if (null!=tools){
			Concept c = tools.addRootConcept("Default");
			this.roots.add(c);
			this.setTreeData(null);
			this.getConceptAdapter().setConcept(c);
			this.getConceptAdapter().resetConceptRelated();
			
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
		
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}
	
//	public void executeQuery (){
//		if (null!=tools && null!=this.query)		
//		this.executeQueryResult = SparqlEndpoint.executeQueryOnModelAsText(tools.getRdfModel().getRdfModel(), this.getQuery());
//	}
//	
//	public String getExecuteQueryResult (){
//		return this.executeQueryResult;
//		
//	}
//	public void setExecuteQueryResult(String executeQueryResult) {
//	}
	
	public void doExportModel (){
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(((HttpServletResponse)
					FacesContext.getCurrentInstance().getExternalContext().getResponse()).getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (null!=out){
		this.tools.getRdfModel().getRdfModel().write(out);
		
	 	((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).
	 		setContentType("application/octet-stream");	 	
		((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).
			addHeader( "Content-Disposition","attachment;filename=export");
    	FacesContext.getCurrentInstance().responseComplete();
		}
	}
	public void doExportModelT (){
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(((HttpServletResponse)
					FacesContext.getCurrentInstance().getExternalContext().getResponse()).getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (null!=out){
		this.tools.getRdfModel().getRdfModel().write(out, "TURTLE");
		
	 	((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).
	 		setContentType("application/octet-stream");	 	
		((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).
			addHeader( "Content-Disposition","attachment;filename=export");
    	FacesContext.getCurrentInstance().responseComplete();
		}
	}
	public void doExportModelN (){
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(((HttpServletResponse)
					FacesContext.getCurrentInstance().getExternalContext().getResponse()).getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (null!=out){
		this.tools.getRdfModel().getRdfModel().write(out, "N3");
		
	 	((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).
	 		setContentType("application/octet-stream");	 	
		((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).
			addHeader( "Content-Disposition","attachment;filename=export");
    	FacesContext.getCurrentInstance().responseComplete();
		}
	}
	
	public void executeQuery (){
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(((HttpServletResponse)
					FacesContext.getCurrentInstance().getExternalContext().getResponse()).getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (null!=out){
		SparqlEndpoint.executeQueryOnModelToOut(tools.getRdfModel().getRdfModel(), this.getQuery(), out);
		
	 	((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).
	 		setContentType("application/octet-stream");	 	
		((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).
			addHeader( "Content-Disposition","attachment;filename=export");
    	FacesContext.getCurrentInstance().responseComplete();
		}
	}
	
	public List<Concept> getRoots() {
		return roots;
	}
}
