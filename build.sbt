import sbt.Keys.{unmanagedJars, unmanagedSourceDirectories}

name := "org.eevolution.LMX"

version := "1.0"

scalaVersion := "2.12.4"

//scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding" , "utf8")
javaOptions in Compile := Seq ("-Dfile.encoding=UTF8")


resolvers += "Scala Modules" at "https://mvnrepository.com/artifact/org.scala-lang.modules/scala-xml"

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"

val sourceAdempiere = "/Users/e-Evolution/Develop/ADempiere/adempiere"

javaSource in Compile := baseDirectory.value   / "src" / "main" / "java"
unmanagedSourceDirectories in Compile += baseDirectory.value   / "base"
scalaSource in Compile := baseDirectory.value / "src" / "main" / "scala"
scalaSource in Test := baseDirectory.value / "src" / "test" / "scala"

unmanagedClasspath in Compile += file(sourceAdempiere + "/bin")

unmanagedJars in Compile ++= (file(sourceAdempiere + "/zkwebui/WEB-INF/lib") * "*.jar").classpath
unmanagedJars in Compile ++= (file(sourceAdempiere + "/tools/lib") * "*.jar").classpath
unmanagedJars in Compile ++= (file(sourceAdempiere + "/lib") * "*.jar").classpath
unmanagedJars in Compile ++= (file(sourceAdempiere + "/packages") * "*.jar").classpath
unmanagedJars in Compile ++= (file(sourceAdempiere + "/zkpackages") * "*.jar").classpath

unmanagedJars in Runtime ++= (file(baseDirectory.value + "/lib") * "*.jar").classpath

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = true, includeDependency = false)

assemblyJarName in assembly := "LMX.jar"

/*lazy val lmx_jar = (project in file("LMX_jar")).
  settings(
      unmanagedJars in Compile ++= (file(sourceAdempiere + "/zkwebui/WEB-INF/lib") * "*.jar").classpath,
      unmanagedJars in Compile ++= (file(sourceAdempiere + "/tools/lib") * "*.jar").classpath,
      unmanagedJars in Compile ++= (file(sourceAdempiere + "/lib") * "*.jar").classpath,
      unmanagedJars in Compile ++= (file(sourceAdempiere + "/packages") * "*.jar").classpath,
      unmanagedJars in Compile ++= (file(sourceAdempiere + "/zkpackages") * "*.jar").classpath,
      unmanagedJars in Runtime ++= {
      (file(baseDirectory.value + "/lib") * "*.jar").classpath
    },
    unmanagedJars in Test ++= {
      (baseDirectory.value / "lib" ** "*.jar").classpath
    },
    assemblyExcludedJars in assembly := {
      (fullClasspath in assembly).value filter {_.data.getName == "compile-0.1.0.jar"}
    },
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = true, includeDependency = false),
    assemblyJarName in assembly := "LMX.jar",
    TaskKey[Unit]("check") := {
      val process = sys.process.Process("java", Seq("-jar", (crossTarget.value / "LMX.jar").toString))
      val out = (process!!)
      if (out.trim != "hello") sys.error("unexpected output: " + out)
      ()
    }
  )

lazy val lmx_zk = (project in file("LMXzk_jar")).
  settings(
    javaSource in Compile :=  baseDirectory.value   / "zkwebui" / "WEB-INF" / "src" ,
    scalaSource in Compile := baseDirectory.value / "zkwebui" / "WEB-INF" / "scala",
    unmanagedJars in Compile ++= (file(sourceAdempiere + "/zkwebui/WEB-INF/lib") * "*.jar").classpath
    ,
    unmanagedJars in Runtime ++= {
      (file(baseDirectory.value + "/lib") * "*.jar").classpath
    },
    unmanagedJars in Test ++= {
      (baseDirectory.value / "lib" ** "*.jar").classpath
    },
    assemblyExcludedJars in assembly := {
      (fullClasspath in assembly).value filter {_.data.getName == "compile-0.1.0.jar"}
    },
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = true, includeDependency = false),
    assemblyJarName in assembly := "LMXzk.jar",
    TaskKey[Unit]("check") := {
      val process = sys.process.Process("java", Seq("-jar", (crossTarget.value / "LMXzk.jar").toString))
      val out = (process!!)
      if (out.trim != "hello") sys.error("unexpected output: " + out)
      ()
    }
  )*/