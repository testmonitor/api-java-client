package com.testmonitor.resources;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TestCase {
    private Integer id;

    private String name;

    private String description;

    private Integer testCaseFolderId;

    private Integer projectId;

    public TestCase setId(Integer id) {
        this.id = id;

        return this;
    }

    public TestCase setId(String id) {
        this.id = Integer.parseInt(id);

        return this;
    }

    public Integer getId() {
        return id;
    }

    public TestCase setName(String name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return this.name;
    }

    public TestCase setTestCaseFolderId(Integer testCaseFolderId) {
        this.testCaseFolderId = testCaseFolderId;

        return this;
    }

    public TestCase setTestCaseFolderId(String testCaseFolderId) {
        return this.setTestCaseFolderId(Integer.parseInt(testCaseFolderId));
    }

    public Integer getTestCaseFolderId() {
        return this.testCaseFolderId;
    }

    public String getDescription() {
        return this.description;
    }

    public TestCase setDescription(String description) {
        this.description = description;

        return this;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public TestCase setProjectId(Integer projectId) {
        this.projectId = projectId;

        return this;
    }

    public TestCase setProjectId(String projectId) {
        return this.setProjectId(Integer.parseInt(projectId));
    }

    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("name", this.name));
        params.add(new BasicNameValuePair("description", this.description));
        params.add(new BasicNameValuePair("test_case_folder_id", this.testCaseFolderId.toString()));
        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));

        return params;
    }
}
