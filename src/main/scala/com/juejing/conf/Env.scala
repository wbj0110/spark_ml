package com.juejing.conf

import com.juejing.utils.Logging

/**
 * @author soledede
 */
class Env (val conf: Conf) extends Logging {
}


object Env extends Logging {
  @volatile private var env: Env = _


  def set(e: Env) {
    env = e
  }

  def get: Env = {
    env
  }

  def init(conf: Conf){
    new Env(conf)
  }
  }


