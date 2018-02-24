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
	static File transactionFile,processedTransactionFile,freqItemsetFile,rulesFile;
	static int noOfTransactions, noOfAttributes, noOfChildsInHT=4, maxItemsPerNodeInHT=5; 
	static double minSup, minConf;
	
	public static void main(String[] args) throws IOException{
		// INPUT
		transactionFile = new File("data/transaction.data");
		minSup=.05; minConf=.5;
		noOfChildsInHT=4; maxItemsPerNodeInHT=5; 
		
		// OUTPUT
		processedTransactionFile = new File("output/processedTransaction.data");
		freqItemsetFile = new File("output/frequentItemsets.data");
		rulesFile = new File("output/associationRules.data");
		
		

		new Preprocess();
		long start = System.currentTimeMillis();

		System.out.println("Generating Frequent Itemsets:");
		FrequentItemsetGeneration f = new FrequentItemsetGeneration();

		BufferedWriter bw = new BufferedWriter(new FileWriter(freqItemsetFile));
		Hashtable<Integer, String> noToAttr = Preprocess.noToAttr;
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
		System.out.println("Frequent Itemsets are saved in output/frequentItemsets.data");

		long end = System.currentTimeMillis();
		double time = (end-start)/1000.0;
		System.out.println("Time: "+time+" sec");

		System.out.println("\nWriting Rules:");
		new RuleGeneration(f.freqK, f.maxLengthOfFreqItemsets);
		System.out.println("Association Rules Generated in output/AssociationRules.data");

		end = System.currentTimeMillis();
		time = (end-start)/1000.0;
		System.out.println("Time: "+time+" sec\nEND...");
	}
}