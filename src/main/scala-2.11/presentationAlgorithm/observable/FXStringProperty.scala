package presentationAlgorithm.observable

import javafx.beans.property.SimpleStringProperty

import presentationAlgorithm.util.Util
/**
  * Created by Dragos on 13.04.2016.
  */
class FXStringProperty extends SimpleStringProperty{
  override def set(newValue: String): Unit = Util.fxThread{ super.set(newValue)}
}
