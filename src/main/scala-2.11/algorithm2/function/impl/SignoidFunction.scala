package algorithm2.function.impl

import algorithm2.function.ActivationFunction

/**
  * Created by Dragos on 08.05.2016.
  */
class SignoidFunction extends ActivationFunction{
  override def activationFunction(x: Double): Double = 1.0 / (1.0 + Math.exp(-x))

  override def activationFunction(arr: Array[Double], start: Int, size: Int): Unit = for(i <- start until start + size) arr(i) = activationFunction(arr(i))

  override def derivateFunction(x: Double, f: Double): Double = f * (1 - f)

  override def derivateFunction(x: Double): Double = derivateFunction(x, activationFunction(x))
}
