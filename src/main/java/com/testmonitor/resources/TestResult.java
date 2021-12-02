package com.testmonitor.resources;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestResult {
    private Integer id;

    private String description;

    private Boolean draft = false;

    private Integer testRunId;

    private Integer testCaseId;

    private Integer testResultCategoryId;

    private ArrayList<File> attachments = new ArrayList<File>();

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

    public TestResult setTestResultCategoryId(String testResultCategoryId) {
        this.testResultCategoryId = Integer.parseInt(testResultCategoryId);

        return this;
    }

    public Integer getTestCaseId() {
        return this.testCaseId;
    }

    public TestResult setTestCaseId(Integer testCaseId) {
        this.testCaseId = testCaseId;

        return this;
    }

    public TestResult setTestCaseId(String testCaseId) {
        this.testCaseId = Integer.parseInt(testCaseId);

        return this;
    }

    public Integer getTestRunId() {
        return this.testCaseId;
    }

    public TestResult setTestRunId(Integer testRunId) {
        this.testRunId = testRunId;

        return this;
    }

    public TestResult setTestRunId(String testRunId) {
        this.testRunId = Integer.parseInt(testRunId);

        return this;
    }

    public Boolean isDraft() {
        return this.draft;
    }

    public TestResult setDraft(Boolean draft) {
        this.draft = draft;

        return this;
    }

    public TestResult setDraft(String draft) {
        this.draft = draft.equals("true") || draft.equals("1");

        return this;
    }

    public void addAttachment(File attachment) {
        this.attachments.add(attachment);
    }

    public HashMap<String, File> getAttachments() {
        return new HashMap<String, File>() {{
            attachments.forEach((file) -> put("attachments[]", file));
        }};
    }

    public void clearAttachments() {
        this.attachments.clear();
    }

    public List<NameValuePair> toHttpParams() {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("description", this.description));
        params.add(new BasicNameValuePair("test_case_id", this.testCaseId.toString()));
        params.add(new BasicNameValuePair("test_run_id", this.testRunId.toString()));
        params.add(new BasicNameValuePair("draft", this.draft ? "1" : "0"));
        params.add(new BasicNameValuePair("test_result_category_id", this.testResultCategoryId.toString()));

        return params;
    }
}
