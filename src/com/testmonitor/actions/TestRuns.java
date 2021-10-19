package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestRun;
import com.testmonitor.resources.Milestone;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TestRuns
{
    private final Connector connector;

    private final String singular = "test-run";

    private final String plural = "test-runs";

    private Integer projectId;

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     */
    public TestRuns(Connector connector)
    {
        this.connector = connector;
    }

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
     * Parse a JSONObject in a list of test cases
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of test cases
     */
    protected ArrayList<TestRun> parse(JSONObject response)
    {
        ArrayList<TestRun> testRuns = new ArrayList<TestRun>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap item = (HashMap) obj;

            testRuns.add(new TestRun(item.get("id").toString(), item.get("name").toString()));
        }

        return testRuns;
    }

    /**
     * List all test cases
     *
     * @return A list of test cases
     */
    public ArrayList<TestRun> list()
    {
        return this.parse(this.connector.get(this.plural));
    }

    /**
     * @param id The test case ID
     *
     * @return The test case that matches the ID
     */
    public TestRun get(Integer id)
    {
        JSONObject response = this.connector.get(this.plural + "/" + id);

        return new TestRun(
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
    public ArrayList<TestRun> search(String search)
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
    public ArrayList<TestRun> search(String search, Integer milestoneId)
    {
        return this.parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&test_suite_id=" + milestoneId + "%query=" + search));
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
     * Create a test case in TestMonitor
     *
     * @param testRun The test case your want to create
     *
     * @return The created test case
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
     * @param search The search query
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

        TestRun testRun = new TestRun();

        testRun.setName(search);
        testRun.setMilestoneId(milestoneId);

        return this.create(testRun);
    }
}
