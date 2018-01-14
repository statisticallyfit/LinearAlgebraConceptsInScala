package org.linalg.studies.linearalgebralibrary


import org.linalg.studies.linearalgebralibrary.util._
import org.linalg.studies.linearalgebralibrary.util.Implicits._
import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._
import org.linalg.studies.linearalgebralibrary.vector._
import org.linalg.studies.linearalgebralibrary.theory._
/**
  *
  */

object Driver {


     def main(args: Array[String]) {


          val mat1: Matrix[Real] = Matrix(Vector(1,1,8,-3,1),
               Vector(2,-4,8,5,-1),
               Vector(9,5,1,-7,0),
               Vector(10,7,2,-4,5)
          )
          println(mat1)
          //println(new QRDecomposition[Real](mat1))
     }
}




// todo:
// 1) testing simple rref for square system
// 2) testing non-square inverse using toRow sedenion (gauss-jordan)
// 3) testing solving system (for a vector)

/*


//error fixed 1/3 on bottom
val mat = new Matrix[Rational](
     Vector(1, 2, 0, 2),
     Vector(3, 6, 0, 6),
     Vector(-2, -5, 5, 0),
     Vector(0, -2, 10, 8),
     Vector(2, 4, 0, 4),
     Vector(0, -3, 15, 18))
val b = Vector[Rational](0, -1, 5, 6)
val aug = new AugmentedMatrix[Rational](mat, b)
print(aug.solve())


-- arbitrary test
val mat = new Matrix[Rational](
               Vector(3, 6, -3),
               Vector(2, 4, -2),
               Vector(-1, -2, 1)
          )
          val b = Vector[Rational](2, 4, -2)
          val aug = new AugmentedMatrix[Rational](mat, b)
          print(aug.solve())


val mat1: Matrix[Complex] = Matrix(Vector(1,3,6), Vector(2, -1, 5), Vector(3, 0, 1), Vector(1, 0, 0), Vector(0,
     1, 0), Vector(0, 0, 1))
val mat2: Matrix[Complex] = Matrix(
     Vector(1, 3, 6, 5),
     Vector(2, -1, 5, -1),
     Vector(3, 0, 1, 0),
     Vector(1, 0, 0, 0),
     Vector(0, 1, 0, 0),
     Vector(0, 0, 1, 0))
val mat3: Matrix[Complex] = Matrix(Vector(1, 3, 6), Vector(2, -1, 5), Vector(3,0,1), Vector(1,4,8))
val mat4: Matrix[Rational] = Matrix(Vector(1,3,6), Vector(2, -1, 5), Vector(3, 0, 1))

println("test 1: simple rref for square: " + mat1.reducedRowEchelon())
println("\ntest 2: gauss jordan for non-square: " + mat2.reducedRowEchelon())
println("\ntest 3: solving for system: " + mat3.reducedRowEchelon())*/

/*val v1: Vector[Complex] = new Vector[Complex](Complex(1,2), Complex(2,0))
val v2: Vector[Rational] = new Vector(Rational(1,2), Rational(2,4))
zeroer(v1.get(0))
zeroer(v2.get(0))
def zeroer[N <: Number[N]: TypeTag](arg: N) = {
     println(Number.ZERO[N] + ", type: " + Number.ZERO[N].getClass.getSimpleName + "; insideType: " + arg.insideType)
}*/




