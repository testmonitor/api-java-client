package com.testmonitor.parsers;

import com.testmonitor.resources.TestSuite;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TestSuiteParser {
    /**
     * Parse a JSONObject in a list of test suites
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of test suites
     */
    public static ArrayList<TestSuite> parse(JSONObject response)
    {
        ArrayList<TestSuite> testSuites = new ArrayList<TestSuite>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap<String, Object> testSuite = (HashMap<String, Object>) obj;

            testSuites.add(parse(testSuite));
        }

        return testSuites;
    }

    /**
     * Parse a hashmap into a test suite.
     *
     * @param item the hashmap that contains the test suite data.
     *
     * @return The parsed test suite
     */
    public static TestSuite parse(HashMap<String, Object> item)
    {
        TestSuite testSuite = new TestSuite();

        testSuite.setId(item.get("id").toString())
                .setName(item.get("name").toString())
                .setProjectId(item.get("project_id").toString());

        if (item.get("description") != null) {
            testSuite.setDescription(item.get("description").toString());
        }

        return testSuite;
    }
}
