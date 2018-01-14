// 8<--
package org.fp.thirdparty.flatten_your_code.snippets

import scalaz.\/

trait API03 {
  // 8<--

  def getUserName(data: Map[String, String]): String \/ String = ???
  def getUser(name: String): String \/ User = ???
  def getEmail(user: User): String = ???
  def validateEmail(email: String): String \/ String = ???
  def sendEmail(email: String): String \/ Boolean = ???

  val data = Map[String, String]()
  // 8<--
}
// 8<--


