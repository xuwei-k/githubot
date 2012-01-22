name := "githubot"

resolvers ++= Seq(
  "twitter repository"   at "http://maven.twttr.com"
 ,"twitter4j repository" at "http://twitter4j.org/maven2"
)

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "2.2.5"
 ,"com.twitter" %% "util-eval" % "1.12.9"
 ,"org.specs2"  %% "specs2" % "1.7.1" % "test"
)

scalacOptions += "-deprecation"

seq(assemblySettings: _*)

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.9.0" % "0.2.7")

scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

