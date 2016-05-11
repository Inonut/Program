package app.gui

import app.mvc.IModel
import presentationAlgorithm.observable.FXObjectProperty

import scalafx.scene.canvas.{Canvas, GraphicsContext}

/**
  * Created by Dragos on 11.05.2016.
  */
class AppModel extends IModel{

  val graphicsContext2D = new FXObjectProperty[GraphicsContext]
  val canvas = new FXObjectProperty[Canvas]

}
