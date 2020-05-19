name := "scalajs-bundler-test"
version := "0.1"
fork := true

val http4sVersion = "0.21.4"
val reactVersion = "16.13.1"

val sharedSettings = Seq(
  scalaVersion := "2.13.2",
  scalacOptions += "-language:higherKinds",
  scalacOptions += "-Ymacro-annotations"
)

lazy val server =
  crossProject(JVMPlatform)
    .enablePlugins(WebScalaJSBundlerPlugin)
    .settings(
      scalaJSProjects := Seq(client.js),
      pipelineStages in Assets := Seq(scalaJSPipeline),
      sharedSettings,
      libraryDependencies ++= Seq(
        "org.http4s" %% "http4s-dsl" % http4sVersion,
        "org.http4s" %% "http4s-blaze-server" % http4sVersion
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
        "com.github.japgolly.scalajs-react" %%% "core" % "1.7.0"
      ),
      npmDependencies in Compile ++= Seq(
        "react" -> reactVersion,
        "react-dom" -> reactVersion,
        "react-select" -> "3.1.0"
      )
    )