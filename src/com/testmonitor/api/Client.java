
package com.testmonitor.api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Base64;

import com.testmonitor.actions.Projects;
import com.testmonitor.actions.TestSuites;
import com.testmonitor.resources.Project;


public class Client
{
    private final Connector connector;

    public Client(String domain, String token)
    {
        this.connector = new Connector(domain, token);
    }

    public Client(Connector connector)
    {
        this.connector = connector;
    }

    public Projects projects()
    {
        return new Projects(this.connector);
    }

    public TestSuites testSuites()
    {
        return new TestSuites(this.connector);
    }

    public TestSuites testSuites(Integer projectId)
    {
        return new TestSuites(this.connector, projectId);
    }

    public TestSuites testSuites(Project project)
    {
        return new TestSuites(this.connector, project);
    }
}