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

package com.hacep.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hacep.commands.InterfaceRequest;
import com.hacep.commands.InterfaceRequestComparator;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RestInterface {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestInterface.class);

    @Inject
    private Instance<InterfaceRequest> commands;

    private List<Object> content = new ArrayList<>();

    public void print(Object message) {
        content.add(message);
    }

    public void println(Object message) {
        content.add(message);
        content.add("\n");
    }

    public void print(String message) {
        content.add(message);
    }

    public void println(String message) {
        content.add(message);
        content.add("\n");
    }

    public void println() {
        content.add("\n");
    }

    public void printUsage() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Start print usage");
        }
        StreamSupport.stream(commands.spliterator(), true)
                .sorted(new InterfaceRequestComparator())
                .forEachOrdered(c -> {
                    c.usage(this);
                    println();
                    println();
                });
    }

    public String toString() {
        return content.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public void start() throws IOException {
        throw new IllegalStateException();
    }

    public RestInterface register(InterfaceRequest cmd) {
        throw new IllegalStateException();
    }

    public Optional<InterfaceRequest> findByName(String name) {
        return StreamSupport.stream(commands.spliterator(), false)
                .filter(c -> name.equalsIgnoreCase(c.command()))
                .findFirst();
    }

    public Object getContent() {
        if (content.size() == 1) {
            return content.get(0);
        }
        return this.toString();
    }

    public void clear() {
        content.clear();
    }
}
