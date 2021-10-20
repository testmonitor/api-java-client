package com.testmonitor.api;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.FileBody;

import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.*;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Connector {

    private final String token;

    private final String domain;

    private final String baseUrl;

    private final CloseableHttpClient httpClient;

    public Connector(String domain, String token) {
        this.token = token;
        this.domain = domain;

        this.baseUrl = this.baseUrl();

        this.httpClient = HttpClients.createDefault();
    }

    protected String baseUrl()
    {
        return "https://" + this.domain + "/api/v1/";
    }

    public JSONObject get(String uri)
    {
        final HttpGet httpget = new HttpGet(this.baseUrl + uri);

        return this.request(httpget);
    }

    public JSONObject post(String uri, List<NameValuePair> params)
    {
        final HttpPost httppost = new HttpPost(this.baseUrl + uri);

        httppost.setEntity(new UrlEncodedFormEntity(params));

        return this.request(httppost);
    }

    public JSONObject put(String uri, List<NameValuePair> params)
    {
        final HttpPut httpput = new HttpPut(this.baseUrl + uri);

        httpput.setEntity(new UrlEncodedFormEntity(params));

        return this.request(httpput);
    }

    public JSONObject postAttachment(String uri, File file)
    {
        HttpPost post = new HttpPost(this.baseUrl + uri);
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
//
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.EXTENDED);
        builder.addPart("file", fileBody);

        HttpEntity entity = builder.build();
//
        post.setEntity(entity);

        return this.request(post);
    }

    public JSONObject request(HttpUriRequestBase httpUriRequestBase) {
        httpUriRequestBase.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.token);
        httpUriRequestBase.setHeader(HttpHeaders.ACCEPT, "application/json");

        final HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {

            @Override
            public String handleResponse(
                    final ClassicHttpResponse response) throws IOException, ParseException {
                final int status = response.getCode();
                if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                    final HttpEntity entity = response.getEntity();
                    try {
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    System.out.println(EntityUtils.toString(response.getEntity()));
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };


        try {
            final String responseBody;

            responseBody = this.httpClient.execute(httpUriRequestBase, responseHandler);

            return new JSONObject(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }
}
