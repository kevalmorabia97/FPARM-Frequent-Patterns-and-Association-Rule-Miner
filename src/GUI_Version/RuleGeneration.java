package src.GUI_Version;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

public class RuleGeneration {
	double minSup, minConf;
	int NoOfTransactions, maxLengthOfFreqItemsets;
	ArrayList<HashMap<ArrayList<Integer>,Integer>> freqK;
	Hashtable<Integer,String> noToAttr;
	BufferedWriter bw;
	
	public RuleGeneration(ArrayList<HashMap<ArrayList<Integer>,Integer>> freqK, int maxLengthOfFreqItemsets, double minSup, double minConf, int NoOfTransactions, Hashtable<Integer, String> noToAttr) throws IOException{
		this.minSup = minSup;
		this.minConf = minConf;
		this.NoOfTransactions = NoOfTransactions;
		this.maxLengthOfFreqItemsets = maxLengthOfFreqItemsets;
		this.freqK = freqK;
		this.noToAttr = noToAttr;
		bw = new BufferedWriter(new FileWriter("AssociationRules.txt"));
		generateRules();
		bw.close();
	}
	
	void generateRules() throws IOException{
		for(int k = 2; k <= maxLengthOfFreqItemsets; k++){
			HashMap<ArrayList<Integer>,Integer> FK = freqK.get(k-1);
			Set<ArrayList<Integer>> s = FK.keySet();
			for(ArrayList<Integer> fk : s){
				int countFk = FK.get(fk);
				ArrayList<ArrayList<Integer>> conseq = new ArrayList<>();
				for(int i = 0; i < k; i++){
					ArrayList<Integer> c = new ArrayList<>();
					c.add(fk.get(i));
					conseq.add(c);
				}
				generateRulesFrom(fk, conseq, countFk);//consequents of size 1
			}
		}
	}
	
	void generateRulesFrom(ArrayList<Integer> fk, ArrayList<ArrayList<Integer>> consequents, int countFk) throws IOException{
		if(consequents.size()==0 || consequents.get(0).size()>=fk.size())return;
		
		ArrayList<ArrayList<Integer>> conseqWithMoreConf = new ArrayList<>();
		for(ArrayList<Integer> conseq : consequents){
			ArrayList<Integer> antec = new ArrayList<>(fk);
			antec.removeAll(conseq);
			int antecCount = freqK.get(antec.size()-1).get(antec);
			int ConseqCount = freqK.get(conseq.size()-1).get(conseq);
			float conf = (float)countFk/antecCount;
			if(conf >= minConf){
				conseqWithMoreConf.add(conseq);
				bw.write(ConvertItemset(antec)+"("+antecCount+") ----> "+ConvertItemset(conseq)+"("+ConseqCount+") conf("+conf+")");
				bw.newLine();
			}
		}
		
		//aprioriGen function
		ArrayList<ArrayList<Integer>> newConsequents = new ArrayList<>();
		for(int i = 0; i < conseqWithMoreConf.size(); i++){
			for(int j = i+1; j < conseqWithMoreConf.size(); j++){
				ArrayList<Integer> i1 = conseqWithMoreConf.get(i);
				ArrayList<Integer> i2 = conseqWithMoreConf.get(j);
				
				int width = i1.size();
				int min = Math.min(i1.get(width-1), i2.get(width-1));
				int max = Math.max(i1.get(width-1), i2.get(width-1));
				
				boolean possible = true;
				ArrayList<Integer> newCons = new ArrayList<>();
				for(int x = 0; x < width-1; x++){
					if(i1.get(x)!=i2.get(x)){
						possible = false;
						break;
					}else	newCons.add(i1.get(x));
				}
				if(possible){
					newCons.add(min);
					newCons.add(max);
					newConsequents.add(newCons);
				}
			}
		}
		generateRulesFrom(fk, newConsequents, countFk);
	}
	
	String ConvertItemset(ArrayList<Integer> a){
		String s = "";
		for(int i : a){
			s+= noToAttr.get(i)+", ";
		}
		return s;
	}
}
