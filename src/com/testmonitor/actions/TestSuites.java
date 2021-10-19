package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestSuite;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSuites
{
    private final Connector connector;

    private final String singular = "test-suite";

    private final String plural = "test-suites";

    private Integer projectId;

    /**
     * @param connector The TestMonitor connector to perfom HTTP requests
     */
    public TestSuites(Connector connector)
    {
        this.connector = connector;
    }

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
     * Parse a JSONObject in a list of test suites
     *
     * @param response The JSON response of a request
     *
     * @return A parsed list of test suites
     */
    protected ArrayList<TestSuite> parse(JSONObject response)
    {
        ArrayList<TestSuite> testSuites = new ArrayList<TestSuite>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap item = (HashMap) obj;

            testSuites.add(new TestSuite(item.get("id").toString(), item.get("name").toString()));
        }

        return testSuites;
    }

    /**
     * List all test suites
     *
     * @return A list of test suites
     */
    public ArrayList<TestSuite> list()
    {
        return this.parse(this.connector.get(this.plural));
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
        return this.parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&query=" + search));
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
     * @param name The name of the test suite
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
    public TestSuite searchOrCreate(String search)
    {
        ArrayList<TestSuite> testSuites = this.search(search);

        if (testSuites.size() > 0) {
            return testSuites.get(0);
        }

        return this.create(search);
    }
}
