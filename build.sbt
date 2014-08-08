name := "githubot"

version := "0.2-SNAPSHOT"

resolvers ++= Seq(
  "twitter4j repository" at "http://twitter4j.org/maven2"
)

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

resolvers += Opts.resolver.sonatypeReleases

scalaVersion := "2.11.2"

val twitter4jVersion = "4.0.2"

libraryDependencies ++= (
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
  ("org.twitter4j" % "twitter4j-core" % twitter4jVersion) ::
  ("io.argonaut" %% "argonaut" % "6.1-M4") ::
  ("org.scalaj"  %% "scalaj-http" % "0.3.16") ::
  Nil
)

scalacOptions ++= (
  "-deprecation" ::
  "-unchecked" ::
  "-Xlint" ::
  "-language:postfixOps" ::
  "-language:existentials" ::
  "-language:higherKinds" ::
  "-language:implicitConversions" ::
  "-Ywarn-unused" ::
  "-Ywarn-unused-import" ::
  Nil
)

assemblySettings

AssemblyKeys.jarName in AssemblyKeys.assembly := {
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
  s"${name.value}-${df.format(new java.util.Date)}-twitter4j-${twitter4jVersion}.jar"
}

resourceGenerators in Compile += task(
  Seq(baseDirectory.value / "build.sbt")
)

sourcesInBase := false
