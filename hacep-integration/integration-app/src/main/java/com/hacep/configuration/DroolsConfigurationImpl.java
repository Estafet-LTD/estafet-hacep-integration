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

package com.hacep.configuration;

import it.redhat.hacep.configuration.RulesConfiguration;
import it.redhat.hacep.drools.channels.NullChannel;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.Channel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hacep.model.channels.SysoutChannel;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class DroolsConfigurationImpl implements RulesConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DroolsConfigurationImpl.class);

    private final Map<String, Channel> channels = new ConcurrentHashMap<>();
    private final Map<String, Channel> replayChannels = new ConcurrentHashMap<>();

    private final KieContainer kieContainer;
    private final KieBase kieBase;

    private static final String KSESSION_RULES = "hacep-sessions";
    private static final String KBASE_NAME = "hacep-rules";

    public DroolsConfigurationImpl() {
        KieServices kieServices = KieServices.Factory.get();
        kieContainer = kieServices.getKieClasspathContainer();
        kieBase = kieContainer.getKieBase(KBASE_NAME);

        channels.put(SysoutChannel.CHANNEL_ID, new SysoutChannel());

        replayChannels.put(SysoutChannel.CHANNEL_ID, new NullChannel());

        if (logger.isInfoEnabled()) {
            logger.info("[Kie Container] successfully initialized.");
        }
    }

    @Override
    public String getKieSessionName() {
        return KSESSION_RULES;
    }

    @Override
    public String getKieBaseName() {
        return KBASE_NAME;
    }

    @Override
    public Map<String, Channel> getChannels() {
        return channels;
    }

    @Override
    public Map<String, Channel> getReplayChannels() {
        return replayChannels;
    }

    @Override
    public String getGroupId() {
        return "com.orwell.hacep";
    }

    @Override
    public String getArtifactId() {
        return "orwell-integration-rules";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }
}