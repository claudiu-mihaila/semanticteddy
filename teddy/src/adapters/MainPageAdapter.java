package adapters;

import java.io.Serializable;
import java.util.List;

import model.Concept;

import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

public class MainPageAdapter implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5284478227777129030L;

	private ConceptAdapter conceptAdapter;
		
	private TreeNode<Concept> treeData;
	
	public MainPageAdapter() {
		if (null==this.conceptAdapter)
			this.conceptAdapter = new ConceptAdapter(this);
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
		List<Concept> roots = null; //new LinkedList (all root concepts)
		treeData = new TreeNodeImpl<Concept>();
		for (Concept root: roots){
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
