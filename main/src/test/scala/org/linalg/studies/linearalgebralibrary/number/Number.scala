package org.linalg.studies.linearalgebralibrary.number

import org.linalg.studies.linearalgebralibrary.util._
import org.linalg.studies.linearalgebralibrary.theory._

import org.apache.commons.lang3.math.Fraction
import scala.reflect.runtime.universe._
import scala.reflect.runtime.{universe=>ru}


trait Number[N <: Number[N]] extends Ring[N] with Field[N] {
     //methods inherited: add,subtract,opposite,divide,inverse,identity

     def ZERO(): N
     def inverse(): N
     def opposite(): N
     def +(that: N): N
     def -(that: N): N
     def *(that: N): N
     def *(factor: Double): N
     def /(that: N): N
     def ^(exp: Double): N
     def sqrt(): N
     def >(that: N): Boolean
     def <(that: N): Boolean
     def >=(that: N): Boolean
     def <=(that: N): Boolean
     def !=(that: N): Boolean = !(this == that)
     def ==(that: N): Boolean
     def >(that: Double): Boolean = this.toDouble > that
     def <(that: Double): Boolean = this.toDouble < that
     def >=(that: Double): Boolean = this.toDouble >= that
     def <=(that: Double): Boolean = this.toDouble <= that
     def !=(that: Double): Boolean = this.toDouble != that
     def ==(that: Double): Boolean = this.toDouble == that
     def isZero: Boolean
     def isNegative: Boolean
     def getMultiple(that: N): Option[N]
     def abs(): N
     def toDouble: Double
     def toString: String
}


object Number {

     def ZERO[N <: Number[N]: TypeTag]: N ={
          typeOf[N].typeSymbol.name.decodedName.toString match {
               case "Real" => Real.ZERO.asInstanceOf[N]
               case "Rational" => Rational.ZERO.asInstanceOf[N]
               case "Complex" => Complex.ZERO.asInstanceOf[N]
          }
     }

     def ONE[N <: Number[N]: TypeTag]: N ={
          Number.runtimeType[N] match {
               case "Real" => Real.ONE.asInstanceOf[N]
               case "Rational" => Rational.ONE.asInstanceOf[N]
               case "Complex" => Complex.ONE.asInstanceOf[N]
          }
     }
     def internalType[N <: Number[N]: TypeTag](o: Any): String ={
          val currentMirror = ru.runtimeMirror(getClass.getClassLoader)
          currentMirror.reflect(o).symbol.toString.replace('$', ' ').split(' ').last
     }
     def runtimeType[N <: Number[N]: TypeTag]: String ={
          typeOf[N].typeSymbol.name.decodedName.toString
     }

     //note used to convert real and ration to generic number N at runtime
     def toNumber[TO <: Number[TO]: TypeTag](real: Double): TO ={
          Number.runtimeType[TO] match {
               case "Real" => Real(real).asInstanceOf[TO]
               case "Rational" => Rational(real).asInstanceOf[TO]
               case "Complex" => Number.toNumber[TO](real, 0)
               case _ => throw new Exception("got other type than real/rational/complex for single-arg toNumber()")
          }
     }

     //note //used to convert complex to generic number N at runtime
     def toNumber[TO <: Number[TO]: TypeTag](real: Double, imag: Double): TO ={
          Number.runtimeType[TO] match {
               case "Complex" => Complex(real, imag).asInstanceOf[TO]
               case _ => throw new Exception("got other type than complex for single-arg toNumber()")
          }
     }


     //F = from type
     // T = to type
     /*def toNumber2[F <: Number[F]: TypeTag, T <: Number[T]: TypeTag](toConvert: Number[F]): T ={
          val inside = Number.internalType(toConvert)  //type of F (from)
          val outside = Number.runtimeType[T]
          outside match {
               case "Real" => Real(toConvert.toDouble).asInstanceOf[T]
               case "Rational" => Rational(toConvert.toDouble).asInstanceOf[T]
               case "Complex" => inside match {
                         case "Complex" => toConvert.asInstanceOf[T]
                         case _ => Complex(toConvert.toDouble).asInstanceOf[T]
                    }
               case _ => throw new Exception("Type is NOT EXPECTED: one of real,rational,complex..")
          }
     }*/
}




