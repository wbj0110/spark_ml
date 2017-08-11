package com.juejing.demo.logistic_regression

import com.juejing.algorithm.logistic_regression.LRClassifier
import com.juejing.conf.Conf
import com.juejing.preprocess.Preprocessor
import com.juejing.utils.Evaluations
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Row, SparkSession}

/**
  * 基于逻辑回归模型的多分类测试
  *
  */
object LRClassPredictDemo extends Serializable {
  val conf = new Conf()


  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    //    HanLP.Config.enableDebug()

    val spark = SparkSession
      .builder
      .master("local[2]")
      .appName("LR_Predict_Demo")
      .getOrCreate()

    val filePath = "data/classnews/predict"

    //=== 预处理(清洗、分词、向量化)
    val preprocessor = Preprocessor(conf)
    val (predictDF, indexModel, _) = preprocessor.forPredict(filePath, spark)

    //=== 模型预测
    val lrClassifier = new LRClassifier
    val predictions = lrClassifier.predict(predictDF, indexModel)

    //=== 模型评估
    val resultRDD = predictions.select("prediction", "indexedLabel").rdd.map{
      case Row(prediction: Double, label: Double) => (prediction, label)
    }
    val (precision, recall, f1) = Evaluations.multiClassEvaluate(resultRDD)
    println("\n\n========= 评估结果 ==========")
    println(s"\n加权准确率：$precision")
    println(s"加权召回率：$recall")
    println(s"F1值：$f1")

    predictions.select("label", "predictedLabel", "content").show(100, truncate = false)

    spark.stop()
  }
}
