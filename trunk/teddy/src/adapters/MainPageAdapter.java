package adapters;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import methods.ThesaurJavaMethods;
import model.Concept;

import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import utils.User;

public class MainPageAdapter implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5284478227777129030L;

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


	
}
