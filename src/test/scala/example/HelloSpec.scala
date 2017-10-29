package example

import org.specs2._

class HelloSpec extends Specification { def is = s2"""

 The string greeting should
   contain 5 characters          $e1
   starts with 'hello'           $e2"""

  val greeting = Hello.greeting
  def e1 = greeting must have size(5)
  def e2 = greeting must startWith("hello")
}
