package algorithm

import classification.impl.BackPropagationMethod
import domain.ClassificationData
import function.impl.Signoid

import scala.collection.mutable.ListBuffer

/**
  * Created by Dragos on 07.05.2016.
  */
object Main {

  def main(args: Array[String]) {

    /*val c = new BackPropagationMethod

    c.createNet(ListBuffer(2,3,2), ListBuffer("1","2"))
    c.train(ListBuffer(
      new ClassificationData{name = "1"; data = Array(1, 9)},
      new ClassificationData{name = "1"; data = Array(2, 9)},
      new ClassificationData{name = "1"; data = Array(1, 8)},
      new ClassificationData{name = "2"; data = Array(8, 1)},
      new ClassificationData{name = "2"; data = Array(8, 2)},
      new ClassificationData{name = "3"; data = Array(5, 5)}),
      1000, 0.01, ListBuffer(2,12), new Signoid)

    c.recognize(new ClassificationData{name = "2"; data = Array(6,4)})*/

    val c = new BackPropagationMethod

    val input = ListBuffer(
      new ClassificationData{name = "1"; data = Array(0.102, 0.167); target = Array(0.5)},
      new ClassificationData{name = "1"; data = Array(0.382, 0.52); target = Array(0.5)},
      new ClassificationData{name = "1"; data = Array(0.244, 0.224); target = Array(0.8)},
      new ClassificationData{name = "1"; data = Array(0.149, 0.28); target = Array(0.8)})

    c.train(input,
      10000, 0.01, 0.25, 0.0001, ListBuffer(2,20), new Signoid)

    input.foreach(data => {
      c.recognize(data).target.foreach(t=>print(t+" "))
      println()
    })

  }
}
