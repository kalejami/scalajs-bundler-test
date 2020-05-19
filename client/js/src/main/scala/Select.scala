import japgolly.scalajs.react.{Callback, Children, JsComponent}
import japgolly.scalajs.react.component.Js.{RawMounted, UnmountedWithRawType}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-select", JSImport.Default)
object Select extends js.Object

// See https://react-select.com/props#select-props for a list of props
// supported by React Select.
trait Props extends js.Object {
  val value: js.Dictionary[js.Any]
  val options: js.Array[js.Dictionary[js.Any]]
  val onChange: js.Function1[js.Dictionary[js.Any], Unit]
  val isSearchable: Boolean
  val name: String
}

object ReactSelectComponent {

  private val component = JsComponent[Props, Children.None, Null](Select)

  def ofStrings(
                 preselected: Option[String],
                 items: List[String],
                 onChangeEvent: String => Callback
               ): UnmountedWithRawType[Props, Null, RawMounted[Props, Null]] =
    of[String](preselected, items, identity, onChangeEvent)

  def of[A](
             preselected: Option[A],
             items: List[A],
             label: A => String,
             onChangeEvent: A => Callback
           ): UnmountedWithRawType[Props, Null, RawMounted[Props, Null]] = {
    val valueKey = "value"
    def toOption(a: A): js.Dictionary[js.Any] =
      js.Dictionary("label" -> label(a), valueKey -> a.asInstanceOf[js.Any])

    val props = new Props {
      override val value: js.Dictionary[js.Any] =
        preselected.map(toOption).orNull

      override val options: js.Array[js.Dictionary[js.Any]] =
        js.Array(items.map(toOption): _*)

      override val onChange: js.Function1[js.Dictionary[js.Any], Unit] =
        selected =>
          Option(selected)
            .flatMap(_.get(valueKey).map(_.asInstanceOf[A]))
            .foreach(a => onChangeEvent(a).runNow())

      override val isSearchable: Boolean = true

      override val name: String = "searchable"
    }
    component(props)
  }
}
