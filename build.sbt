
name := "geolocation"
version := "1.0.0"
scalaVersion := "2.12.6"

val slickVersion = "3.3.0"
val akkaHttpVersion= "10.1.7"
val akkaHttpSprayVersion="10.1.7"
val h2Version = "1.4.185"
val dshGbCommonsVersion = "1.0.5"
val logbackClassicVersion = "1.1.2"
val scalaTestVersion       = "3.0.5"
val commonsCsvVersion = "1.5"

resolvers ++= Seq("Apache Releases Repository" at "https://repository.apache.org/content/repositories/releases/",
  Resolver.mavenLocal)



libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor" % "2.5.19",
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpSprayVersion,
  "org.apache.commons" % "commons-csv" % commonsCsvVersion,
  "com.typesafe.slick" %% "slick"          % slickVersion,
  "org.scalatest"     %% "scalatest"            % scalaTestVersion % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "com.typesafe.slick" %% "slick-testkit" % slickVersion % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.23" % Test,
  "org.mockito" % "mockito-all" % "1.9.5" % Test,
  "com.h2database"     % "h2"              % h2Version,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.19",
  "org.slf4j"     % "slf4j-api" % "1.7.25")