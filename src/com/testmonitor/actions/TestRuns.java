package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestResultParser;
import com.testmonitor.parsers.TestRunParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestResult;
import com.testmonitor.resources.TestRun;
import com.testmonitor.resources.Milestone;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestRuns
{
    private final Connector connector;

    private final String singular = "test-run";

    private final String plural = "test-runs";

    private final Integer projectId;

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     * @param projectId The project id you want to work on
     */
    public TestRuns(Connector connector, Integer projectId)
    {
        this.connector = connector;
        this.projectId = projectId;
    }

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     * @param project The project you want to work on
     */
    public TestRuns(Connector connector, Project project)
    {
        this.connector = connector;
        this.projectId = project.getId();
    }

    /**
     * @return A list of test runs
     */
    public ArrayList<TestRun> list()
    {
        return this.list(1);
    }

    /**
     * @return A list of test runs
     */
    public ArrayList<TestRun> list(Integer page)
    {
        return TestRunParser.Parse(this.connector.get(this.plural + "?page=" + page + "&project_id=" + this.projectId));
    }

    /**
     * @return A list of test runs
     */
    public ArrayList<TestRun> list(Integer page, Integer limit)
    {
        return TestRunParser.Parse(this.connector.get(this.plural + "?page=" + page + "&limit=" + limit + "&project_id=" + this.projectId));
    }

    /**
     * @param id The test case ID
     *
     * @return The test case that matches the ID
     */
    public TestRun get(Integer id)
    {
        JSONObject response = this.connector.get(this.plural + "/" + id + "?project_id=" + this.projectId);

        HashMap<String, Object> testRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.Parse(testRun);
    }

    /**
     * Search a test case
     *
     * @param search The search string
     *
     * @return A list of results
     */
    public ArrayList<TestRun> search(String search)
    {
        return TestRunParser.Parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&query=" + search));
    }

    /**
     * Search a test case
     *
     * @param search The search string
     *
     * @return A list of results
     */
    public ArrayList<TestRun> search(String search, Integer milestoneId)
    {
        return TestRunParser.Parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&test_suite_id=" + milestoneId + "%query=" + search));
    }

    /**
     * Create a test case in TestMonitor
     *
     * @param testRun The test case your want to create
     *
     * @return The created test case
     */
    public TestRun create(TestRun testRun)
    {
        JSONObject response = this.connector.post(this.plural, testRun.toHttpParams());

        testRun.setId(response.getJSONObject("data").get("id").toString());

        return testRun;
    }

    /**
     * @param name Name of the test run
     * @param milestoneId ID of the milestone
     *
     * @return The test run
     */
    public TestRun create(String name, Integer milestoneId)
    {
        TestRun testRun = new TestRun();

        testRun.setName(name);
        testRun.setMilestoneId(milestoneId);

        return this.create(testRun);
    }

    /**
     * Search or create a test case. When the test case is not found there will be a test case created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestRun searchOrCreate(String search, Milestone testSuite)
    {
        return this.searchOrCreate(search, testSuite.getId());
    }

    /**
     * Search or create a test case. When the test case is not found there will be a test case created.
     *
     * @param testRun The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestRun searchOrCreate(TestRun testRun)
    {
        return this.searchOrCreate(testRun.getName(), testRun.getMilestoneId());
    }

    /**
     * Search or create a test case. When the test case is not found there will be a test case created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestRun searchOrCreate(String search, Integer milestoneId)
    {
        ArrayList<TestRun> testRuns = this.search(search);

        if (testRuns.size() > 0) {
            return testRuns.get(0);
        }

        return this.create(search, milestoneId);
    }

    /**
     * Update a test run
     *
     * @param testRun The test run you want to update
     *
     * @return A new instance of the test run
     */
    public TestRun update(TestRun testRun)
    {
        JSONObject response = this.connector.put(this.plural + "/" + testRun.getId(), testRun.toHttpParams());

        HashMap<String, Object> updatedTestRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.Parse(updatedTestRun);
    }

    /**
     * Add users to a test run
     *
     * @param testRun The test run you want to update
     *
     * @return A new instance of the test run
     */
    public TestRun addUsers(TestRun testRun, List<Integer> userIds)
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        for (Integer userId : userIds) {
            params.add(new BasicNameValuePair("users[]", userId.toString()));
        }

        JSONObject response = this.connector.put(this.plural + "/" + testRun.getId(), params);

        HashMap<String, Object> updatedTestRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.Parse(updatedTestRun);
    }

    /**
     * Add users to a test run
     *
     * @param testRun The test run you want to update
     *
     * @return A new instance of the test run
     */
    public TestRun addTestCases(TestRun testRun, List<Integer> testCaseIds)
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        for (Integer testCaseId : testCaseIds) {
            params.add(new BasicNameValuePair("test_cases[]", testCaseId.toString()));
        }

        JSONObject response = this.connector.put(this.plural + "/" + testRun.getId(), params);

        HashMap<String, Object> updatedTestRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.Parse(updatedTestRun);
    }
}
