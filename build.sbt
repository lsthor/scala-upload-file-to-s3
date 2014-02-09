name := "Upload-File-To-S3"
 
version := "1.0"
 
// add repository for the library
resolvers ++= Seq(
	"jets3t" at "http://www.jets3t.org/maven2"
)
 
//specify which Scala version we are using in this project.
scalaVersion := "2.10.3"

// library we use to upload files to S3
libraryDependencies ++= Seq(
"net.java.dev.jets3t" % "jets3t" % "0.9.0"
)