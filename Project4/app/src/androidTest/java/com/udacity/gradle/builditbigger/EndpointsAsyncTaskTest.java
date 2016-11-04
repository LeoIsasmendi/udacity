package com.udacity.gradle.builditbigger;

import android.text.TextUtils;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;


public class EndpointsAsyncTaskTest {

    @Test
    public void testRetrievesJoke() {

        try {

            EndpointsAsyncTask jokeTask = new EndpointsAsyncTask();
            jokeTask.execute();
            jokeTask.setDelegate(new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    assertFalse(TextUtils.isEmpty(output));
                }
            });
            jokeTask.get(30, TimeUnit.SECONDS);

        } catch (Exception e) {
            fail("Timed out");
        }
    }
}