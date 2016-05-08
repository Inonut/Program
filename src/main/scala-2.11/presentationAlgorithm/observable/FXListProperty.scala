package presentationAlgorithm.observable

import java.util
import javafx.beans.property.SimpleListProperty
import javafx.collections.{FXCollections, ObservableList}

import presentationAlgorithm.util.Util

/**
  * Created by Dragos on 14.04.2016.
  */
class FXListProperty[E] extends SimpleListProperty[E](FXCollections.observableArrayList()){

  override def clear(): Unit = Util.fxThread{super.clear()}

  override def add(element: E): Boolean =  Util.fxThread[Boolean] { super.add(element) }

  override def addAll(elements: util.Collection[_ <: E]): Boolean = Util.fxThread[Boolean] { super.addAll(elements) }

  override def addAll(i: Int, elements: util.Collection[_ <: E]): Boolean = Util.fxThread[Boolean] { super.addAll(i, elements) }

  override def addAll(elements: E*): Boolean = {
    var elems = java.util.Arrays.asList(elements: _*)
    addAll(elems)
  }

  override def set(newValue: ObservableList[E]): Unit = Util.fxThread { super.set(newValue) }

}