//todo 1) testing kernel
/*val mat5A: VectorSet[Rational] = VectorSet(Vector[Rational](1,2),
Vector[Rational](-4,5), Vector[Rational](-9, -7))
val z5: Vector[Rational] = Vector.ZERO[Rational](2)
val aug = new AugmentedMatrix[Rational](mat5A, z5)
Console.println(aug.solve())
Console.println(new NullSpace(mat5A).kernel())

val mat5B: Matrix[Rational] = new Matrix(Vector[Rational](1,2,-14), Vector[Rational](3, 5,-37))
Console.println(new NullSpace(mat5B).kernel())

val mat5C: Matrix[Rational] = new Matrix(Vector[Rational](1,2,1),
Vector[Rational](3, 6, 3), Vector[Rational](-9,7,-8), Vector[Rational](5,1,1))
Console.println(new NullSpace(mat5C).kernel())


val mat3A: Matrix[Rational] = new Matrix(Vector[Rational](1,4,7),
Vector[Rational](-2, -5, -8), Vector[Rational](-3,-6,-9))
Console.println("mat3A: " + new NullSpace(mat3A).kernel())

val mat3B: Matrix[Rational] = new Matrix(Vector[Rational](2,4,8),
Vector[Rational](-2,-4,-8), Vector[Rational](-2,-4,-8))
Console.println("mat3B: " + new NullSpace(mat3B).kernel())

val mat3C: Matrix[Rational] = new Matrix(Vector[Rational](2, 5, 9),
Vector[Rational](9, 6, 8), Vector[Rational](-3, -1, -9))
Console.println("mat3C: " + new NullSpace(mat3C).kernel())

val mat3D: Matrix[Rational] = new Matrix(Vector[Rational](-3, 2, 4),
Vector[Rational](1, 5, 8), Vector[Rational](-1, -7, -4))
Console.println("mat3D: " + new NullSpace(mat3D).kernel())*/



//todo 2) testing solve augmented matrix
/*val mat1A: Matrix[Rational] = Matrix(Vector[Rational](1,2,-1), Vector[Rational](3,-1,2),
     Vector[Rational](2,-1,1))
val v1A: Vector[Rational] = Vector[Rational](5,1,3)
val aug1A = new AugmentedMatrix[Rational](mat1A, v1A)
Console.println("1A: " + aug1A.solve())

val mat1B: Matrix[Rational] = Matrix(Vector[Rational](-1,3,4), Vector[Rational](1,-2,-1),
     Vector[Rational](1,5,-2))
val v1B: Vector[Rational] = Vector[Rational](0,0,0)
val aug1B = new AugmentedMatrix[Rational](mat1B, v1B)
Console.println("1B: " + aug1B.solve())

val mat1C: Matrix[Rational] = Matrix(Vector[Rational](-1,2,6), Vector[Rational](1,2,6),
     Vector[Rational](1,3,9))
val v1C: Vector[Rational] = Vector[Rational](2,5,7)
val aug1C = new AugmentedMatrix[Rational](mat1C, v1C)
Console.println("1C: " + aug1C.solve())

val mat1D: Matrix[Rational] = Matrix(Vector[Rational](1,1,3), Vector[Rational](1,2,3),
     Vector[Rational](-1,1,-3))
val v1D: Vector[Rational] = Vector[Rational](2,4,6)
val aug1D = new AugmentedMatrix[Rational](mat1D, v1D)
Console.println("1D: " + aug1D.solve())

val mat1E: Matrix[Rational] = Matrix(Vector[Rational](3,6,1,2), Vector[Rational](-3,-7,-1,-2),
     Vector[Rational](-1,1,-2,6), Vector[Rational](2, 1, -1, 8))
val v1E: Vector[Rational] = Vector[Rational](0,0,0, 0)
val aug1E = new AugmentedMatrix[Rational](mat1E, v1E)
Console.println("1E: " + aug1E.solve())

val mat1F: Matrix[Rational] = Matrix(Vector[Rational](2,2,8,1), Vector[Rational](3,3,12,2),
     Vector[Rational](5,2,20,4), Vector[Rational](2,2,8,5))
val v1F: Vector[Rational] = Vector[Rational](6,7,24,6)
val aug1F = new AugmentedMatrix[Rational](mat1F, v1F)
Console.println("1F: " + aug1F.solve())

val mat1G: Matrix[Rational] = Matrix(Vector[Rational](0,5,10), Vector[Rational](-10,6,7),
     Vector[Rational](0,-8,3), Vector[Rational](38,4,8))
val v1G: Vector[Rational] = Vector[Rational](6,3,9)
val aug1G = new AugmentedMatrix[Rational](mat1G, v1G)
Console.println("1G: " + aug1G.solve())

val mat1H: Matrix[Rational] = Matrix(Vector[Rational](0,3,15,12), Vector[Rational](-1,-4,-20,-16),
     Vector[Rational](0,1,2,7), Vector[Rational](0,6,30,24), Vector[Rational](5,7,3,60))
val v1H: Vector[Rational] = Vector[Rational](3,5,-1,10)
val aug1H = new AugmentedMatrix[Rational](mat1H, v1H)
Console.println("1H: " + aug1H.solve())

val mat1I: Matrix[Rational] = Matrix(Vector[Rational](0,3,15,12), Vector[Rational](-1,-4,-20,-16),
     Vector[Rational](0,1,2,7), Vector[Rational](0,6,30,24), Vector[Rational](5,7,3,60))
val v1I: Vector[Rational] = Vector[Rational](3,5,-1,46)
val aug1I = new AugmentedMatrix[Rational](mat1I, v1I)
Console.println(aug1I)
Console.println("1I: " + aug1I.solve())

val mat1J: Matrix[Rational] = Matrix(Vector[Rational](2,1,1,2), Vector[Rational](-1,7,-2,-6),
     Vector[Rational](3,6,6,0), Vector[Rational](5,-11,4,14), Vector[Rational](5,7,1,2))
val v1J: Vector[Rational] = new Vector(Rational(1),Rational(-8), Rational(5),Rational(20,3))
val aug1J = new AugmentedMatrix[Rational](mat1J, v1J)
Console.println("1J: " + aug1J.solve())


val mat: Matrix[Rational] = Matrix(Vector[Rational](1,2,0,2), Vector[Rational](3,6,0,6),
     Vector[Rational](-2,-5,5,0), Vector[Rational](0,-2,10,8), Vector[Rational](2,4,0,4),
     Vector[Rational](0,-3,15,18))
Console.println(new NullSpace[Rational](mat).kernel())*/

