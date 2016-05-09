package algorithm2.domain

import algorithm2.function.ActivationFunction

/**
  * Created by Dragos on 08.05.2016.
  */
class Network {

  var inputCount: Int = 0
  var outputCount: Int = 0
  var layerCounts: Array[Int] = null
  var weightIndex: Array[Int] = null
  var layerIndex: Array[Int] = null
  var activationFunctions: Array[ActivationFunction] = null
  var layerFeedCounts: Array[Int] = null
  var biasActivation: Array[Double] = null
  var beginTraining: Int = 0
  var endTraining: Int = 0
  var weights: Array[Double] = null
  var layerOutput: Array[Double] = null
  var layerSums: Array[Double] = null
  var layerContextCount: Array[Int] = null

  def create(layers: Array[Layer]): Network ={

    var layerCount = layers.length

    this.inputCount = layers.head.neuronsCount
    this.outputCount = layers.last.neuronsCount
    this.layerCounts = new Array[Int](layerCount)
    this.layerContextCount = new Array[Int](layerCount)
    this.weightIndex = new Array[Int](layerCount)
    this.layerIndex = new Array[Int](layerCount)
    this.activationFunctions = new Array[ActivationFunction](layerCount)
    this.layerFeedCounts = new Array[Int](layerCount)
    this.biasActivation = new Array[Double](layerCount)

    var index = 0
    var neuronCount = 0
    var weightCount = 0

    for(i <- layers.length - 1 to 0 by -1){
      var layer = layers(i)
      var nextlayer: Layer = null

      if(i > 0){
        nextlayer = layers(i-1)
      }

      this.biasActivation(index) = layer.biasActivation
      this.layerCounts(index) = layer.totalNeuronCount
      this.layerFeedCounts(index) = layer.neuronsCount
      this.layerContextCount(index) = 0
      this.activationFunctions(index) = layer.activation

      neuronCount += layer.totalNeuronCount

      if(nextlayer != null){
        weightCount += layer.neuronsCount * nextlayer.totalNeuronCount
      }

      if(index == 0){
        this.weightIndex(index) = 0
        this.layerIndex(index) = 0
      } else {
        this.weightIndex(index) = this.weightIndex(index - 1) + this.layerCounts(index) * this.layerFeedCounts(index - 1)
        this.layerIndex(index) = this.layerIndex(index - 1) + this.layerCounts(index - 1)
      }

      index += 1
    }

    this.beginTraining = 0
    this.endTraining = this.layerCounts.length - 1

    this.weights = new Array[Double](weightCount)
    this.layerOutput = new Array[Double](neuronCount)
    this.layerSums = new Array[Double](neuronCount)

    this.clearContext()

    this
  }

  def clearContext(): Unit = {
    var index = 0
    for (i <- 0 until this.layerIndex.length) {

      var hasBias = (this.layerContextCount(i) + this.layerFeedCounts(i)) != this.layerCounts(i)

      // fill in regular neurons
      for(i <- index until index + this.layerFeedCounts(i)){
        this.layerOutput(i) = 0
      }
      index += this.layerFeedCounts(i)

      // fill in the bias
      if (hasBias) {
        this.layerOutput(index) = this.biasActivation(i)
        index += 1
      }

      // fill in context
      for(i <- index until index + this.layerContextCount(i)){
        this.layerOutput(i) = 0
      }
      index += this.layerContextCount(i)
    }
  }

  def randomize(): Unit = {
    this.weights = this.weights.map(w => Math.random() * 2 - 1)
  }

  def computeLayer(currentLayer: Int): Unit = {
    var inputIndex = this.layerIndex(currentLayer)
    var outputIndex = this.layerIndex(currentLayer - 1)
    var inputSize = this.layerCounts(currentLayer)
    var outputSize = this.layerFeedCounts(currentLayer - 1)

    var index = this.weightIndex(currentLayer - 1)

    var limitX = outputIndex + outputSize
    var limitY = inputIndex + inputSize

    for(x <- outputIndex until limitX){
      var sum = 0.0
      for(y <- inputIndex until limitY){
        sum += this.weights(index) * this.layerOutput(y)
        index += 1
      }
      this.layerSums(x) = sum
      this.layerOutput(x) = sum
    }

    this.activationFunctions(currentLayer - 1).activationFunction(this.layerOutput, outputIndex, outputSize)
  }

  def compute(input: Array[Double], output: Array[Double]): Unit = {
    var sourceIndex = this.layerOutput.length - this.layerCounts(this.layerCounts.length - 1)

    for(i <- 0 until this.inputCount){
      this.layerOutput(i + sourceIndex) = input(i)
    }

    for (i <- this.layerIndex.length - 1 until 0 by -1) {
      this.computeLayer(i)
    }

    for(i <- 0 until this.outputCount){
      output(i) = this.layerOutput(i)
    }
  }

}
