package com.jetbrains.test;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class MongoDBContainerExtension implements AfterAllCallback, BeforeAllCallback {
    private MongoDBContainer mongoDBContainer;

    private boolean isSpaceAutomation() {
        return System.getenv("JB_SPACE_API_URL") != null;
    }

    @Override
    public void beforeAll(ExtensionContext context) {

        if (isSpaceAutomation()) {
            // When running in Space Automation, a service container "mongodb" will be running
            System.setProperty("spring.data.mongodb.host", "mongodb");
            return;
        }

        // When running locally, spin up a test container
        mongoDBContainer = new MongoDBContainer("mongo:5.0.2");
        mongoDBContainer.start();
        mongoDBContainer.waitingFor(Wait.forListeningPort()
                                        .withStartupTimeout(Duration.ofSeconds(180L)));
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (isSpaceAutomation()) return;

        mongoDBContainer.stop();
    }

}
