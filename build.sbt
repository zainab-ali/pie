scalaVersion := "3.0.2"

initialCommands / console := """
      |import doodle.core._
      |import doodle.image._
      |import doodle.image.syntax._
      |import doodle.image.syntax.core._
      |import doodle.java2d._
    """.trim.stripMargin

libraryDependencies ++= Seq(
  "org.creativescala" %% "doodle" % "0.9.25",
  "com.disneystreaming" %% "weaver-cats" % "0.6.6" % Test
)
testFrameworks += new TestFramework("weaver.framework.CatsEffect")
