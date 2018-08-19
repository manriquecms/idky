class Practice1 {

}

object Practice1 {

  def sqrt(x: Double) = {
    def sqrtIter(guess: Double, x: Double): Double =
      if (isGoodEnough(guess, x)) guess
      else sqrtIter(improve(guess, x), x)

    def improve(guess: Double, x: Double) =
      (guess + x / guess) / 2

    def square(x: Double) = x * x

    def abs(d: Double) = if (d >= 0) d else -d

    def isGoodEnough(guess: Double, x: Double) : Boolean =
      abs(square(guess) - x) < 0.001

    sqrtIter(1.0, x)
  }

  def prueba(x: String) = {
    def prueba2 (x: String, y: String) = s"$x$y"
    prueba2(x,"quetal")
  }

  def main(args: Array[String]): Unit = {
    val builder = new StringBuilder

    val x = { builder += 'x'; 1 }
    lazy val y = { builder += 'y'; 2 }
    def z = { builder += 'z'; 3 }

    z + y + x + z + y + x
    println(builder.result())

//    1.to(10).filterNot(_==2).foreach(i => print(i))
//    print(16.toHexString)
//    println(16.toHexString)
//
//    println((0 to 10).contains(10))
//    println((0 until 10).contains(10))
//
//    println("foo".drop(1))
//    println("bar".take(2))
//    println(0 until 10 foreach(print(_)))
  }






}
