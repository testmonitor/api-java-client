package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestResult;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TestResults
{
    private final Connector connector;

    private final String singular = "test-result";

    private final String plural = "test-results";

    private Integer projectId;

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     */
    public TestResults(Connector connector)
    {
        this.connector = connector;
    }

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     * @param projectId The project id you want to work on
     */
    public TestResults(Connector connector, Integer projectId)
    {
        this.connector = connector;
        this.projectId = projectId;
    }

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     * @param project The project you want to work on
     */
    public TestResults(Connector connector, Project project)
    {
        this.connector = connector;
        this.projectId = project.getId();
    }

    /**
     * Parse a JSONObject in a list of test suites
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of test suites
     */
    protected ArrayList<TestResult> parse(JSONObject response)
    {
        ArrayList<TestResult> testResults = new ArrayList<TestResult>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap item = (HashMap) obj;

            testResults.add(new TestResult(item.get("id").toString(), item.get("name").toString()));
        }

        return testResults;
    }

    /**
     * List all test suites
     *
     * @return A list of test suites
     */
    public ArrayList<TestResult> list()
    {
        return this.parse(this.connector.get(this.plural));
    }

    /**
     * @param id The test suite ID
     *
     * @return The test suite that matches the ID
     */
    public TestResult get(Integer id)
    {
        JSONObject response = this.connector.get(this.plural + "/" + id);

        return new TestResult(
            response.getJSONObject("data").get("id").toString(),
            response.getJSONObject("data").get("name").toString()
        );
    }

    /**
     * Search a test suite
     *
     * @param search The search string
     *
     * @return A list of results
     */
    public ArrayList<TestResult> search(String search)
    {
        return this.parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&query=" + search));
    }

    /**
     * Create a test suite with a given name.
     *
     * @param name The name of the test suite
     *
     * @return The created test suite
     */
    public TestResult create(TestResult testResult)
    {
        JSONObject response = this.connector.post(this.plural, testResult.toHttpParams());

        System.out.println(response);
        testResult.setId(response.getJSONObject("data").get("id").toString());

        return testResult;
    }
}
