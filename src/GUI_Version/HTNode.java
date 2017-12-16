package src.GUI_Version;

import java.util.ArrayList;

public class HTNode{
	boolean isLeaf;
	HTNode[] child;
	ArrayList<Itemset> items;
	int depth;

	public HTNode(int depth, int noOfChild){
		isLeaf = false;
		this.depth = depth;
		items = new ArrayList<Itemset>();
		child = new HTNode[noOfChild];
	}
}