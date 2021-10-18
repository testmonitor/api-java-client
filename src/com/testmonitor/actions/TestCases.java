package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestCaseParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestCase;
import com.testmonitor.resources.TestSuite;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestCases
{
    private final Connector connector;

    private final Integer projectId;

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     * @param project The project you want to work on
     */
    public TestCases(Connector connector, Project project)
    {
        this.connector = connector;
        this.projectId = project.getId();
    }

    /**
     * @return A list of test cases
     */
    public ArrayList<TestCase> list()
    {
        return this.list(1);
    }

    /**
     * @return A list of test cases
     */
    public ArrayList<TestCase> list(Integer page)
    {
        return this.list(page, 15);
    }

    /**
     * @return A list of test cases
     */
    public ArrayList<TestCase> list(Integer page, Integer limit)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));
        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        return TestCaseParser.parse(this.connector.get("test-cases", params));
    }

    /**
     * @param id The test case ID
     *
     * @return The test case that matches the ID
     */
    public TestCase get(Integer id)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        JSONObject response = this.connector.get("test-cases/" + id, params);

        HashMap<String, Object> testCase = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestCaseParser.parse(testCase);
    }

    /**
     * Search a test case
     *
     * @param search The search string
     *
     * @return A list of results
     */
    public ArrayList<TestCase> search(String search)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("query", search));
        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        return TestCaseParser.parse(this.connector.get("test-cases", params));
    }

    /**
     * Search a test case
     *
     * @param search The search string
     *
     * @return A list of results
     */
    public ArrayList<TestCase> search(String search, Integer testSuiteId)
    {
        ArrayList<TestCase> testCases = this.search(search);

        testCases.removeIf(testCase -> !testCase.getTestSuiteId().equals(testSuiteId));

        return testCases;
    }

    /**
     * Create a test case in TestMonitor
     *
     * @param testCase The test case your want to create
     *
     * @return The created test case
     */
    public TestCase create(TestCase testCase)
    {
        JSONObject response = this.connector.post("test-cases", testCase.toHttpParams());

        testCase.setId(response.getJSONObject("data").get("id").toString());

        return testCase;
    }

    /**
     * Create a test case in TestMonitor
     *
     * @param name The name of the test case
     * @param testSuiteId The ID of the test suite
     *
     * @return The created test case
     */
    public TestCase create(String name, Integer testSuiteId)
    {
        TestCase testCase = new TestCase();

        testCase.setName(name);
        testCase.setTestSuiteId(testSuiteId);

        return this.create(testCase);
    }

    /**
     * Create a test case in TestMonitor
     *
     * @param name The name of the test case
     * @param testSuite The ID of the test suite
     *
     * @return The created test case
     */
    public TestCase create(String name, TestSuite testSuite)
    {
        return this.create(name, testSuite.getId());
    }

    /**
     * Search or create a test case. When the test case is not found there will be a test case created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestCase findOrCreate(String search, TestSuite testSuite)
    {
        return this.findOrCreate(search, testSuite.getId());
    }

    /**
     * Search or create a test case. When the test case is not found there will be a test case created.
     *
     * @param testCase The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestCase findOrCreate(TestCase testCase)
    {
        return this.findOrCreate(testCase.getName(), testCase.getTestSuiteId());
    }

    /**
     * Search or create a test case. When the test case is not found there will be a test case created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestCase findOrCreate(String search, Integer testSuiteId)
    {
        ArrayList<TestCase> testCases = this.search('"' + search + '"', testSuiteId);

        if (testCases.size() > 0) {
            return testCases.get(0);
        }

        return this.create(search, testSuiteId);
    }

    /**
     * Update a test case
     *
     * @param testCase The test case you want to update
     *
     * @return A new instance of the project
     */
    public TestCase update(TestCase testCase)
    {
        JSONObject response = this.connector.put("test-cases/" + testCase.getId(), testCase.toHttpParams());

        HashMap<String, Object> updatedTestCase = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestCaseParser.parse(updatedTestCase);
    }
}
