package src.Command_Line_Version;

import java.util.ArrayList;

public class Itemset{
	ArrayList<Integer> value;
	int count = 0;
	
	public Itemset(ArrayList<Integer> a){
		value = a;
	}
	
	public boolean equals(Object obj){
		@SuppressWarnings("unchecked")
		ArrayList<Integer> given = (ArrayList<Integer>)obj;
		if(this.value.size()!=given.size())	return false;
		for(int i = 0; i < given.size(); i++){
			if(this.value.get(i)!=given.get(i))	return false;
		}
		return true;
	}
}