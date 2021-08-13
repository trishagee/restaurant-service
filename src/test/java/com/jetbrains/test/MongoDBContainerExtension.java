package com.jetbrains.test;

import org.junit.jupiter.api.extension.Extension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class MongoDBContainerExtension implements Extension {
    private static final MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new MongoDBContainer("mongo:5.0.2");
        mongoDBContainer.start();
        mongoDBContainer.waitingFor(Wait.forListeningPort()
                                        .withStartupTimeout(Duration.ofSeconds(180L)));
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
        System.out.println("*****");
        System.out.println("replica set URL: " + mongoDBContainer.getReplicaSetUrl());
        System.out.println("*****");
    }

//    @Override
//    public void beforeAll(ExtensionContext context) {
//        mongoDBContainer = new MongoDBContainer("mongo:5.0.2");
//        mongoDBContainer.start();
//        mongoDBContainer.waitingFor(Wait.forListeningPort()
//                                        .withStartupTimeout(Duration.ofSeconds(180L)));
//        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
//        System.out.println("*****");
//        System.out.println("replica set URL: " + mongoDBContainer.getReplicaSetUrl());
//        System.out.println("*****");
//    }
//
//    @DynamicPropertySource
//    static void mongoDbProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
//    }
//
//    @Override
//    public void afterAll(ExtensionContext context) {
//        mongoDBContainer.stop();
//    }

//    @AfterAll
//    static void tearDownAll() {
//        if (!MONGO_DB_CONTAINER.isShouldBeReused()) {
//            MONGO_DB_CONTAINER.stop();
//        }
//    }
}
