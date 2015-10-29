github link : https://github.com/chaohunc/Foreign-Exchange <br />

## hw4
MainPipelineRFCassandra.java - whole pipline to read the data from "Cassnadra", construct the random forest, and test the accuracy <br />
ForestSerializer.java - serialize and deserialize a random forest to a string to save the forest in Cassandra<br />
ReadFileToCassandra.java - preprocess and save the data at first in Cassandra for MainPipelineRFCassandra.java to read later <br/>

## hw3
MainPipelineRF.java - whole pipline to read the data, construct the random forest, and test the accuracy <br />
ForestConstructor.java - contruct a random forest by using TreeConstructor class to construct a series of decision trees <br />
ForestTester.java - validate the accuracy of the random forest <br />
Helper.java - now with only one function to help split the datasets into training datasets and testing datasets <br />

## hw2
MainPipeline.java - whole pipline to read the data, construct the decsion tree, and test the accuracy <br />
Node.java - Data structure of node of decision tree <br /> 
TreeConstructor.java - construct a tree based on Tuple <br />
TreeTester.java - validate the accuracy of the decision tree <br />
Tuple.java - each data row represents a tuple <br />
