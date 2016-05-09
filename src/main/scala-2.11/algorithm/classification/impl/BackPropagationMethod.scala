package algorithm.classification.impl

import algorithm.classification.Classification
import algorithm.domain.{ClassificationData, Layer, Neuron, Weight}
import algorithm.function.ActivationFunction

import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
  * Created by Dragos on 06.05.2016.
  */
class BackPropagationMethod extends Classification{

  var neurons = new ListBuffer[Neuron]
  var layers = new ListBuffer[Layer]
  var activationFunction: ActivationFunction = null
  var alfa = 0.0001
  var learningRate = 0.1


  override def createNet(layerCount: ListBuffer[Int], nameOfData: ListBuffer[String]): Unit ={

    val random = new Random

    val sumNeurons = layerCount.sum

    // creare neuroni
    (1 to sumNeurons).foreach(i => this.neurons += new Neuron)

    // creare strat neuronal
    var sum = 0
    layerCount.foreach(nrNeuron => {
      val layer = new Layer
      //primii nrNeuroni Neuroni
      val neuronsLayer: ListBuffer[Neuron] = (for(i <- sum until sum + nrNeuron) yield neurons(i))(collection.breakOut)

      //init neuron pentru stratul curent
      neuronsLayer.foreach(neuron => {
        neuron.layer = layer
        neuron.bias = random.nextDouble() / 10
      })

      //init startul curent
      layer.neurons = neuronsLayer

      layers += layer

      sum = sum + nrNeuron
    })

    //creare sinapse // mai putin pentru ultimul,
    for(k <- 0 until layerCount.length-1){
      for(n1 <- layers(k).neurons; n2 <- layers(k+1).neurons) {
        val weight = new Weight
        weight.neuronFrom = n1
        weight.neuronTo = n2
        weight.w = random.nextDouble() / 10
        weight.wOld = weight.w

        layers(k).weights += weight
        layers(k+1).weights += weight
      }
    }


    // init net
    //(layers.last.neurons, nameOfData).zipped.foreach((neuron, name) => neuron.name = name)

  }

  override def clearNet(): Unit = {
    neurons.clear()
    layers.clear()
  }

  override def setActivationFunction(activationFunction: ActivationFunction): Unit = this.activationFunction = activationFunction

  def forwardPropagate(data: ClassificationData): Unit = {

    //set input
    (layers.head.neurons, data.data).zipped.foreach((neuron, o) => neuron.output = o)

    for(i <- 1 until layers.length){
      val layer = layers(i) // prin referinta (vezi initul)
      val weights = layer.weights.filter(weight => layer.neurons.contains(weight.neuronTo))

      layer.neurons.foreach(neuron => neuron.weightSum = neuron.bias)
      weights.foreach(weight => weight.neuronTo.weightSum += weight.neuronFrom.output * weight.w)
      layer.neurons.foreach(neuron => neuron.output = activationFunction.calc(neuron.weightSum))
    }
  }

  def updateError(): Unit = {
    //ultimul strat
    layers.last.neurons.foreach(neuron => neuron.error = activationFunction.derivate(neuron.weightSum) * (neuron.target - neuron.output))

    for(i <- layers.length-2 until 0 by -1){
      val layer = layers(i) // prin referinta (vezi initul)
      val weights = layer.weights.filter(weight => layer.neurons.contains(weight.neuronFrom))
      weights.foreach(weight => weight.neuronFrom.error = activationFunction.derivate(weight.neuronFrom.weightSum) * weight.neuronTo.error * weight.w)
    }
  }

  def updateWeights(): Unit = {

    for(i <- 1 until layers.length){
      val layer = layers(i) // prin referinta (vezi initul)
      val weights = layer.weights.filter(weight => layer.neurons.contains(weight.neuronTo))
      weights.foreach(weight => {
        val oldWeight = weight.w
        
        weight.w = weight.w + alfa * weight.wOld + learningRate * weight.neuronTo.error * weight.neuronFrom.output

        weight.wOld = oldWeight
      })
    }
  }

  def updateBias(): Unit = {
    for(i <- 1 until layers.length){
      val layer = layers(i) // prin referinta (vezi initul)
      layer.neurons.foreach(neuron => {
        val oldBias = neuron.bias

        neuron.bias = neuron.bias + alfa * neuron.biasOld + learningRate * neuron.error * 1

        neuron.biasOld = neuron.bias
      })
    }
  }


  protected override def trainContinue(classificationData: ListBuffer[ClassificationData], nrEpochs: Int, error: Double, learningRate: Double, alfa: Double, layerCount: ListBuffer[Int], activationFunction: ActivationFunction): Unit = {

    this.learningRate = learningRate
    this.alfa = alfa

    clearNet()

    //set ultimul strat
    val names = classificationData.map(cd => cd.name).distinct
    //createNet(layerCount += names.length, names)
    createNet(layerCount += 1, names)
    setActivationFunction(activationFunction)


    /********/

 /*   layers(0).weights(0).w = -0.2
    layers(0).weights(0).wOld = -0.2
    layers(0).weights(1).w = -0.1
    layers(0).weights(1).wOld = -0.1
    layers(0).weights(2).w = 0.1
    layers(0).weights(2).wOld = 0.1
    layers(0).weights(3).w = 0.3
    layers(0).weights(3).wOld = 0.3
    layers(1).weights(4).w = 0.2
    layers(1).weights(4).wOld = 0.2
    layers(1).weights(5).w = 0.3
    layers(1).weights(5).wOld = 0.3
    layers(1).neurons(0).bias = 0.1
    layers(1).neurons(0).biasOld = 0.1
    layers(1).neurons(1).bias = 0.1
    layers(1).neurons(1).biasOld = 0.1
    layers(2).neurons(0).bias = 0.2
    layers(2).neurons(0).biasOld = 0.2


*/
    /********/
    var totalError = Double.MaxValue
    for(iter <- 0 until nrEpochs if totalError > error && !stop){

      totalError = 0

      classificationData.foreach(data => {

        if(!stop){
          //set target
          /*layers.last.neurons.foreach(neuron => {
            if(neuron.name.equals(data.name.toString)) {
              neuron.target = 1
            } else {
              neuron.target = 0
            }
          })*/

          layers.last.neurons.last.target = data.target

          forwardPropagate(data)

          updateError()

          updateWeights()

          updateBias()

          //total error
          //val targetNeuron = layers.last.neurons.find(neuron => neuron.name.equals(data.name)).get
          val targetNeuron = layers.last.neurons.last
          totalError += Math.abs(targetNeuron.target - targetNeuron.output)
        }
      })

      println(totalError)


      if(!stop){
        //println(totalError)
        doAfterIteration(iter, totalError)
      }

    }
  }

  override def recognize(data: ClassificationData): ClassificationData = {

    forwardPropagate(data)

    println(layers.last.neurons.maxBy(neuron => neuron.output).output)

    //new ClassificationData {name = layers.last.neurons.maxBy(neuron => neuron.output).name}
    data
  }


  override def toString = s"BackPropagationMethod()"

}