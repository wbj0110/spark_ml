package com.juejing.demo.logistic_regression

import com.juejing.algorithm.logistic_regression.LRClassifier
import com.juejing.conf.Conf
import com.juejing.preprocess.Preprocessor
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * 基于逻辑回归的多分类模型训练
  *
  */
object LRClassTrainDemo extends Serializable {
  val conf = new Conf()

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    //    HanLP.Config.enableDebug()

    val spark = SparkSession
      .builder
      .master("local[2]")
      .appName("LR_Train_Demo")
      .getOrCreate()

    val filePath = "data/classnews/train"

    //=== 预处理(清洗、标签索引化、分词、向量化)
    val preprocessor = Preprocessor(conf)
    val trainDF = preprocessor.forTrain(filePath, spark)._1

    //=== 模型训练
    val lrClassifier = new LRClassifier(conf)
    lrClassifier.train(trainDF)

    spark.stop()
  }
}
