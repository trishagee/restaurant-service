job("Build and publish restaurant-service container") {
    val containerTagVersion = "v0.\$JB_SPACE_EXECUTION_NUMBER"

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

    docker(displayName = "Build and push container") {
        beforeBuildScript {
            content = """
                mkdir target/
                cp -r $mountDir/share/target/*.jar target
            """.trimIndent()
        }
        build {
            context = "."
            file = "./Dockerfile"
            labels["vendor"] = "JetBrains"
        }

        push("registry.jetbrains.team/p/blossom/containers/restaurant-service") {
            tags(containerTagVersion)
        }
    }
}