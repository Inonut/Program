package presentationAlgorithm.presentationAlgorithm

import javafx.scene.Node
import javafx.scene.paint.Color

import algorithm.classification.Classification
import presentationAlgorithm.mvc.IModel
import presentationAlgorithm.observable.{FXIntegerProperty, FXListProperty, FXObjectProperty}


/**
  * Created by Dragos on 06.05.2016.
  */
class PresentationModel extends IModel{


  val points = new FXListProperty[Node]
  val colorSelected = new FXObjectProperty[Color]
  val selectedMethod = new FXObjectProperty[Classification]
  val hiddenLayerValue = new FXObjectProperty[String]
  val learnRateValue = new FXObjectProperty[String]
  val alfaValue = new FXObjectProperty[String]
  val epochValue = new FXObjectProperty[String]
  val errorValue = new FXObjectProperty[String]
  val width = new FXIntegerProperty
  val height = new FXIntegerProperty

}
