package app.observable

import javafx.beans.property.SimpleObjectProperty

import presentationAlgorithm.util.Util


/**
  * Created by Dragos on 13.04.2016.
  */
class FXObjectProperty[T] extends SimpleObjectProperty[T]{
  override def set(newValue: T): Unit = Util.fxThread { super.set(newValue)}

  override def getValue: T = Util.fxThread { super.getValue}
}


