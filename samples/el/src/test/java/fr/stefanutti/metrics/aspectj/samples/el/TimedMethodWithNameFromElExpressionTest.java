/**
 * Copyright (C) 2013 Antonin Stefanutti (antonin.stefanutti@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.stefanutti.metrics.aspectj.samples.el;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.Timer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class TimedMethodWithNameFromElExpressionTest {

    private final static String REGISTRY_NAME = "timerWithElRegistry";

    private String timerName;

    private TimedMethodWithNameFromElExpression instance;

    @Before
    public void createTimedInstance() {
        long id = Math.round(Math.random() * 10);
        instance = new TimedMethodWithNameFromElExpression(id);
        timerName = TimedMethodWithNameFromElExpression.class.getName() + "." + "timer" + id;
    }

    @After
    public void clearSharedMetricRegistries() {
        SharedMetricRegistries.clear();
    }

    @Test
    public void timedMethodNotCalledYet() {
        assertThat(SharedMetricRegistries.names(), hasItem(REGISTRY_NAME));
        MetricRegistry registry = SharedMetricRegistries.getOrCreate(REGISTRY_NAME);
        assertThat(registry.getTimers(), hasKey(timerName));
        Timer timer = registry.getTimers().get(timerName);

        // Make sure that the timer hasn't been called yet
        assertThat(timer.getCount(), is(equalTo(0L)));
    }

    @Test
    public void callTimedMethodOnce() {
        assertThat(SharedMetricRegistries.names(), hasItem(REGISTRY_NAME));
        MetricRegistry registry = SharedMetricRegistries.getOrCreate(REGISTRY_NAME);
        assertThat(registry.getTimers(), hasKey(timerName));
        Timer timer = registry.getTimers().get(timerName);

        // Call the timed method and assert it's been timed
        instance.expressionTimedMethod();
        assertThat(timer.getCount(), is(equalTo(1L)));
    }
}
