package com.testmonitor.parsers;

import com.testmonitor.resources.TestCaseFolder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TestCaseFolderParser {
    /**
     * Parse a JSONObject in a list of test cases
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of test case folders
     */
    public static ArrayList<TestCaseFolder> parse(JSONObject response)
    {
        ArrayList<TestCaseFolder> testCaseFolders = new ArrayList<TestCaseFolder>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap<String, Object> testCaseFolder = (HashMap<String, Object>) obj;

            testCaseFolders.add(parse(testCaseFolder));
        }

        return testCaseFolders;
    }

    /**
     * Parse a hashmap into a test case.
     *
     * @param item the hashmap that contains the milestone data.
     *
     * @return The parsed test case folder
     */
    public static TestCaseFolder parse(HashMap<String, Object> item)
    {
        TestCaseFolder testCaseFolder = new TestCaseFolder();

        testCaseFolder.setId(item.get("id").toString())
                .setName(item.get("name").toString())
                .setPath(item.get("path").toString())
                .setProjectId(item.get("project_id").toString());

        if (item.get("parent_id") != null) {
            testCaseFolder.setParentId(item.get("parent_id").toString());
        }

        if (item.get("description") != null) {
            testCaseFolder.setDescription(item.get("description").toString());
        }

        if (item.get("children_count") != null) {
            testCaseFolder.setChildrenCount(item.get("children_count").toString());
        }

        if (item.get("test_cases_count") != null) {
            testCaseFolder.setTestCasesCount(item.get("test_cases_count").toString());
        }

        return testCaseFolder;
    }
}
