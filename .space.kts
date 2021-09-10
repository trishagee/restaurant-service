job("Build restaurant-service") {
    container(displayName = "Run mvn package", image = "maven:3.8.2-openjdk-16") {
        shellScript {
            content = """
	            mvn clean package
                mkdir $mountDir/share/target/
                cp -r target/*.jar $mountDir/share/target
            """.trimIndent()
        }

        service("mongo:5.0.2") {
            alias("mongodb")
        }
    }
}