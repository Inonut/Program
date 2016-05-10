package algorithm.classification

import algorithm.domain.ClassificationData
import algorithm.function.ActivationFunction
import algorithm.function.impl.SignoidFunction
import presentationAlgorithm.util.Util


/**
  * Created by Dragos on 06.05.2016.
  */
trait Classification {

  private var callback: PartialFunction[Any,Unit] = null

  protected var stop = false

  var activationFunction = new SignoidFunction
  var nrEpochs = 100000
  var error = 0.001
  var learningRate = 0.25
  var alfa = 1E-4
  var layerCount = Array(20)

  protected def createNet(layerCount: Array[Int]): Unit

  protected def clearNet(): Unit

  protected def trainContinue(classificationData: Array[ClassificationData]): Unit

  protected def doAfterIteration(params: Any): Unit = {
    if(this.callback != null){
      this.callback(params)
    }
  }


  private def getInputSize(classificationData: Array[ClassificationData]): Int = {
    val dataLengths = classificationData.map(cData=> cData.data.length)

    val min = dataLengths.min
    val max = dataLengths.max
    if(min != max){
      throw new Exception("Date incorecte")
    }

    min
  }

  private def getOutputSize(classificationData: Array[ClassificationData]): Int = {
    val dataLengths = classificationData.map(cData=> cData.target.length)

    val min = dataLengths.min
    val max = dataLengths.max
    if(min != max){
      throw new Exception("Date incorecte")
    }

    min
  }


  def train(classificationData: Array[ClassificationData]): Unit = {
    stop = false

    layerCount = getInputSize(classificationData) +: layerCount :+ getOutputSize(classificationData)

    trainContinue(classificationData)

  }

  def recognize(data: ClassificationData): ClassificationData

  def stopTrain(): Unit = this.stop = true

  def afterIteration(callback: PartialFunction[Any,Unit]): Unit = this.callback = callback

}

object Classification {
  def prepareData(classificationData: Array[ClassificationData]): Unit = {

    classificationData.groupBy(cdata => cdata.name).foreach(names => {
      val target = Array(Math.random())
      names._2.foreach(d => d.target = target)
    })

    classificationData.foreach(cdata=>cdata.data = cdata.data.map(d => d/1000))

  }
}
