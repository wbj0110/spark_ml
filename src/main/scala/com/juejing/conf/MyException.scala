package com.juejing.conf

/**
 * @author soledede
 */
class MyException(message: String, cause: Throwable)
  extends Exception(message, cause) {

  def this(message: String) = this(message, null)
}
