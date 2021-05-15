inThisBuild(
  List(
    scalaVersion := "3.0.0",
    name := "tmt-mono",
    version := "1.0"
  )
)

lazy val root = (project in file("."))
  .settings(
    libraryDependencies += ("com.lihaoyi" %% "os-lib" % "0.7.3").cross(CrossVersion.for3Use2_13)
  )
  .dependsOn(ProjectRef(uri("git://github.com/zio/zio-cli.git"), "zioCliJVM"))