//------------------------------------------------------------------------------------------------------

class Real(num: Double) extends Number[Real] {

     def ZERO(): Real = Real.ZERO
     def inverse(): Real = Real(1 / num)
     def opposite(): Real = Real(-num)
     def +(that: Real): Real = Real(this.toDouble + that.toDouble)
     def -(that: Real): Real = Real(this.toDouble - that.toDouble)
     def *(that: Real): Real = Real(this.toDouble * that.toDouble)
     def *(factor: Double): Real = Real(this.toDouble * factor)
     def /(that: Real): Real = Real(this.toDouble / that.toDouble)
     def ^(exp: Double): Real = Real(Math.pow(this.toDouble, exp))
     def sqrt(): Real = this ^ 0.5
     def >(that: Real): Boolean = this.toDouble > that.toDouble
     def <(that: Real): Boolean = this.toDouble < that.toDouble
     def >=(that: Real): Boolean = this.toDouble >= that.toDouble
     def <=(that: Real): Boolean = this.toDouble <= that.toDouble
     def ==(that: Real): Boolean = this.toDouble == that.toDouble
     /*def ==(that: Double): Boolean = this.toDouble == that
     def !=(that: Double): Boolean = this.toDouble != that*/
     def isZero: Boolean = this.toDouble == 0
     def isNegative: Boolean = this.toDouble < 0
     def getMultiple(that: Real): Option[Real] = that.isZero match {
          case true => None
          case false => Some(this / that)
     }
     def abs(): Real = Real(Math.abs(this.toDouble))

     def toDouble: Double = num
     def toRational: Rational = Rational(num)
     def toComplex: Complex = Complex(num)
     def toFrac: Fraction = Fraction.getFraction(num )

     override def toString: String = {
          toDouble.round - toDouble == 0 match {
               case true => toDouble.round.toInt.toString
                    //round to 4 decimal places anyway
               case false => Util.GenOps.roundTo(toDouble, 4).toString
          }
     }
}

object Real {
     val ZERO = Real(0)
     val ONE = Real(1)

     def apply(): Real = new Real(0)
     def apply(double: Double): Real = new Real(double)
     def apply[N <: Number[N]](number: Number[N]): Real = new Real(number.toDouble)
}



//------------------------------------------------------------------------------------------------------

class Rational(num:Int, denom:Int) extends Number[Rational] {

     private val numDouble = num * 1.0
     private val denomDouble = denom * 1.0
     private val frac: Fraction = Fraction.getFraction(num, denom).reduce()
     var numerator = frac.getNumerator
     var denominator = frac.getDenominator
     val double = numDouble / denomDouble

     def ZERO(): Rational = Rational.ZERO
     def inverse(): Rational = Rational(denom, num)
     def opposite(): Rational = Rational(-num, denom)
     def +(that: Rational): Rational = Rational(this.double + that.double)
     def -(that: Rational): Rational = Rational(this.double - that.double)
     def *(that: Rational): Rational = Rational(this.double * that.double)
     def *(factor: Double): Rational = Rational(this.double * factor)
     def /(that: Rational): Rational = Rational(this.double / that.double)
     def ^(exp: Double): Rational = Rational(Math.pow(this.double, exp))
     def sqrt(): Rational = this ^ 0.5
     def >(that: Rational): Boolean = this.double > that.double
     def <(that: Rational): Boolean = this.double < that.double
     def >=(that: Rational): Boolean = this.double >= that.double
     def <=(that: Rational): Boolean = this.double <= that.double
     def ==(that: Rational): Boolean = this.double == that.double
     /*def ==(that: Double): Boolean = this == Rational(that)
     def !=(that: Double): Boolean = this != Rational(that)*/
     def isZero: Boolean = this.numDouble == 0
     def isNegative: Boolean = this.numDouble < 0 && this.denomDouble >= 0 ||
          this.numDouble >= 0 && this.denomDouble < 0
     def getMultiple(that: Rational): Option[Rational] = that.isZero match {
          case true => None
          case false => Some(this / that)
     }
     def abs(): Rational = Rational(Math.abs(numDouble).toInt, Math.abs(denomDouble).toInt)
     def toDouble: Double = numDouble / denomDouble
     //def toFrac: Fraction = frac
     //def toNumber: Number[Rational] = this.asInstanceOf[Number[Rational]]
     def toReal: Real = Real(double)
     //def toRational: Rational = this
     def toComplex: Complex = Complex(double)

