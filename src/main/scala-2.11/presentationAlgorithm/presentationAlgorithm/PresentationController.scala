package presentationAlgorithm.presentationAlgorithm

import java.util.function.Predicate
import javafx.scene.Node

import algorithm.classification.Classification
import algorithm.domain.ClassificationData
import algorithm.function.ActivationFunction
import com.sun.javafx.application.PlatformImpl
import presentationAlgorithm.mvc.IController
import presentationAlgorithm.observable.{FXListProperty, FXObjectProperty}
import presentationAlgorithm.util.Util

import scala.collection.mutable.ListBuffer
import scalafx.Includes._
import scalafx.scene.paint.{Color, Paint}
import scalafx.scene.shape.Circle

/**
  * Created by Dragos on 06.05.2016.
  */
class PresentationController extends IController{

  private var sfxPoints: Array[Circle] = Array()

  def clearAllPane(model: PresentationModel): Unit = {
    stopTrain(model)
    model.points.clear()
  }

  def stopTrain(model: PresentationModel): Unit = {
    model.selectedMethod.get.stopTrain()
  }

  def clearPane(model: PresentationModel): Unit = {
    stopTrain(model)

    PlatformImpl.runLater(new Runnable {
      override def run(): Unit = model.points.removeIf(new Predicate[Node] {
        override def test(t: Node): Boolean = !sfxPoints.contains(t)
      })
    })

  }

  def startTrain(model: PresentationModel): Unit = {

    sfxPoints = model.points.map(node => new Circle(node.asInstanceOf[javafx.scene.shape.Circle]))(collection.breakOut)
    val classificationData: Array[ClassificationData] = sfxPoints.map(point=> {
      new ClassificationData{
            name = point.fill.value.toString
            data = Array(point.centerX.value, point.centerY.value)
      }
    })

    /****init***/
    val width = model.width.get
    val height = model.height.get
    clearPane(model.points)
    for(i <- 1 until width by 5; j <- 1 until height by 5){
      model.points.add(getPoint( i, j, Color.White))
    }
    sfxPoints.foreach(node => model.points.add(node))



    Classification.prepareData(classificationData)

    classificationData.foreach(elem=> println(elem.toString))

    /*****/
    val method = model.selectedMethod.get
    method.afterIteration{
      case (iter: Int, totalError: Double) =>
        //println(iter + " " + totalError)
        model.errorValue.set(totalError.toString)
        model.epochValue.set(iter.toString)
        if(iter % 1000 == 0 ){
          model.points.map(node => new Circle(node.asInstanceOf[javafx.scene.shape.Circle]))(collection.breakOut)
              .filter(node => !sfxPoints.contains(node))
              .foreach(point => {

                val data = method.recognize(new ClassificationData{
                  data = Array(point.centerX.value / 1000, point.centerY.value / 1000)
                })
                val color = detNameFromOutput(data, classificationData)
                PlatformImpl.runLater(new Runnable {
                  override def run(): Unit = point.fill = Color.valueOf(color)
                })
              })
        }
    }

    method.layerCount = Array(model.hiddenLayerValue.get.toInt)
    method.alfa = model.alfaValue.get.toDouble
    method.learningRate = model.learnRateValue.get.toDouble
    method.train(classificationData)

  }

  def detNameFromOutput(data: ClassificationData, classificationData: Array[ClassificationData]): String = {
    if(data.target == null){
      data.name
    } else {
      classificationData.minBy(cdata => (cdata.target,data.target).zipped.map((a,b) => Math.abs(a-b)).sum).name
    }
  }

  def stopTrain(method: Classification): Unit = method.stopTrain()

  def clearPane(points: FXListProperty[Node]): Unit = points.clear()


  /*def prepareTarget(classificationData: ListBuffer[ClassificationData]) = {
    classificationData.groupBy(cdata => cdata.name).foreach(names => {
      val target = Array(Math.random())
      names._2.foreach(d => d.target = target)
    })

    classificationData.foreach(cdata=>cdata.data = cdata.data.map(d => Util.convertToSubUnit(d)))
  }
*/


 /* def startTrain(points: FXListProperty[Node], method: Classification, epochs: Int, error: Double, learningRate: Double, alfa: Double, layers: ListBuffer[Int], function: ActivationFunction, width: Int, height: Int, errorValue: FXObjectProperty[String], epochValue: FXObjectProperty[String]): Unit = {

    /*val sfxPoints: ListBuffer[Circle] = points.map(node => new Circle(node.asInstanceOf[javafx.scene.shape.Circle]))(collection.breakOut)
    val classificationData = sfxPoints.map(point=> new ClassificationData{name = point.fill.value.toString; data = Array(point.centerX.value, point.centerY.value)})

    prepareTarget(classificationData)

    //init
    clearPane(points)
    for(i <- 1 until width by 5; j <- 1 until height by 5){
      points.add(getPoint( i, j, Color.White))
    }
    sfxPoints.foreach(node => points.add(node))

    method.afterIteration{
      case (iter: Int, totalError: Double) =>
        println(iter + " " + totalError)
        //errorValue.set(totalError.toString)
        //epochValue.set(iter.toString)
        if(iter % 10 == 0 ){
          points.map(node => new Circle(node.asInstanceOf[javafx.scene.shape.Circle]))(collection.breakOut)
                .filter(node => !sfxPoints.contains(node))
                .foreach(point => {

                  val data = method.recognize(new ClassificationData{
                    name = point.fill.value.toString;
                    data = Array(Util.convertToSubUnit(point.centerX.value), Util.convertToSubUnit(point.centerY.value))
                  })
                  val color = detNameFromOutput(data, classificationData)
                  point.fill = Color.valueOf(color)
                })

        }

    }*/
    //method.train(classificationData)


  }*/

  def addPointTo(points: FXListProperty[Node], x: Double, y: Double, color: Paint): Unit = {

    val point = getPoint(x,y,color)
    point.stroke = Color.Black
    point.strokeWidth = 2
    points.add(point)
  }

  def getPoint(x: Double, y: Double, color: Paint): Circle ={
    new Circle{
      radius = 5
      centerY = y
      centerX = x
      fill = color
    }
  }
}
