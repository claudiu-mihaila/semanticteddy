package adapters;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import methods.ThesaurJavaMethods;
import model.Concept;

import org.ajax4jsf.context.AjaxContext;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import utils.User;

public class MainPageAdapter implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5284478227777129030L;

	private User user;
	
	private ConceptAdapter conceptAdapter;
		
	private TreeNode<Concept> treeData;
	
	private List<Concept> roots;
	
	public MainPageAdapter() {
		if (null==this.conceptAdapter)
			this.conceptAdapter = new ConceptAdapter(this);
		roots = new LinkedList<Concept>();
		
		try {
			roots.add(this.sample());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Concept sample () throws Exception{
		ThesaurJavaMethods tools = new ThesaurJavaMethods(new User("SimSim", "Sim"),null);
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
			this.getConceptAdapter().setConcept(c);
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
	
	public Boolean adviseNodeOpened(UITree tree) {
		TreeNode<Concept> node = tree.getTreeNode();
		if (null!=this.getConceptAdapter().getConcept())
			if (null!=node.getChild(this.conceptAdapter.getConcept().getUUID()))
					return Boolean.TRUE;
			
		return Boolean.FALSE;
	}
	
	public String close (){
		return "Close";
	}
}
