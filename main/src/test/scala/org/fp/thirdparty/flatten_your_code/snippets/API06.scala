// 8<--
package org.fp.thirdparty.flatten_your_code.snippets

import scala.concurrent.Future

trait API06 {
  // 8<--

  def getUserName(data: Map[String, String]): Option[String] = ???
  def getUser(name: String): Future[Option[User]] = ???
  def getEmail(user: User): String = ???
  def validateEmail(email: String): Option[String] = ???
  def sendEmail(email: String): Future[Option[Boolean]] = ???

  val data = Map[String, String]()
  // 8<--
}
// 8<--


