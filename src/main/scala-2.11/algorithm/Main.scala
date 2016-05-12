package algorithm

import algorithm.classification.Classification
import algorithm.classification.impl.BackPropagationMethod
import algorithm.domain.ClassificationData


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

    var c = new BackPropagationMethod

   /* var input = Array(
      new ClassificationData{name = "1"; data = Array(0.075, 0.234); target = Array(0.702)},
      new ClassificationData{name = "1"; data = Array(0.076, 0.180); target = Array(0.702)},
      new ClassificationData{name = "1"; data = Array(0.081, 0.105); target = Array(0.702)},
      new ClassificationData{name = "1"; data = Array(0.073, 0.056); target = Array(0.702)},
      new ClassificationData{name = "1"; data = Array(0.226, 0.218); target = Array(0.379)},
      new ClassificationData{name = "1"; data = Array(0.204, 0.114); target = Array(0.379)},
      new ClassificationData{name = "1"; data = Array(0.200, 0.050); target = Array(0.379)},
      new ClassificationData{name = "1"; data = Array(0.390, 0.189); target = Array(0.603)},
      new ClassificationData{name = "1"; data = Array(0.393, 0.133); target = Array(0.603)},
      new ClassificationData{name = "1"; data = Array(0.393, 0.020); target = Array(0.603)})
*/

    var input = Array(
      new ClassificationData{name = "1"; data = Array(65, 154); },
      new ClassificationData{name = "2"; data = Array(221, 148); },
      new ClassificationData{name = "3"; data = Array(333, 141); },
      new ClassificationData{name = "4"; data = Array(463, 145); })

  /*  val input = Array(
      new ClassificationData{name = "1"; data = Array(0, 1); target = Array(0)},
      new ClassificationData{name = "1"; data = Array(1, 0); target = Array(0)},
      new ClassificationData{name = "1"; data = Array(1, 1); target = Array(1)},
      new ClassificationData{name = "1"; data = Array(0, 0); target = Array(1)})
*/
    Classification.prepareData(input)


    c.layerCount = Array(30)
    c.alfa = 1E-6
    c.learningRate = 0.8
    c.error = 1E-3
    c.train(input)

    input :+= new ClassificationData{data = Array(0.383, 0.400);}
    input :+= new ClassificationData{data = Array(0.463, 0.185);}

    input.foreach(data => {
      c.recognize(data).target.foreach(t=>print(t+" "))
      println()
    })



  }
}
