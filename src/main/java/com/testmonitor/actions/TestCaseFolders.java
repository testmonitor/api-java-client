package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestCaseFolderParser;
import com.testmonitor.parsers.TestCaseParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestCase;
import com.testmonitor.resources.TestCaseFolder;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestCaseFolders {
    private final Connector connector;

    private final Integer projectId;

    /**
     * @param connector The TestMonitor connector
     * @param project The TestMonitor project
     */
    public TestCaseFolders(Connector connector, Project project)
    {
        this.connector = connector;
        this.projectId = project.getId();
    }

    /**
     * @return A list of test case folders
     */
    public ArrayList<TestCaseFolder> list() throws IOException, URISyntaxException {
        return this.list(1);
    }

    /**
     * @param page Page number
     *
     * @return A list of test case folders
     */
    public ArrayList<TestCaseFolder> list(Integer page) throws IOException, URISyntaxException {
        return this.list(page, 15);
    }

    /**
     * @param page Page number
     * @param limit Paging limit
     *
     * @return A list of test case folders
     */
    public ArrayList<TestCaseFolder> list(Integer page, Integer limit) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));

        return TestCaseFolderParser.parse(this.connector.get("test-case/folders", params));
    }

    /**
     * Create a test case folder
     *
     * @param testCaseFolder The test case your want to create
     *
     * @return The created test case folder
     */
    public TestCaseFolder create(TestCaseFolder testCaseFolder) throws IOException {
        JSONObject response = this.connector.post("test-case/folders", testCaseFolder.toHttpParams());

        testCaseFolder.setId(response.getJSONObject("data").get("id").toString());

        return testCaseFolder;
    }

    /**
     * Update a test case folder.
     *
     * @param testCaseFolder The test case folder you want to update
     *
     * @return The updated test case folder
     */
    public TestCaseFolder update(TestCaseFolder testCaseFolder) throws IOException {
        JSONObject response = this.connector.put("test-case/folders/" + testCaseFolder.getId(), testCaseFolder.toHttpParams());

        HashMap<String, Object> updatedTestCaseFolder = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestCaseFolderParser.parse(updatedTestCaseFolder);
    }
}
