package app

import app.gui.AppView

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage

/**
  * Created by Dragos on 11.05.2016.
  */
object Main extends JFXApp{

  this.stage = new PrimaryStage{
    scene = new AppView
  }
}
