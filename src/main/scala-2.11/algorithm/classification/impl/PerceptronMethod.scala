package algorithm.classification.impl

import algorithm.classification.Classification
import algorithm.domain.ClassificationData
import algorithm.function.ActivationFunction

import scala.collection.mutable.ListBuffer

/**
  * Created by Dragos on 06.05.2016.
  */
class PerceptronMethod extends Classification{
  override protected def createNet(layerCount: Array[Int]): Unit = ???

  override def recognize(data: ClassificationData): ClassificationData = ???

  override protected def clearNet(): Unit = ???

  override protected def trainContinue(classificationData: Array[ClassificationData]): Unit = ???
}
