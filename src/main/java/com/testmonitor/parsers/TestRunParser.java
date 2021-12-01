package com.testmonitor.parsers;

import com.testmonitor.resources.TestRun;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TestRunParser {
    /**
     * Parse a JSONObject in a list of test runs
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of projects
     */
    public static ArrayList<TestRun> parse(JSONObject response)
    {
        ArrayList<TestRun> testRuns = new ArrayList<>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap<String, Object> testRun = (HashMap<String, Object>) obj;

            testRuns.add(parse(testRun));
        }

        return testRuns;
    }

    /**
     * Parse a hashmap into a test run.
     *
     * @param item the hashmap that contains the test run data.
     *
     * @return The parsed milestone
     */
    public static TestRun parse(HashMap<String, Object> item)
    {
        TestRun testRun = new TestRun();

        testRun.setId(item.get("id").toString())
                .setName(item.get("name").toString())
                .setMilestoneId(item.get("milestone_id").toString())
                .setStartsAt(DateParser.toDateObject(item.get("starts_at").toString()))
                .setEndsAt(DateParser.toDateObject(item.get("ends_at").toString()));

        if (item.get("description") != null) {
            testRun.setDescription(item.get("description").toString());
        }

        return testRun;
    }
}
