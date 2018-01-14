// 8<--
package org.fp.thirdparty.flatten_your_code.snippets

import scalaz.{-\/, \/-, \/}

trait API04 {
  // 8<--

  def getUserName: \/[String, String] =
    if(theRepo.nonEmpty) \/-(theRepo.head._1)
    else -\/("No user found")

  def getUser(name: String): Option[User] = theRepo.get(name)
  def getEmail(user: User): String = user.email
  def validateEmail(email: String): Option[String] =
    if(email.contains("@")) Some(email)
    else None

  def sendEmail(email: String): \/[String, Boolean] =
    if(email.nonEmpty) -\/(email)
    else \/-(false)

  type Repo = Map[String, User]
  var theRepo : Repo = emptyUserRepo
  def workWithRepo(repo: Repo): Unit = theRepo = repo

  lazy val emptyUserRepo = Map[String, User]()
  val user1: (String, UserImpl) = "user1" -> UserImpl("user1", "user1@email.com")
  val user2: (String, UserImpl) = "user2" -> UserImpl("user1", "user2$email.com")

  def getUserName2 = \/-(user2._1)
  lazy val userRepo = Map[String, User] (user1, user2)
  // 8<--
}
// 8<--


  case class UserImpl(name: String, email: String) extends User
