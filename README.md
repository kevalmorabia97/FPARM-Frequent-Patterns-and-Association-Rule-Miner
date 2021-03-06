# _FPARM - Frequent Patterns and Association Rule Miner_

This is an implementation of Apriori algorithm for frequent itemset generation using HashTree data structure and association rules are generated from these frequent itemsets
<br>There is a GUI version and a command line version.
<br>![Image](https://github.com/kevalmorabia97/FPARM-Frequent-Patterns-and-Association-Rule-Miner/blob/master/data/gui.png)<br>

**Running The Program from _FPARM-Frequent-Patterns-and-Association-Rule-Miner_ folder**
<br>**Note:** For Command_Line_Version, the dataset path should be specified in src\Command_Line_Version\Main.java file
```
Command_Line_Version:
$ javac src\Command_Line_Version\Main.java
$ java src.Command_Line_Version.Main

GUI_Version:
$ javac src\GUI_Version\Main.java src\GUI_Version\MainController.java
$ java src.GUI_Version.Main
```
**Note**: If GUI_Version is not running properly then you need to install Oracle JDK as below:
```
$ sudo add-apt-repository ppa:webupd8team/java
$ sudo apt-get update
$ sudo apt-get install oracle-java8-installer
```

You can directly run the application by extracting the jar inside **_FPARM-Frequent.Patterns.and.Association.Rule.Miner.zip_** file and running it. You will need ORACLE JDK to run the JAVA Fx .jar file.
<br>The program can handle any kind of data as long as it is in the required format
<br>**Note:** Dont directly run from zip file as the files generated will be stored in cache. So first extract then only run the .jar.
<hr>

- Support is an indication of how frequently the itemset appears in the dataset.
The support of X with respect to T is defined as the proportion of transactions t in the dataset which contains the itemset X.
 <br>![Image](https://wikimedia.org/api/rest_v1/media/math/render/svg/1c6acacd3b17051205704b5d323c83fc737e5db1)

- Confidence is an indication of how often the rule has been found to be true.
The confidence value of a rule, X => Y with respect to a set of transactions T, is the proportion of the transactions that contains X which also contains Y.
<br>![Image](https://wikimedia.org/api/rest_v1/media/math/render/svg/90324dedc399441696116eed3658fd17c5da4329)

- Apriori Algorithm uses a breadth-first search strategy to count the support of itemsets and uses a candidate generation function which exploits the downward closure property of support.
<br>Read More: https://en.wikipedia.org/wiki/Apriori_algorithm

<hr>

1. First, the application generates frequent 1 itemsets.
2. After generating Frequent K itemsets, it generate Frequent K+1 itemsets by using F<sub>k-1</sub> x F<sub>k-1</sub> approach.
3. The frequent itemsets are stored in **frequentItemsets.data** file
4. When all frequent itemsets are generated, associating rules that have confidence greater than minimum confidence are generated and stored in the file named **associationRules.data** file.

Read about Association Rule Mining: _https://en.wikipedia.org/wiki/Association_rule_learning_

<hr>

To generate frequent itemsets and association rules you have to select the **transaction file** which should be of the following form:
```
apple,ball,egg
apple,ball,dandiya,egg,fish,apple
ball,caramel,dandiya,fish,milk
apple,caramel,fish,fish
apple,ball,dandiya,fish,milk
ball,caramel,fish
```
**Attribute Names Required? - YES or NO**
```
If YES then first line of dataset mush contain names of attributes seperated by comma(,)
Example: buying,maint,doors,person,lug_boo,LABEL

Required if the data is of the following form:

 | buying | maint | doors | persons | lug_boot | LABEL |
---------------------------------------------------------
 | vhigh  | vhigh |  2    |    2    |  small   | unacc
 | vhigh  |  high |  2    |   more  |   big    | unacc
 | vhigh  |  med  |  2    |    4    |  small   |  acc 
 |  high  | vhigh |  4    |    2    |  small   | unacc 

So, the dataset implies that the data is of the form: buying=vhigh,maint=vhigh,doors=2,persons=2,lug_boot=small,LABEL=unacc
Doing this, ambiguity in data values can be eliminated
That is, buying=vhigh is considered different than maint=vhigh
```
For generaly market basket datasets, this is not required as the the values in each dataset row
<br>does not mean that it belongs that particular column

### Note: 
- If the transaction has more than one appearance of an item then only one appearance is considered
- If you have transaction in the form of like this then also the program works
```
bus=yes,plane=no,rich=yes
```
- If you have continuous data then you can descretize the data then run this application
<br>For Ex: divide age in groups like
<br>Age=[0,10)
<br>Age=[10,20)
<br>Age=[20,30)
<br>and so on...
```
Age=[21,30),Salary=[70k,120k),Browser=Mozilla
```

<hr>

After that, the transaction is processed and converted to numbers which makes it easier to handle in the hashtree
The processed transaction is saved inside **_processedTransaction.data_** file which looks like this:
```
0,1,2,
0,1,2,3,4,
1,3,4,5,6,
0,4,5,
0,1,3,4,6,
1,4,5,
```

<hr>

After that the frequent itemsets are generated which are stored in **_frequentItemsets.data_** file which looks like this:
```
Frequent 1 Itemsets:
ball, (8)
egg, (3)
dandiya, (5)
fish, (6)

Frequent 2 Itemsets:
apple, ball, (5)
fish, caramel, (3)

Frequent 3 Itemsets:
apple, ball, egg, (3)
```
Here the number inside parenthesis indicates the support count of the frequent itemset

<hr>

Finally, the association rules are generated and stored in **_associationRules.data_** file which looks like this:
```
apple, (6) ----> ball, (8) conf(0.8333333)
egg, (3) ----> ball, (8) conf(1.0)
milk, (5) ----> caramel, (6) conf(0.8)
dandiya, (5) ----> fish, (6) conf(0.8)
egg, (3) ----> apple, (6) conf(1.0)
```
Here the number inside parenthesis besides the itemset is its support count and conf(*) denotes the confidence of the association rule

**To-Do**: Handling continuous data
