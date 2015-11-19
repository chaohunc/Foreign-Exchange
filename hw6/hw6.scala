import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.util.MLUtils

val data = MLUtils.loadLabeledPoints(sc,"/user/root/currencyData.txt")
val percentOfTrainingData = 0.8
val numTrees = 20

val dataSplits = data.randomSplit(Array(percentOfTrainingData, 1-percentOfTrainingData))
val trainingData = dataSplits(0)
val testingData = dataSplits(1)
val testingDataNum = testingData.count()

val rfmodel = RandomForest.trainClassifier( trainingData, 2, Map[Int, Int](),
  numTrees, "auto", "gini", 4, 16)

val rowLabelWithRowPrediction = testingData.map { row =>
  val predLabel = rfmodel.predict(row.features)
  (row.label, predLabel)
}
val testAccFil = rowLabelWithRowPrediction.filter(l => l._1 == l._2)
val testAcc = testAccFil.count.toDouble / testingDataNum
println("RF model accuracy = " + testAcc)