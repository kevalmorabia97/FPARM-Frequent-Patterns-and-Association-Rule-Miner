package src.Command_Line_Version;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Preprocess{
	Hashtable<Integer, String> noToAttr = new Hashtable<>();
	Hashtable<String, Integer> AttrToNo = new Hashtable<>();
	File transactionFile;
	int noOfTransactions=0, noOfAttributes=0;

	public Preprocess(File transactionFile) throws IOException{
		this.transactionFile = transactionFile;
		preprocess();
	}

	void preprocess() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(transactionFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/ProcessedTransaction.txt"));
		String s;
		while((s=br.readLine())!=null){	
			bw.write(convert(s));
			noOfTransactions++;
		}
		br.close();
		bw.close();
	}

	String convert(String transaction){
		StringTokenizer st = new StringTokenizer(transaction,",");
		String s = "";
		ArrayList<Integer> trans = new ArrayList<>();
		while(st.hasMoreTokens()){
			String token = st.nextToken();
			if(!AttrToNo.containsKey(token)){
				AttrToNo.put(token, noOfAttributes);
				noToAttr.put(noOfAttributes, token);
				noOfAttributes++;
			}
			trans.add(AttrToNo.get(token));
		}
		Collections.sort(trans);
		ArrayList<Integer> transWithNoDuplicates = new ArrayList<>();//considering every item atmost once
		for(int i : trans){
			if(!transWithNoDuplicates.contains(i)){
				transWithNoDuplicates.add(i);
			}
		}
		for(int i : transWithNoDuplicates){
			s+=i+",";
		}
		s+="\n";
		return s;
	}
}
