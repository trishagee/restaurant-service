package com.jetbrains.test;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class MongoDBContainerExtension implements AfterAllCallback, BeforeAllCallback {
    private MongoDBContainer mongoDBContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        mongoDBContainer = new MongoDBContainer("mongo:5.0.2");
        mongoDBContainer.start();
        mongoDBContainer.waitingFor(Wait.forListeningPort()
                                        .withStartupTimeout(Duration.ofSeconds(180L)));
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        mongoDBContainer.stop();
    }

}
