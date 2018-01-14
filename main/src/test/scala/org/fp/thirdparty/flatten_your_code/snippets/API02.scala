// 8<--
package org.fp.thirdparty.flatten_your_code.snippets

trait API02 {
  // 8<--

  def getUserName(data: Map[String, String]): Either[String, String] = ???
  def getUser(name: String): Either[String, User] = ???
  def getEmail(user: User): String = ???
  def validateEmail(email: String): Either[String, String] = ???
  def sendEmail(email: String): Either[String, Boolean] = ???

  val data = Map[String, String]()
  // 8<--
}
// 8<--


