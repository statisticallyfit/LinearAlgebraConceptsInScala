package org.specs2.ugbase


//import org.specs2.guide.linalg.linearindependence.Basis_UserGuidePage
//import org.specs2.guide.linalg.linearindependence.LinearIndependence_UserGuidePage

/**
  * * ${link(LinearIndependence_UserGuidePage)}
  * ${link(Basis_UserGuidePage)}

  */

object UserGuide extends UserGuidePage {

     //implicit val snippetParams = SnippetParams(evalCode = true)
     def is = "Linear Algebra in Scala - by Example".title ^
          s2"""

$specs2 is a library for writing executable software specifications in Scala.
In this user guide, you will find:

Concepts related to:


## Vector Properties

## Matrix Properties

## Gaussian Elimination

## Linear Independence:



## Vector Spaces

## Subspaces

## Inner Product Spaces


## Factorization Methods (Qr, LU, SVD)

## Linear Transformations

## Determinants

## Eigenvalues

          """

}
