package algorithm.domain

import scala.util.Random

/**
  * Created by Dragos on 06.05.2016.
  */
class ClassificationData {

  var name: String = null
  var target: Array[Double] = null
  var data: Array[Double] = null


  override def toString = s"ClassificationData(target = ${target.mkString(", ")}, data = ${data.mkString(", ")})"
}
