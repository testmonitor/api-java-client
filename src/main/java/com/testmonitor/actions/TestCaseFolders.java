package com.testmonitor.actions;

import com.testmonitor.api.Connector;
import com.testmonitor.parsers.TestCaseFolderParser;
import com.testmonitor.resources.Project;
import com.testmonitor.resources.TestCaseFolder;
import com.testmonitor.utilities.Convert;
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
     * @param id The test case ID
     *
     * @return The test case matching the ID
     */
    public TestCaseFolder get(Integer id) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        JSONObject response = this.connector.get("test-case/folders/" + id, params);

        HashMap<String, Object> testCaseFolder = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestCaseFolderParser.parse(testCaseFolder);
    }

    /**
     * Create a test case folder
     *
     * @param testCaseFolder The test case your want to create
     *
     * @return The created test case folder
     */
    public TestCaseFolder create(TestCaseFolder testCaseFolder) throws IOException {
        testCaseFolder.setProjectId(this.projectId);

        JSONObject response = this.connector.post("test-case/folders", testCaseFolder.toHttpParams());

        testCaseFolder.setId(response.getJSONObject("data").get("id").toString());

        return testCaseFolder;
    }

    /**
     * Create a test case folder
     *
     * @param name The test case folder name you want to create
     *
     * @return The created test case folder
     */
    public TestCaseFolder create(String name) throws IOException {
        TestCaseFolder testCaseFolder = new TestCaseFolder();

        testCaseFolder.setName(name);

        return this.create(testCaseFolder);
    }

    /**
     * Clome a test case folder
     *
     * @param source The test case folder you want to clone
     * @param name The name of the clone
     * @param includeSubfolders Sets the option to include subfolders
     * @param includeTestCases Sets the option to include testcases
     *
     * @return The cloned test case folder
     */
    public TestCaseFolder clone(TestCaseFolder source, String name, boolean includeSubfolders, boolean includeTestCases) throws IOException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("include_subfolders", Convert.booleanToNumericString(includeSubfolders)));
        params.add(new BasicNameValuePair("include_test_cases", Convert.booleanToNumericString(includeTestCases)));

        JSONObject response = this.connector.post("test-case/folder/" + source.getId().toString() + "/clone", params);

        HashMap<String, Object> testCaseFolder = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestCaseFolderParser.parse(testCaseFolder);
    }

    /**
     * Clome a test case folder
     *
     * @param source The test case folder you want to clone
     *
     * @return The cloned test case folder
     */
    public TestCaseFolder clone(TestCaseFolder source) throws IOException {
        return this.clone(source, source.getName() + " (Copy)", true, true);
    }

    /**
     * Clome a test case folder
     *
     * @param source The test case folder you want to clone
     * @param name The name of the clone
     *
     * @return The cloned test case folder
     */
    public TestCaseFolder clone(TestCaseFolder source, String name) throws IOException {
        return this.clone(source, name, true, true);
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
