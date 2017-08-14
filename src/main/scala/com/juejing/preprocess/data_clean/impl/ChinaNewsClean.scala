package com.juejing.preprocess.data_clean.impl

import com.juejing.conf.Conf
import com.juejing.preprocess.data_clean.DataClean
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by soledede.weng on 2017-08-14.
  */
class ChinaNewsClean(conf: Conf,filePath: String) extends DataClean{
  /**
    * 对数据进行清洗
    *
    * @param spark    SparkSession
    * @return 清洗后的数据, 包含字段: "label", "title", "time", "content"
    */
  override def clean(spark: SparkSession): DataFrame = {
    import spark.implicits._
    val textDF = spark.sparkContext.textFile(filePath).flatMap { line =>
      val fields = line.split("\u00EF")

      if (fields.length > 3) {
        val categoryLine = fields(0)
        val categories = categoryLine.split("\\|")
        val category = categories.last

        var label = "其他"
        if (category.contains("文化")) label = "文化"
        else if (category.contains("财经")) label = "财经"
        else if (category.contains("军事")) label = "军事"
        else if (category.contains("体育")) label = "体育"
        else {}

        val title = fields(1)
        val time = fields(2)
        val content = fields(3)
        if (!label.equals("其他")) Some(label, title, time, content) else None
      } else None
    }.toDF("label", "title", "time", "content")

    textDF
  }
}
