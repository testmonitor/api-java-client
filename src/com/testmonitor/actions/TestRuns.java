package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestRunParser;
import com.testmonitor.resources.Milestone;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestRun;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TestRuns
{
    private final Connector connector;

    private final Integer projectId;

    /**
     * @param connector The TestMonitor connector
     * @param project The TestMonitor project
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

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));

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
     * Search through test runs.
     *
     * @param keywords The search keywords
     *
     * @return A list of test runs
     */
    public ArrayList<TestRun> search(String keywords)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("query", keywords));

        return TestRunParser.parse(this.connector.get("test-runs", params));
    }

    /**
     * Search through test runs within a milestone.
     *
     * @param keywords The search keywords
     * @param milestoneId The milestone ID
     *
     * @return A list of test runs
     */
    public ArrayList<TestRun> search(String keywords, Integer milestoneId)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("milestone", milestoneId.toString()));

        params.add(new BasicNameValuePair("query", keywords));

        return TestRunParser.parse(this.connector.get("test-runs", params));
    }

    /**
     * Create a test run.
     *
     * @param testRun The test run your want to create
     *
     * @return The created test run
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
     * Create a test run using the provided name and milestone ID. The start and end date will be set to today.
     *
     * @param name Name of the test run
     * @param milestoneId The milestone ID
     *
     * @return The created test run
     */
    public TestRun create(String name, Integer milestoneId)
    {
        TestRun testRun = new TestRun();

        testRun.setName(name);
        testRun.setStartsAt(new Date());
        testRun.setEndsAt(new Date());
        testRun.setMilestoneId(milestoneId);

        return this.create(testRun);
    }

    /**
     * Create a test run using the provided name and milestone. The start and end date will be set to today.
     *
     * @param name Name of the test run
     * @param milestone The milestone
     *
     * @return The created test run
     */
    public TestRun create(String name, Milestone milestone)
    {
        return this.create(name, milestone.getId());
    }

    /**
     * Find a test run using the provided test run object or create a new one.
     *
     * @param testRun The test run
     *
     * @return A test run matching the test run or a new test run.
     */
    public TestRun findOrCreate(TestRun testRun)
    {
        return this.findOrCreate(testRun.getName(), testRun.getMilestoneId());
    }

    /**
     * Find a test run using the provided keywords and milestone or create a new one.
     *
     * @param search The search keywords
     * @param milestone The milestone
     *
     * @return A test run matching the keywords and milestone or a new test run.
     */
    public TestRun findOrCreate(String keywords, Milestone milestone)
    {
        return this.findOrCreate(keywords, milestone.getId());
    }

    /**
     * Find a test run using the provided keywords and milestone ID or create a new one.
     *
     * @param search The search keywords
     * @param milestoneId The milestone ID
     *
     * @return A test run matching the keywords and milestone ID or a new test run.
     */
    public TestRun findOrCreate(String keywords, Integer milestoneId)
    {
        ArrayList<TestRun> testRuns = this.search('"' + keywords + '"');

        if (testRuns.size() > 0) {
            return testRuns.get(0);
        }

        return this.create(keywords, milestoneId);
    }

    /**
     * Update a test run.
     *
     * @param testRun The test run you want to update
     *
     * @return The updated test run
     */
    public TestRun update(TestRun testRun)
    {
        JSONObject response = this.connector.put("test-runs/" + testRun.getId(), testRun.toHttpParams());

        HashMap<String, Object> updatedTestRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.parse(updatedTestRun);
    }

    /**
     * Assign users to a test run.
     *
     * @param testRun The test run you want to update
     * @param userIds A list of users ID's
     *
     * @return The updated test run
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
     * Assign new test cases to a test run.
     *
     * @param testRun The test run you want to update
     * @param testCaseIds A list of test case ID's
     *
     * @return The updated test run
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
