
package com.testmonitor.api;

import com.testmonitor.actions.*;
import com.testmonitor.resources.Project;

/**
 * TestMonitor Client
 */
public class Client
{
    private final Connector connector;

    /**
     * @param domain The TestMonitor domain
     * @param token The TestMonitor API token
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
     * @return Project actions
     */
    public Projects projects()
    {
        return new Projects(this.connector);
    }

    /**
     * @param project Project
     *
     * @return Test Suite actions
     */
    public TestSuites testSuites(Project project)
    {
        return new TestSuites(this.connector, project);
    }

    /**
     * @param project Project
     *
     * @return Test Case actions
     */
    public TestCases testCases(Project project)
    {
        return new TestCases(this.connector, project);
    }

    /**
     * @param project Project
     *
     * @return Test Result actions
     */
    public TestResults testResults(Project project)
    {
        return new TestResults(this.connector, project);
    }

    /**
     * @param project Project
     *
     * @return Test Run actions
     */
    public TestRuns testRuns(Project project)
    {
        return new TestRuns(this.connector, project);
    }

    /**
     * @param project Project
     *
     * @return Milestone actions
     */
    public Milestones milestones(Project project)
    {
        return new Milestones(this.connector, project);
    }

    /**
     * @return User actions
     */
    public Users users()
    {
        return new Users(this.connector);
    }
}
