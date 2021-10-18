package com.testmonitor.actions;

import com.testmonitor.api.Connector;
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

    private Integer projectId;


    public TestSuites(Connector connector)
    {
        this.connector = connector;
    }

    public TestSuites(Connector connector, Integer projectId)
    {
        this.connector = connector;
        this.projectId = projectId;
    }

    public TestSuites(Connector connector, Project project)
    {
        this.connector = connector;
        this.projectId = project.getId();
    }

    protected ArrayList<TestSuite> parse(JSONObject response)
    {
        ArrayList<TestSuite> testSuites = new ArrayList<TestSuite>();

        for (Object obj : response.getJSONArray("data").toList()) {
            HashMap item = (HashMap) obj;

            testSuites.add(new TestSuite(item.get("id").toString(), item.get("name").toString()));
        }

        return testSuites;
    }

    public ArrayList<TestSuite> list()
    {
        return this.parse(this.connector.get(this.plural));
    }

    public TestSuite get(Integer id)
    {
        JSONObject response = this.connector.get(this.plural + "/" + id);

        return new TestSuite(
            response.getJSONObject("data").get("id").toString(),
            response.getJSONObject("data").get("name").toString()
        );
    }

    public ArrayList<TestSuite> search(String search)
    {
        return this.parse(this.connector.get(this.plural + "/?project_id=" + this.projectId + "&query=" + search));
    }

    public TestSuite create(String name)
    {
        TestSuite testSuite = new TestSuite();

        testSuite.setName(name).setProjectId(this.projectId);

        this.connector.post(this.plural);

        return new TestSuite("1", "1");
    }

    public TestSuite searchOrCreate(String search)
    {
        ArrayList<TestSuite> testSuites = this.search(search);

        if (testSuites.size() > 0) {
            return testSuites.get(0);
        }

        return new TestSuite("1", "1");
    }
}
