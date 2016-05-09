package algorithm2.domain

import scala.collection.mutable.ListBuffer

/**
  * Created by Dragos on 08.05.2016.
  */
class PropagationTrainer {

  var network: Network = null
  var trainingInput: Array[Array[Double]] = null
  var trainingIdeal: Array[Array[Double]] = null
  var learningRate: Double = 0
  var layerDelta: Array[Double] = null
  var gradients: Array[Double] = null
  var lastGradient: Array[Double] = null
  var lastDelta: Array[Double] = null
  var actual: Array[Double] = null
  var flatSpot: Array[Double] = null
  var updateValues: Array[Double] = null
  var globalError: Double = 0
  var setSize: Int = 0
  var error: Double = 0


  var POSITIVE_ETA = 1.2
  var NEGATIVE_ETA = 0.5
  var DELTA_MIN = 1e-6
  var MAX_STEP = 50

  def errorFunction(ideal: Array[Double], actual: Array[Double], error: Array[Double]): Unit ={
    for (i <- actual.indices) {
      error(i) = ideal(i) - actual(i)
    }
  }

  def create(network: Network, input: Array[Array[Double]], ideal: Array[Array[Double]], learningRate: Double): PropagationTrainer ={
    this.network = network
    this.trainingInput = input
    this.trainingIdeal = ideal
    this.learningRate = learningRate

    this.layerDelta = new Array[Double](network.layerOutput.length)
    this.gradients = new Array[Double](network.weights.length)
    this.lastGradient = new Array[Double](network.weights.length)
    this.lastDelta = new Array[Double](network.weights.length)
    this.actual = new Array[Double](network.outputCount)
    this.flatSpot = new Array[Double](network.layerOutput.length)
    this.updateValues = new Array[Double](network.weights.length)

    this.updateValues = this.updateValues.map(x => 0.1)

    this
  }

  def processLevel(currentLevel: Int): Unit ={
    var fromLayerIndex = this.network.layerIndex(currentLevel + 1)
    var toLayerIndex = this.network.layerIndex(currentLevel)
    var fromLayerSize = this.network.layerCounts(currentLevel + 1)
    var toLayerSize = this.network.layerFeedCounts(currentLevel)

    var index = this.network.weightIndex(currentLevel)
    var activation = this.network.activationFunctions(currentLevel + 1)
    var currentFlatSpot = this.flatSpot(currentLevel + 1)

    // handle weights
    var yi = fromLayerIndex
    for (y <-0 until fromLayerSize) {
      var output = this.network.layerOutput(yi)
      var sum = 0.0
      var xi = toLayerIndex
      var wi = index + y
      for (x <-0 until  toLayerSize) {
        this.gradients(wi) += output * this.layerDelta(xi)
        sum += this.network.weights(wi) * this.layerDelta(xi)
        wi += fromLayerSize
        xi += 1
      }

      this.layerDelta(yi) = sum * (activation.derivateFunction(this.network.layerSums(yi), this.network.layerOutput(yi)) + currentFlatSpot)
      yi += 1
    }
  }

  def sign(x: Double): Double = {
    if (Math.abs(x) < 0.0000001) {
      0
    } else if (x > 0) {
      1
    } else {
      -1
    }
  }

  def learnRPROP(): Unit = {

    for (i <-0 until this.network.weights.length) {
      var change = sign(this.gradients(i) * this.lastGradient(i))
      var weightChange = 0.0

      if (change > 0) {
        var delta = this.updateValues(i) * this.POSITIVE_ETA
        delta = Math.min(delta, this.MAX_STEP)
        weightChange = sign(this.gradients(i)) * delta
        this.updateValues(i) = delta
        this.lastGradient(i) = this.gradients(i)
      } else if (change < 0) {
        var delta = this.updateValues(i) * this.NEGATIVE_ETA
        delta = Math.max(delta, this.DELTA_MIN)
        this.updateValues(i) = delta
        weightChange = -this.lastDelta(i)
        this.lastGradient(i) = 0
      } else if (change == 0) {
        var delta = this.updateValues(i)
        weightChange = sign(this.gradients(i)) * delta
        this.lastGradient(i) = this.gradients(i)
      }

      this.network.weights(i) += weightChange
    }
  }

  def process(input: Array[Double], ideal: Array[Double], s: Double) {

    this.network.compute(input, this.actual)

    for (j <-0 until ideal.length) {
      var delta = this.actual(j) - ideal(j)
      this.globalError = this.globalError + (delta * delta)
      this.setSize += 1
    }

    this.errorFunction(ideal, this.actual, this.layerDelta)

    for (i <- 0 until this.actual.length) {
      this.layerDelta(i) = (this.network.activationFunctions(0).derivateFunction(this.network.layerSums(i), this.network.layerOutput(i)) + this.flatSpot(0)) * (this.layerDelta(i) * s)
    }

    for (i <- this.network.beginTraining until this.network.endTraining) {
      this.processLevel(i)
    }
  }

  def iteration(): Unit = {
    this.globalError = 0
    this.setSize = 0

    this.gradients = this.gradients.map(x => 0.0)
    this.lastDelta = this.lastDelta.map(x => 0.0)

    for (i <- 0 until this.trainingInput.length) {
      this.process(this.trainingInput(i), this.trainingIdeal(i), 1.0)
    }

    this.learnRPROP()

    this.error = this.globalError / this.setSize
  }
}
