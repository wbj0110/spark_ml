package com.juejing.demo.preprocess

import com.juejing.conf.Conf
import com.juejing.preprocess.Preprocessor
import com.juejing.preprocess.data_clean.impl.ChinaNewsClean
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * 预处理，包括: "数据清洗", "标签索引化", "分词", "向量化"
  *
  */
object ChinaNewsPreprocessDemo extends Serializable {
  val conf = new Conf()

  //val filePath = "C:\\Users\\soledede.weng\\Documents\\spark_ml\\data\\classnews\\train"
  val filePath = "data\\classnews\\train"
  val chinaNewsClean = new ChinaNewsClean(conf,filePath)

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)
    //    HanLP.Config.enableDebug()
    val spark = SparkSession
      .builder
      .master("local[2]")
      .appName("Preprocess Demo")
      .getOrCreate()




    val preprocessor = Preprocessor(conf,chinaNewsClean)
    preprocessor.forTrain(filePath, spark)

    spark.stop()
  }
}
