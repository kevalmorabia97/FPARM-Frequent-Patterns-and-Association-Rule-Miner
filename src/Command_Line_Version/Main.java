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
		String transactionFile,processedTransactionFile,freqItemsetFile,rulesFile;
		int noOfTransactions, noOfAttributes, noOfChildsInHT, maxItemsPerNodeInHT; 
		double minSup, minConf;
		boolean attributesRequired;
		
		////////// INPUT //////////
		minSup=.1; minConf=.5;
		noOfChildsInHT=4; maxItemsPerNodeInHT=5; 
		
		/* car.data DATASET */
		transactionFile = "data/car.data";
		attributesRequired = true; // if 1st line contains attribute names seperated by comma(,)
		
		/* groceries.csv DATASET */
		//transactionFile = "data/groceries.csv";
		//attributesRequired = false; // if 1st line contains attribute names seperated by comma(,)
		
		////////// OUTPUT //////////
		processedTransactionFile = "output/processedTransaction.data";
		freqItemsetFile = "output/frequentItemsets.data";
		rulesFile = "output/associationRules.data";
		
///////////////////// DONT MODIFY AFTER THIS ///////////////////////////////////////////////		

		Preprocess p = new Preprocess(attributesRequired, new File(transactionFile), processedTransactionFile);
		noOfTransactions = p.noOfTransactions;
		noOfAttributes = p.noOfAttributes;
		long start = System.currentTimeMillis();

		System.out.println("Generating Frequent Itemsets:");
		FrequentItemsetGeneration f = new FrequentItemsetGeneration(minSup,noOfTransactions,noOfAttributes,noOfChildsInHT,maxItemsPerNodeInHT,processedTransactionFile);

		BufferedWriter bw = new BufferedWriter(new FileWriter(freqItemsetFile));
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
		System.out.println("Frequent Itemsets are saved in output/frequentItemsets.data");

		long end = System.currentTimeMillis();
		double time = (end-start)/1000.0;
		System.out.println("Time: "+time+" sec");

		System.out.println("\nWriting Rules:");
		new RuleGeneration(f.freqK,p.noToAttr,f.maxLengthOfFreqItemsets,minSup,minConf,rulesFile);
		System.out.println("Association Rules Generated in output/AssociationRules.data");

		end = System.currentTimeMillis();
		time = (end-start)/1000.0;
		System.out.println("Time: "+time+" sec\nEND...");
	}
}