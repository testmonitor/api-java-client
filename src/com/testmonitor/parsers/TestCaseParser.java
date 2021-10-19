package com.testmonitor.parsers;

import com.testmonitor.resources.Milestone;
import com.testmonitor.resources.TestCase;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TestCaseParser {
    /**
     * Parse a JSONObject in a list of milestones
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of projects
     */
    public static ArrayList<TestCase> Parse(JSONObject response)
    {
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap<String, Object> testCase = (HashMap<String, Object>) obj;

            testCases.add(Parse(testCase));
        }

        return testCases;
    }

    /**
     * Parse a hashmap into a milestone.
     *
     * @param item the hashmap that contains the milestone data.
     *
     * @return The parsed milestone
     */
    public static TestCase Parse(HashMap<String, Object> item)
    {
        TestCase testCase = new TestCase();

        testCase.setId(item.get("id").toString())
                .setName(item.get("name").toString())
                .setTestSuiteId(item.get("test_suite_id").toString());

        if (item.get("description") != null) {
            testCase.setDescription(item.get("description").toString());
        }

        return testCase;
    }
}
