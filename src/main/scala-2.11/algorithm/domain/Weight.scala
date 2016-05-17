package algorithm.domain

/**
  * Created by Dragos on 07.05.2016.
  */
class Weight {

  var neuronFrom: Neuron = null
  var neuronTo: Neuron = null
  var w: Double = 0
  var wOld: Double = 0.1

  var gradientNeuron: Double = 0
  var prevGradientNeuron: Double = 0.0
  var gradientBias: Double = 0
  var prevGradientBias: Double = 0.0
  var lastError: Double = 0
  var updateValue: Double = 0.1


  /*****/


}
