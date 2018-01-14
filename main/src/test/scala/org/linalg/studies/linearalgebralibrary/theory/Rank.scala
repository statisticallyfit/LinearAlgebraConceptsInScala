package org.linalg.studies.linearalgebralibrary.theory

import org.linalg.studies.linearalgebralibrary.number._
/**
  *
  */

trait Rank[V/* <: VectorSet[N]*/, N <: Number[N]] {
     def rank(): Int
}

