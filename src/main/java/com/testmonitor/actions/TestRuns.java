package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestCaseParser;
import com.testmonitor.parsers.TestRunParser;
import com.testmonitor.resources.Milestone;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestCase;
import com.testmonitor.resources.TestRun;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public ArrayList<TestRun> list() throws IOException, URISyntaxException {
        return this.list(1);
    }

    /**
     * @param page Page number
     *
     * @return A list of test runs
     */
    public ArrayList<TestRun> list(Integer page) throws IOException, URISyntaxException {
        return this.list(page, 15);
    }

    /**
     * @param page Page number
     * @param limit Paging limit
     *
     * @return A list of test runs
     */
    public ArrayList<TestRun> list(Integer page, Integer limit) throws IOException, URISyntaxException {
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
    public TestRun get(Integer id) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        JSONObject response = this.connector.get("test-runs/" + id, params);

        HashMap<String, Object> testRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.parse(testRun);
    }

    /**
     * Search through test runs.
     *
     * @param query The search query
     *
     * @return A list of test runs
     */
    public ArrayList<TestRun> search(String query) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("query", query));

        return TestRunParser.parse(this.connector.get("test-runs", params));
    }

    /**
     * Search through test runs within a milestone.
     *
     * @param query The search query
     * @param milestoneId The milestone ID
     *
     * @return A list of test runs
     */
    public ArrayList<TestRun> search(String query, Integer milestoneId) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("milestone", milestoneId.toString()));
        params.add(new BasicNameValuePair("query", query));

        return TestRunParser.parse(this.connector.get("test-runs", params));
    }

    /**
     * Create a test run.
     *
     * @param testRun The test run your want to create
     *
     * @return The created test run
     */
    public TestRun create(TestRun testRun) throws IOException {
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
    public TestRun create(String name, Integer milestoneId) throws IOException {
        TestRun testRun = new TestRun();

        testRun.setName(name);
        testRun.setStartsAt(LocalDate.now());
        testRun.setEndsAt(LocalDate.now());
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
    public TestRun create(String name, Milestone milestone) throws IOException {
        return this.create(name, milestone.getId());
    }

    /**
     * Find a test run using the provided test run object or create a new one.
     *
     * @param testRun The test run
     *
     * @return A test run matching the test run or a new test run.
     */
    public TestRun findOrCreate(TestRun testRun) throws IOException, URISyntaxException {
        return this.findOrCreate(testRun.getName(), testRun.getMilestoneId());
    }

    /**
     * Find a test run using the provided query and milestone or create a new one.
     *
     * @param query The search query
     * @param milestone The milestone
     *
     * @return A test run matching the query and milestone or a new test run.
     */
    public TestRun findOrCreate(String query, Milestone milestone) throws IOException, URISyntaxException {
        return this.findOrCreate(query, milestone.getId());
    }

    /**
     * Find a test run using the provided name and milestone ID.
     *
     * @param name The name of thje test run
     * @param milestoneId The milestone ID
     *
     * @return A test run matching the name and milestone ID or a new test run.
     */
    public ArrayList<TestRun> findByName(String name, Integer milestoneId) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("milestone", milestoneId.toString()));
        params.add(new BasicNameValuePair("filter[name]", name));

        return TestRunParser.parse(this.connector.get("test-runs", params));
    }

    /**
     * Find a test run using the provided query and milestone ID or create a new one.
     *
     * @param name The search query
     * @param milestoneId The milestone ID
     *
     * @return A test run matching the query and milestone ID or a new test run.
     */
    public TestRun findOrCreate(String name, Integer milestoneId) throws IOException, URISyntaxException {
        ArrayList<TestRun> testRuns = findByName(name, milestoneId);

        if (testRuns.size() > 0) {
            return testRuns.get(0);
        }

        return this.create(name, milestoneId);
    }

    /**
     * Update a test run.
     *
     * @param testRun The test run you want to update
     *
     * @return The updated test run
     */
    public TestRun update(TestRun testRun) throws IOException {
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
    public TestRun assignUsers(TestRun testRun, List<Integer> userIds) throws IOException {
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
    public TestRun assignTestCases(TestRun testRun, List<Integer> testCaseIds) throws IOException {
        List<NameValuePair> params = new ArrayList<>();

        for (Integer testCaseId : testCaseIds) {
            params.add(new BasicNameValuePair("test_cases[]", testCaseId.toString()));
        }

        JSONObject response = this.connector.put("test-runs/" + testRun.getId(), params);

        HashMap<String, Object> updatedTestRun = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestRunParser.parse(updatedTestRun);
    }

    /**
     * Merge new test cases to a test run.
     *
     * @param testRun The test run you want to update
     * @param testCaseIds A list of test case ID's
     *
     * @return The updated test run
     */
    public TestRun mergeTestCases(TestRun testRun, List<Integer> testCaseIds) throws IOException {
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