     override def toString: String = denomDouble match {
          case 1 => numerator.toString
          case _ => numerator + "/" + denominator
     }
}

object Rational {
     val ZERO = Rational(0)
     val ONE = Rational(1)

     def apply(): Rational = new Rational(0, 1)
     def apply(num:Int, denom:Int): Rational = new Rational(num, denom)
     def apply[N <: Number[N]](number: Number[N]): Rational = Rational(number.toDouble)
     //def apply(num: Double, denom: Double): Rational = Rational(num) / Rational(denom)
     def apply(double: Double): Rational = {
          val f: Fraction = Fraction.getFraction(double).reduce()
          new Rational(f.getNumerator, f.getDenominator)
     }
}


//------------------------------------------------------------------------------------------------------

class Complex(val real:Real, val imaginary:Real) extends Number[Complex] {

     private val modulus: Double = Math.sqrt(Math.pow(real.toDouble, 2) + Math.pow(imaginary.toDouble, 2))

     def ZERO(): Complex = Complex.ZERO
     def inverse(): Complex = Complex(real/(real*real + imaginary*imaginary),
          imaginary.opposite()/(real*real + imaginary*imaginary))
     def opposite(): Complex = new Complex(this.real.opposite(), this.imaginary.opposite())
     def conjugate(): Complex = Complex(real, imaginary.opposite())
     def +(that: Complex): Complex = Complex(real + that.real, imaginary + that.imaginary)
     def -(that: Complex): Complex = Complex(real - that.real, imaginary - that.imaginary)
     def *(that: Complex): Complex = Complex(real*that.real - imaginary*that.imaginary,
          real*that.imaginary + imaginary*that.real)
     def *(factor: Double): Complex = Complex(real * factor, imaginary * factor)
     def /(that: Complex): Complex ={
          // this is more numerically stable than the concise formulation
          if (that.isZero) throw new ArithmeticException

          val quot: Complex = this * that.conjugate()
          val recMod: Real = (that.real ^ 2) + (that.imaginary ^ 2)
          new Complex(quot.real/recMod, quot.imaginary/recMod)
     }
     def square(): Complex = Complex((real^2) - (imaginary^2), real * imaginary * 2)
     def ^(exp: Double): Complex = Complex() // TODO and say if that == 1/2 then use sqrt() method
     def sqrt(): Complex = Complex() // TODO https://www.google.ro/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=sqrt%20of%20complex%20number
     def >(that: Complex): Boolean = this.modulus > that.modulus
     def <(that: Complex): Boolean = this.modulus < that.modulus
     def >=(that: Complex): Boolean = this.modulus >= that.modulus
     def <=(that: Complex): Boolean = this.modulus <= that.modulus
     def ==(that: Complex): Boolean = this.real == that.real && this.imaginary == that.imaginary
     /*def ==(that: Double): Boolean = this == Complex(that)
     def !=(that: Double): Boolean = this != Complex(that)*/
     def isZero: Boolean = this.real.isZero && this.imaginary.isZero
     def isNegative: Boolean = this.real.isNegative && (this.imaginary.isZero || this.imaginary.isNegative)
     def getMultiple(that: Complex): Option[Complex] = that.isZero match {
          case true => None
          case false => Some(this / that)
     }
     def abs(): Complex = Complex(this.real.abs(), this.imaginary.abs()) //todo is valid operation?
     def magnitude(): Double = modulus
     def theta(): Angle = new Angle(Math.atan(imaginary.toDouble / real.toDouble))

