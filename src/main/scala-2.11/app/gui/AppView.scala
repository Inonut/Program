package app.gui

import app.mvc.IView

import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.layout.{Pane, VBox}
import scalafx.Includes._
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent

/**
  * Created by Dragos on 11.05.2016.
  */
class AppView extends Scene with IView{

  val controller = new AppController
  val model = new AppModel


  private val trainButton = new Button{

    onMouseClicked = (e: MouseEvent) => controller.onTrainClick(model)

    text = "Train"
  }

  private val testButton = new Button{

    onMouseClicked = (e: MouseEvent) => controller.onTestClick(model)

    text = "Test"
  }

  private val clearButton = new Button{

    onMouseClicked = (e: MouseEvent) => controller.onClearClick(model)

    text = "Clear"
  }

  private val canvas = new Canvas{

    model.graphicsContext2D set graphicsContext2D

    onMousePressed = (e: MouseEvent) => controller.onMousePressedCnv(e, model.graphicsContext2D.get)
    onMouseDragged = (e: MouseEvent) => controller.onMouseDraggedCnv(e, model.graphicsContext2D.get)

    graphicsContext2D.lineWidth = 3
    width = 500
    height = 500
  }
  model.canvas set canvas

  private val paneCanvas = new Pane{
    style = "" +
      "-fx-border-color: #2e8b57;" +
      "-fx-border-width: 2px;"

    maxWidth = 500
    maxHeight = 500
    minWidth = 500
    minHeight = 500

    children = List(
      canvas
    )
  }

  root = new VBox{
    children = List(
      paneCanvas,
      trainButton,
      testButton,
      clearButton
    )
  }
}
