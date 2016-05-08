package presentationAlgorithm

import _root_.presentationAlgorithm.presentationAlgorithm.PresentationView

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage

/**
  * Created by Dragos on 06.05.2016.
  */
object Main extends JFXApp{

  this.stage = new PrimaryStage{
    scene = new PresentationView
  }

}
