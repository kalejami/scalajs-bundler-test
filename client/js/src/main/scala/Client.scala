import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.document

object Client {

  private val component = ScalaComponent
    .builder[Unit]("TestComponent")
    .renderStatic(
      <.div(
        <.h1("Hello World"),
        ReactSelectComponent.ofStrings(None, List("A", "B", "C"), _ => Callback.empty)
      )
    )
    .build

  def main(args: Array[String]): Unit = {
    component().renderIntoDOM(document.getElementById("app"))
  }
}
