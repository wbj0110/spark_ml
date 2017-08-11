package com.juejing.algorithm.decision_tree

import java.io.File

import com.juejing.conf.{Conf, Constant}
import com.juejing.utils.IOUtils
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier}
import org.apache.spark.ml.feature.{IndexToString, StringIndexerModel}
import org.apache.spark.sql.DataFrame

/**
  * 决策树多分类
  *
  */
class DTClassifier(conf: Conf) extends Serializable {
  val _constant = Constant(conf)

  /**
    * 决策树模型训练处理过程, 包括: "模型训练"
    *
    * @param data   训练集
    * @return (向量模型, 决策树模型)
    */
  def train(data: DataFrame): DecisionTreeClassificationModel = {

    //=== LR分类模型训练
    data.persist()
    val dtModel = new DecisionTreeClassifier()
      .setMinInfoGain(_constant.minInfoGain)
      .setMaxDepth(_constant.maxDepth)
      .setLabelCol("indexedLabel")
      .setFeaturesCol("features")
      .fit(data)
    data.unpersist()
    this.saveModel(dtModel)

    dtModel
  }


  /**
    * 决策树预测过程, 包括"决策树预测", "模型评估"
    *
    * @param data     测试集
    * @param indexModel 索引模型
    * @return 预测DataFrame, 增加字段:"rawPrediction", "probability", "prediction", "predictedLabel"
    */
  def predict(data: DataFrame, indexModel: StringIndexerModel): DataFrame = {
    val dtModel = this.loadModel()

    //=== DT预测
    val predictions = dtModel.transform(data)

    //=== 索引转换为label
    val labelConverter = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(indexModel.labels)
    val result = labelConverter.transform(predictions)

    result
  }


  /**
    * 保存模型
    *
    * @param dtModel  决策树模型
    */
  def saveModel(dtModel: DecisionTreeClassificationModel): Unit = {
    val filePath = _constant.modelDTPath
    val file = new File(filePath)
    if (file.exists()) {
      println("决策树模型已存在，新模型将覆盖原有模型...")
      IOUtils.delDir(file)
    }

    dtModel.save(filePath)
    println("决策树模型已保存！")
  }


  /**
    * 加载模型
    *
    * @return 决策树模型
    */
  def loadModel(): DecisionTreeClassificationModel = {
    val filePath = _constant.modelDTPath
    val file = new File(filePath)
    if (!file.exists()) {
      println("决策树模型不存在，即将退出！")
      System.exit(1)
    } else {
      println("开始加载决策树模型...")
    }

    val dtModel = DecisionTreeClassificationModel.load(filePath)
    println("决策树模型加载成功！")

    dtModel
  }
}
