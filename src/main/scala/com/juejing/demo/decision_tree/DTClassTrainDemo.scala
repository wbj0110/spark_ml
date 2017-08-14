package com.juejing.demo.decision_tree

import com.juejing.algorithm.decision_tree.DTClassifier
import com.juejing.conf.Conf
import com.juejing.preprocess.Preprocessor
import com.juejing.preprocess.data_clean.impl.ChinaNewsClean
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * 基于决策树的多分类模型训练
  *
  */
object DTClassTrainDemo extends Serializable {
  val conf = new Conf()
  val filePath = "data/classnews/train"
  val chinaNewsClean = new ChinaNewsClean(conf,filePath)


  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    //    HanLP.Config.enableDebug()

    val spark = SparkSession
      .builder
      .master("local[2]")
      .appName("DT_Train_Demo")
      .getOrCreate()



    //=== 预处理(清洗、标签索引化、分词、向量化)
    val preprocessor = Preprocessor(conf,chinaNewsClean)
    val trainDF = preprocessor.forPredict(spark)._1

    //=== 模型训练
    val dtClassifier = new DTClassifier(conf)
    dtClassifier.train(trainDF)

    spark.stop()
  }
}
