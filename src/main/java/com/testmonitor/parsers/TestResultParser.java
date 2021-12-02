package com.testmonitor.parsers;

import com.testmonitor.resources.TestResult;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class TestResultParser {
    /**
     * Parse a JSONObject in a list of test results
     *
     * @param response The JSON response of a reqsuest
     *
     * @return A parsed list of test results
     */
    public static ArrayList<TestResult> parse(JSONObject response)
    {
        ArrayList<TestResult> testResults = new ArrayList<TestResult>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap<String, Object> testResult = (HashMap<String, Object>) obj;

            testResults.add(parse(testResult));
        }

        return testResults;
    }

    /**
     * Parse a hashmap into a test result.
     *
     * @param item the hashmap that contains the test result data.
     *
     * @return The parsed test result
     */
    public static TestResult parse(HashMap<String, Object> item)
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
