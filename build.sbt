name := "githubot"

version := "0.2-SNAPSHOT"

resolvers ++= Seq(
  "twitter4j repository" at "http://twitter4j.org/maven2"
)

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

resolvers += Opts.resolver.sonatypeReleases

scalaVersion := "2.10.4"

val twitter4jVersion = "4.0.2"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % twitter4jVersion
 ,"io.argonaut" %% "argonaut" % "6.1-M3"
 ,"com.twitter" %% "util-eval" % "6.3.6"
 ,"org.scalaj"  %% "scalaj-http" % "0.3.14"
)

scalacOptions += "-deprecation"

assemblySettings


AssemblyKeys.jarName in AssemblyKeys.assembly := {
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
  s"${name.value}-${df.format(new java.util.Date)}-twitter4j-${twitter4jVersion}.jar"
}

sourcesInBase := false
