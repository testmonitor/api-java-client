package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestRunParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestRun;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestRuns
{
    private final Connector connector;

    private final Integer projectId;

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
        return this.list(page, 15);
    }

    /**
     * @return A list of test runs
     */
    public ArrayList<TestRun> list(Integer page, Integer limit)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));
        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        return TestRunParser.parse(this.connector.get("test-runs", params));
    }

    /**
     * @param id The test run ID
     *
     * @return The test run that matches the ID
     */
    public TestRun get(Integer id)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        JSONObject response = this.connector.get("test-runs/" + id, params);

        HashMap<String, Object> testRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.parse(testRun);
    }

    /**
     * Search a test run
     *
     * @param search The search string
     *
     * @return A list of results
     */
    public ArrayList<TestRun> search(String search)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("query", search));
        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        return TestRunParser.parse(this.connector.get("test-runs", params));
    }

    /**
     * Search a test run
     *
     * @param search The search string
     *
     * @return A list of results
     */
    public ArrayList<TestRun> search(String search, Integer milestoneId)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("query", search));
        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("milestone_id", milestoneId.toString()));

        return TestRunParser.parse(this.connector.get("test-runs", params));
    }

    /**
     * Create a test run in TestMonitor
     *
     * @param testRun The test case your want to create
     *
     * @return The created test case
     */
    public TestRun create(TestRun testRun)
    {
        List<NameValuePair> params = testRun.toHttpParams();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        JSONObject response = this.connector.post("test-runs", params);

        HashMap<String, Object> newTestRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.parse(newTestRun);
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

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        testRun.setName(name);
        testRun.setStartsAt(dtf.format(now));
        testRun.setEndsAt(dtf.format(now));
        testRun.setMilestoneId(milestoneId);

        return this.create(testRun);
    }

    /**
     * Search or create a test run. When the test run is not found there will be a test run created.
     *
     * @param testRun The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestRun findOrCreate(TestRun testRun)
    {
        return this.findOrCreate(testRun.getName(), testRun.getMilestoneId());
    }

    /**
     * Search or create a test rub. When the test run is not found there will be a test run created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test case
     */
    public TestRun findOrCreate(String search, Integer milestoneId)
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
        JSONObject response = this.connector.put("test-runs/" + testRun.getId(), testRun.toHttpParams());

        HashMap<String, Object> updatedTestRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.parse(updatedTestRun);
    }

    /**
     * Assign users to a test run
     *
     * @param testRun The test run you want to update
     *
     * @return A new instance of the test run
     */
    public TestRun assignUsers(TestRun testRun, List<Integer> userIds)
    {
        List<NameValuePair> params = new ArrayList<>();

        for (Integer userId : userIds) {
            params.add(new BasicNameValuePair("users[]", userId.toString()));
        }

        JSONObject response = this.connector.put("test-runs/" + testRun.getId(), params);

        HashMap<String, Object> updatedTestRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.parse(updatedTestRun);
    }

    /**
     * Assign new test cases to a test run
     *
     * @param testRun The test run you want to update
     *
     * @return A new instance of the test run
     */
    public TestRun assignTestCases(TestRun testRun, List<Integer> testCaseIds)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("merge", "1"));

        for (Integer testCaseId : testCaseIds) {
            params.add(new BasicNameValuePair("test_cases[]", testCaseId.toString()));
        }

        JSONObject response = this.connector.put("test-runs/" + testRun.getId(), params);

        HashMap<String, Object> updatedTestRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.parse(updatedTestRun);
    }
}
