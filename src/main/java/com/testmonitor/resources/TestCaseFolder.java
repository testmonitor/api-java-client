package com.testmonitor.resources;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TestCaseFolder {
    private Integer id;

    private String name;

    private String description;

    private String path;

    private Integer childrenCount;

    private Integer testCasesCount;

    private Integer projectId;

    private Integer parentId;

    public TestCaseFolder setId(Integer id) {
        this.id = id;

        return this;
    }

    public TestCaseFolder setId(String id) {
        this.id = Integer.parseInt(id);

        return this;
    }

    public Integer getId() {
        return id;
    }

    public TestCaseFolder setName(String name) {
        this.name = name;

        return this;
    }

    public String getName() {
        return this.name;
    }

    public TestCaseFolder setDescription(String description) {
        this.description = description;

        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPath() {
        return path;
    }

    public TestCaseFolder setPath(String path) {
        this.path = path;

        return this;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public TestCaseFolder setParentId(Integer parentId) {
        this.parentId = parentId;

        return this;
    }

    public TestCaseFolder setParentId(String parentId) {
        return this.setParentId(Integer.parseInt(parentId));
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public TestCaseFolder setProjectId(Integer projectId) {
        this.projectId = projectId;

        return this;
    }

    public TestCaseFolder setProjectId(String projectId) {
        return this.setProjectId(Integer.parseInt(projectId));
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public Integer getTestCasesCount() {
        return testCasesCount;
    }

    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("name", this.name));
        params.add(new BasicNameValuePair("description", this.description));
        params.add(new BasicNameValuePair("path", this.path));
        params.add(new BasicNameValuePair("project_id", this.projectId.toString()));
        params.add(new BasicNameValuePair("parent_id", this.parentId.toString()));

        return params;
    }
}
