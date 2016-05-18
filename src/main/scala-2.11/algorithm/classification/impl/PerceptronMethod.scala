package algorithm.classification.impl

import algorithm.classification.Classification
import algorithm.domain.ClassificationData
import algorithm.function.ActivationFunction

import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
  * Created by Dragos on 06.05.2016.
  */
class PerceptronMethod extends Classification{

  private var data: Map[String, Array[Double]] = null

  override protected def createNet(layerCount: Array[Int]): Unit = {
    data = Map()
  }

  override def recognize(input: ClassificationData): ClassificationData = {

    val nameResult = data.maxBy(elem => (elem._2, 1.0 +:input.data).zipped.map((w, i) => w * i).sum)._1

    new ClassificationData{
      this.name = nameResult
    }
  }

  override protected def clearNet(): Unit = {
    data = Map()
    nbIter = 0
  }

  def makeData(name: String, classificationData: Array[ClassificationData]): Array[Array[Double]] = {

    var data = Array[Array[Double]]()

    classificationData.filter(cd => cd.name.equals(name)).foreach(cd => {
      var elem = Array(1.0)
      cd.data.foreach(d => elem :+= d)
      data :+= elem
    })
    classificationData.filter(cd => !cd.name.equals(name)).foreach(cd => {
      var elem = Array(-1.0)
      cd.data.foreach(d => elem :+= -d)
      data :+= elem
    })

    data
  }

  var nbIter: Int = 0

  def getBestWeight(name: String, inputs: Array[Array[Double]]): Array[Double] = {

    var weight = Array[Double]()
    inputs.head.foreach(d => weight :+= Random.nextDouble() * 2.0 - 1.0)

    var ok = false
    for (i <-0 until this.nrEpochs if !ok) {
      nbIter += 1
      ok = true
      inputs.foreach( input => {
        if((weight, input).zipped.map((w,i) => w*i).sum <= 0){
          ok = false
          //println(weight.mkString(","))
          weight = (weight, input).zipped.map((w,i) => w+i)
        }
      })
    }

    weight
  }


  override protected def trainContinue(classificationData: Array[ClassificationData]): Unit = {

    clearNet()

    val names = classificationData.map(cd => cd.name).distinct

    names.zipWithIndex.foreach{
      case (name: String, i: Int) =>

        println(name)

        var dataResult = makeData(name, classificationData)
        data += name -> getBestWeight(name, dataResult)

        doAfterIteration( nbIter, 0.0)
        Thread.sleep(2000)
    }
  }


  override def toString = s"PerceptronMethod()"
}
