package src.Command_Line_Version;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

public class Main{

	public static void main(String[] args) throws IOException{
		int noOfTransactions, noOfChildsInHT=4, maxItemsPerNodeInHT=5, noOfAttributes;
		double minSup=.05, minConf=.5;

		Preprocess p = new Preprocess(new File("data/Transaction.txt"));
		noOfTransactions = p.noOfTransactions;
		noOfAttributes = p.noOfAttributes;
		long start = System.currentTimeMillis();

		System.out.println("Generating Frequent Itemsets:");
		FrequentItemsetGeneration f = new FrequentItemsetGeneration(noOfChildsInHT,maxItemsPerNodeInHT,minSup, noOfTransactions,noOfAttributes);

		BufferedWriter bw = new BufferedWriter(new FileWriter("data/frequentItemsets.txt"));
		Hashtable<Integer, String> noToAttr = p.noToAttr;
		for(int k = 1; k <= f.maxLengthOfFreqItemsets; k++){
			bw.write("\nFrequent "+k+" Itemsets:\n");
			HashMap<ArrayList<Integer>,Integer> FK = f.getFreqKItemset(k);
			Set<ArrayList<Integer>> s = FK.keySet();
			for(ArrayList<Integer> fk : s){
				int countFk = FK.get(fk);
				String freqItemset = "";
				for(int i : fk){
					freqItemset+= noToAttr.get(i)+", ";
				}
				freqItemset+="("+countFk+")\n";
				bw.write(freqItemset);
			}
			System.out.println("Frequent "+k+": "+FK.size());
		}
		bw.close();
		System.out.println("Frequent Itemsets are saved in data/frequentItemsets.txt");

		long end = System.currentTimeMillis();
		double time = (end-start)/1000.0;
		System.out.println("Time: "+time+" sec");

		System.out.println("\nWriting Rules:");
		new RuleGeneration(f.freqK, f.maxLengthOfFreqItemsets, minSup, minConf, noOfTransactions, p.noToAttr);
		System.out.println("Association Rules Generated in data/AssociationRules.txt");

		end = System.currentTimeMillis();
		time = (end-start)/1000.0;
		System.out.println("Time: "+time+" sec\nEND...");
	}
}