package org.fp

/**
  *
  */
package object concepts {

  val keyPoint = "$$key point$$"
  val bookmarks = "$$bookmarks$$"

  //
  val partialFunction = "[partial function]"
  val selfType = "self-type"
  val ad_hocPolymorphism = "[ad-hoc polymorphism]"
  val functionOverloading = "[function overloading]"
  val glueObject = "[glue object]"

  /**
    * @todo https://hyp.is/iJIsfC2GEeam8q-I4gf9FA/archive.is/KAdCc
    */
  val structuralType = "structural type"

  //
  val constructor = "[constructor]"
  val typeConstructor = "[type constructor]"
  val typeClass = "[type class]"
  val typeClassInstance = "[type class instance]"
  val companionObject = "[companion object]"
  val taggedType = "[tagged type]"
  val higherKindedType = "[higher-Kinded type]"
  val forComprehension = "[for comprehension]"
  //
  val semigroup = "[semigroup]"
  val monoid = "[monoid]"

  /**
    * See Functor.md
    */
  val functor = "[functor]"
  val biFunctor = "[bi functor]"
  /**
    * @wiki Applies [[partialFunction]] nested in a instance of [[functor]] F to an argument nested in an instance of F and returns the result in instance of F.
    *      If the [[partialFunction]] has more than 1 argument then it may apply multiple arguments but the function needs to be in curried form.
    */
  val applyFunctor = "[apply functor]"
  val applicativeFunctor = "[applicative functor]"
  val applicativeBuilder = "[applicative builder]"
  val applicativeValue = "[applicative value]"
  val traverseFunctor = "[traverse]"
  val Arrow = "[arrow]"
  val FunctionArrow = "[function arrow]"
  val KleisliArrow = "[Kleisli arrow]"
  val KleisliFunction = "[Kleisli function]"
  val coProduct = "[coproduct]"
  val freeMonad = "[free monad]"
  val naturalTransformation = "[natural transformation]"

  /**
    * @todo related to [[operator_lift]]
    */
  val functionLifting = "[function lifting]"
  val functionComposition = "[function composition]"
  val functorComposition = "[functor composition]"
  val functorLifting = "[functor lifting]"

  /**
    * @todo "fmap" in Haskell
    */
  val operator_map = "[map operator]"
  val operator_mapK = "[mapK operator]"
  val operator_flatMap = "[flatMap operator]"
  val operator_foldMap = "[foldMap operator]"
  val operator_leftMap = "[leftMap operator]"
  val operator_swap = "[swap operator]"
  val operator_swapped = "[swapped operator]"
  val operator_lift = "[lift operator]"
  val operator_void = "[void operator]"
  val operator_Fproduct = "[fproduct operator]"
  val operator_Fpair = "[fpair operator]"
  val operator_as = "[as operator]"
  val operator_shift = "[>| operator]" //@todo what is the correct name?
  val operator_strengthL = "[strengthL operator]"
  val operator_strengthR = "[RightL operator]"
  val operator_point = "[pure/point operator]"
  val operator_apply = "[apply operator]"
  val operator_LHS = "[LHS operator]"
  val operator_RHS = "[RHS operator]"
  val operator_sequence = "[sequence operator]"
  val operator_traverse = "[traverse operator]"
  val operator_append = "[append operator]"
  //
  val operator_compose = "[compose operator]"
  val operator_<=< = "[<=< operator]" /** alias for [[operator_compose]]*/
  //
  val operator_composeK = "[composeK operator]"
  val operator_<==< = "[<==< operator]" /** alias for [[operator_composeK]]*/
  //
  val operator_andThen = "[andThen operator]"
  val operator_>=> = "[>=> operator]" /** alias for [[operator_andThen]]*/
  val operator_>>= = "[>>= operator]"
  //
  val operator_andThenK = "[andThenK operator]"
  val operator_>==> = "[>==> operator]" /** alias for [[operator_andThenK]]*/
  //
  val operator_=<< = "[operator operator=<<]"
  val operator_local = "[operator local]" //@todo clarify
  //
  val operator_bind = "[operator bind]"

  /**
    *
    */
  val lawIdentity = "[identity law]"
  val lawAssociativity = "[associativity law]"
  val lawCommutativity = "[commutativity law]"
  val lawComposition = "[composition law]"
  val lawHomomorphism = "[homomorphism law]"
  val lawInterchange = "[interchange law]"
  val lawMapping = "[mapping law]" //@todo ??

  /**
    * [[lawAssociativity]]
    */
  val semigroupLaws = "[semigroup laws]"
  /**
    * [[lawAssociativity]]
    * [[lawCommutativity]]
    */
  val monadLaws = "[monad laws]"

  val functorLaws = "[functor laws]"

  //
  val monad = "[monad]"
  val monadInstance = "[monad instance]"
  val monadicStructure = "[monadic structure]"
  val monadicContext = "[monadic context]"
  val monadicFunction = "[monadic function]"
  val monadicBind = "[monadic bind]"
  val monadTransformer = "[monad transformer]"
  val optionTransformer = "[Option transformer]"
  val eitherTransformer = "[Either transformer]"

  /**
    *
    */
  val disjunction = "[disjunction]"
  val leftProjection = "[left projection]"
  val rightProjection = "[right projection]"
  val validation = "[validation]"
}

