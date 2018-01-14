/*package org.linalg.studies.linearindependence


import org.specs2.execute.{Success, SnippetParams}



import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussianElimination
import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination
import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix


import org.linalg.studies.Ext._


/**
  * PLAN:
  * * linear independence: R3,4vs, R4,4vs,  R4,3vs (vectors not multiples of each other)
  * * linear dependence: R2,3vs, R4,4vs,  R4,3vs (with vectors as multiples as each other)
  */
object LinearIndependence extends org.specs2.mutable.Specification {

     s"Hello, how does this text thing work? ".p

     eg {
          val v1 = new DenseVector(3, 2, -1, 4)
          val v2 = new DenseVector(5, 5, 0, 1)
          val v3 = new DenseVector(9, 2, 3, 0)
          val vectors1 = new ColumnBindMatrix(v1, v2, v3)

          val u1 = new DenseVector(-1,3,2,5)
          val u2 = new DenseVector(2,-6,-4,-10)
          val u3 = new DenseVector(1,0,8,-1)
          val vectors2 = new ColumnBindMatrix(u1, u2, u3)


          val c1 = new DenseVector(234.467, 1.9, -10.1, -108.1238)
          val c2 = new DenseVector(1.0, 34.21, 0.00, 2.12)
          val c3 = new DenseVector(1.000, 1.00, 1.789, 2.433)
          val c4 = new DenseVector(11.234, 14.1, 9, 19.009)
          val vectors3 = new ColumnBindMatrix(c1, c2, c3, c4)

          println(vectors1.show())
          println("\n" + vectors2.show())
          println("\n" + vectors3.show())


          success
          //vectors1.linearlyIndependent shouldEqual true
          /*vectors1 shouldNotEqual vectors2

          println("\n\n1 - indep?: " + vectors1.linearlyIndependent)
          println("2 - indep?: " + vectors2.linearlyIndependent)

          //vectors1.linearlyIndependent shouldEqual true


          vectors1.rows shouldEqual List(
               new DenseVector(3,5,9),
               new DenseVector(2,5,2),
               new DenseVector(-1,0,3),
               new DenseVector(4,1,0)
          )


          // 8<--
          // the 8<-- is so that this part is not shown, so we see the printing
          Success(List(
               new DenseVector(3,5,9),
               new DenseVector(2,5,2),
               new DenseVector(-1,0,3),
               new DenseVector(4,1,0)).toString)
          Success(vectors1.rows.map(vector => vector.toArray.mkString("  ")).mkString("\n"))

          Success(vectors1.show())*/
          // 8<--
     }

}*/

//--------------------------------------------------------------------------------------------------------------
/*

object LinearIndependence extends org.specs2.mutable.Specification with Snippets with ScalaCheck with CheckedSpec with TextDsl {

     implicit def snippetParams[T]: SnippetParams[T] = defaultSnippetParameters[T].copy(evalCode = true).offsetIs(-14)

     override def is = s"Linear Independence".title ^ s2"""


## Definition


<a href='https://i.imgsafe.org/22125bfcca.png' target='_blank'><img src='https://i.imgsafe.org/22125bfcca.png' border='0'/></a>

All the scalars must be zero for the linear combination of scalars and vectors to be linearly independent. In other
words, there is no way to combine the vectors other than with the zero scalars. Intuitively, the vectors v1 .. vn are
 not multiples of each other.


## Example 1: Show that the vectors below are linearly independent.

###### Step 1:
Set up a homogeneous system of equations

###### Step 2:
Transform the coefficient matrix to reduced row echelon form.

###### Step 3:
Decide if the original vectors are linearly independent: If the system solution is the
     (1) trivial solution only X = 0, which causes the scalars to be zero as well. By definition, this results in
     linearly independent vectors.
     (2) trivial solution AND nontrivial ones X = 0 and X ≠ 0 which means some scalars are zero and some not. By
     definition, all scalars are not zero, resulting in linearly dependent vectors.


##### Code
     ${snippet{
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussianElimination
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._


          val v1 = new DenseVector(3, 2, -1, 4)
          val v2 = new DenseVector(5, 5, 0, 1)
          val v3 = new DenseVector(9, 2, 3, 0)
          val vecCols1 = new ColumnBindMatrix(v1, v2, v3)

          val u1 = new DenseVector(-1,3,2,5)
          val u2 = new DenseVector(2,-6,-4,-10)
          val u3 = new DenseVector(1,0,8,-1)
          val vecCols2 = new ColumnBindMatrix(u1, u2, u3)


          println(vecCols1)
          println(vecCols1.rows)
          vecCols1 shouldEqual vecCols1
          vecCols1 shouldNotEqual vecCols2

          vecCols1.linearlyIndependent shouldEqual true

          vecCols1.rows shouldEqual List(
               Vector(3,5,9),
               Vector(2,5,2),
               Vector(-1,0,3),
               Vector(4,1,0)
          )




     }}


##### Output

```
       [,1]       [,2]      [,3]
[1,] 1.000000, -2.000000, 0.000000,
[2,] 0.000000, 0.000000, 1.000000,
[3,] 0.000000, 0.000000, 0.000000,
[4,] 0.000000, 0.000000, 0.000000,
```
     ${snippet{

     }}

All columns have leading entries, meaning there has been no zero row. That means there is only the trivial solution X
 = 0 which causes the scalars to be zero, which means the vectors are linearly independent.


Some columns do not have leading coefficients so that means there are nontrivial solutions, which means the vectors
are linearly dependent. In simpler terms, we can see that every column has a leading "1", one after the other, like a
 stairway. Now imagine the scalars in place of those "1"s. This shows that every scalar equals zero. If we miss a
 position, then we miss a scalar and that scalar is not zero.


  """.stripMargin
}*/
