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

package com.hacep.model;

import it.redhat.hacep.model.Fact;
import it.redhat.hacep.model.Key;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class Purchase implements Fact {

    private static final long serialVersionUID = 7517352753296362943L;

    protected long id;

    protected Long customerId;

    protected Date timestamp;

    protected Double amount;

    public Purchase(long id, Long customerId, Date timestamp, Double amount) {
        this.id = id;
        this.customerId = customerId;
        this.timestamp = timestamp;
        this.amount = amount;
    }

    @Override
    public Instant getInstant() {
        return timestamp.toInstant();
    }

    @Override
    public Key extractKey() {
        return new PurchaseKey(String.valueOf(id), String.valueOf(customerId));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase)) return false;
        Purchase purchase = (Purchase) o;
        return id == purchase.id &&
                Objects.equals(customerId, purchase.customerId) &&
                Objects.equals(timestamp, purchase.timestamp) &&
                Objects.equals(amount, purchase.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, timestamp, amount);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", timestamp=" + timestamp +
                ", amount=" + amount +
                '}';
    }
}
