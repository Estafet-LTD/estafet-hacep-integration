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

package com.hacep.commands;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import org.infinispan.Cache;

import com.hacep.rest.RestInterface;

import it.redhat.hacep.HACEP;
import it.redhat.hacep.model.Key;

public class InfoInterfaceRequest implements InterfaceRequest {

	private static final String COMMAND_NAME = "info";

    private final HACEP application;

    @Inject
    public InfoInterfaceRequest(HACEP application) {
        this.application = application;
    }

    @Override
    public String command() {
        return COMMAND_NAME;
    }

    @Override
    public boolean execute(RestInterface console, Iterator<String> args) throws IllegalArgumentException {
        try {
            String cacheName = args.next();
            Cache<Key, Object> cache = application.getCacheManager().getCache(cacheName, false);

            if (cache != null) {
                console.println(buildInfo(cache));
            } else {
                console.println("Cache " + cacheName + " not existent");
            }

        } catch (NoSuchElementException e) {
            console.println(application.info());
        }
        return true;

    }

    private String buildInfo(Cache<Key, Object> cache) {

        StringBuilder info = new StringBuilder();

        info.append("Cache: ").append(cache).append("\n");
        info.append("Cache Mode: ").append(cache.getCacheConfiguration().clustering().cacheModeString()).append("\n");
        info.append("Cache Size: ").append(cache.size()).append("\n");
        info.append("Cache Status: ").append(cache.getStatus()).append("\n");
        info.append("Number of Owners: ").append(cache.getAdvancedCache().getDistributionManager().getConsistentHash().getNumOwners()).append("\n");
        info.append("Number of Segments: ").append(cache.getAdvancedCache().getDistributionManager().getConsistentHash().getNumSegments()).append("\n");

        return info.toString();
    }

    @Override
    public void usage(RestInterface console) {
        console.println(COMMAND_NAME + " [<cache>]");
        console.println("\t\tGeneral information or specific information on cache.");
    }
}
