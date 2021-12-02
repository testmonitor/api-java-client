package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestResultParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestResult;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestResults
{
    private final Connector connector;

    private final Integer projectId;

    /**
     * @param connector The TestMonitor connector
     * @param project The TestMonitor project
     */
    public TestResults(Connector connector, Project project)
    {
        this.connector = connector;
        this.projectId = project.getId();
    }

    /**
     * @return A list of test results
     */
    public ArrayList<TestResult> list() throws IOException, URISyntaxException {
        return this.list(1);
    }

    /**
     * @param page Page number
     *
     * @return A list of test results
     */
    public ArrayList<TestResult> list(Integer page) throws IOException, URISyntaxException {
        return this.list(page, 15);
    }

    /**
     * @param page Page number
     * @param limit Paging limit
     *
     * @return A list of test results
     */
    public ArrayList<TestResult> list(Integer page, Integer limit) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));

        return TestResultParser.parse(this.connector.get("test-results", params));
    }

    /**
     * @param id The test result ID
     *
     * @return The test result that matches the ID
     */
    public TestResult get(Integer id) throws IOException {
        JSONObject response = this.connector.get("test-results/" + id);

        HashMap<String, Object> testResult = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestResultParser.parse(testResult);
    }

    /**
     * Search through test results.
     *
     * @param query The search query
     *
     * @return A list of results
     */
    public ArrayList<TestResult> search(String query) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("query", this.projectId.toString()));

        return TestResultParser.parse(this.connector.get("test-results", params));
    }

    /**
     * Create a test result
     *
     * @param testResult The test result
     *
     * @return The created test result
     */
    public TestResult create(TestResult testResult) throws IOException {
        JSONObject response = this.connector.multiPartPost("test-results", testResult.toHttpParams(), testResult.getAttachments());

        HashMap<String, Object> newTestResult = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestResultParser.parse(newTestResult);
    }

    /**
     * Update a test result.
     *
     * @param testResult The test result you want to update
     *
     * @return The updated test result
     */
    public TestResult update(TestResult testResult) throws IOException {
        JSONObject response = this.connector.put("test-results/" + testResult.getId(), testResult.toHttpParams());

        HashMap<String, Object> updatedTestResult = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestResultParser.parse(updatedTestResult);
    }

    /**
     * Add an attachment to a test result.
     *
     * @param testResult The test result you want to add an attachment to
     * @param attachment The file attachment
     *
     * @return The test result
     */
    public TestResult addAttachment(TestResult testResult, File attachment) throws IOException {
        this.connector.postFile("test-result/" + testResult.getId()  + "/attachments", "file", attachment);

        return testResult;
    }
}
