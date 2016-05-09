package presentationAlgorithm.presentationAlgorithm

import java.util.function.Consumer
import java.util.stream.Collectors
import javafx.scene.Node

import algorithm.classification.Classification
import algorithm.domain.ClassificationData
import algorithm.function.ActivationFunction
import algorithm.function.impl.Signoid
import presentationAlgorithm.mvc.IController
import presentationAlgorithm.observable.{FXListProperty, FXObjectProperty}

import scala.collection.mutable.ListBuffer
import scalafx.scene.paint.{Color, Paint}
import scalafx.scene.shape.Circle
import scalafx.Includes._
import scala.async.Async._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Dragos on 06.05.2016.
  */
class PresentationController extends IController{
  def stopTrain(method: Classification): Unit = method.stopTrain()

  def clearPane(points: FXListProperty[Node]): Unit = points.clear()

  def startTrain(points: FXListProperty[Node], method: Classification, epochs: Int, error: Double, learningRate: Double, alfa: Double, layers: ListBuffer[Int], function: ActivationFunction, width: Int, height: Int, errorValue: FXObjectProperty[String], epochValue: FXObjectProperty[String]): Unit = {

    val sfxPoints: ListBuffer[Circle] = points.map(node => new Circle(node.asInstanceOf[javafx.scene.shape.Circle]))(collection.breakOut)
    val classificationData = sfxPoints.map(point=> new ClassificationData{name = point.fill.value.toString; data = Array(point.centerX.value, point.centerY.value)})

    //init
    clearPane(points)
    for(i <- 1 until width by 5; j <- 1 until height by 5){
      points.add(getPoint( i, j, Color.White))
    }
    sfxPoints.foreach(node => points.add(node))

    method.afterIteration{
      case (iter: Int, totalError: Double) =>
        println(iter + " " + totalError)
        errorValue.set(totalError.toString)
        epochValue.set(iter.toString)
        if(iter % 100 == 0 ){
          points.map(node => new Circle(node.asInstanceOf[javafx.scene.shape.Circle]))(collection.breakOut)
                .filter(node => !sfxPoints.contains(node))
                .foreach(point => point.fill = Color.valueOf(method.recognize(new ClassificationData{name = point.fill.value.toString; data = Array(point.centerX.value,point.centerY.value)}).name))

        }

    }
    method.train(classificationData, epochs, error, learningRate, alfa, layers, function)
  }

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
