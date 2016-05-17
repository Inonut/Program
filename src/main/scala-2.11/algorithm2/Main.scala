package algorithm2

import algorithm2.domain.{Layer, Network, PropagationTrainer}
import algorithm2.function.impl.SignoidFunction
import sun.security.jgss.spnego.NegTokenInit

/**
  * Created by Dragos on 08.05.2016.
  */
object Main {

  def main(args: Array[String]) {

    var XOR_INPUT = Array(
      Array(0.0,0.0),
      Array(1.0,0.0),
      Array(0.0,1.0),
      Array(1.0,1.0)

    )

    var XOR_IDEAL = Array(
      Array(0.0, 0.0),
      Array(1.0, 1.0),
      Array(1.0, 1.0),
      Array(0.0, 0.0)
    )

    var network = new Network().create(Array(
      new Layer().create(new SignoidFunction, 2, 1),
      new Layer().create(new SignoidFunction, 3, 1),
      new Layer().create(new SignoidFunction, 2, 1)))
    network.randomize()

    var train = new PropagationTrainer().create(network, XOR_INPUT, XOR_IDEAL, 0.2)

    var iteration = 1

    do
    {
      train.iteration()
      var str = "Training Iteration #" + iteration + ", Error: " + train.error
      println(str)
      iteration+=1
    } while( iteration<1000 && train.error>0.01)

    var output = new Array[Double](1)

    for(i<-0 until XOR_INPUT.length){
      network.compute(XOR_INPUT(i),output)
      var str = "Input: " + XOR_INPUT(i)(0) + " ; " + XOR_INPUT(i)(0)  + "   Output: " + output(0) + "   Ideal: " + XOR_IDEAL(i)(0)

      println(str)
    }


  }
}
