package com.juejing.conf

/**
  * record constant
  *
  * @author soledede
  */
 class Constant private(conf: Conf) {


  val preffix = "spark_ml."

  /**
    * Preprocess
    * ############################################################################################
    */


  /**
    * 停用词表路径
    */
  val stopwordFilePath: String = conf.get(s"${preffix}stopword.file.path", "dictionaries/hanlp/data/dictionary/stopwords.txt")

  /**
    * 分词方式
    */
  val segmentType: String = conf.get(s"${preffix}segment.type", "StandardSegment")

  /**
    * 是否去除数字
    */
  val delNum: Boolean = conf.getBoolean(s"${preffix}is.delete.number", false)

  /**
    * 是否去除英语单词
    */
  val delEn: Boolean = conf.getBoolean(s"${preffix}is.delete.english", false)

  /**
    * 是否添加词性
    */
  val addNature: Boolean = conf.getBoolean(s"${preffix}add.nature", false)

  /**
    * 最小词长度
    */
  val minTermLen: Int = conf.getInt(s"${preffix}min.term.len", 1)

  /**
    * 行最小词数
    */
  val minTermNum: Int = conf.getInt(s"${preffix}min.term.num", 3)

  /**
    * 特征词汇表大小
    */
  val vocabSize: Int = conf.getInt(s"${preffix}vocab.size", 10000)


  val indexModelPath: String =  conf.get(s"${preffix}model.index.path", "models/preprocession/indexModel")

  val vecModelPath: String = conf.get(s"${preffix}model.vectorize.path", "models/preprocession/vecModel")




  /**
    * Classfication
    * ############################################################################################
    */


  ////逻辑回归

  /**
    * 模型最大迭代次数
    */
  val maxIteration: Int = conf.getInt(s"${preffix}max.iteration", 100)

  /**
    * 正则化项参数
    */
  val regParam: Double = conf.getDouble(s"${preffix}reg.param", 0.3)

  /**
    * L1范式比例, L1/(L1 + L2)
    */
  val elasticNetParam: Double = conf.getDouble(s"${preffix}elastic.net.param", 0.1)

  /**
    * 模型收敛阈值
    */
  val converTol: Double = conf.getDouble(s"${preffix}conver.tol", 1E-6)


  /**
    * LR模型保存路径
    */
  val modelLRPath: String = conf.get(s"${preffix}model.lr.path", "models/classification/lrModel")



  //决策树

  /**
    * 最小信息增益阈值
    */
  val minInfoGain: Double = conf.getDouble(s"${preffix}min.info.gain", 0.0)


  /**
    * 决策树最大深度
    */
  val maxDepth: Int = conf.getInt(s"${preffix}max.depth", 10)


  /**
    * 决策树模型保存路径
    */
  val modelDTPath: String = conf.get(s"${preffix}model.dt.path", "models/classification/dtModel")

}

object Constant extends Configuration{
  var constant: Constant = null

  def apply(conf: Conf): Constant = {
    if (constant == null) constant = new Constant(conf)
    constant
  }
}