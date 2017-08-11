package com.juejing.utils

import java.io.{File, FileInputStream, IOException, InputStreamReader}
import java.util.Properties

import com.juejing.conf.{Conf, MyException}

import scala.collection.JavaConversions.{asScalaSet, enumerationAsScalaIterator, propertiesAsScalaMap}

import scala.collection.Map


/**
  * Created by soledede.weng on 2017-08-11.
  */
object Utils extends Logging{


  def loadDefaultProperties(conf: Conf, filePath: String = null): String = {
    val path = Option(filePath).getOrElse(getDefaultPropertiesFile())
    if (path == null) {
      Option(getClass.getClassLoader.getResourceAsStream("defaults.properties")) match {
        case Some(input) =>
          val properties = new Properties()
          properties.load(input)
          properties.filter {
            case (k, v) =>
              k.startsWith("com.juejing.") || k.startsWith("spark.") || k.startsWith("spark_ml.")
          }.foreach {
            case (k, v) =>
              conf.setIfMissing(k, v)
              sys.props.getOrElseUpdate(k, v)
          }
        case None =>
          logError("cant't found defaults.properties")
      }
    } else {
      Option(path).foreach { confFile =>
        getPropertiesFromFile(confFile).filter {
          case (k, v) =>
            k.startsWith("com.juejing.") || k.startsWith("spark.") || k.startsWith("spark_ml.")
        }.foreach {
          case (k, v) =>
            conf.setIfMissing(k, v)
            sys.props.getOrElseUpdate(k, v)
        }
      }
    }
    path
  }



  def getPropertiesFromFile(filename: String): Map[String, String] = {
    val file = new File(filename)
    require(file.exists(), s"Properties file $file does not exist")
    require(file.isFile(), s"Properties file $file is not a normal file")

    val inReader = new InputStreamReader(new FileInputStream(file), "UTF-8")
    try {
      val properties = new Properties()
      properties.load(inReader)
      properties.stringPropertyNames().map(k => (k, properties(k).trim)).toMap
    } catch {
      case e: IOException =>
        throw new MyException(s"Failed when loading  properties from $filename", e)
    } finally {
      inReader.close()
    }
  }

  def getDefaultPropertiesFile(env: Map[String, String] = sys.env): String = {
    var filePath = env.get("CONF_DIR")
      .orElse(env.get("SPARK_ML_HOME").map { t => s"$t${File.separator}conf" })
      .map { t => new File(s"$t${File.separator}defaults.properties") }
      .filter(_.isFile)
      .map(_.getAbsolutePath)
      .orNull
    /*if(filePath==null){
      Option(getClass.getClassLoader.getResource("defaults.properties")) match {
        case Some(url) =>
          filePath = url.getPath
        case None =>
          logError("cant't found defaults.properties")
      }
    }*/
    if (filePath == null) {
      try {
        filePath = System.getProperty("conf")
        if (filePath != null) return filePath
      } catch {
        case e: Exception =>
          logError("can't found defaults.properties from sys")
      }
    }
    filePath
  }












}
