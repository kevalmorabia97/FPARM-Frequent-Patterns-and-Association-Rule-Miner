# _Apriori Frequent Itemset Generation and Association Rule Mining in Java_

This is an implementation of Apriori algorithm for frequent itemset generation using HashTree data structure and association rules are generated from these frequent itemsets

The GUI of this application is made using JAVA FX and scene builder

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

1. Numbered First, the application generates frequent 1 itemsets.
2. Numbered After generating Frequent K itemsets, it generate Frequent K+1 itemsets by using F<sub>k-1</sub> x F<sub>k-1</sub> approach.
3. Numbered The frequent itemsets are stored in **frequentItemsets.txt** file
4. Numbered When all frequent itemsets are generated, associating rules that have confidence greater than minimum confidence are generated and stored in the file named **AssociationRules.txt** file.

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
apple,ball,dandiya,dandiya,egg,fish,apple
caramel,dandiya,milk
apple,ball,caramel,milk
ball,caramel,milk,ball
```
### Note: 
- If the transaction has more than one appearance of an item then only one appearance is considered
- If you have transaction in the form of like this then also the algorithm works
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
The processed transaction is saved inside **_ProcessedTransaction.txt_** file which looks like this:
```
0,1,2,
0,1,2,3,4,
1,3,4,5,6,
0,4,5,
0,1,3,4,6,
1,4,5,
0,1,2,3,4,
3,5,6,
0,1,5,6,
1,5,6,
```

<hr>

After that the frequent itemsets are generated which are stored in **_frequentItemsets.txt_** file which looks like this:
```
Frequent 1 Itemsets:
ball, (8)
egg, (3)
dandiya, (5)
fish, (6)
caramel, (6)
milk, (5)
apple, (6)
Frequent 2 Itemsets:
apple, ball, (5)
fish, caramel, (3)
ball, egg, (3)
caramel, milk, (4)
dandiya, fish, (4)
apple, egg, (3)
ball, dandiya, (4)
apple, dandiya, (3)
ball, fish, (5)
dandiya, milk, (3)
apple, fish, (4)
ball, caramel, (4)
ball, milk, (4)
Frequent 3 Itemsets:
apple, ball, egg, (3)
apple, dandiya, fish, (3)
apple, ball, dandiya, (3)
ball, dandiya, fish, (4)
ball, caramel, milk, (3)
apple, ball, fish, (3)
Frequent 4 Itemsets:
apple, ball, dandiya, fish, (3)
```
Here the number inside parenthesis indicates the support count of the frequent itemset

<hr>

Finally, the association rules are generated and stored in **_AssociationRules.txt_** file which looks like this:
```
apple, (6) ----> ball, (8) conf(0.8333333)
egg, (3) ----> ball, (8) conf(1.0)
milk, (5) ----> caramel, (6) conf(0.8)
dandiya, (5) ----> fish, (6) conf(0.8)
egg, (3) ----> apple, (6) conf(1.0)
dandiya, (5) ----> ball, (8) conf(0.8)
fish, (6) ----> ball, (8) conf(0.8333333)
milk, (5) ----> ball, (8) conf(0.8)
ball, egg, (3) ----> apple, (6) conf(1.0)
apple, egg, (3) ----> ball, (8) conf(1.0)
egg, (3) ----> apple, ball, (5) conf(1.0)
dandiya, fish, (4) ----> apple, (6) conf(0.75)
apple, fish, (4) ----> dandiya, (5) conf(0.75)
apple, dandiya, (3) ----> fish, (6) conf(1.0)
ball, dandiya, (4) ----> apple, (6) conf(0.75)
apple, dandiya, (3) ----> ball, (8) conf(1.0)
dandiya, fish, (4) ----> ball, (8) conf(1.0)
ball, fish, (5) ----> dandiya, (5) conf(0.8)
ball, dandiya, (4) ----> fish, (6) conf(1.0)
dandiya, (5) ----> ball, fish, (5) conf(0.8)
caramel, milk, (4) ----> ball, (8) conf(0.75)
ball, milk, (4) ----> caramel, (6) conf(0.75)
ball, caramel, (4) ----> milk, (5) conf(0.75)
apple, fish, (4) ----> ball, (8) conf(0.75)
ball, dandiya, fish, (4) ----> apple, (6) conf(0.75)
apple, dandiya, fish, (3) ----> ball, (8) conf(1.0)
apple, ball, fish, (3) ----> dandiya, (5) conf(1.0)
apple, ball, dandiya, (3) ----> fish, (6) conf(1.0)
dandiya, fish, (4) ----> apple, ball, (5) conf(0.75)
ball, dandiya, (4) ----> apple, fish, (4) conf(0.75)
apple, fish, (4) ----> ball, dandiya, (4) conf(0.75)
apple, dandiya, (3) ----> ball, fish, (5) conf(1.0)
```
Here the number inside parenthesis besides the itemset is its support count and conf(*) denotes the confidence of the association rule

<hr>

**Feel free to contact me if you have any doubt. My email ID is: f2015143@hyderabad.bits-pilani.ac.in**
