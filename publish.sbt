publishTo  := Some("Artifactory Realm" at "https://artifacts.kpn.org/dsh-iuc-local")
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

organizationName in publish := "kpn"
organizationHomepage in publish := Some(url("http://kpn.com/"))

pomIncludeRepository in publish := { _ => false }
publishMavenStyle in publish := true
