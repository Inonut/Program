package algorithm.domain

import scala.collection.mutable.ListBuffer

/**
  * Created by Dragos on 06.05.2016.
  */
class Layer {

  var neurons = new ListBuffer[Neuron]
  var weights = new ListBuffer[Weight]
}
