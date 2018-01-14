package org.linalg.studies.linearalgebralibrary.vector


import org.linalg.studies.linearalgebralibrary.util._
import org.linalg.studies.linearalgebralibrary.util.Implicits._
import org.linalg.studies.linearalgebralibrary.theory._
import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._

import matrixLib.exception.DimensionMismatchException
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

/**
  *
  */

//(Util.seqToVecSet(elems:_*).getColumns():_*)
class Vector[N <: Number[N]: TypeTag](elems: N*) //extends VectorSet[N](elems:_*)
     extends VectorSpace[Vector[N], N]
          with AbelianGroup[Vector[N]] //with Monoid[Vector[N]] with Ring[Vector[N]]
          with HilbertSpace[Vector[N], N] with BanachSpace[Vector[N], N]
          with Span[Vector[N], N]
          with LinearIndependence[Vector[N], N]
          with Basis[Vector[N], N] with Dimension[Vector[N], N]
          with OrthogonalTo[Vector[N], N] with Normal[Vector[N], N] {

     private val elements: ListBuffer[N] = ListBuffer(elems:_*)


     def this(length:Int) = this(Vector.ZERO[N](length).toList:_*)
     def this(v: Vector[N]) = this(Vector[N](v).toList:_*) //copy constructor


     def dimension(): Int = this.toList.length // num elements
     def isBasisOfSpaceWith(dim: Int): Boolean = new VectorSet(this).spansSpaceWith(dim) //&& linearlyIndependent()
     def basis(): Option[Vector[N]] = isBasisOfSpaceWith(this.dimension()) match {
          case false => None
          case true => Some(this)
     }
     def linearlyIndependent(): Boolean = true //since it's just one vector
     def spansSpaceWith(dim: Int): Boolean = dim == this.dimension()
     def isSpanned(v: Vector[N]): Boolean = new AugmentedMatrix[N](VectorSet(v), this).solve().isDefined
     def getVectorInSpace(coefs: Vector[N]): Vector[N] = this.scale(coefs.get(0)) // + 0 <0,0,0> + 0 <0,0,0> ...
     // todo check above
     def getSpanningCoefficients(v: Vector[N]): Vector[N] ={ //if none, gives zero vset in vset's size
          if(isSpanned(v))
               return new AugmentedMatrix[N](VectorSet(v), this).solve().get.getCol(0) //note it's the only one
          Vector.ZERO[N](v.dimension())
     }

     //operations
     def ZERO(): Vector[N] = Vector(elems.length)
     //todo fix so we have auxiliary constructors instead of applies
     def +(that: Vector[N]): Vector[N] = new Vector(this.toList.zip(that.toList).map(pair => pair._1 + pair._2):_*)
     def -(that: Vector[N]): Vector[N] = new Vector(this.toList.zip(that.toList).map(pair => pair._1 - pair._2):_*)
     def *(that: Vector[N]): Vector[N] = new Vector(this.toList.zip(that.toList).map(pair => pair._1 * pair._2):_*)
     def scale(factor: N): Vector[N] = new Vector(this.toList.map(e => e * factor):_*)
     def scale(factor: Double): Vector[N] = new Vector(this.toList.map(e => e * factor):_*)
     def /(that: Vector[N]): Vector[N] = new Vector(this.toList.zip(that.toList).map(pair => pair._1 / pair._2):_*)
     def opposite(): Vector[N] = new Vector(this.toList.map(e => e.opposite()):_*)
     // todo how to use sum and provide implicit type for it?
     def dotProduct(that: Vector[N]): N = (this * that).elementSum
     def outerProduct(that: Vector[N]): VectorSet[N] = new VectorSet[N](that.toList.map(s => this.scale(s)):_*)
     def crossProduct(that: Vector[N]): Vector[N] ={ //todo three dimensional ONLY
          if(this.dimension() != 3 || that.dimension() != 3) throw new DimensionMismatchException()

          //todo check making this return runtime number type so not necessarily complex
          val coords: ListBuffer[N] = ListBuffer.fill[N](3)(Number.ZERO[N])
          // todo check if need () around multiplications
          coords(0) = (this.get(1) * that.get(2)) - (this.get(2) * that.get(1))
          coords(1) = (this.get(2) * that.get(0)) - (this.get(0) * that.get(2))
          coords(2) = (this.get(0) * that.get(1)) - (this.get(1) * that.get(0))

          new Vector(coords:_*)
     }
     def isZero(): Boolean = this.toList.forall(e => e.toDouble == 0)

     def isMultipleOf(that: Vector[N]): Boolean = {
          def zeroInSamePos(v1: Vector[N], v2: Vector[N]): Boolean ={
               v1.toList.zip(v2.toList).exists(p => p._1.isZero && p._2.isZero)
          }

          val divs = toList.zip(that.toList).map(p => p._1.getMultiple(p._2))

          zeroInSamePos(this, that) || divs.forall(_.isDefined) match {
               case false => false
               case true => {
                    val defined = divs.filter(_.isDefined).map(_.get)
                    defined.tail.count(_ == defined.head) == (defined.length - 1)
               }
          }
     }

     def isOrthogonalTo(that: Vector[N]): Boolean = (this * that) == Vector.ZERO[N](this.dimension())
     def norm(): N = new Vector(this.toList.map(e => e ^ 2):_*).elementSum.sqrt()
     def isNormalized(): Boolean = normalize() == this
     def normalize(): Vector[N] = {
          val norm: N = this.norm()
          new Vector(this.toList.map(e => e / norm):_*)
     }

     // todo check if this will work for complex numbers.
     def angle(that: Vector[N]): Angle = new Angle(Math.acos((this.dotProduct(that)/(this.norm() * that.norm())).toDouble))
     def distance(that: Vector[N]): N = (this - that).norm()
     //project this onto `onto`
     def projection(onto: Vector[N]): Vector[N] = onto.scale(this.dotProduct(onto) / onto.norm())
     //my made up method, not really for vector operation
     //def transpose(): VectorSet[N] = VectorSet(this).transpose()

     //utilities
     def copy(): Vector[N] = new Vector(this.toList:_*)
     def attach(that: Vector[N]): Vector[N] = new Vector[N]((this.toList ++ that.toList):_*)
     def attach(that: N): Vector[N] = new Vector((this.toList :+ that):_*)
     def combine(that: Vector[N]): Matrix[N] = new Matrix[N](this, that)
     def get(i: Int): N = elements(i)
     def set(i: Int, value: N): Unit = elements(i) = value
     def toList: List[N] = elements.toList //non-mutable
     def toBuffer: ListBuffer[N] = elements  //mutable
     def elementSum: N = elements.reduceLeft[N]((acc, y) => acc + y)
     override def toString: String = new VectorSet(this).toString
}

object Vector {
     def ZERO[N <: Number[N]: TypeTag](len: Int): Vector[N] = new Vector(List.fill[N](len)(Number.ZERO[N]):_*)
     //todo check making this return runtime number type so not necessarily complex
     def apply[N <: Number[N]: TypeTag](elems: Double*): Vector[N] ={

          Number.runtimeType[N] match {
               case "Real" => new Vector(elems.map(Real(_).asInstanceOf[N]):_*)
               case "Rational" => new Vector(elems.map(Rational(_).asInstanceOf[N]):_*)
               case "Complex" => new Vector(elems.map(Complex(_).asInstanceOf[N]):_*)
          }
     }

     def apply[N <: Number[N]: TypeTag](v: Vector[N]): Vector[N] = v //copy constructor
     def apply[N <: Number[N]: TypeTag](length: Int): Vector[N] = new Vector(length)
}
