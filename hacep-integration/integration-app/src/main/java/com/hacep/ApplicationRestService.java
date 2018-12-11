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

package com.hacep;


import com.hacep.commands.InterfaceRequest;
import com.hacep.rest.RestInterface;
import com.hacep.util.JDGUtility;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@ApplicationPath("/")
@Path("/")
public class ApplicationRestService extends Application {
	private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationRestService.class);
    @Inject
    private RestInterface restInterface;

    @GET
    @Path("/execute/{command}")
    @Produces("application/json")
    public Response executeCommand(@PathParam("command") String commandName, @QueryParam("params") String params) {
    	LOGGER.info("########################## Rest executeCommand ##########################");
    	try {
            Optional<InterfaceRequest> command = restInterface.findByName(commandName);
            if (command.isPresent()) {
                if (params != null) {
                    command.get().execute(restInterface, Arrays.asList(params.split(",")).iterator());
                } else {
                    command.get().execute(restInterface, Collections.emptyIterator());
                }
            } else {
                restInterface.printUsage();
            }
            return Response.ok(restInterface.getContent()).build();
        } finally {
            restInterface.clear();
        }
    }

    @GET
    @Path("/help")
    @Produces("application/json")
    public Response help() {
    	LOGGER.info("########################## Rest help ##########################");
        restInterface.printUsage();
        return Response.ok(restInterface.toString()).build();
    }

    @GET
    @Path("/help/{command}")
    @Produces("application/json")
    public Response helpOnCommand(@PathParam("command") String commandName) {
    	LOGGER.info("########################## Rest helpOnCommand ##########################");
        Optional<InterfaceRequest> command = restInterface.findByName(commandName);
        if (command.isPresent()) {
            command.get().usage(restInterface);
            return Response.ok(restInterface.toString()).build();
        }
        return help();
    }
}
