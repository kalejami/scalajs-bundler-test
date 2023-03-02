
import java.util.concurrent.Executors

import cats.effect._
import org.http4s._
import org.http4s.implicits._
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.ember.server._
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.http4s.server.staticcontent.WebjarServiceBuilder
import com.comcast.ip4s._

object Server extends IOApp{

  private val clientPath = """/webjars/server/0.1.0-SNAPSHOT/client-fastopt"""
  val index: String =
    s"""|<!DOCTYPE html>
        |<html lang="de">
        |  <head>
        |    <meta charset="UTF-8">
        |  </head>
        |  <body>
        |    <div id="app"></div>
        |    <script src="$clientPath-library.js"></script>
        |    <script src="$clientPath-loader.js"></script>
        |    <script src="$clientPath.js"></script>
        |  </body>
        |</html>
        |""".stripMargin.trim

  val indexRouter: HttpRoutes[IO] = HttpRoutes.of[IO]{
    case GET -> Root => Ok(index, `Content-Type`(MediaType.text.html, Charset.`UTF-8`))
  }


  override def run(args: List[String]): IO[ExitCode] = {
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8888")
      .withHttpApp(
        Router[IO](
          "/" -> indexRouter,
          "/webjars" -> WebjarServiceBuilder[IO].toRoutes
        ).orNotFound
      )
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
