package src.GUI_Version;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HashTree{
	HTNode root;//depth=0;
	int maxItemsPerNode, maxDepth, minSupCount, NoOfTransactions, noOfChilds;
	HashMap<ArrayList<Integer>,Integer> freqK = new HashMap<>();
	
	public HashTree(ArrayList<Itemset> candidateKItemsets, int k, int noOfChilds, int maxItemsPerNode, double minSup, int noOfTransactions) throws IOException{
		this.maxItemsPerNode = maxItemsPerNode;
		this.noOfChilds = noOfChilds;
		this.NoOfTransactions = noOfTransactions;
		this.minSupCount = (int)(minSup*noOfTransactions);
		maxDepth = k;
		root = new HTNode(0,noOfChilds);//depth = 0
		for(Itemset i: candidateKItemsets){
			insert(root, i, 0);//0th digit for hash
		}
		getFreqKFromHT(k, minSup);
	}

	void insert(HTNode root, Itemset i, int digit){//digit(0-indexed) on which hash is applied
		if(root.depth == maxDepth){	
			root.items.add(i); 
			return;
		}
		int hash = hashValue(i.value, digit);
		if(root.child[hash]==null){
			root.child[hash] = new HTNode(root.depth+1, noOfChilds);
			root.child[hash].isLeaf = true;
			root.child[hash].items.add(i);
		}else if(!root.child[hash].isLeaf)	insert(root.child[hash],i,digit+1);
		else if(root.child[hash].items.size()==maxItemsPerNode){//rehash
			if(root.child[hash].depth==maxDepth)	root.child[hash].items.add(i);
			else{
				root.child[hash].isLeaf = false;
				ArrayList<Itemset> is = root.child[hash].items;
				is.add(i);
					for(Itemset item: is)
						insert(root.child[hash],item,digit+1);			
					root.child[hash].items = null;
			}
		}else	root.child[hash].items.add(i);
	}

	int hashValue(ArrayList<Integer> i, int digit){
		return i.get(digit)%noOfChilds;
	}

	void preOrder(HTNode root){
		if(root==null)	return;
		if(root.isLeaf){
			for(Itemset i: root.items)
				System.out.print(i.value+" "+i.count+" ");
			System.out.println();
		}else	System.out.println("Internal Node");
		for(int i = 0; i<noOfChilds; i++){
			preOrder(root.child[i]);
		}
	}
	
	void getFreqKFromHT(int k, double minSup) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("ProcessedTransaction.txt"));
		String s;
		while((s=br.readLine())!=null){
			StringTokenizer st = new StringTokenizer(s,",");
			int lengthOfTransaction=st.countTokens();
			ArrayList<Integer> trans = new ArrayList<>();
			while(st.hasMoreTokens())	trans.add(Integer.parseInt(st.nextToken()));
			int[][] subsets = Combinations.combinations(lengthOfTransaction, k);
			for (int i = 0; i < subsets.length; i++){
				ArrayList<Integer> t = new ArrayList<>();
				int[] comb = subsets[i];
				for(int j = 0; j <k; j++)	t.add(trans.get(comb[j]));
	            updateCount(t);
			}
		}
		br.close();
		search(root);
	}
	
	void updateCount(ArrayList<Integer> trans){
		int digit=0;
		HTNode n = root;
		while(n!=null && !n.isLeaf){
			int hash = hashValue(trans, digit++);
			n = n.child[hash];
		}
		if(n==null)	return;
		for(Itemset i: n.items)	if(i.equals(trans)){i.count++;	break;}
	}
	
	void search(HTNode n){// select those itemsets which have supportCount >= minSupportCount
		if(n==null)	return;
		if(n.isLeaf)
			for(Itemset i : n.items){
				if(i.count>=minSupCount)
					freqK.put(i.value,i.count);
			}
		else
			for(int i = 0; i < noOfChilds; i++)	search(n.child[i]);
	}
}