package algorithm.classification

import algorithm.domain.ClassificationData
import algorithm.function.ActivationFunction

import scala.collection.mutable.ListBuffer

/**
  * Created by Dragos on 06.05.2016.
  */
trait Classification {

  private var callback: PartialFunction[Any,Unit] = null
  protected var stop = false

  def createNet(layerCount: ListBuffer[Int], nameOfData: ListBuffer[String]): Unit

  def clearNet(): Unit

  def setActivationFunction(activationFunction: ActivationFunction): Unit

  protected def trainContinue(classificationData: ListBuffer[ClassificationData], nrEpochs: Int, error: Double, learningRate: Double, alfa: Double, layerCount: ListBuffer[Int], activationFunction: ActivationFunction ): Unit

  def train(classificationData: ListBuffer[ClassificationData], nrEpochs: Int, error: Double, learningRate: Double, alfa: Double, layerCount: ListBuffer[Int], activationFunction: ActivationFunction ): Unit = {
    stop = false
    trainContinue(classificationData,nrEpochs,error,learningRate, alfa,layerCount,activationFunction)
  }

  def recognize(data: ClassificationData): ClassificationData

  def afterIteration(callback: PartialFunction[Any,Unit]): Unit = this.callback = callback

  def stopTrain(): Unit = this.stop = true

  protected def doAfterIteration(params: Any): Unit = {
    if(this.callback != null){
      this.callback(params)
    }
  }

}
