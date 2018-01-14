package org.linalg.studies.linearalgebralibrary.util


import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._

import scala.reflect.runtime.universe._
/**
  *
  */
object Implicits {

     implicit def double2Rational(double: Double): Rational = Rational(double)
     implicit def double2Real(double: Double): Real = Real(double)


     implicit def VectorSet2Matrix[N <: Number[N]: TypeTag](vset: VectorSet[N]): Matrix[N] =
          new Matrix(vset.getColumns():_*)

     /*implicit def VectorSet2SquareMatrix[N <: Number[N]: TypeTag](vset: VectorSet[N]): SquareMatrix[N] =
          new SquareMatrix(vset.getColumns():_*)*/

     //implicit def tovecset[N <: Number[N]](mat: Matrix[N]): VectorSet[N] = new VectorSet[N](mat.getColumns():_*)

     //note making vecset to vector for spanning methods
     /*implicit def Vector2VecSet[N <: Number[N]: TypeTag](vec: Vector[N]): VectorSet[N]
          = new VectorSet[N](vec:_*)*/


     //note: fixing nullspace[nothing] into nullspace[n]
     /*implicit def MatNothing2MatN[N <: Number[N]: TypeTag](mat: Matrix[Any]): Matrix[N] ={
          Matrix.fromLists[N](mat.getColumns().map(vec => vec.toList.map(e => e.asInstanceOf[N])):_*)
     }*/
}
