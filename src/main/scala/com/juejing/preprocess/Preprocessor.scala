package com.juejing.preprocess

import com.juejing.conf.Conf
import com.juejing.preprocess.data_clean.DataClean
import com.juejing.preprocess.data_clean.impl.ChinaNewsClean
import com.juejing.preprocess.impl.ChinaNewsPreprocessor
import org.apache.spark.ml.feature.{CountVectorizerModel, StringIndexerModel}
import org.apache.spark.sql._

/**
  * 数据预处理
  * Created by soledede.weng on 2017-08-11.
  */
trait Preprocessor {

  /**
    * 用于训练的预处理
    *
    * @param filePath 数据路径
    * @param spark    SparkSession
    * @return (预处理后的数据, 索引模型, 向量模型), 数据包括字段: "label", "indexedLabel", ... "features"
    */
  def forTrain(filePath: String, spark: SparkSession): (DataFrame, StringIndexerModel, CountVectorizerModel)


  /**
    * 用于训练的预处理
    *
    * @param spark    SparkSession
    * @return (预处理后的数据, 索引模型, 向量模型), 数据包括字段: "label", "indexedLabel", ... "features"
    */
  def forPredict(spark: SparkSession): (DataFrame, StringIndexerModel, CountVectorizerModel)

}

object Preprocessor {
  def apply( conf: Conf,dataClean: DataClean,preprocessor: String="china_news"): Preprocessor = {
    preprocessor match {
      case "china_news" => new ChinaNewsPreprocessor(conf,dataClean)
      case _ => null
    }
  }
}