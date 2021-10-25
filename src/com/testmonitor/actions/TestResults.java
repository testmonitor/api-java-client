package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestResultParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestResult;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestResults
{
    private final Connector connector;

    private final Integer projectId;

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
        return this.list(page, 15);
    }

    /**
     * @return A list of test results
     */
    public ArrayList<TestResult> list(Integer page, Integer limit)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));
        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        return TestResultParser.parse(this.connector.get("test-results", params));
    }

    /**
     * @param id The test result ID
     *
     * @return The test result that matches the ID
     */
    public TestResult get(Integer id)
    {
        JSONObject response = this.connector.get("test-results/" + id);

        HashMap<String, Object> testResult = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestResultParser.parse(testResult);
    }

    /**
     * Search a test result
     *
     * @param search The search string
     *
     * @return A list of results
     */
    public ArrayList<TestResult> search(String search)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("query", this.projectId.toString()));

        return TestResultParser.parse(this.connector.get("test-results", params));
    }

    /**
     * Create a test result
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
     * Update a test result
     *
     * @param testResult The test result you want to update
     *
     * @return A new instance of the test result
     */
    public TestResult update(TestResult testResult)
    {
        JSONObject response = this.connector.put("test-results/" + testResult.getId(), testResult.toHttpParams());

        HashMap<String, Object> updatedTestResult = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestResultParser.parse(updatedTestResult);
    }

    /**
     * Add an attachment to a test result
     *
     * @param testResult The test result you want to update with an attachment
     *
     * @return A new instance of the project
     */
    public TestResult addAttachment(TestResult testResult, File attachment)
    {
        this.connector.postAttachment("test-result/" + testResult.getId()  + "/attachments", attachment);

        return testResult;
    }
}
