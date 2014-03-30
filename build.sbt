name := "githubot"

version := "0.2-SNAPSHOT"

resolvers ++= Seq(
  "twitter4j repository" at "http://twitter4j.org/maven2"
)

resolvers += Opts.resolver.sonatypeReleases

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "4.0.1"
 ,"io.argonaut" %% "argonaut" % "6.1-M2"
 ,"com.twitter" %% "util-eval" % "6.3.6"
 ,"org.specs2"  %% "specs2" % "2.3.10" % "test"
 ,"org.scalaj"  %% "scalaj-http" % "0.3.14"
)

scalacOptions += "-deprecation"

assemblySettings

AssemblyKeys.jarName in AssemblyKeys.assembly <<= (name, version).map{ (name, version) =>
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
  <x>{name}-{df.format(new java.util.Date)}-{version}.jar</x>.text
}

sourcesInBase := false
