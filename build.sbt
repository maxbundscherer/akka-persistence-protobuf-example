name := "akka-persistence-protobuf-example"
version := "0.1"
scalaVersion := "2.12.4"

//Akka Persistence
libraryDependencies += "com.typesafe.akka" %% "akka-persistence" % "2.5.16"

//PostgresSQL
libraryDependencies += "be.wegenenverkeer" %% "akka-persistence-pg" % "0.10.0"

//Trigger ScalaPB (see ./project/protoc.sbt)
PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

//Import-Bug (scalaPB)
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"