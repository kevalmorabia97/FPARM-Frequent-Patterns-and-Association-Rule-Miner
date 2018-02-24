package src.GUI_Version;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;

public class MainController implements Initializable{
	@FXML private TextArea status;
	@FXML private Button generateRules;
	@FXML private Button transactionFileChooser;
	@FXML private TextField TFminSup;
	@FXML private TextField TFminConf;
	@FXML private TextField TFnoOfChildsInHT;
	@FXML private TextField TFmaxItemsPerNodeInHT;
	@FXML private Hyperlink githubLink;
	static File transactionFile,processedTransactionFile,freqItemsetFile,rulesFile;
	static int noOfTransactions, noOfChildsInHT, maxItemsPerNodeInHT, noOfAttributes;
	static double minSup, minConf;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TFminSup.setTooltip(new Tooltip("Minimum Support"));
		TFminConf.setTooltip(new Tooltip("Minimum Confidence"));
		TFnoOfChildsInHT.setTooltip(new Tooltip("No Of Childs In HashTree"));
		TFmaxItemsPerNodeInHT.setTooltip(new Tooltip("Max Items Per Node In HashTree"));
		status.setEditable(false);
		
		githubLink.setTooltip(new Tooltip("kevalmorabia97/FPARM-Frequent-Patterns-and-Association-Rule-Miner"));
		githubLink.setOnAction(e -> {
		        try {
					new ProcessBuilder("x-www-browser", "https://github.com/kevalmorabia97/FPARM-Frequent-Patterns-and-Association-Rule-Miner").start();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		});
		
	}

	@FXML public void getTransactionFile() {
		FileChooser fc = new FileChooser();
		//fc.getExtensionFilters().add(new ExtensionFilter("Text", "*.txt"));
		/*
		try {
			fc.setInitialDirectory(new File(MainController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}*/
		transactionFile = fc.showOpenDialog(null);
		if(transactionFile == null){
			status.setText("Transaction File not Selected");
		}
	}

	@FXML public void btnGenRules(ActionEvent event){
		status.setText("Generating frequent itemsets and Association Rules");
		genRules(event);
	}
	
	private void genRules(ActionEvent event){
		status.setText("");
		processedTransactionFile = new File("data/processedTransaction.data");
		freqItemsetFile = new File("data/frequentItemsets.data");
		rulesFile = new File("data/associationRules.data");

		try {
			minSup = Double.valueOf(TFminSup.getText());
			if(minSup>1 || minSup<0){throw new NumberFormatException();}
		} catch (NumberFormatException e) {
			status.setText("Minimum Support should be a double value between 0 and 1");
			e.printStackTrace();
			return;
		}

		try {
			minConf = Double.valueOf(TFminConf.getText());
			if(minConf>1 || minConf<0){throw new NumberFormatException();}
		} catch (NumberFormatException e) {
			status.setText("Minimum Confidence should be a double value between 0 and 1");
			e.printStackTrace();
			return;
		}

		try {
			noOfChildsInHT = Integer.valueOf(TFnoOfChildsInHT.getText());
			if(noOfChildsInHT<=0){throw new NumberFormatException();}
		} catch (NumberFormatException e) {
			status.setText("No Of Childs in HashTree should be a positive integer value");
			e.printStackTrace();
			return;
		}

		try {
			maxItemsPerNodeInHT = Integer.valueOf(TFmaxItemsPerNodeInHT.getText());
			if(maxItemsPerNodeInHT<=0){throw new NumberFormatException();}
		} catch (NumberFormatException e) {
			status.setText("Max items per node in HashTree should be a positive integer value");
			e.printStackTrace();
			return;
		}

		try {
			new Preprocess();
		} catch (Exception e) {
			status.setText("Transaction File is not selected or in the required format");
			e.printStackTrace();
			return;
		}
		noOfTransactions = Preprocess.noOfTransactions;
		noOfAttributes = Preprocess.noOfAttributes;
		long start = System.currentTimeMillis();

		//status.setText(status.getText()+"Generating Frequent Itemsets:");
		FrequentItemsetGeneration f;
		try {
			f = new FrequentItemsetGeneration();

			//Saving Frequent Itemsets in frequentItemsets.txt
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
				status.appendText("Frequent "+k+": "+FK.size()+"\n");
			}
			bw.close();
			status.appendText("\nFrequent Itemsets are saved in data/frequentItemsets.data\n");
		} catch (IOException e) {
			status.appendText("Error in generating frequent itemsets");
			e.printStackTrace();
			return;
		}

		long end = System.currentTimeMillis();
		double time = (end-start)/1000.0;
		status.appendText("Time: "+time+" sec");

		status.appendText("\n\nWriting Rules:");
		try {
			new RuleGeneration(f.freqK, f.maxLengthOfFreqItemsets);
		} catch (IOException e) {
			status.appendText("Error in rule Generation");
			e.printStackTrace();
			return;
		}

		end = System.currentTimeMillis();
		time = (end-start)/1000.0;
		status.appendText("\nTime: "+time+" sec\nEND...");
		status.appendText("\n\nAssociation Rules Generated in data/AssociationRules.data");
	}

}
