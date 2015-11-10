github link : https://github.com/chaohunc/Foreign-Exchange <br />

## hw5
RandomForestDriver.java - a driver that initializes the mapreduce pipeline <br />
RandomForestMapper.java - a mapper that reads input data and constructs a decision tree in each mapper <br />
RandomForestReducer.java - reducer that combines all decision trees together and put the results back to cassandra <br />

## hw4
ForestSerializer.java - serialize and deserialize a random forest to a string to save the forest in Cassandra<br />

## hw2
Node.java - Data structure of node of decision tree <br /> 
TreeConstructor.java - construct a tree based on Tuple <br />
Tuple.java - each data row represents a tuple <br />
