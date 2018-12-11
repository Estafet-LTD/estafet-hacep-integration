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

import com.hacep.model.Purchase;

import javax.jms.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class PurchaseProducer {

    private final Connection connection;
    private final Session session;

    private String queueName;
    private long customerId;
    ConnectionFactory connectionFactory;

    private static final Double PURCHASE_MIN = 10.0;
    private static final Double PURCHASE_MAX = 500.0;


    public PurchaseProducer(ConnectionFactory connectionFactory, String queueName, long customerId) {

        this.customerId = customerId;
        this.connectionFactory = connectionFactory;
        this.queueName = queueName;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void produce(Integer id, Long timestamp) {

        try {

            Queue destination = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            Purchase fact = new Purchase(id, customerId, new Date(timestamp),
                    round(ThreadLocalRandom.current().nextDouble(PURCHASE_MIN, PURCHASE_MAX)));

            System.out.println("sending purchase for customer " + customerId + " for amount of " + fact.getAmount());

            ObjectMessage message = session.createObjectMessage(fact);
            message.setStringProperty("JMSXGroupID", String.format("P%05d", customerId));
            message.setIntProperty("JMSXGroupSeq", id);
            
            System.out.println("sending purchase for customer " + customerId + " for amount of " + fact.getAmount()+" JMSXGroupID"+String.format("P%05d", customerId)+" JMSXGroupSeq"+ id);
            
            producer.send(message);

        } catch (Exception e) {
            System.out.println("Caught: " + e);
        }
    }

    public static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}