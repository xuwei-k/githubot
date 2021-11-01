name := "githubot"

version := "0.2-SNAPSHOT"

licenses := Seq("MIT License" -> url("https://opensource.org/licenses/mit-license"))

scalaVersion := "2.13.7"

val twitter4jVersion = "4.0.7"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.apache.commons" % "commons-text" % "1.9",
  "com.github.xuwei-k" % "html2image" % "0.1.0",
  "org.twitter4j" % "twitter4j-core" % twitter4jVersion,
  "io.argonaut" %% "argonaut" % "6.3.7",
  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "com.github.sbt" % "junit-interface" % "0.13.2" % "test"
)

val unusedWarnings = "-Ywarn-unused" :: Nil

scalacOptions ++= List(
  "-deprecation",
  "-unchecked",
  "-Xlint",
  "-language:postfixOps",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions"
) ::: unusedWarnings

Seq(Compile, Test).flatMap(c => scalacOptions in (c, console) --= unusedWarnings)

assemblyJarName in assembly := {
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
  s"${name.value}-${df.format(new java.util.Date)}-twitter4j-${twitter4jVersion}.jar"
}

resourceGenerators in Compile += task(
  Seq(baseDirectory.value / "build.sbt")
)

sourcesInBase := false
