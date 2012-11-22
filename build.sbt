name := "githubot"

version := "0.2-SNAPSHOT"

resolvers ++= Seq(
  "twitter repository"   at "http://maven.twttr.com"
 ,"twitter4j repository" at "http://twitter4j.org/maven2"
)

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "3.0.1"
 ,"com.twitter" % "util-eval" % "5.3.13"
 ,"org.specs2"  %% "specs2" % "1.12.3" % "test"
 ,"org.scalaj"  %% "scalaj-http" % "0.3.2"
)

scalacOptions += "-deprecation"

assemblySettings

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.9.0" % "0.2.7")

scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

AssemblyKeys.jarName in AssemblyKeys.assembly <<= (name,version){(name,version) =>
  val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
  <x>{name}-{df.format(new java.util.Date)}-{version}.jar</x>.text
}

