package com.juejing.preprocess.data_clean

import org.apache.spark.sql._

/**
  * Created by soledede.weng on 2017-08-14.
  */
trait DataClean extends Serializable{

  /**
    * 对数据进行清洗
    * @param spark    SparkSession
    * @return  清洗后的数据 至少包含字段: "label",  "content"
    */
  def clean(spark: SparkSession): DataFrame

}
