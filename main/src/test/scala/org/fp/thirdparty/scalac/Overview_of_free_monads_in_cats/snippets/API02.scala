//package org.fp.thirdparty.scalac.Overview_of_free_monads_in_cats.snippets
//
//import scala.language.implicitConversions
//
///**
//  *
//  */
//trait API02 extends API01 {
//
//  import cats.free.Free
//  import cats.Id
//
//  import Logo._
//
//  //object dsl {
//    implicit def liftPosition[P](i: Instruction[P]): Free[Instruction, P] = Free.liftF(i)
//    implicit def runInstruction(i: Free[Instruction, Position]): Id[Position] = i.run
//
//    def forward(pos: Position, l: Int): Free[Instruction, Position] = /*Free.liftF(*/Forward(pos, l)/*)*/
//    def backward(pos: Position, l: Int): Free[Instruction, Position] = /*Free.liftF(*/Backward(pos, l)/*)*/
//    def left_(pos: Position, degree: Degree): Free[Instruction, Position] = /*Free.liftF(*/RotateLeft(pos, degree)/*)*/
//    def right_(pos: Position, degree: Degree): Free[Instruction, Position] = /*Free.liftF(*/RotateRight(pos, degree)/*)*/
//    def showPosition(pos: Position): Free[Instruction, Unit] = /*Free.liftF(*/ShowPosition(pos)/*)*/
//  //}
//}
//
//// 8<--
//object API02 extends API02
//
//trait Computations {
//
//}
//
//// 8<--