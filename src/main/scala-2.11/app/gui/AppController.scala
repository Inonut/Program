package app.gui

import java.io.File
import java.nio.file.Paths
import java.util

import algorithm.classification.Classification
import algorithm.classification.impl.BackPropagationMethod
import algorithm.domain.ClassificationData
import app.domain.FileImage
import app.mvc.IController
import app.util.{BlockUI, Util}

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.input.MouseEvent
import scalafx.Includes._
import scala.collection.JavaConversions._

/**
  * Created by Dragos on 11.05.2016.
  */
class AppController extends IController{
  def onStopClick(model: AppModel) = {
    c.stopTrain()
  }

  def onClearClick(model: AppModel) = {
    model.graphicsContext2D.get.clearRect(0, 0, 500, 500)
  }


  var c = new BackPropagationMethod
  var classificationData: Array[ClassificationData] = null

  def detNameFromOutput(data: ClassificationData, classificationData: Array[ClassificationData]) = {
    println(classificationData.minBy(cdata => (cdata.target,data.target).zipped.map((a,b) => Math.abs(a-b)).sum).name)
  }

  def onTestClick(model: AppModel) = {

    var dataImage = Util.prepareFileImage(model.canvas.get.snapshot(null,null))
    var dataInput = new ClassificationData{data = dataImage.getSemnificativeScaledImageTransformated}

    Classification.prepareData(Array(dataInput))



    detNameFromOutput(c.recognize(dataInput), classificationData)
  }


  def onTrainClick(model: AppModel) = {

    val elem= Util.getImagesFrom(new File("F:\\MEGA\\LICENTA\\src\\main\\resources\\images").toPath, 5 )

    classificationData = elem.map(el => new ClassificationData{name = el.getName; data = el.getSemnificativeScaledImageTransformated} )(collection.breakOut)
    Classification.prepareData(classificationData)


    c.layerCount = Array(50)
    c.alfa = 1E-6
    c.learningRate = 0.25
    c.train(classificationData)



    classificationData.foreach(data => {
      c.recognize(data).target.foreach(t=>print(t+" "))
      println()
    })
  }


  def onMouseDraggedCnv(event: MouseEvent, graphicsContext: GraphicsContext) = {
    graphicsContext.lineTo(event.x, event.y)
    graphicsContext.strokePath()
  }

  def onMousePressedCnv(event: MouseEvent, graphicsContext: GraphicsContext) = {
    graphicsContext.beginPath()
    graphicsContext.moveTo(event.x, event.y)
    graphicsContext.strokePath()
  }


}