//todo rowspace/colspace tester
/*val mat1E: Matrix[Rational] = Matrix(
               Vector[Rational](1,2,2,-1),
               Vector[Rational](-3,-6,-6,3),
               Vector[Rational](4, 9,9,-4),
               Vector[Rational](-2,-1,-1,2),
               Vector[Rational](5,8,9,-5),
               Vector[Rational](4,2,7,-4))*/




//todo 3) testing spanned methods now
/*
val vset: VectorSet[Rational] = new VectorSet(Vector[Rational](1,2,3), Vector[Rational](1,5,4),
Vector[Rational](0,0,9))
val uset: VectorSet[Rational] = new VectorSet(Vector[Rational](1,4,7), Vector[Rational](2,5,8),
Vector[Rational](3,6,9))
val v = Vector[Rational](1,3,4)
Console.println(vset)
Console.println(vset.reducedRowEchelon())
Console.println(vset.linearlyIndependent())
Console.println(vset.spansSpaceWith(vset.dimension()))
Console.println(vset.isSpanned(v))
Console.println(vset.getSpanningCoefficients(v))

Console.println("\n\n" + uset)
Console.println(uset.reducedRowEchelon())
Console.println(uset.linearlyIndependent())
Console.println(uset.spansSpaceWith(uset.dimension()))
Console.println(uset.isSpanned(v))
Console.println(uset.getSpanningCoefficients(v))


val wset: VectorSet[Rational] = new VectorSet(Vector[Rational](9,4,-2), Vector[Rational](-1,1,-2),
Vector[Rational](3,3,1))
val w = Vector[Rational](3,4,5)
Console.println(wset.isSpanned(w))
Console.println(wset.getSpanningCoefficients(w))*/
