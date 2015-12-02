import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.classification.GBTClassifier
import org.apache.spark.ml.feature.{StringIndexer, IndexToString, VectorIndexer}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SQLContext
import org.apache.spark.ml.feature.PolynomialExpansion
import org.apache.spark.mllib.linalg.Vectors

val sqlContext = new SQLContext(sc)
import sqlContext.implicits._

val dataFrame = MLUtils.loadLabeledPoints(sc, "currency.txt").toDF()

// Transformer. Apply polynomial expansions on the original features
val polyExpansion = new PolynomialExpansion()
  .setInputCol("features")
  .setOutputCol("polyFeatures")
  .setDegree(2)

val polyDF = polyExpansion.transform(dataFrame)

val labelIndexer = new StringIndexer()
    .setInputCol("label")
    .setOutputCol("indexedLabel")
    .fit(polyDF)

// Automatically identify categorical features, and index them. 
// If the features with less and equal than 3 distinct value (max categories), they will be treated as catgorical value
val featureIndexer = new VectorIndexer()
  .setInputCol("polyFeatures")
  .setOutputCol("maxCatFeatures")
  .setMaxCategories(3)
  .fit(polyDF)
  
// Split the data into training (80%) and test sets (20%)
val Array(trainingData, testData) = polyDF.randomSplit(Array(0.8, 0.2))

// Estimator 1. Train a LogisticRegression classifier.
val lr = new LogisticRegression()
  .setMaxIter(30)
  .setLabelCol("indexedLabel")
  .setFeaturesCol("maxCatFeatures")
  .setRegParam(0.0001)

// Estimator 2. Train a RandomForest classifier.
val rf = new RandomForestClassifier()
  .setLabelCol("indexedLabel")
  .setFeaturesCol("maxCatFeatures")
  .setNumTrees(100)

// Estimator 3. Train a Gradient Boosted classifier.
val gbt = new GBTClassifier()
  .setLabelCol("indexedLabel")
  .setFeaturesCol("maxCatFeatures")
  .setMaxIter(30)

// PipelineForLRmodel
val lrmodel = new Pipeline()
  .setStages(Array(labelIndexer, featureIndexer, lr)).fit(trainingData)

// PipelineForRFmodel
val rfmodel = new Pipeline()
  .setStages(Array(labelIndexer, featureIndexer, rf)).fit(trainingData)

// PipelineForGBTmodel
val gbtmodel = new Pipeline()
  .setStages(Array(labelIndexer, featureIndexer, gbt)).fit(trainingData)

// Apply estimators on testing data
val predictionsLR = lrmodel.transform(testData)
val predictionsRF = rfmodel.transform(testData)
val predictionsGBT = gbtmodel.transform(testData)
  
val evaluator = new MulticlassClassificationEvaluator()
  .setLabelCol("indexedLabel")
  .setPredictionCol("prediction")
  .setMetricName("precision")

val accuracyLR = evaluator.evaluate(predictionsLR)
val accuracyRF = evaluator.evaluate(predictionsRF)
val accuracyGBT = evaluator.evaluate(predictionsGBT)

println("Logistic Regression Accuracy; " + (accuracyLR))
println("RandomForest Accuracy; " + (accuracyRF))
println("Gradient Boosted Tree Accuracy; " + (accuracyGBT))