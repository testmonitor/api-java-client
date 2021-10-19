package com.testmonitor.actions;

import com.testmonitor.api.Connector;
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

    private final String singular = "test-case";

    private final String plural = "test-cases";

    private Integer projectId;

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     */
    public TestCases(Connector connector)
    {
        this.connector = connector;
    }

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     * @param projectId The project id you want to work on
     */
    public TestCases(Connector connector, Integer projectId)
    {
        this.connector = connector;
        this.projectId = projectId;
    }

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
     * Parse a JSONObject in a list of test cases
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of test cases
     */
    protected ArrayList<TestCase> parse(JSONObject response)
    {
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap item = (HashMap) obj;

            testCases.add(new TestCase(item.get("id").toString(), item.get("name").toString()));
        }

        return testCases;
    }

    /**
     * List all test cases
     *
     * @return A list of test cases
     */
    public ArrayList<TestCase> list()
    {
        return this.parse(this.connector.get(this.plural));
    }

    /**
     * @param id The test case ID
     *
     * @return The test case that matches the ID
     */
    public TestCase get(Integer id)
    {
        JSONObject response = this.connector.get(this.plural + "/" + id);

        return new TestCase(
            response.getJSONObject("data").get("id").toString(),
            response.getJSONObject("data").get("name").toString()
        );
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
        return this.parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&query=" + search));
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
        return this.parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&test_suite_id=" + testSuiteId + "%query=" + search));
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
        JSONObject response = this.connector.post(this.plural, testCase.toHttpParams());

        testCase.setId(response.getJSONObject("data").get("id").toString());

        return testCase;
    }

    /**
     * Create a test case in TestMonitor
     *
     * @param testCase The test case your want to create
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
     * Search or create a test case. When the test case is not found there will be a test case created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestCase searchOrCreate(String search, TestSuite testSuite)
    {
        return this.searchOrCreate(search, testSuite.getId());
    }

    /**
     * Search or create a test case. When the test case is not found there will be a test case created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestCase searchOrCreate(TestCase testCase)
    {
        return this.searchOrCreate(testCase.getName(), testCase.getTestSuiteId());
    }

    /**
     * Search or create a test case. When the test case is not found there will be a test case created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestCase searchOrCreate(String search, Integer testSuiteId)
    {
        ArrayList<TestCase> testCases = this.search(search);

        if (testCases.size() > 0) {
            return testCases.get(0);
        }

        TestCase testCase = new TestCase();

        testCase.setName(search);
        testCase.setTestSuiteId(testSuiteId);

        return this.create(testCase);
    }
}
