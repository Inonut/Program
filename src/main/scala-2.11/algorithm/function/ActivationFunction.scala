package algorithm.function

/**
  * Created by Dragos on 07.05.2016.
  */
trait ActivationFunction {

  def calc(x: Double): Double

  def derivate(x: Double): Double
}
