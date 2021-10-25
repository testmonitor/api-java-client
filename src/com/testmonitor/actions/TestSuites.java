package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestSuiteParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestSuite;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TestSuites
{
    private final Connector connector;

    private final String singular = "test-suite";

    private final String plural = "test-suites";

    private final Integer projectId;

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     * @param projectId The project id you want to work on
     */
    public TestSuites(Connector connector, Integer projectId)
    {
        this.connector = connector;
        this.projectId = projectId;
    }

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     * @param project The project you want to work on
     */
    public TestSuites(Connector connector, Project project)
    {
        this.connector = connector;
        this.projectId = project.getId();
    }

    /**
     * @return A list of test suites
     */
    public ArrayList<TestSuite> list()
    {
        return this.list(1);
    }

    /**
     * @return A list of test suites
     */
    public ArrayList<TestSuite> list(Integer page)
    {
        return TestSuiteParser.parse(this.connector.get(this.plural + "?page=" + page + "&project_id=" + this.projectId));
    }

    /**
     * @return A list of test suites
     */
    public ArrayList<TestSuite> list(Integer page, Integer limit)
    {
        return TestSuiteParser.parse(this.connector.get(this.plural + "?page=" + page + "&limit=" + limit + "&project_id=" + this.projectId));
    }

    /**
     * @param id The test suite ID
     *
     * @return The test suite that matches the ID
     */
    public TestSuite get(Integer id)
    {
        JSONObject response = this.connector.get(this.plural + "/" + id);

        return new TestSuite(
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
    public ArrayList<TestSuite> search(String search)
    {
        return TestSuiteParser.parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&query=" + search));
    }

    /**
     * Create a test suite with a given name.
     *
     * @param name The name of the test suite
     *
     * @return The created test suite
     */
    public TestSuite create(String name)
    {
        TestSuite testSuite = new TestSuite();

        testSuite.setName(name);
        testSuite.setProjectId(this.projectId);

        return this.create(testSuite);
    }

    /**
     * Create a test suite with a given name.
     *
     * @param testSuite The name of the test suite
     *
     * @return The created test suite
     */
    public TestSuite create(TestSuite testSuite)
    {
        JSONObject response = this.connector.post(this.plural, testSuite.toHttpParams());

        testSuite.setId(response.getJSONObject("data").get("id").toString());

        return testSuite;
    }

    /**
     * Search or create a test suite. When the test suite is not found there will be a test suite created.
     *
     * @param search The search query
     *
     * @return The first result or a fresh created test suite
     */
    public TestSuite findOrCreate(String search)
    {
        ArrayList<TestSuite> testSuites = this.search(search);

        if (testSuites.size() > 0) {
            return testSuites.get(0);
        }

        return this.create(search);
    }

    /**
     * Update a test suite
     *
     * @param testSuite The test suite you want to update
     *
     * @return A new instance of the test suite
     */
    public TestSuite update(TestSuite testSuite)
    {
        JSONObject response = this.connector.put(this.plural + "/" + testSuite.getId(), testSuite.toHttpParams());

        HashMap<String, Object> updatedTestSuite = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestSuiteParser.parse(updatedTestSuite);
    }
}
