package org.linalg.studies.linearalgebralibrary.factorization

import org.linalg.studies.linearalgebralibrary.util._
import org.linalg.studies.linearalgebralibrary.theory._
import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._

/**
  *
  */
trait Decomposition[N <: Number[N]]{
     def decompose(): (Matrix[N], Matrix[N])
}
