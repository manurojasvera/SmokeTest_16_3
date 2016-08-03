name := "SmokeTest_16_3"

version := "1.0"

scalaVersion := "2.11.8"

/**
  * DEPENDENCIES
  */
val scalaTestVersion = "2.2.6"
val scalaTestPlusPlayVersion = "1.4.0"
val seleniumVersion = "2.35.0"
val akkaVersion = "2.4.4"
val pegdownVersion = "1.1.0"
val logbackVersion = "1.1.7"

libraryDependencies ++= Seq(
  "org.pegdown"             % "pegdown"                             % pegdownVersion,
  "ch.qos.logback"          % "logback-classic"                     % logbackVersion,
  "com.typesafe.akka"       %% "akka-testkit"                       % akkaVersion,
  "com.typesafe.akka"       %% "akka-http-spray-json-experimental"  % akkaVersion,
  "org.scalatestplus"       %% "play"                               % scalaTestPlusPlayVersion,
  "org.scalatest"           %% "scalatest"                          % scalaTestVersion,
  "org.seleniumhq.selenium" % "selenium-java"                       % seleniumVersion
)

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.1.2"
    