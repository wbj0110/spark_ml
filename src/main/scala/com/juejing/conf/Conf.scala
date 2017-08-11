package com.juejing.conf

import com.juejing.utils.{Logging, Utils}

import scala.collection.JavaConverters._
import scala.collection.mutable.HashMap

/**
  * @author:soledede
  * @email:wengbenjue @163.com
  */
class Conf(loadDefaults: Boolean) extends Cloneable with Logging with Serializable with Configuration {
    val settings = new HashMap[String, String]()

  import Conf._

  /** Create a CrawlerConf that loads defaults from system properties and the classpath */
  def this() = this(true)

  //加载默认配置文件，crawler-defaults.conf
  Utils.loadDefaultProperties(this, null)






  if (loadDefaults) {
    // Load any crawler.* system properties
    for ((k, v) <- System.getProperties.asScala if(k.startsWith("com.juejing.") || k.startsWith("spark.") || k.startsWith("spark_ml."))) {
      settings(k) = v
    }
  }


  def set(key: String, value: String): Conf = {
    if (key == null) {
      throw new NullPointerException("null key")
    }
    if (value == null) {
      throw new NullPointerException("null value")
    }
    settings(key) = value
    this
  }


  /** Set multiple parameters together */
  def setAll(settings: Traversable[(String, String)]) = {
    this.settings ++= settings
    this
  }

  /** Set a parameter if it isn't already configured */
  def setIfMissing(key: String, value: String): Conf = {
    if (!settings.contains(key)) {
      settings(key.trim) = value.trim
    }
    this
  }


  /** Remove a parameter from the configuration */
  def remove(key: String): Conf = {
    settings.remove(key)
    this
  }

  /** Get a parameter; throws a NoSuchElementException if it's not set */
  def get(key: String): String = {
    settings.getOrElse(key, throw new NoSuchElementException(key))
  }

  /** Get a parameter, falling back to a default if not set */
  def get(key: String, defaultValue: String): String = {
    settings.getOrElse(key, defaultValue)
  }

  /** Get a parameter as an Option */
  def getOption(key: String): Option[String] = {
    settings.get(key)
  }

  /** Get all parameters as a list of pairs */
  def getAll: Array[(String, String)] = settings.clone().toArray

  /** Get a parameter as an integer, falling back to a default if not set */
  def getInt(key: String, defaultValue: Int): Int = {
    getOption(key).map(_.toInt).getOrElse(defaultValue)
  }

  /** Get a parameter as a long, falling back to a default if not set */
  def getLong(key: String, defaultValue: Long): Long = {
    getOption(key).map(_.toLong).getOrElse(defaultValue)
  }

  /** Get a parameter as a double, falling back to a default if not set */
  def getDouble(key: String, defaultValue: Double): Double = {
    getOption(key).map(_.toDouble).getOrElse(defaultValue)
  }

  /** Get a parameter as a boolean, falling back to a default if not set */
  def getBoolean(key: String, defaultValue: Boolean): Boolean = {
    getOption(key).map(_.toBoolean).getOrElse(defaultValue)
  }

  /** Get all akka conf variables set on this CrawlerConf */
  def getAkkaConf: Seq[(String, String)] =
  /* This is currently undocumented. If we want to make this public we should consider
   * nesting options under the crawler namespace to avoid conflicts with user akka options.
   * Otherwise users configuring their own akka code via system properties could mess up
   * crawler's akka options.
   *
   *   E.g. crawler.akka.option.x.y.x = "value"
   */
    getAll.filter { case (k, _) => isAkkaConf(k) }


  /** Does the configuration contain a given parameter? */
  def contains(key: String): Boolean = settings.contains(key)

  /** Copy this object */
  override def clone: Conf = {
    new Conf(false).setAll(settings)
  }

  /**
    * By using this instead of System.getenv(), environment variables can be mocked
    * in unit tests.
    */
  def getenv(name: String): String = System.getenv(name)


  /**
    * Return a string listing all keys and values, one per line. This is useful to print the
    * configuration out for debugging.
    */
  def toDebugString: String = {
    settings.toArray.sorted.map { case (k, v) => k + "=" + v }.mkString("\n")
  }
}

object Conf {

  def isAkkaConf(name: String): Boolean = name.startsWith("akka.")


}
