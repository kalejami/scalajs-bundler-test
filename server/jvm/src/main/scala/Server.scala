
import java.util.concurrent.Executors

import cats.effect.{Blocker, ExitCode, IO, IOApp}
import org.http4s._
import org.http4s.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.impl.Root
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.http4s.server.staticcontent.WebjarService

import scala.concurrent.ExecutionContext

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
    val blockerContext = Blocker.liftExecutionContext(ExecutionContext.fromExecutor(Executors.newCachedThreadPool()))
    BlazeServerBuilder[IO](ExecutionContext.global)
      .bindHttp(8888, "localhost")
      .withHttpApp(
        Router[IO](
          "/" -> indexRouter,
          "/webjars" -> WebjarService[IO](WebjarService.Config[IO](blockerContext))
        ).orNotFound
      )
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
