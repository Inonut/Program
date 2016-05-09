package algorithm2.function

/**
  * Created by Dragos on 08.05.2016.
  */
trait ActivationFunction {

  def activationFunction(x: Double): Double

  def activationFunction(arr: Array[Double], start: Int, size: Int): Unit

  def derivateFunction(x: Double, f: Double): Double

  def derivateFunction(x: Double): Double
}
