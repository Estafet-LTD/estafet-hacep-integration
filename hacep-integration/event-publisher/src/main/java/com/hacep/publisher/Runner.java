/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hacep.publisher;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Runner {

    private ScheduledExecutorService scheduler = null;

    /*private final String BROKER_URL = "failover:(tcp://amq1:61616,tcp://amq2:61616,tcp://amq3:61616,tcp://localhost:61616)";*/
    private final String BROKER_URL = "failover:(tcp://localhost:61616)";
    private final String QUEUE_NAME = "facts";
    private final String BROKER_USERNAME = "admin";
    private final String BROKER_PASSWORD = "admin";

    private final Integer CONCURRENT_PURCHASERS = 5;
    private final Integer DURATION = 15;
    private final Integer EVENT_DELAY = 15;
    private final Integer EVENT_INTERVAL = 5;
    private final Integer MAX_ACTIVE_SESSIONS = 250;
    private final Integer POOL_SIZE = 8;


    public Runner() {
        scheduler = Executors.newScheduledThreadPool(CONCURRENT_PURCHASERS);
    }

    public static void main(String[] args) throws Exception {
        new Runner().produce();
    }

    private void produce() throws InterruptedException {

        ActiveMQConnectionFactory amqConnectionFactory;

        amqConnectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        amqConnectionFactory.setUserName(BROKER_USERNAME);
        amqConnectionFactory.setPassword(BROKER_PASSWORD);

        PooledConnectionFactory connectionFactory = new PooledConnectionFactory(amqConnectionFactory);
        connectionFactory.setMaxConnections(POOL_SIZE);
        connectionFactory.setMaximumActiveSessionPerConnection(MAX_ACTIVE_SESSIONS);

        for (int i = 0; i < CONCURRENT_PURCHASERS; i++) {

            int delay = (int) (Math.random() * EVENT_DELAY);

            final ScheduledFuture<?> playerHandle = scheduler
                    .scheduleAtFixedRate(
                            new PurchaseRunner(
                                    new PurchaseProducer(connectionFactory, QUEUE_NAME, i)),
                                        delay,
                                        EVENT_INTERVAL,
                                        TimeUnit.SECONDS);

            scheduler.schedule(() -> playerHandle.cancel(true), DURATION, TimeUnit.MINUTES);
        }
    }

    private class PurchaseRunner implements Runnable {

        private final PurchaseProducer purchaseProducer;
        private int id = 10000;

        public PurchaseRunner(PurchaseProducer purchaseProducer) {
            this.purchaseProducer = purchaseProducer;
        }

        @Override
        public void run() {
            purchaseProducer.produce(id++, System.currentTimeMillis());
        }
    }
}