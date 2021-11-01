package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestSuiteParser;
import com.testmonitor.parsers.UserParser;
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

    private final Integer projectId;

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
        return this.list(page, 15);
    }

    /**
     * @return A list of test suites
     */
    public ArrayList<TestSuite> list(Integer page, Integer limit)
    {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));
        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        return TestSuiteParser.parse(this.connector.get("test-suites", params));
    }

    /**
     * @param id The test suite ID
     *
     * @return The test suite that matches the ID
     */
    public TestSuite get(Integer id)
    {
        JSONObject response = this.connector.get("test-suites/" + id);

        HashMap<String, Object> testSuite = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestSuiteParser.parse(testSuite);
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
        return TestSuiteParser.parse(this.connector.get("test-suites/?project_id=" + this.projectId + "&query=" + search));
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
        JSONObject response = this.connector.post("test-suites", testSuite.toHttpParams());

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
        JSONObject response = this.connector.put("test-suites/" + testSuite.getId(), testSuite.toHttpParams());

        HashMap<String, Object> updatedTestSuite = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestSuiteParser.parse(updatedTestSuite);
    }
}
