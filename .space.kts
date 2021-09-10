job("Build restaurant-service") {
    container(displayName = "Run mvn", image = "maven:3.8.2-openjdk-16") {
        shellScript {
            content = """
	            mvn clean install
            """.trimIndent()
        }

        service("mongo:5.0.2") {
            alias("mongodb")
        }
    }
}