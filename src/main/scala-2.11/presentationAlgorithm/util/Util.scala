package presentationAlgorithm.util

import javafx.{scene => jfxs}

import com.sun.javafx.application.PlatformImpl


/**
  * Created by Dragos on 19.04.2016.
  */
object Util {
  def defaultError: PartialFunction[Throwable, Any] = {
    case err => err.printStackTrace()
  }

  def initToT[T]: T = {
    null.asInstanceOf[T]
  }

  def fxThread[R](op: => R): R = {
    var result: R = initToT
    PlatformImpl.runAndWait(new Runnable {
      override def run(): Unit = result = op
    })
    result
  }

  def default[T](elem1: T, elem2: T): T ={
    if(elem1 != null){
      elem1
    } else {
      elem2
    }
  }
}
