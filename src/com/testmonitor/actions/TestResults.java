package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestResultParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestResult;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TestResults
{
    private final Connector connector;

    private final Integer projectId;

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
     * @return A list of test results
     */
    public ArrayList<TestResult> list()
    {
        return this.list(1);
    }

    /**
     * @return A list of test results
     */
    public ArrayList<TestResult> list(Integer page)
    {
        return TestResultParser.parse(this.connector.get("test-results?page=" + page + "&project_id=" + this.projectId));
    }

    /**
     * @return A list of test results
     */
    public ArrayList<TestResult> list(Integer page, Integer limit)
    {
        return TestResultParser.parse(this.connector.get("test-results?page=" + page + "&limit=" + limit + "&project_id=" + this.projectId));
    }

    /**
     * @param id The test suite ID
     *
     * @return The test suite that matches the ID
     */
    public TestResult get(Integer id)
    {
        JSONObject response = this.connector.get("test-results/" + id);

        HashMap<String, Object> testResult = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestResultParser.parse(testResult);
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
        return TestResultParser.parse(this.connector.get("test-results/?project_id=" + this.projectId + "&query=" + search));
    }

    /**
     * Create a test suite with a given name.
     *
     * @param testResult The name of the test result
     *
     * @return The created test suite
     */
    public TestResult create(TestResult testResult)
    {
        JSONObject response = this.connector.post("test-results", testResult.toHttpParams());

        HashMap<String, Object> newTestResult = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestResultParser.parse(newTestResult);
    }

    /**
     * Update a test results
     *
     * @param testResult The test result you want to update
     *
     * @return A new instance of the project
     */
    public TestResult update(TestResult testResult)
    {
        JSONObject response = this.connector.put("test-results/" + testResult.getId(), testResult.toHttpParams());

        HashMap<String, Object> updatedTestResult = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestResultParser.parse(updatedTestResult);
    }

    /**
     * Update a test results
     *
     * @param testResult The test result you want to update
     *
     * @return A new instance of the project
     */
    public TestResult addAttachment(TestResult testResult, File attachment)
    {
        this.connector.postAttachment("test-result/" + testResult.getId()  + "/attachments", attachment);

        return testResult;
    }
}
