package com.juejing.conf

import com.typesafe.config.ConfigFactory

import scala.util.Try


trait Configuration {
  /**
    * Application config object.
    */
  val config = ConfigFactory.load()

  /**
    * log4j
    */

  lazy val logShow = Try(config.getBoolean("log.show")).getOrElse(true)
}
