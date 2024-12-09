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
        return this.list(0, page, 15);
    }

    /**
     * @param page Page number
     * @param limit Paging limit
     *
     * @return A list of test case folders
     */
    public ArrayList<TestCaseFolder> list(Integer page, Integer limit) throws IOException, URISyntaxException {
        return this.list(0, page, limit);
    }

    /**
     * @param parentId test case folder parent id
     * @param page Page number
     * @param limit Paging limit
     *
     * @return A list of test case folders
     */
    public ArrayList<TestCaseFolder> list(Integer parentId, Integer page, Integer limit) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("parent_id", parentId.toString()));
        params.add(new BasicNameValuePair("page", page.toString()));
        params.add(new BasicNameValuePair("limit", limit.toString()));

        return TestCaseFolderParser.parse(this.connector.get("test-case/folders", params));
    }

    /**
     * @param id The test case folder ID
     *
     * @return The test case folder matching the ID
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
     * @param testCaseFolder The name of the test case folder
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
     * @param name The name of the test case folder
     *
     * @return The created test case folder
     */
    public TestCaseFolder create(String name) throws IOException {
        TestCaseFolder testCaseFolder = new TestCaseFolder();

        testCaseFolder.setName(name);

        return this.create(testCaseFolder);
    }

    /**
     * Create a test case folder
     *
     * @param name The name of the test case folder
     * @param parent The parent folder
     *
     * @return The created test case folder
     */
    public TestCaseFolder create(String name, TestCaseFolder parent) throws IOException {
        TestCaseFolder testCaseFolder = new TestCaseFolder();

        testCaseFolder
                .setName(name)
                .setProjectId(parent.getProjectId())
                .setParentId(parent.getId());

        return this.create(testCaseFolder);
    }

    /**
     * Create a test case folder
     *
     * @param child The name of the test case folder
     * @param parent The parent folder
     *
     * @return The created test case folder
     */
    public TestCaseFolder create(TestCaseFolder child, TestCaseFolder parent) throws IOException {
        return this.create(child.setParent(parent));
    }

    /**
     * Clone a test case folder
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
        params.add(new BasicNameValuePair("include_subfolders", includeSubfolders ? "1" : "0"));
        params.add(new BasicNameValuePair("include_test_cases", includeTestCases ? "1" : "0"));

        JSONObject response = this.connector.post("test-case/folder/" + source.getId().toString() + "/clone", params);

        HashMap<String, Object> testCaseFolder = (HashMap<String, Object>) response.getJSONObject("data").toMap();

        return TestCaseFolderParser.parse(testCaseFolder);
    }

    /**
     * Clone a test case folder
     *
     * @param source The test case folder you want to clone
     *
     * @return The cloned test case folder
     */
    public TestCaseFolder clone(TestCaseFolder source) throws IOException {
        return this.clone(source, source.getName() + " (Copy)", true, true);
    }

    /**
     * Clone a test case folder
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
     * Find a test case folder using the provided name or create a new one.
     *
     * @param name The provided name and parent folder
     *
     * @return A test case folder matching the name or a new test case folder
     */
    public TestCaseFolder findOrCreate(String name) throws IOException, URISyntaxException {
        ArrayList<TestCaseFolder> testCaseFolders = this.findByName(name);

        if (testCaseFolders.size() > 0) {
            return testCaseFolders.get(0);
        }

        return this.create(name);
    }

    /**
     * Find a test case folder using the provided name and parent folder or create a new one.
     *
     * @param name The name of the test case folder
     * @param parent The parent folder
     *
     * @return A test case folder matching the name or a new test case folder
     */
    public TestCaseFolder findOrCreate(String name, TestCaseFolder parent) throws IOException, URISyntaxException {
        ArrayList<TestCaseFolder> testCaseFolders = this.findByName(name, parent);

        if (testCaseFolders.size() > 0) {
            return testCaseFolders.get(0);
        }

        return this.create(name, parent);
    }

    /**
     * Find a test case folder using the provided name.
     *
     * @param name The name of the test case folder
     *
     * @return Test case folders matching the provided name.
     */
    public ArrayList<TestCaseFolder> findByName(String name) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("filter[name]", name));

        return TestCaseFolderParser.parse(this.connector.get("test-case/folders", params));
    }

    /**
     * Find a test case folder using the provided name and parent folder id
     *
     * @param name The name of the test case folder
     * @param parentId The id of the parent folder
     *
     * @return Test case folders matching the provided name.
     */
    public ArrayList<TestCaseFolder> findByName(String name, Integer parentId) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("parent_id", parentId.toString()));
        params.add(new BasicNameValuePair("filter[name]", name));

        return TestCaseFolderParser.parse(this.connector.get("test-case/folders", params));
    }

    /**
     * Find a test case folder using the provided name and parent folder
     *
     * @param name The name of the test case folder
     * @param parent The parent folder
     *
     * @return Test case folders matching the provided name.
     */
    public ArrayList<TestCaseFolder> findByName(String name, TestCaseFolder parent) throws IOException, URISyntaxException {
        return this.findByName(name, parent.getId());
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
