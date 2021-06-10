package com.jetbrains.test;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MongoDBContainer;

public class MongoDBContainerExtension implements BeforeAllCallback, AfterAllCallback {
    private MongoDBContainer mongoDBContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        mongoDBContainer = new MongoDBContainer("mongo:4.4.6");
        mongoDBContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        mongoDBContainer.stop();
    }
}
