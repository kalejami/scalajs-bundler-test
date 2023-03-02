name := "scalajs-bundler-test"
version := "0.1"
fork := true

val http4sVersion = "0.23.18"
val reactVersion = "18.2.0"

val sharedSettings = Seq(
  scalaVersion := "2.13.10",
  scalacOptions += "-language:higherKinds",
  scalacOptions += "-Ymacro-annotations"
)

lazy val server =
  crossProject(JVMPlatform)
    .enablePlugins(WebScalaJSBundlerPlugin)
    .settings(
      scalaJSProjects := Seq(client.js),
      Assets / pipelineStages := Seq(scalaJSPipeline),
      sharedSettings,
      libraryDependencies ++= Seq(
        "org.http4s" %% "http4s-dsl" % http4sVersion,
        "org.http4s" %% "http4s-ember-server" % http4sVersion
      )
    )

lazy val client =
  crossProject(JSPlatform)
    .enablePlugins(ScalaJSBundlerPlugin)
    .settings(
      scalaJSUseMainModuleInitializer := true,
      webpackBundlingMode := BundlingMode.LibraryOnly(),
      sharedSettings,
      libraryDependencies ++= Seq(
        "com.github.japgolly.scalajs-react" %%% "core" % "2.1.1"
      ),
      useYarn := true,
      Compile / npmDependencies ++= Seq(
        "react" -> reactVersion,
        "react-dom" -> reactVersion,
        "react-select" -> "5.7.0"
      )
    )
