package algorithm.domain

/**
  * Created by Dragos on 07.05.2016.
  */
class Neuron {

  var bias: Double = 0
  var biasOld: Double = 0
  var weightSum: Double = 0
  var output: Double = 0
  var target: Double = -1
  var error: Double = 0

  var layer: Layer = null
}
