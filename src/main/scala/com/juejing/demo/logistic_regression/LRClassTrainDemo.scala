package com.juejing.demo.logistic_regression

import com.juejing.algorithm.logistic_regression.LRClassifier
import com.juejing.conf.Conf
import com.juejing.preprocess.Preprocessor
import com.juejing.preprocess.data_clean.impl.{ChinaNewsClean, QAIntentClean}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * 基于逻辑回归的多分类模型训练
  *
  */
object LRClassTrainDemo extends Serializable {
  val conf = new Conf()

  //val filePath = "data/classnews/train"
  val filePath = "data/qa/train"
  //val chinaNewsClean = new ChinaNewsClean(conf)

  val qaIntentClean = new QAIntentClean(conf,filePath)

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    //    HanLP.Config.enableDebug()

    val spark = SparkSession
      .builder
      .master("local[2]")
      .appName("LR_Train_Demo")
      .getOrCreate()



    //=== 预处理(清洗、标签索引化、分词、向量化)
    val preprocessor = Preprocessor(conf,qaIntentClean)
    val trainDF = preprocessor.forTrain(filePath, spark)._1

    //=== 模型训练
    val lrClassifier = new LRClassifier(conf)
    lrClassifier.train(trainDF)

    spark.stop()
  }
}
