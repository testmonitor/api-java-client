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
     * @param connector The TestMonitor connector
     * @param project The TestMonitor project
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

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));

        return TestCaseParser.parse(this.connector.get("test-cases", params));
    }

    /**
     * @param id The test case ID
     *
     * @return The test case matching the ID
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
     * Search through test cases.
     *
     * @param search The search query
     *
     * @return A list of results
     */
    public ArrayList<TestCase> search(String query)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        params.add(new BasicNameValuePair("query", query));

        return TestCaseParser.parse(this.connector.get("test-cases", params));
    }

    /**
     * Search though test cases in a test suite.
     *
     * @param search The search query
     *
     * @return A list of results
     */
    public ArrayList<TestCase> search(String query, Integer testSuiteId)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("test_suite", testSuiteId.toString()));

        params.add(new BasicNameValuePair("query", query));

        return TestCaseParser.parse(this.connector.get("test-cases", params));
    }

    /**
     * Create a test case.
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
     * Create a test case using the provided name and test suite ID.
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
     * Create a test case using the provided name and test suite.
     *
     * @param name The name of the test case
     * @param testSuite The test suite
     *
     * @return The created test case
     */
    public TestCase create(String name, TestSuite testSuite)
    {
        return this.create(name, testSuite.getId());
    }

    /**
     * Find a test case using the provided query and test suite or create a new one.
     *
     * @param query The search query
     *
     * @return A test case matching the query or a new test case.
     */
    public TestCase findOrCreate(String query, TestSuite testSuite)
    {
        return this.findOrCreate(query, testSuite.getId());
    }

    /**
     * Find a test case using the provided test case object or create a new one.
     *
     * @param testCase The test case
     *
     * @return A test case matching the test case object or a new test case.
     */
    public TestCase findOrCreate(TestCase testCase)
    {
        return this.findOrCreate(testCase.getName(), testCase.getTestSuiteId());
    }

    /**
     * Find a test case using the provided query and test suite ID or create a new one.
     *
     * @param query The search query
     *
     * @return A test case matching the query and test suite ID or a new test case.
     */
    public TestCase findOrCreate(String query, Integer testSuiteId)
    {
        ArrayList<TestCase> testCases = this.search('"' + query + '"', testSuiteId);

        if (testCases.size() > 0) {
            return testCases.get(0);
        }

        return this.create(query, testSuiteId);
    }

    /**
     * Update a test case.
     *
     * @param testCase The test case you want to update
     *
     * @return The updated test case
     */
    public TestCase update(TestCase testCase)
    {
        JSONObject response = this.connector.put("test-cases/" + testCase.getId(), testCase.toHttpParams());

        HashMap<String, Object> updatedTestCase = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestCaseParser.parse(updatedTestCase);
    }
}
