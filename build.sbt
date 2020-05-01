name := "githubot"

version := "0.2-SNAPSHOT"

resolvers ++= Seq(
  "twitter4j repository" at "http://twitter4j.org/maven2"
)

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

scalaVersion := "2.12.8"

val twitter4jVersion = "4.0.7"

libraryDependencies ++= (
  ("org.scala-lang" % "scala-compiler" % scalaVersion.value) ::
    ("org.apache.commons" % "commons-text" % "1.8") ::
    ("com.github.xuwei-k" % "html2image" % "0.1.0") ::
    ("org.twitter4j" % "twitter4j-core" % twitter4jVersion) ::
    ("io.argonaut" %% "argonaut" % "6.3.0") ::
    ("org.scalaj" %% "scalaj-http" % "2.4.2") ::
    ("com.novocode" % "junit-interface" % "0.11" % "test") ::
    Nil
)

val unusedWarnings = "-Ywarn-unused" :: Nil

scalacOptions ++= (
  "-deprecation" ::
    "-unchecked" ::
    "-Xlint" ::
    "-language:postfixOps" ::
    "-language:existentials" ::
    "-language:higherKinds" ::
    "-language:implicitConversions" ::
    Nil
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
