package com.testmonitor.parsers;

import com.testmonitor.resources.TestResult;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class TestResultParser {
    /**
     * Parse a JSONObject in a list of milestones
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of test results
     */
    public static ArrayList<TestResult> Parse(JSONObject response)
    {
        ArrayList<TestResult> testResults = new ArrayList<TestResult>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap<String, Object> testResult = (HashMap<String, Object>) obj;

            testResults.add(Parse(testResult));
        }

        return testResults;
    }

    /**
     * Parse a hashmap into a milestone.
     *
     * @param item the hashmap that contains the milestone data.
     *
     * @return The parsed milestone
     */
    public static TestResult Parse(HashMap<String, Object> item)
    {
        TestResult testResult = new TestResult();

        testResult.setId(item.get("id").toString())
                .setTestCaseId(item.get("test_case_id").toString())
                .setTestRunId(item.get("test_run_id").toString())
                .setDraft(item.get("draft").toString());

        if (item.get("description") != null) {
            testResult.setDescription(item.get("description").toString());
        }

        if (item.get("test_result_category_id") != null) {
            testResult.setTestResultCategoryId(item.get("test_result_category_id").toString());
        }

        return testResult;
    }
}
