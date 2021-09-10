job("Build restaurant-service") {
    container(displayName = "Run mvn package", image = "maven:3.8.2-openjdk-16") {
        shellScript {
            content = """
	            mvn clean package
            """.trimIndent()
        }

        service("mongo:5.0.2") {
            alias("mongodb")
        }
    }
}