package presentationAlgorithm.presentationAlgorithm

import javafx.scene.layout.Border

import algorithm.classification.Classification
import algorithm.classification.impl.{BackPropagationMethod, PerceptronMethod}
import algorithm.function.impl.SignoidFunction
import presentationAlgorithm.mvc.IView
import presentationAlgorithm.observable.FXListProperty
import presentationAlgorithm.util.Util

import scala.async.Async._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.ListBuffer
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, Pane, VBox}
import scalafx.scene.paint.Color

/**
  * Created by Dragos on 06.05.2016.
  */
class PresentationView extends Scene with IView{

  val controller = new PresentationController
  val model = new PresentationModel


  private val pane = new Pane{
    style = "" +
      "-fx-border-color: #2e8b57;" +
      "-fx-border-width: 2px;"

    //bind
    model.width <==> maxWidth
    model.height <==> maxHeight
    model.width <==> minWidth
    model.height <==> minHeight
    model.points set children


    //add action
    onMouseClicked = (event: MouseEvent) =>async {
      controller.addPointTo(model.points, event.x, event.y, model.colorSelected.getValue)
    } onFailure Util.defaultError

    //init:
    maxWidth = 500
    maxHeight = 300
    minWidth = 500
    minHeight = 300
  }

  private val startButton = new Button{

    //addAction
    onAction = (e: ActionEvent) => async { /*controller.startTrain(
      model.points,
      model.selectedMethod.getValue,
      1000000,
      0.01,
      java.lang.Double.parseDouble(model.learnRateValue.getValue),
      java.lang.Double.parseDouble(model.alfaValue.getValue),
      ListBuffer(2,Integer.parseInt(model.hiddenLayerValue.getValue)),
      new Signoid,
      model.width.value,
      model.height.value,
      model.errorValue,
      model.epochValue)*/

      controller.startTrain(model)
    } onFailure Util.defaultError


    //init
    text = "Start"
  }

  private val stopButton = new Button{
    text = "Stop"
    onAction = (e: ActionEvent) => async {
      controller.stopTrain(model)
    } onFailure Util.defaultError
  }

  private val clearButton = new Button{
    text = "Clear"
    onAction = (e: ActionEvent) => async {
      /*controller.stopTrain(model.selectedMethod.getValue)
      controller.clearPane(model.points)*/
      controller.clearPane(model)
    } onFailure Util.defaultError
  }

  private val clearAllButton = new Button{
    text = "ClearAll"
    onAction = (e: ActionEvent) => async {
      /*controller.stopTrain(model.selectedMethod.getValue)
      controller.clearPane(model.points)*/
      controller.clearAllPane(model)
    } onFailure Util.defaultError
  }

  private val colorPiker = new ColorPicker{
    model.colorSelected <==> value

    //init:
    value = Color.Red
  }

  private val choiceBox = new ChoiceBox[Classification]{
    value <==> model.selectedMethod

    //init:
    items = new FXListProperty[Classification]{this.addAll(new BackPropagationMethod/*, new PerceptronMethod*/)}
    selectionModel.value.selectFirst()
  }

  private val hiddenLayer = new TextField{
    model.hiddenLayerValue <==> text

    //init:
    text = "30"
  }

  private val learnRate = new TextField{
    model.learnRateValue <==> text

    //init:
    text = "0.8"
  }

  private val alfa = new TextField{
    model.alfaValue <==> text

    //init:
    text = "1E-6"
  }

  private val epochValue = new TextField{
    text <==> model.epochValue
  }

  private val errorValue = new TextField{
    text <==> model.errorValue
  }

  root = new VBox{
    children = List(
      pane,
      new HBox{
        children = List(
          startButton,
          stopButton,
          clearButton,
          clearAllButton,
          colorPiker,
          choiceBox
        )
      },
      new HBox{
        children = List(
          new Label("Strat Ascuns"),
          hiddenLayer,
          new Label("Rata de Invatare"),
          learnRate,
          new Label("Alfa"),
          alfa
        )
      },
      new HBox{
        children = List(
          new Label("Epoca"),
          epochValue,
          new Label("Eroare"),
          errorValue
        )
      }

    )
  }
}
