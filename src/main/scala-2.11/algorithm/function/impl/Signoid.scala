package algorithm.function.impl

import algorithm.function.ActivationFunction

/**
  * Created by Dragos on 07.05.2016.
  */
class Signoid extends ActivationFunction{
  override def calc(x: Double): Double = 1.0 / (1.0 + Math.exp(-x))

  override def derivate(x: Double): Double = calc(x) * (1 - calc(x))
}
