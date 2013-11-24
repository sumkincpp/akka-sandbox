name := "Test"
 
version := "1.0"
 
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies  ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "com.typesafe.akka" %% "akka-remote" % "2.2.3",
  "com.typesafe.akka" %% "akka-cluster" % "2.2.3",
  "com.typesafe.akka" %% "akka-contrib" % "2.2.3"
)