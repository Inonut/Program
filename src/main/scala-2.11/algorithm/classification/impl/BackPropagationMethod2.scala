package algorithm.classification.impl

import algorithm.classification.Classification
import algorithm.domain.{ClassificationData, Layer, Neuron, Weight}

/**
  * Created by Dragos on 12.05.2016.
  */
class BackPropagationMethod2 extends Classification{

  var neurons = Array[Neuron]()
  var layers = Array[Layer]()


  val etaPlus = 1.2
  val etaMinus = 0.5
  val deltaMax = 50.0
  val deltaMin = 1.0E-6

  override protected def createNet(lCount: Array[Int]): Unit = {

    val layerCount = lCount.map(_+1)

    val sumNeurons = layerCount.sum

    // creare neuroni
    (1 to sumNeurons).foreach(i => this.neurons :+= new Neuron)

    // creare strat neuronal
    var sum = 0
    layerCount.foreach(nrNeuron => {
      val layer = new Layer
      //primii nrNeuroni Neuroni
      val neuronsLayer: Array[Neuron] = (for(i <- sum until sum + nrNeuron) yield neurons(i))(collection.breakOut)

      //init neuron pentru stratul curent
      neuronsLayer.foreach(neuron => {
        neuron.layer = layer
        neuron.output = 1
      })

      //init startul curent
      layer.neurons = neuronsLayer

      layers :+= layer

      sum = sum + nrNeuron
    })

    //creare sinapse // mai putin pentru ultimul,
    for(k <- 0 until layerCount.length-1){
      for(n1 <- layers(k).neurons; n2 <- layers(k+1).neurons if n2 != layers(k+1).neurons.last) {
        val weight = new Weight
        weight.neuronFrom = n1
        weight.neuronTo = n2
        weight.w = Math.random() * 2.0 - 1.0

        layers(k).weights :+= weight
        layers(k+1).weights :+= weight
      }
    }
  }


  override protected def clearNet(): Unit = {
    neurons = Array()
    layers = Array()
  }

  def reset(): Unit ={
    layers.flatMap(layer => layer.weights).foreach(weight => {
      weight.gradientNeuron = 0
      weight.lastError = 0
    })
  }

  def forwardPropagate(data: ClassificationData): Unit = {
    //resetOutput
    layers.flatMap(layer => layer.neurons).foreach(neuron => neuron.weightSum = 0)

    //set input
    (layers.head.neurons, data.data).zipped.foreach((neuron, o) => neuron.output = o)

    for(i <- 1 until layers.length){
      val layer = layers(i) // prin referinta (vezi initul)
      val weights = layer.weights.filter(weight => layer.neurons.contains(weight.neuronTo))

      weights.foreach(weight => weight.neuronTo.weightSum += weight.neuronFrom.output * weight.w)
      weights.foreach(weight => weight.neuronTo.output = activationFunction.calc(weight.neuronTo.weightSum))
    }
  }

  def updateError(): Unit = {
    //ultimul strat
    layers.last.neurons.foreach(neuron => neuron.error = activationFunction.derivate(neuron.weightSum) * (neuron.target - neuron.output))

    for(i <- layers.length-2 to 0 by -1){
      val layer = layers(i) // prin referinta (vezi initul)
      val weights = layer.weights.filter(weight => layer.neurons.contains(weight.neuronFrom))

      weights.foreach(weight => weight.neuronFrom.error = 0)
      weights.foreach(weight => weight.neuronFrom.error += activationFunction.derivate(weight.neuronFrom.weightSum) * weight.neuronTo.error * weight.w)
    }
  }


  def updateGradient(): Unit = {

    for(i <- layers.length-2 to 0 by -1){
      val layer = layers(i) // prin referinta (vezi initul)
      val weights = layer.weights.filter(weight => layer.neurons.contains(weight.neuronFrom))

      weights.foreach(weight => weight.gradientNeuron += weight.neuronFrom.output * weight.neuronTo.error)
    }
  }

  def updateWeights(): Unit ={
    layers.flatMap(layer => layer.weights).distinct.foreach(weight => {
      val change = Math.signum(weight.gradientNeuron * weight.prevGradientNeuron)

      change match {
        case -1 =>
          var delta = weight.updateValue * this.etaMinus
          delta = Math.max(delta, this.deltaMax)
          weight.updateValue = delta
          weight.prevGradientNeuron = 0

        case 1 =>
          var delta = weight.updateValue * this.etaPlus
          delta = Math.min(delta, this.deltaMax)
          weight.w += Math.signum(weight.gradientNeuron) * delta
          weight.updateValue = delta
          weight.prevGradientNeuron = weight.gradientNeuron

        case 0 =>
          var delta = weight.updateValue
          weight.w += Math.signum(weight.gradientNeuron) * delta
          weight.prevGradientNeuron = weight.gradientNeuron
      }
    })
  }

  override protected def trainContinue(classificationData: Array[ClassificationData]): Unit = {

    clearNet()

    createNet(layerCount)

    /********/
    var totalError = Double.MaxValue
    for(iter <- 0 until nrEpochs if totalError > error && !stop){

      totalError = 0
      reset()

      classificationData.foreach(data => {

        if(!stop){

          (layers.last.neurons, data.target).zipped.foreach((neuron, target) => neuron.target = target)

          forwardPropagate(data)

          updateError()

          updateGradient()

          layers.last.neurons.foreach(neuron => totalError += Math.pow(neuron.target - neuron.output, 2))

        }
      })

      updateWeights()


      println(iter + " "+ totalError)


      if(!stop){
        //println(totalError)
        doAfterIteration(iter, totalError)
      }

    }
  }

  override def recognize(data: ClassificationData): ClassificationData = {

    forwardPropagate(data)

    new ClassificationData{target = layers.last.neurons.map(neuron => neuron.output)(collection.breakOut)}
  }
}
