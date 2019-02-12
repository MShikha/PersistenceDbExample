name := "PersistenceDbExample"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.19",
  "com.typesafe.akka" %% "akka-stream" % "2.5.19",
  "org.postgresql" % "postgresql" % "42.1.4",
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.19",
  "ch.qos.logback" % "logback-classic" % "1.0.9",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.7",
  "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test",
  "org.iq80.leveldb" % "leveldb" % "0.9",
  "com.github.dnvriend" %% "akka-persistence-jdbc" % "3.4.0",
  "com.typesafe.akka" %% "akka-persistence-query" % "2.5.19"

)