     def toDouble: Double = if(imaginary.isZero && real.isNegative) -modulus else modulus //todo is this correct?
     //def toNumber: Number[Complex] = this.asInstanceOf[Number[Complex]]
     def toReal: Real = Real(modulus)
     def toRational: Rational = Rational(modulus)
     //def toComplex: Complex = this
     def toFrac: Fraction = Fraction.getFraction(modulus)
     //def imaginaryIsZero: Boolean = imaginary == Rational.ZERO


     def displayWithReals(): String ={
          if(real.isZero && imaginary.isZero) return "0"
          if(imaginary.isZero) return real.toString

          var imagStr: String = ""
          var realStr: String = ""

          if(real.isZero){
               //dealing with imag now
               imagStr = if(imaginary == -1) "-i"
               else if(imaginary == 1) "i"
               else if(imaginary.isNegative) imaginary + "i"
               else imaginary + "i"
          } else {
               realStr = real.toString
               imagStr = if(imaginary == -1) " - i"
               else if(imaginary == 1) " + i"
               else if(imaginary.isNegative) " - " + imaginary.abs() + "i"
               else " + " + imaginary + "i"
          }

          realStr + imagStr
     }

     override def toString: String = {

          val realTemp: Rational = Rational(real)
          val imagTemp: Rational = Rational(imaginary)

          if(realTemp.isZero && imagTemp.isZero) return "0"
          if(imagTemp.isZero) return realTemp.toString

          var imagStr: String = ""
          var realStr: String = ""

          if(realTemp.isZero){
               //dealing with imag now
               imagStr = if(imagTemp == -1) "-i"
               else if(imagTemp == 1) "i"
               else if(imagTemp.isNegative) imagTemp + "i"
               else imagTemp + "i"
          } else {
               realStr = realTemp.toString
               imagStr = if(imagTemp == -1) " - i"
               else if(imagTemp == 1) " + i"
               else if(imagTemp.isNegative) " - " + imagTemp.abs() + "i"
               else " + " + imagTemp + "i"
          }

          realStr + imagStr
     }
}

object Complex {
     val ZERO = Complex() // for use inside class
     val ONE = Complex(1, 0)

     def apply(): Complex = new Complex(Real.ZERO, Real.ZERO)
     def apply(num: Double): Complex = new Complex(Real(num), Real.ZERO)
     def apply(a: Double, b: Double): Complex = new Complex(Real(a), Real(b))
     def apply(a: Real, b: Real): Complex  = Complex(a.toDouble, b.toDouble)
     def apply(a: Real, b: Double) = new Complex(a, Real(b))
     def apply(a: Double, b: Real) = new Complex(Real(a), b)
     def apply(a: Rational, b: Rational): Complex  = new Complex(a.toReal, b.toReal)
     def apply(a: Complex, b: Complex): Complex  = new Complex(a.real + b.real, a.imaginary + b.imaginary)
     def apply(a: Complex, b: Rational): Complex  = new Complex(a.real + b.toReal, a.imaginary)
     def apply(a: Rational, b: Complex): Complex  = new Complex(a.toReal + b.real, b.imaginary)
     def apply(a: Complex, b: Real): Complex  = new Complex(a.real + b, a.imaginary)
     def apply(a: Real, b: Complex): Complex  = new Complex(a + b.real, b.imaginary)
     def apply(a: Real, b: Rational): Complex  = new Complex(a, b.toReal)
     def apply(a: Rational, b: Real): Complex  = new Complex(a.toReal, b)
}

