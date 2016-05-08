package algorithm.domain

import scala.util.Random

/**
  * Created by Dragos on 06.05.2016.
  */
class ClassificationData {

  var name: String = null
  val target: Double = new Random().nextDouble()
  var data: Array[Double] = null
}
