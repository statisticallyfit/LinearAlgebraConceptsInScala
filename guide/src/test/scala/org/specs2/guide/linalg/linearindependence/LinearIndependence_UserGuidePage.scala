/*
package org.specs2.guide.linalg.linearindependence


import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination
import org.fp.{resources, concepts}
import org.linalg.studies.linearindependence.LinearIndependence
import org.specs2.execute.Success
import scala.collection.immutable.{Vector => _}
import org.specs2.ugbase.UserGuidePage


/**
  * ${concepts.traverseFunctor} ...
  * Examples for a 'custom' ${concepts.traverseFunctor} with ${resources.Scalaz.md} and ${resources.Cats.md} in
  */

object LinearIndependence_UserGuidePage extends UserGuidePage {

     def is = "Linear Independence".title ^ s2"""


## DEFINITION: Linear Independence


<a href='https://i.imgsafe.org/22125bfcca.png' target='_blank'><img src='https://i.imgsafe.org/22125bfcca.png' border='0'/></a>

All the scalars must be zero for the linear combination of scalars and vectors to be linearly independent. In other
words, there is no way to combine the vectors other than with the zero scalars. Intuitively, the vectors v1 .. vn are
not multiples of each other.


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




On the other hand, linear dependent vectors occur if the scalars are not ALL equal to zero:

<a href='https://i.imgsafe.org/b6d36c6ffb.png' target='_blank'><img src='https://i.imgsafe.org/b6d36c6ffb.png' border='0'/></a>


Now, for some examples.


## Example 1: Linear independence and dependence examples for the case of more rows than columns

There are 4 elements in the 3 vectors, so this case is where there are more rows than columns. In this case, we will
see that when there are more rows than columns we can have either linear independence OR dependence.


##### Code: linear independence for a 4x3 system:
     ${snippet{
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(3, 2, -1, 4)
          val v2 = new DenseVector(5, 5, 0, 1)
          val v3 = new DenseVector(9, 2, 3, 0)
          val vectors = new ColumnBindMatrix(v1, v2, v3)

          // matrix 4 x 3 (R4, 3 vectors) - linear independence case
          vectors.linearlyIndependent shouldEqual true


          // 8<--
          Success("The vectors shown as a matrix: " + vectors.show() + "\n\n" +
               "Linearly independent? " + vectors.linearlyIndependent)
          // 8<--

     }.offsetIs(-14)}


In the above code, the vectors v1, v2, v3 are shown to be linearly independent. Here is the gauss-jordan calculation
 that transforms the `vectors` matrix into reduced row echelon form. If each column has a leading "1" then the scalars
 are all zero which means the vectors are linearly independent. But if there is a column that skips having a "1" then
  the vectors are linearly dependent because the scalar in that column is not zero.

     ${snippet{
          // 8<--
          // TODO: make the above snippet import into here so we don't need to recopy vectors and imports.
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(3, 2, -1, 4)
          val v2 = new DenseVector(5, 5, 0, 1)
          val v3 = new DenseVector(9, 2, 3, 0)
          val vectors = new ColumnBindMatrix(v1, v2, v3)
          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.Matrix
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination

          val reducedRowEchelon: Matrix = new GaussJordanElimination(vectors).U()
          reducedRowEchelon.show()
     }.offsetIs(-14)}


All columns have leading entries, meaning there has been no zero row. That means there is only the trivial solution X
= 0 which causes the scalars to be zero, which means the vectors are linearly independent.







##### Code: linear dependence for a 4x3 system:
     ${snippet{
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(-1, 3, 2, 5)
          val v2 = new DenseVector(2, -6, -4, -10)
          val v3 = new DenseVector(1, 0, 8, -1)
          val vectors = new ColumnBindMatrix(v1, v2, v3)

          // matrix 4 x 3 (R4, 3 vectors) - linear dependence case
          vectors.linearlyIndependent shouldEqual false

          // 8<--
          Success("The vectors aligned as columns to form a matrix: " + vectors.show() + "\n\n" +
               "Linearly independent? " + vectors.linearlyIndependent)
          // 8<--

     }.offsetIs(-14)}


Above, we see that the vectors are linearly dependent. This is because, in the reduced row echelon form, some columns
 do not have leading coefficients so that means there are nontrivial solutions. For the vectors to be linearly
 independent, every column must have a leading "1", one after the other, like a stairway. Now imagine the scalars in
 place of those "1"s. This would lead to every scalar being zero. So it is clear that if we miss a position, i.e. a
 column is missing a leading "1", then  we miss a scalar and that scalar is not zero.

     ${snippet{
          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(-1, 3, 2, 5)
          val v2 = new DenseVector(2, -6, -4, -10)
          val v3 = new DenseVector(1, 0, 8, -1)
          val vectors = new ColumnBindMatrix(v1, v2, v3)
          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.Matrix
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination

          // gauss jordan to get rref form
          val reducedRowEchelon: Matrix = new GaussJordanElimination(vectors).U()
          reducedRowEchelon.show()
     }.offsetIs(-14)}

All columns DO NOT have leading entries which means not all scalars are zero. In conclusion, the vectors are linearly
 dependent.










## Example 2: Linear independence and dependence examples for the case of fewer rows than columns

There are 3 elements in the 4 vectors, so this case is where there are more columns than rows. In this case, we will
see that when there are fewer rows than columns we will get ONLY linear dependence, regardless of the actual
relationship between the vectors. So if the vectors are multiples of themselves or not, we still get linear
dependence. See Proposition 2.22

     ${snippet{
          //TODO LINK ABOVE proposition to actual proposition page.
     }}


##### Code: linear dependence for a 3x4 system:

     ${snippet {
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(14, 12, -108)
          val v2 = new DenseVector(13, 11, 3)
          val v3 = new DenseVector(1, 1, 4)
          val v4 = new DenseVector(-2, 0 , 9)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4)

          /**
            * NOTE: see? the vectors are not multiples of each other yet they are linearly dependent, just like
            * Proposition 2.22 says (when rows < columns, we ALWAYS have linear dependence)
            */
          // matrix 3 x 4 (R3, 4 vectors) - vectors are NOT multiples of each other
          vectors.linearlyIndependent shouldEqual false

          // 8<--
          Success("The vectors shown as a matrix: " + vectors.show() + "\n\n" +
               "Linearly independent? " + vectors.linearlyIndependent)
          // 8<--

     }.offsetIs(-14)}


In the above code, the vectors v1, v2, v3, v4 are shown to be linearly dependent. Here is the gauss-jordan calculation
that transforms the `vectors` matrix into reduced row echelon form. If each column has a leading "1" then the scalars
are all zero which means the vectors are linearly independent. But if there is a column that skips having a "1" then
the vectors are linearly dependent because the scalar in that column is not zero.

     ${snippet {
          // 8<--
          // TODO: make the above snippet import into here so we don't need to recopy vectors and imports.
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(14, 12, -108)
          val v2 = new DenseVector(13, 11, 3)
          val v3 = new DenseVector(1, 1, 4)
          val v4 = new DenseVector(-2, 0 , 9)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4)
          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.Matrix
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination

          val reducedRowEchelon: Matrix = new GaussJordanElimination(vectors).U()
          reducedRowEchelon.show()

     }.offsetIs(-14)}

In the above row reduced echelon matrix, we see that there are some columns with no leading "1"s, namely the 4th
column. This means the vectors are linearly dependent.





##### Code: linear dependence for a 3x4 system:

     ${snippet {
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(2, 1, 3)
          val v2 = new DenseVector(4, 2, 6)
          val v3 = new DenseVector(1, 0 , -9)
          val v4 = new DenseVector(13, 8 , 88)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4)

          /**
            * NOTE: this is the vacuous case - if we didn't have the previous case to confirm that non-multiple
            * vectors can still be linearly dependent for rows < columns, then we wouldn't know what caused the linear
            * dependence in this case - the vectors being multiples of each other or Proposition 2.22 (rows < columns rule).
            */
          // matrix 3 x 4 (R3, 4 vectors) - vectors are multiples of each other
          vectors.linearlyIndependent shouldEqual false

          // 8<--
          Success("The vectors aligned as columns to form a matrix: " + vectors.show() + "\n\n" +
               "Linearly independent? " + vectors.linearlyIndependent)
          // 8<--

     }.offsetIs(-14)}


Above, we see that the vectors are linearly dependent. This is because, in the reduced row echelon form, some columns
do not have leading coefficients so that means there are nontrivial solutions. For the vectors to be linearly
independent, every column must have a leading "1", one after the other, like a stairway. Now imagine the scalars in
place of those "1"s. This would lead to every scalar being zero. So it is clear that if we miss a position, i.e. a
column is missing a leading "1", then  we miss a scalar and that scalar is not zero.

     ${snippet {
          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(2, 1, 3)
          val v2 = new DenseVector(4, 2, 6)
          val v3 = new DenseVector(1, 0 , -9)
          val v4 = new DenseVector(13, 8 , 88)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4)

          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.Matrix
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination

          // gauss jordan to get rref form
          val reducedRowEchelon: Matrix = new GaussJordanElimination(vectors).U()
          reducedRowEchelon.show()

     }.offsetIs(-14)}







## Example 3: Linear independence and dependence examples for same number of rows and columns (square matrix)


There are 5 elements in the 5 vectors, so row number equals column number. In this case, we will see that when there
are equal rows and columns we can get either linear independence or dependence, like in the first example.


##### Code: linear independence for a 5x5 square system:

     ${snippet {
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(1, 9, 0 , 0, 1)
          val v2 = new DenseVector(2, -3, 4, -9, 3)
          val v3 = new DenseVector(2, 2, -4, 17, 31)
          val v4 = new DenseVector(12, 90, -4, 5, 0)
          val v5 = new DenseVector(4, 8, -11, 7, 2)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4, v5)

          // matrix 5 x 5 (R5, 5 vectors) - vectors are NOT multiples of each other
          vectors.linearlyIndependent shouldEqual true

          // 8<--
          Success("The vectors shown as a matrix: " + vectors.show() + "\n\n" +
               "Linearly independent? " + vectors.linearlyIndependent)
          // 8<--

     }.offsetIs(-14)}


In the above code, the vectors v1, v2, v3, v4, v5 are shown to be linearly independent. The gauss-jordan calculation
below transforms the `vectors` matrix into reduced row echelon form. As before, the vectors are proven independent if
 each column has a leading "1" because that way, the scalars k1, k2, k3, k4, k5 are zero.

     ${snippet {
          // 8<--
          // TODO: make the above snippet import into here so we don't need to recopy vectors and imports.
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(1, 9, 0 , 0, 1)
          val v2 = new DenseVector(2, -3, 4, -9, 3)
          val v3 = new DenseVector(2, 2, -4, 17, 31)
          val v4 = new DenseVector(12, 90, -4, 5, 0)
          val v5 = new DenseVector(4, 8, -11, 7, 2)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4, v5)

          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.Matrix
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination

          val reducedRowEchelon: Matrix = new GaussJordanElimination(vectors).U()
          reducedRowEchelon.show()

     }.offsetIs(-14)}

Since ALL the columns have leading "1"s, all the vectors are linearly independent.


Additionally, if the row reduced echelon form of the system is equal to the inverse of the system, then the vectors
making up the system are linearly independent, according to Theorem 1.35. We can show this in code by running the
Gauss Jordan algorithm to find matrices T and U. Matrix T is the resulting coefficient matrix after finding the
inverse and matrix U is the row reduced echelon form. If U is the identity matrix, meaning has "1"s along its
diagonal and zeroes everywhere else, then we know the vectors are linearly independent. Thus, we can tell they are
independent when matrix T and the inverse match.

     ${snippet{
          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(1, 9, 0 , 0, 1)
          val v2 = new DenseVector(2, -3, 4, -9, 3)
          val v3 = new DenseVector(2, 2, -4, 17, 31)
          val v4 = new DenseVector(12, 90, -4, 5, 0)
          val v5 = new DenseVector(4, 8, -11, 7, 2)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4, v5)

          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.Matrix
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination

          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.Inverse

          val elim = new GaussJordanElimination(vectors)
          val reducedRowEchelon: Matrix = elim.U()
          val inverseByGaussJordan: Matrix = elim.T()
          val inverse: Matrix = new Inverse(vectors)

          //The coefficients should be equal
          inverseByGaussJordan.equalTo(inverse) shouldEqual true

          // The rref form should be the identity matrix
          reducedRowEchelon.equalTo(vectors.ONE()) shouldEqual true

          // 8<--
          Success("Inverse from Gauss-Jordan method: " + inverseByGaussJordan.show() + "\n\n" +
               "Inverse from matrix inverse method: " + inverse.show() + "\n\n" +
               "Identity matrix: " + vectors.ONE().show() + "\n\n" +
               "Reduced row echelon form - since it is equal to the identity matrix, the vectors are independent: " +
               reducedRowEchelon.show())
          // 8<--

     }.offsetIs(-14)}




##### Code: linear dependence for a 5x5 square system:

     ${snippet {
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(2, 4, 1, 3, 8)
          val v2 = new DenseVector(10, 20, 5, 15, 40)
          val v3 = new DenseVector(1, 1, -1, 0, 1)
          val v4 = new DenseVector(2, 0, 4, 9, 6)
          val v5 = new DenseVector(3, 3, 1, 7, 8)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4, v5)

          // matrix 5 x 5 (R5, 5 vectors) - vectors are multiples of each other
          vectors.linearlyIndependent shouldEqual false

          // 8<--
          Success("The vectors aligned as columns to form a matrix: " + vectors.show() + "\n\n" +
               "Linearly independent? " + vectors.linearlyIndependent)
          // 8<--

     }.offsetIs(-14)}


Since the reduced row echelon form has some columns that do not have a leading "1", the vectors are linearly
independent.

     ${snippet {
          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(2, 4, 1, 3, 8)
          val v2 = new DenseVector(10, 20, 5, 15, 40)
          val v3 = new DenseVector(1, 1, -1, 0, 1)
          val v4 = new DenseVector(2, 0, 4, 9, 6)
          val v5 = new DenseVector(3, 3, 1, 7, 8)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4, v5)

          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.Matrix
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination

          // gauss jordan to get rref form
          val reducedRowEchelon: Matrix = new GaussJordanElimination(vectors).U()
          reducedRowEchelon.show()

     }.offsetIs(-14)}




Now, again, we test if the inverse method coefficients and gauss-jordan coefficients are equal, which confirms if the
U matrix is the identity matrix and thus that the vectors are linearly independent. Note, however, that the inverse
matrix from the direct inverse method does not exist. An exception is thrown by the `Inverse` class because the
vectors are found to be linearly dependent. Thus, for this particular square matrix, the direct matrix inverse method
 does not work. Instead, we can rely on the inverse matrix produced by the Gauss-Jordan algorithm.

     ${snippet{
          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(2, 4, 1, 3, 8)
          val v2 = new DenseVector(10, 20, 5, 15, 40)
          val v3 = new DenseVector(1, 1, -1, 0, 1)
          val v4 = new DenseVector(2, 0, 4, 9, 6)
          val v5 = new DenseVector(3, 3, 1, 7, 8)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4, v5)

          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.Matrix
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination

          // 8<--
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.Inverse

          val elim = new GaussJordanElimination(vectors)
          val reducedRowEchelon: Matrix = elim.U()
          val inverseByGaussJordan: Matrix = elim.T()

          //NOTE: throwing an error because vectors are dependent.
          val message = try {
               new Inverse(vectors)
          } catch {
               case e:Exception => "Cause: " + e.getCause + "\nMessage: " + e.getMessage
          }

          // The rref form should be the identity matrix
          reducedRowEchelon.equalTo(vectors.ONE()) shouldEqual false

          // 8<--
          Success("Inverse from Gauss-Jordan method: " + inverseByGaussJordan.show() + "\n\n" +
               "Inverse matrix error message: \n" + message + "\n\n" +
               "Identity matrix: " + vectors.ONE().show() + "\n\n" +
               "Reduced row echelon form - since it is NOT equal to the identity matrix, the vectors are dependent:" +
               reducedRowEchelon.show())
          // 8<--

     }.offsetIs(-14)}




## Propositions and Examples

Fortunately, there are easier ways of testing for linear independence instead of gaussian elimination (that is, if
your vectors satisfy the criteria).



### PROPOSITION 2.21

<a href='https://i.imgsafe.org/b6f08927dc.png' target='_blank'><img src='https://i.imgsafe.org/b6f08927dc.png' border='0'/></a>


### Example

     ${snippet{
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(2, 3, 7)
          val v2 = new DenseVector(-4, 19, -5)
          val v3 = new DenseVector(0, 0, 0)
          val vectors = new ColumnBindMatrix(v1, v2, v3)

          vectors.linearlyIndependent shouldEqual false

          // 8<--
          val rref = new GaussJordanElimination(vectors).U()

          Success("The vectors as matrix: " + vectors.show() + "\n\n" +
               "Linearly independent? " + vectors.linearlyIndependent + "\n\n" +
               "Row reduced echelon form - vectors are dependent since not all columns have leading '1': " + rref
               .show())
          // 8<--

     }.offsetIs(-10)}




### PROPOSITION 2.22

<a href='https://i.imgsafe.org/b73903b2ab.png' target='_blank'><img src='https://i.imgsafe.org/b73903b2ab.png' border='0'/></a>

In other words, if we have more rows than columns in the matrix, which is equivalent to having more vectors than
elements, then the vectors are linearly dependent. In example 2, we encountered this proposition because regardless
of whether the vectors were multiples of themselves or not, the vectors were always going to be linearly dependent,
since there were more rows than columns, leading to infinite number of solutions and thus non-zero scalars.

### Example

<a href='https://i.imgsafe.org/b738f8ef1c.png' target='_blank'><img src='https://i.imgsafe.org/b738f8ef1c.png' border='0'/></a>

     ${snippet{
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(-3, 1, 0)
          val v2 = new DenseVector(0, 1, -1)
          val v3 = new DenseVector(2, 0, 0)
          val v4 = new DenseVector(1, 2, 3)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4)

          vectors.linearlyIndependent shouldEqual false

          // 8<--
          val rref = new GaussJordanElimination(vectors).U()

          Success("The vectors as matrix: " + vectors.show() + "\n\n" +
               "Linearly independent? " + vectors.linearlyIndependent + "\n\n" +
               "Row reduced echelon form - vectors are dependent since not all columns have leading '1': " + rref
               .show())
          // 8<--

     }.offsetIs(-10)}





### PROPOSITION 2.23

<a href='https://i.imgsafe.org/b7740ce65e.png' target='_blank'><img src='https://i.imgsafe.org/b7740ce65e.png' border='0'/></a>


### Example

     ${snippet {
          import com.numericalmethod.suanshu.algebra.linear.vector.doubles.dense.DenseVector
          import com.numericalmethod.suanshu.algebra.linear.matrix.doubles.operation.ColumnBindMatrix

          import org.linalg.studies.Ext._

          val v1 = new DenseVector(-3, 1, 0)
          val v2 = new DenseVector(0, 1, -1)
          val v3 = new DenseVector(2, 0, 0)
          val v4 = new DenseVector(1, 2, 3)
          val vectors = new ColumnBindMatrix(v1, v2, v3, v4)

          vectors.linearlyIndependent shouldEqual false

          // 8<--
          val rref = new GaussJordanElimination(vectors).U()

          Success ("The vectors as matrix: " + vectors.show() + "\n\n" +
           "Linearly independent? " + vectors.linearlyIndependent + "\n\n" +
           "Row reduced echelon form - vectors are dependent since not all columns have leading '1': " + rref
          .show())
          // 8<--


     }.offsetIs(-10)}


     """
}




/*object LinearIndependence_UserGuidePage extends UserGuidePage {

     def is = "Linear Independence".title ^ s2"""

Newest link:
* ${link(LinearIndependence)}

"""
}*/


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
}
  */
*/
