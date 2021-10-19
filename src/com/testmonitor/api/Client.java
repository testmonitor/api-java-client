
package com.testmonitor.api;

import com.testmonitor.actions.*;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestRun;

public class Client
{
    private final Connector connector;

    /**
     * @param domain The TestMonitor domain
     * @param token The TestMonitor auth token
     */
    public Client(String domain, String token)
    {
        this.connector = new Connector(domain, token);
    }

    /**
     * @param connector The TestMonitor API connector
     */
    public Client(Connector connector)
    {
        this.connector = connector;
    }

    /**
     * @return
     */
    public Projects projects()
    {
        return new Projects(this.connector);
    }

    public TestSuites testSuites(Integer projectId)
    {
        return new TestSuites(this.connector, projectId);
    }

    public TestSuites testSuites(Project project)
    {
        return new TestSuites(this.connector, project);
    }

    public TestCases testCases(Integer projectId)
    {
        return new TestCases(this.connector, projectId);
    }

    public TestCases testCases(Project project)
    {
        return new TestCases(this.connector, project);
    }

    public TestResults testResults(Integer projectId)
    {
        return new TestResults(this.connector, projectId);
    }

    public TestResults testResults(Project project)
    {
        return new TestResults(this.connector, project);
    }

    public TestRuns testRuns(Integer projectId)
    {
        return new TestRuns(this.connector, projectId);
    }

    public TestRuns testRuns(Project project)
    {
        return new TestRuns(this.connector, project);
    }
}