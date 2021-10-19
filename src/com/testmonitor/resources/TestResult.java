package com.testmonitor.resources;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TestResult {
    private Integer id;

    private String description;

    private Boolean draft = true;

    private Integer testRunId;

    private Integer testCaseId;

    private Integer createdByUserId;

    private Integer testResultCategoryId;


    public TestResult(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public TestResult(String id, String name) {
        this.id = Integer.parseInt(id);
        this.description = description;
    }

    public TestResult()
    {
        
    }

    public TestResult setId(Integer id) {
        this.id = id;

        return this;
    }

    public TestResult setId(String id) {
        this.id = Integer.parseInt(id);

        return this;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return this.description;
    }

    public TestResult setDescription(String description) {
        this.description = description;

        return this;
    }

    public Integer getTestResultCategoryId() {
        return this.testResultCategoryId;
    }

    public TestResult setTestResultCategoryId(Integer testResultCategoryId) {
        this.testResultCategoryId = testResultCategoryId;

        return this;
    }

    public Integer getTestCaseId() {
        return this.testCaseId;
    }

    public TestResult setTestCaseId(Integer testCaseId) {
        this.testCaseId = testCaseId;

        return this;
    }

    public Integer getTestRunId() {
        return this.testCaseId;
    }

    public TestResult setTestRunId(Integer testRunId) {
        this.testRunId = testRunId;

        return this;
    }

    public Boolean isDraft() {
        return this.draft;
    }

    public TestResult setDraft(Boolean draft) {
        this.draft = draft;

        return this;
    }

    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String draft;

        if (this.draft) {
            draft = "1";
        } else {
            draft = "0";
        }
        params.add(new BasicNameValuePair("project_id", "22"));
        params.add(new BasicNameValuePair("description", this.description));
        params.add(new BasicNameValuePair("test_case_id", this.testCaseId.toString()));
        params.add(new BasicNameValuePair("test_run_id", this.testRunId.toString()));
        params.add(new BasicNameValuePair("draft", draft));
        params.add(new BasicNameValuePair("test_result_category_id", this.testResultCategoryId.toString()));

        return params;
    }

    public String toString()
    {
        return "ID: " + this.id + "\n" + "NAME: " + this.description + "\n";
    }
}
