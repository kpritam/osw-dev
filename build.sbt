scalaVersion := "3.0.0"
name := "tmt-mono"
version := "1.0"

libraryDependencies += ("com.lihaoyi" %% "os-lib" % "0.7.3").cross(CrossVersion.for3Use2_13)

