/**
  * Created by Dragos on 10.05.2016.
  */
object Test {

  def main(args: Array[String]) {
    var list = List(1,2,3)

    list = 0 +: list :+ 5

    //list = Nil

    //list.foreach(e => println(e))


    var l = Array(1,2,3)

    l = l :+ 4

    l.foreach(e => println(e))

  }
}
