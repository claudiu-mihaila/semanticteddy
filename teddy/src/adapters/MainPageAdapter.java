package adapters;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

public class MainPageAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5284478227777129030L;

	private TreeNode<String> treeData;

	public void setTreeData(TreeNode<String> treeData) {
		this.treeData = treeData;
	}

	public TreeNode<String> getTreeData() {
		if (this.treeData == null)
			this.buildTree();
		return this.treeData;	}

	private void buildTree() {
		List<String> roots = Arrays.asList("1","2","3");
		treeData = new TreeNodeImpl<String>();
		for (String root: roots){
			TreeNode<String> uiNode = new TreeNodeImpl<String>();
			uiNode.setData(root);
			treeData.addChild(root, uiNode);
			this.createChildren(root, uiNode);
		}
	}
	
	public void createChildren(String node, TreeNode<String> uiNode) {
		TreeNode<String> uiChildNode;
		String childNode = "content";
		uiChildNode = new TreeNodeImpl<String>();
		uiChildNode.setData(childNode);
		uiNode.addChild(childNode, uiChildNode);
	}	
	
}
