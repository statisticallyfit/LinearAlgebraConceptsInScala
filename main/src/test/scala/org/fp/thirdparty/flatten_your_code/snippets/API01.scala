// 8<--
package org.fp.thirdparty.flatten_your_code.snippets

trait API01 {
  // 8<--

  // These are the service methods from which we're going to build a program.
  // We'll reuse these five methods in all the parts, although they will evolve a bit.

  def getUserName(data: Map[String, String]): Option[String] = ???
  def getUser(name: String): Option[User] = ???
  def getEmail(user: User): String = ???
  def validateEmail(email: String): Option[String] = ???
  def sendEmail(email: String): Option[Boolean] = ???

  val data = Map[String, String]()
  // 8<--
}
// 8<--

trait User {
  def name: String
  def email : String
}
