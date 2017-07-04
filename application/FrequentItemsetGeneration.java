package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FrequentItemsetGeneration{
	double minSup;
	int noOfTransactions, noOfAttributes, maxLengthOfFreqItemsets, noOfChilds, maxItemsPerNode;
	ArrayList<HashMap<ArrayList<Integer>,Integer>> freqK = new ArrayList<>();//freqK[0] = F1

	public FrequentItemsetGeneration(int noOfChilds, int maxItemsPerNode, double minSup, int noOfTransactions, int noOfAttributes) throws IOException{
		this.minSup = minSup;
		this.noOfTransactions = noOfTransactions;
		this.noOfAttributes = noOfAttributes;
		this.noOfChilds = noOfChilds;
		this.maxItemsPerNode = maxItemsPerNode;

		ArrayList<Itemset> C1 = new ArrayList<>();
		for(int i=0;i<noOfAttributes;i++){
			ArrayList<Integer> item = new ArrayList<>();
			item.add(i);
			C1.add(new Itemset(item));
		}
		HashMap<ArrayList<Integer>,Integer> F1 = new HashTree(C1,1,noOfChilds,maxItemsPerNode,minSup,noOfTransactions).freqK;
		freqK.add(F1);
		int k = 2;
		while(getFreqKItemset(k-1).size()!=0){
			generateFreqK(k);
			k++;
		}
		maxLengthOfFreqItemsets = k-2;
	}

	HashMap<ArrayList<Integer>,Integer> getFreqKItemset(int k){
		return freqK.get(k-1);
	}

	void printFrequentKItemsets(int k){
		HashMap<ArrayList<Integer>,Integer> FK = getFreqKItemset(k);
		System.out.println("Frequent "+k+" Itemsets: "+FK.size());
		Set<ArrayList<Integer>> s = FK.keySet();
		for(ArrayList<Integer> i : s)	System.out.println(s+" "+FK.get(i));
	}

	ArrayList<Itemset> aprioriGen(int k){//Ck from (Fk-1)X(Fk-1)
		HashMap<ArrayList<Integer>,Integer> FKMinus1 = getFreqKItemset(k-1);
		ArrayList<Itemset> CK = new ArrayList<>();
		Set<ArrayList<Integer>> s = FKMinus1.keySet();
		ArrayList<ArrayList<Integer>> items = new ArrayList<ArrayList<Integer>>(s);
		
		for(int i = 0; i < items.size(); i++){
			for(int j = i+1; j < items.size(); j++){
				ArrayList<Integer> i1 = items.get(i);
				ArrayList<Integer> i2 = items.get(j);

				int min = Math.min(i1.get(k-2), i2.get(k-2));
				int max = Math.max(i1.get(k-2), i2.get(k-2));

				boolean possible = true;
				ArrayList<Integer> candK = new ArrayList<>();
				for(int x = 0; x < k-2; x++){
					if(i1.get(x)!=i2.get(x)){
						possible = false;
						break;
					}else	candK.add(i1.get(x));
				}
				if(possible){
					candK.add(min);
					candK.add(max);
					Itemset item = new Itemset(candK);
					CK.add(item);
				}
			}
		}
		//CK = Candidate K Itemsets by merging pairs of F(k-1)
		return candidatePruning(CK, k);//pruning those whose subsets are infrequent
	}

	//check if all k-1 subsets are frequent
	ArrayList<Itemset> candidatePruning(ArrayList<Itemset> CK, int k){
		ArrayList<Itemset> CKPruned = new ArrayList<>();
		for(Itemset is : CK ){
			boolean areSubsetsFrequent = true;
			HashMap<ArrayList<Integer>,Integer> freqKHashMap = getFreqKItemset(k-1);
			for(int i = 0; i < k; i++){
				ArrayList<Integer> subset = new ArrayList<>(is.value);
				subset.remove(i);
				if(!freqKHashMap.containsKey(subset)){
					areSubsetsFrequent = false;
					break;
				}
			}
			if(areSubsetsFrequent)	CKPruned.add(is);
		}
		//Candidate K Itemsets after pruning
		return CKPruned;
	}

	void generateFreqK(int k) throws IOException{
		ArrayList<Itemset> CK = aprioriGen(k);
		HashMap<ArrayList<Integer>,Integer> FK = new HashTree(CK,k,noOfChilds,maxItemsPerNode,minSup,noOfTransactions).freqK;
		freqK.add(FK);
		//printFrequentKItemsets(k);
	}
}
