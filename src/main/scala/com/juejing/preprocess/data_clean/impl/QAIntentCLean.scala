package com.juejing.preprocess.data_clean.impl


import com.juejing.conf.Conf
import com.juejing.preprocess.data_clean.DataClean
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by soledede.weng on 2017-08-14.
  */
class QAIntentClean(conf: Conf,filePath: String, separator: String = "###") extends DataClean {
  /**
    * 对数据进行清洗
    *
    * @param spark    SparkSession
    * @return 清洗后的数据 至少包含字段: "label",  "content"
    */
  override def clean( spark: SparkSession): DataFrame = {
    import spark.implicits._
    val textDF = spark.sparkContext.textFile(filePath).flatMap { line =>
      val fields = line.split(separator)

      if (fields.length > 1) {
        val label = fields(0).trim
        val content = fields(1).trim
        Some(label, content)
      } else None
    }.toDF("label", "content")
    textDF
  }
}
