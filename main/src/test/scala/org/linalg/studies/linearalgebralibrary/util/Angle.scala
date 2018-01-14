package org.linalg.studies.linearalgebralibrary.util

/**
  *
  */

class Angle(val angle: Double)

//precondition: deg: Double must be in degrees form
class Degrees(deg: Double) extends Angle(deg) {
     def toRadians(): Radians = new Radians(deg * Math.PI / 180)
     def toDouble: Double = this.deg

     def normalize(): Degrees = {
          var tempDeg: Double = deg
          while(deg > 360){
               tempDeg -= 360
          }
          new Degrees(tempDeg)
     }
     override def toString: String = Util.GenOps.roundTo(deg, 5) + " °"
}

//precondition: rad: Double must be in radians form
class Radians(rad: Double) extends Angle(rad) {
     def toDegrees(): Degrees = new Degrees(rad * 180 / Math.PI)
     def toDouble: Double = this.rad
     def normalize(): Radians ={
          var tempRad: Double = rad
          val twoPI = 2 * Math.PI

          while(rad > twoPI) tempRad -= twoPI
          new Radians(tempRad)
     }
     override def toString: String = Util.GenOps.roundTo(rad, 5) + " rad"
}



object Angle {
     // TODO make normalize method that puts radians between degrees 0 and 360
     //def apply(d: Double): Angle = new Angle(d)
     def toRadians(deg: Degrees): Radians = new Radians(deg.toDouble * Math.PI / 180)
     def toDegrees(rad: Radians): Degrees = new Degrees(rad.toDouble * 180 / Math.PI )
     // def normalize(degrees: Double): Double =
     // def normalize(radians: Double): Double =
}
