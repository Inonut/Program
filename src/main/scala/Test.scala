/**
  * Created by Dragos on 10.05.2016.
  */
object Test {

  def main(args: Array[String]) {
    var list = Array(1,2,3)

    asd(list)

    list :+= 5

    list.foreach(println(_))

    println(Math.round(0.45))

  }

  def asd(list: Array[Int]): Unit ={

    //list = list.map(_+2)
  }
}
