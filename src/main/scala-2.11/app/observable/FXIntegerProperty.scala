package app.observable

import javafx.beans.property.SimpleIntegerProperty

import presentationAlgorithm.util.Util

/**
  * Created by Dragos on 14.04.2016.
  */
class FXIntegerProperty(initialValue: Int) extends SimpleIntegerProperty(initialValue){

  def this() = this(0)

  override def set(newValue: Int): Unit = Util.fxThread{ super.set(newValue)}

  override def get(): Int = Util.fxThread{ super.get()}
}
