package algorithm2.domain

import algorithm2.function.ActivationFunction

/**
  * Created by Dragos on 08.05.2016.
  */
class Layer {

  var activation: ActivationFunction = null
  var neuronsCount: Int = 0
  var biasActivation: Double = 0

  def totalNeuronCount = neuronsCount + (if (hasBias)  1 else 0)
  def hasBias = Math.abs(this.biasActivation) > 0.0000001

  def create(activation: ActivationFunction, neuronsCount: Int, biasActivation: Double): Layer = {
    this.activation =activation
    this.neuronsCount = neuronsCount
    this.biasActivation = biasActivation

    this
  }
}
